DROP DATABASE IF EXISTS PayMyBuddyDB;

CREATE DATABASE PayMyBuddyDB;
USE PayMyBuddyDB;

CREATE TABLE `User` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(20) UNIQUE NOT NULL,
    `email` VARCHAR(50) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL
);

CREATE TABLE `Wallet` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `userId` BIGINT NOT NULL,
    `balance` BIGINT NOT NULL DEFAULT 0,
    FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE
);

CREATE TABLE `Transaction` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `issuerWalletId` BIGINT NOT NULL,
    `receiverWalletId` BIGINT NOT NULL,
    `timestamp` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `amount` BIGINT NOT NULL,
    'transactionFee' DOUBLE NOT NULL DEFAULT 0.05,
    `description` VARCHAR(255),
    FOREIGN KEY (`issuerWalletId`) REFERENCES `Wallet` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`receiverWalletId`) REFERENCES `Wallet` (`id`) ON DELETE CASCADE
);

CREATE TABLE `Friends` (
    `userId` BIGINT NOT NULL,
    `friendId` BIGINT NOT NULL,
    PRIMARY KEY (`userId`, `friendId`),
    FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`friendId`) REFERENCES `User` (`id`) ON DELETE CASCADE
);

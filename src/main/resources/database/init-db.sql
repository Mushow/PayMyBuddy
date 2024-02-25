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
    `user_id` BIGINT NOT NULL,
    `balance` BIGINT NOT NULL DEFAULT 0,
    FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE
);

CREATE TABLE `Transaction` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `issuer_wallet_id` BIGINT NOT NULL,
    `receiver_wallet_id` BIGINT NOT NULL,
    `timestamp` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `amount` BIGINT NOT NULL,
    `description` VARCHAR(255),
    FOREIGN KEY (`issuer_wallet_id`) REFERENCES `Wallet` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`receiver_wallet_id`) REFERENCES `Wallet` (`id`) ON DELETE CASCADE
);

CREATE TABLE `Friends` (
    `user_id` BIGINT NOT NULL,
    `friend_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `friend_id`),
    FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`friend_id`) REFERENCES `User` (`id`) ON DELETE CASCADE
);

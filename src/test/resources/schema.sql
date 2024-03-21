DROP DATABASE IF EXISTS PayMyBuddyDBTest;

CREATE DATABASE PayMyBuddyDBTest;
USE PayMyBuddyDBTest;

CREATE TABLE "user" (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE "wallet" (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE "transaction" (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issuer_wallet BIGINT,
    receiver_wallet BIGINT,
    amount DECIMAL(19, 2) NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (issuer_wallet) REFERENCES wallet(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_wallet) REFERENCES wallet(id) ON DELETE CASCADE
);

CREATE TABLE "friends" (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES user(id) ON DELETE CASCADE
);

drop database if exists bank;

create database bank;
use bank;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pseudo VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL DEFAULT 0.0
);

CREATE TABLE user_friendships (
    user_id INT,
    friend_id INT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE user_wallet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL UNIQUE,
    balance DECIMAL NOT NULL DEFAULT 0.0,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_account_number VARCHAR(255) NOT NULL,
    receiver_account_number VARCHAR(255) NOT NULL,
    description TEXT,
    amount DECIMAL NOT NULL,
    transaction_fee DECIMAL NOT NULL DEFAULT 0.0,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    wallet_id INT,
    FOREIGN KEY (wallet_id) REFERENCES user_wallet(id)
);

INSERT INTO users (pseudo, email, password, balance) VALUES
('Jane', 'jane@example.com', 'password123', 5000.0),
('Mark', 'mark@example.com', 'password123', 3000.0),
('John', 'john@example.com', 'password123', 2000.0),
('Ken', 'ken@example.com', 'password123', 4000.0);

-- Jane est amie avec Mark et John
INSERT INTO user_friendships (user_id, friend_id) VALUES
(1, 2),
(1, 3);

-- Mark est ami avec Ken
INSERT INTO user_friendships (user_id, friend_id) VALUES
(2, 4);

-- John est ami avec Ken
INSERT INTO user_friendships (user_id, friend_id) VALUES
(3, 4);

INSERT INTO user_wallet (account_number, balance, user_id) VALUES
('ACC1001', 5000.0, 1),
('ACC1002', 3000.0, 2),
('ACC1003', 2000.0, 3),
('ACC1004', 4000.0, 4);

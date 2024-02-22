create database bank;
use bank;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pseudo VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DOUBLE DEFAULT 0.0
);

CREATE TABLE user_friendships (
    userId INT,
    friendId INT,
    PRIMARY KEY (userId, friendId),
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friendId) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE user_wallet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    accountNumber VARCHAR(255) NOT NULL UNIQUE,
    balance DOUBLE NOT NULL DEFAULT 0.0,
    userId INT,
    FOREIGN KEY (userId) REFERENCES users(id)
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    senderAccountNumber VARCHAR(255) NOT NULL,
    receiverAccountNumber VARCHAR(255) NOT NULL,
    description TEXT,
    amount DOUBLE NOT NULL,
    transactionFee DOUBLE NOT NULL DEFAULT 0.0,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    walletId INT,
    FOREIGN KEY (walletId) REFERENCES user_wallet(id)
);

INSERT INTO users (pseudo, email, password, balance) VALUES
('Jane', 'jane@example.com', 'password123', 5000.0),
('Mark', 'mark@example.com', 'password123', 3000.0),
('John', 'john@example.com', 'password123', 2000.0),
('Ken', 'ken@example.com', 'password123', 4000.0);

-- Jane est amie avec Mark et John
INSERT INTO user_friendships (userId, friendId) VALUES
(1, 2),
(1, 3);

-- Mark est ami avec Ken
INSERT INTO user_friendships (userId, friendId) VALUES
(2, 4);

-- John est ami avec Ken
INSERT INTO user_friendships (userId, friendId) VALUES
(3, 4);

INSERT INTO user_wallet (accountNumber, balance, userId) VALUES
('ACC1001', 5000.0, 1),
('ACC1002', 3000.0, 2),
('ACC1003', 2000.0, 3),
('ACC1004', 4000.0, 4);

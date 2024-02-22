CREATE TABLE User (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      pseudo VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      balance DOUBLE DEFAULT 0.0
);

CREATE TABLE Friends (
                         userId INT,
                         friendId INT,
                         PRIMARY KEY (userId, friendId),
                         FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE,
                         FOREIGN KEY (friendId) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Wallet (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        accountNumber VARCHAR(255) NOT NULL UNIQUE,
                        balance DOUBLE NOT NULL DEFAULT 0.0,
                        userId INT,
                        FOREIGN KEY (userId) REFERENCES User(id)
);

CREATE TABLE Transaction (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             senderAccountNumber VARCHAR(255) NOT NULL,
                             receiverAccountNumber VARCHAR(255) NOT NULL,
                             description TEXT,
                             amount DOUBLE NOT NULL,
                             transactionFee DOUBLE NOT NULL DEFAULT 0.0,
                             timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                             walletId INT,
                             FOREIGN KEY (walletId) REFERENCES Wallet(id)
);
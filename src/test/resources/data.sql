-- Assuming 'id' can be manually set for demonstration purposes
INSERT INTO user (id, username, email, password) VALUES
(1, 'User1', 'user1@example.com', 'password1'),
(2, 'User2', 'user2@example.com', 'password2'),
(3, 'User3', 'user3@example.com', 'password3');

INSERT INTO wallet (id, user_id, balance) VALUES
(1, 1, 100.00),
(2, 2, 150.00),
(3, 3, 200.00);

-- Assuming transactions can reference wallet IDs directly
INSERT INTO transaction (id, issuer_wallet, receiver_wallet, amount, description) VALUES
(1, 1, 2, 20.00, 'Lunch payment'),
(2, 2, 3, 15.00, 'Book reimbursement'),
(3, 3, 1, 10.00, 'Coffee treat');

-- Assuming friendships are based on user IDs
INSERT INTO friends (user_id, friend_id) VALUES
(1, 2),
(1, 3),
(2, 1),
(2, 3),
(3, 1),
(3, 2);

CREATE TABLE confirmation_tokens
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    token       VARCHAR(255) NOT NULL,
    created_at   DATETIME NOT NULL,
    expired_at   DATETIME NOT NULL,
    confirmed_at DATETIME,
    user_id     BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

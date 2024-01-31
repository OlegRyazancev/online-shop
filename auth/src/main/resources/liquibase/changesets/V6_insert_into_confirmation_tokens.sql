INSERT INTO confirmation_tokens (token,
                                 created_at,
                                 expired_at,
                                 confirmed_at,
                                 user_id)
VALUES ('token_for_john',
        NOW(),
        NOW() + INTERVAL 15 MINUTE,
        NOW() + INTERVAL 2 MINUTE,
        1),

       ('token_for_jane',
        NOW(),
        NOW() + INTERVAL 15 MINUTE,
        NOW() + INTERVAL 2 MINUTE,
        2),

       ('token_for_bob',
        NOW(),
        NOW() + INTERVAL 15 MINUTE,
        NOW() + INTERVAL 2 MINUTE,
        3),

       ('token_for_alice',
        NOW(),
        NOW() + INTERVAL 15 MINUTE,
        NOW() + INTERVAL 2 MINUTE,
        4),

       ('token_for_charlie',
        NOW(),
        NOW() + INTERVAL 15 MINUTE,
        NOW() + INTERVAL 2 MINUTE,
        5);

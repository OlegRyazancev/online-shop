CREATE TABLE IF NOT EXISTS registration_requests
(
    id                    BIGSERIAL PRIMARY KEY,
    object_to_register_id BIGINT       NOT NULL,
    object_type           VARCHAR(255) NOT NULL,
    status                VARCHAR(255) NOT NULL,
    created_at            TIMESTAMP    NOT NULL,
    reviewed_at           TIMESTAMP
);
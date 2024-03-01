CREATE TABLE organizations
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    description   VARCHAR(500) NOT NULL UNIQUE,
    logo          VARCHAR(255),
    owner_id      BIGINT       NOT NULL,
    status        VARCHAR(255) NOT NULL,
    registered_at TIMESTAMP
);

CREATE TABLE organizations
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    description   VARCHAR(500) NOT NULL,
    logo          VARCHAR(255),
    owner_id      BIGINT       NOT NULL,
    status        VARCHAR(255) NOT NULL,
    registered_at TIMESTAMP
);

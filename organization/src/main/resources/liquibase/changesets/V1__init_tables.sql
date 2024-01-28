CREATE TABLE organizations
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(500) UNIQUE NOT NULL,
    logo        VARCHAR(255),
    owner_id    BIGINT           NOT NULL
);

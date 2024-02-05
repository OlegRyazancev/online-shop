CREATE TABLE products
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name      VARCHAR(255) NOT NULL UNIQUE,
    description       VARCHAR(255) NOT NULL,
    organization_id   BIGINT       NOT NULL,
    price             DOUBLE       NOT NULL CHECK ( price > 0 ),
    quantity_in_stock INT          NOT NULL,
    keywords          TEXT         NOT NULL,
    status            VARCHAR(255) NOT NULL,
    registered_at     TIMESTAMP
);

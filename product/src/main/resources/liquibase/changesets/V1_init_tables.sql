CREATE TABLE products
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    description       VARCHAR(255) NOT NULL,
    organization_id   BIGINT       NOT NULL,
    price             DOUBLE       NOT NULL CHECK ( price > 0 ),
    quantity_in_stock INT          NOT NULL CHECK ( quantity_in_stock > 0 ),
    keywords          TEXT         NOT NULL
);

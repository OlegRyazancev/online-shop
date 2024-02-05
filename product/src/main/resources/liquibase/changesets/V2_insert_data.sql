INSERT INTO products (product_name, description,
                      organization_id, price, quantity_in_stock,
                      keywords, status, registered_at)

VALUES ('Laptop', 'High-performance laptop with SSD storage',
        1, 1299.99, 50,
        'electronics, laptop, SSD', 'ACTIVE', CURRENT_TIMESTAMP),

       ('Smartphone', 'Latest model with dual cameras',
        2, 699.99, 100,
        'electronics, smartphone, camera', 'ACTIVE', CURRENT_TIMESTAMP),

       ('Running Shoes', 'Lightweight running shoes for comfort',
        3, 79.99, 200,
        'footwear, sports, running', 'ACTIVE', CURRENT_TIMESTAMP),

       ('Coffee Maker', 'Automatic coffee maker with built-in grinder',
        1, 149.99, 30,
        'appliances, coffee, kitchen', 'ACTIVE', CURRENT_TIMESTAMP),

       ('Headphones', 'Noise-canceling wireless headphones',
        2, 199.99, 50,
        'electronics, headphones, wireless', 'ACTIVE', CURRENT_TIMESTAMP);

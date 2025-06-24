-- 1. user_accountINSERT INTO user_account (create_at, email, password, role, username) VALUES
('2024-06-01 10:00:00', 'useracc1@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc1'),
('2024-06-02 10:00:00', 'useracc2@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc2'),
('2024-06-03 10:00:00', 'useracc3@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc3'),
('2024-06-04 10:00:00', 'useracc4@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc4'),
('2024-06-05 10:00:00', 'useracc5@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc5'),
('2024-06-06 10:00:00', 'useracc6@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc6'),
('2024-06-07 10:00:00', 'useracc7@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc7'),
('2024-06-08 10:00:00', 'useracc8@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc8'),
('2024-06-09 10:00:00', 'useracc9@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc9'),
('2024-06-10 10:00:00', 'useracc10@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'CUSTOMER', 'useracc10'),
('2024-06-01 12:00:00', 'manageracc1@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'MANAGER', 'manageracc1'),
('2024-06-02 12:00:00', 'manageracc2@example.com', '$2a$10$9IMAT.9RFpznQJsD7oxwbOfFGDM6Qj3/e.do7iY52kmVYz.lI49Pe', 'MANAGER', 'manageracc2');

-- 2. category
-- Insert 9 categories
INSERT INTO category (category_name, category_description) VALUES
('Electronics', 'Electronic devices and gadgets'),
('Fashion and Apparel', 'Clothing and accessories'),
('Beauty and Personal Care', 'Cosmetics and personal care products'),
('Furniture', 'Home and office furniture'),
('Beverages', 'Drinks and beverages'),
('Food', 'Food items and snacks'),
('Household Essentials', 'Household cleaning and essentials'),
('Toys and Hobbies', 'Toys and hobby-related products'),
('Media', 'Books, movies, and music');

-- 3. product
INSERT INTO product 
(product_name, product_description, product_price, product_value, product_barcode, product_status, category_id, 
 product_specification, product_weight, product_rush_eligible, stock_quantity, warehouse_entry_timestamp)
VALUES
('Smartphone X', 'Latest smartphone model', 17500000, 17500000, 'ELEC001', 'ACTIVE', 1, '5G, 128GB', 0.18, TRUE, 50, '2024-07-01 08:00:00'),
('Wireless Earbuds', 'Noise-cancelling earbuds', 3750000, 3750000, 'ELEC002', 'ACTIVE', 1, 'Bluetooth 5.0', 0.05, TRUE, 100, '2024-07-02 08:00:00'),
('Denim Jacket', 'Stylish denim jacket', 1500000, 1500000, 'FASH001', 'ACTIVE', 2, 'Medium size', 0.6, TRUE, 30, '2024-07-03 08:00:00'),
('Sneakers', 'Comfortable running shoes', 2250000, 2250000, 'FASH002', 'ACTIVE', 2, 'Size 10', 0.8, TRUE, 40, '2024-07-04 08:00:00'),
('Moisturizer', 'Hydrating face cream', 750000, 750000, 'BEAU001', 'ACTIVE', 3, '50ml', 0.1, TRUE, 60, '2024-07-05 08:00:00'),
('Lipstick', 'Long-lasting lipstick', 500000, 500000, 'BEAU002', 'ACTIVE', 3, 'Matte finish', 0.05, TRUE, 80, '2024-07-06 08:00:00'),
('Coffee Table', 'Wooden coffee table', 5000000, 5000000, 'FURN001', 'ACTIVE', 4, 'Oak wood', 20.0, FALSE, 20, '2024-07-07 08:00:00'),
('Office Chair', 'Ergonomic office chair', 3750000, 3750000, 'FURN002', 'ACTIVE', 4, 'Adjustable height', 15.0, FALSE, 25, '2024-07-08 08:00:00'),
('Cola Drink', 'Carbonated cola', 50000, 50000, 'BEV001', 'ACTIVE', 5, '500ml', 0.5, FALSE, 200, '2024-07-09 08:00:00'),
('Orange Juice', 'Fresh orange juice', 100000, 100000, 'BEV002', 'ACTIVE', 5, '1L', 1.0, FALSE, 150, '2024-07-10 08:00:00'),
('Chocolate Bar', 'Milk chocolate bar', 75000, 75000, 'FOOD001', 'ACTIVE', 6, '100g', 0.1, TRUE, 300, '2024-07-11 08:00:00'),
('Pasta Pack', 'Italian pasta', 125000, 125000, 'FOOD002', 'ACTIVE', 6, '500g', 0.5, TRUE, 120, '2024-07-12 08:00:00'),
('Detergent', 'Laundry detergent', 250000, 250000, 'HOUSE001', 'ACTIVE', 7, '1kg', 1.0, FALSE, 90, '2024-07-13 08:00:00'),
('Dish Soap', 'Dishwashing liquid', 100000, 100000, 'HOUSE002', 'ACTIVE', 7, '500ml', 0.5, FALSE, 110, '2024-07-14 08:00:00'),
('Action Figure', 'Superhero action figure', 625000, 625000, 'TOY001', 'ACTIVE', 8, '12 inches', 0.4, TRUE, 70, '2024-07-15 08:00:00'),
('Puzzle Set', '1000-piece puzzle', 500000, 500000, 'TOY002', 'ACTIVE', 8, 'Landscape design', 0.7, TRUE, 60, '2024-07-16 08:00:00'),
('Novel Book', 'Bestselling novel', 375000, 375000, 'MEDIA001', 'ACTIVE', 9, 'Paperback', 0.3, TRUE, 100, '2024-07-17 08:00:00'),
('Music CD', 'Pop music album', 325000, 325000, 'MEDIA002', 'ACTIVE', 9, 'CD format', 0.1, TRUE, 80, '2024-07-18 08:00:00');


-- 4. product_image
-- At least 1 image per product (18 images)
INSERT INTO product_image (image_url, product_id) VALUES
('http://example.com/images/smartphone_x.jpg', 1),
('http://example.com/images/wireless_earbuds.jpg', 2),
('http://example.com/images/denim_jacket.jpg', 3),
('http://example.com/images/sneakers.jpg', 4),
('http://example.com/images/moisturizer.jpg', 5),
('http://example.com/images/lipstick.jpg', 6),
('http://example.com/images/coffee_table.jpg', 7),
('http://example.com/images/office_chair.jpg', 8),
('http://example.com/images/cola_drink.jpg', 9),
('http://example.com/images/orange_juice.jpg', 10),
('http://example.com/images/chocolate_bar.jpg', 11),
('http://example.com/images/pasta_pack.jpg', 12),
('http://example.com/images/detergent.jpg', 13),
('http://example.com/images/dish_soap.jpg', 14),
('http://example.com/images/action_figure.jpg', 15),
('http://example.com/images/puzzle_set.jpg', 16),
('http://example.com/images/novel_book.jpg', 17),
('http://example.com/images/music_cd.jpg', 18);

-- 5. product_review
-- At least 2 reviews per product (36 reviews)
INSERT INTO product_review (review_comment, review_timestamp, rating, product_id, user_id) VALUES
('Great phone, fast delivery!', '2024-08-01 12:00:00', 5, 1, 1),
('Battery life is amazing.', '2024-08-02 12:00:00', 4, 1, 2),
('Clear sound quality.', '2024-08-03 12:00:00', 4, 2, 3),
('Comfortable fit!', '2024-08-04 12:00:00', 5, 2, 4),
('Stylish and durable.', '2024-08-05 12:00:00', 4, 3, 5),
('Perfect for casual wear.', '2024-08-06 12:00:00', 5, 3, 6),
('Very comfortable shoes.', '2024-08-07 12:00:00', 5, 4, 7),
('Great for running.', '2024-08-08 12:00:00', 4, 4, 8),
('Keeps skin hydrated.', '2024-08-09 12:00:00', 5, 5, 9),
('Non-greasy formula.', '2024-08-10 12:00:00', 4, 5, 10),
('Vibrant color!', '2024-08-11 12:00:00', 5, 6, 1),
('Long-lasting.', '2024-08-12 12:00:00', 4, 6, 2),
('Elegant design.', '2024-08-13 12:00:00', 5, 7, 3),
('Sturdy build.', '2024-08-14 12:00:00', 4, 7, 4),
('Very comfortable chair.', '2024-08-15 12:00:00', 5, 8, 5),
('Easy to adjust.', '2024-08-16 12:00:00', 4, 8, 6),
('Refreshing taste.', '2024-08-17 12:00:00', 5, 9, 7),
('Great value.', '2024-08-18 12:00:00', 4, 9, 8),
('Tastes amazing.', '2024-08-19 12:00:00', 5, 10, 9),
('Fresh and healthy.', '2024-08-20 12:00:00', 4, 10, 10),
('Delicious chocolate.', '2024-08-21 12:00:00', 5, 11, 1),
('Perfect snack.', '2024-08-22 12:00:00', 4, 11, 2),
('Cooks perfectly.', '2024-08-23 12:00:00', 5, 12, 3),
('Good quality pasta.', '2024-08-24 12:00:00', 4, 12, 4),
('Cleans well.', '2024-08-25 12:00:00', 5, 13, 5),
('Gentle on clothes.', '2024-08-26 12:00:00', 4, 13, 6),
('Effective soap.', '2024-08-27 12:00:00', 5, 14, 7),
('Nice scent.', '2024-08-28 12:00:00', 4, 14, 8),
('Kids love it!', '2024-08-29 12:00:00', 5, 15, 9),
('Well-made toy.', '2024-08-30 12:00:00', 4, 15, 10),
('Fun puzzle.', '2024-08-31 12:00:00', 5, 16, 1),
('Great for family time.', '2024-09-01 12:00:00', 4, 16, 2),
('Engaging story.', '2024-09-02 12:00:00', 5, 17, 3),
('Great read.', '2024-09-03 12:00:00', 4, 17, 4),
('Awesome album!', '2024-09-04 12:00:00', 5, 18, 5),
('Good sound quality.', '2024-09-05 12:00:00', 4, 18, 6);

-- 6. related_product
-- Add related products based on same category
INSERT INTO related_product (relation_type, product_id, related_product_id) VALUES
('SIMILAR', 1, 2), -- Smartphone and Earbuds
('SIMILAR', 3, 4), -- Jacket and Sneakers
('SIMILAR', 5, 6), -- Moisturizer and Lipstick
('SIMILAR', 7, 8), -- Coffee Table and Office Chair
('SIMILAR', 9, 10), -- Cola and Orange Juice
('SIMILAR', 11, 12), -- Chocolate and Pasta
('SIMILAR', 13, 14), -- Detergent and Dish Soap
('SIMILAR', 15, 16), -- Action Figure and Puzzle
('SIMILAR', 17, 18), -- Novel and Music CD
('FREQUENTLY_BOUGHT_TOGETHER', 9, 11); -- Cola and Chocolate

-- 7. product_edit_history
-- Random edits by managers for each product, updating price-related changes to VND
INSERT INTO product_edit_history (changes_made, edit_timestamp, editor_user_id, product_id) VALUES
('Added new product', '2024-07-01 09:00:00', 11, 1),
('Updated price to 17500000 VND', '2024-07-02 09:00:00', 12, 1),
('Added new product', '2024-07-03 09:00:00', 11, 2),
('Updated quantity to 100', '2024-07-04 09:00:00', 12, 2),
('Added new product', '2024-07-05 09:00:00', 11, 3),
('Updated description', '2024-07-06 09:00:00', 12, 3),
('Added new product', '2024-07-07 09:00:00', 11, 4),
('Updated price to 2250000 VND', '2024-07-08 09:00:00', 12, 4),
('Added new product', '2024-07-09 09:00:00', 11, 5),
('Updated quantity to 60', '2024-07-10 09:00:00', 12, 5),
('Added new product', '2024-07-11 09:00:00', 11, 6),
('Updated description', '2024-07-12 09:00:00', 12, 6),
('Added new product', '2024-07-13 09:00:00', 11, 7),
('Updated price to 5000000 VND', '2024-07-14 09:00:00', 12, 7),
('Added new product', '2024-07-15 09:00:00', 11, 8),
('Updated quantity to 25', '2024-07-16 09:00:00', 12, 8),
('Added new product', '2024-07-17 09:00:00', 11, 9),
('Updated description', '2024-07-18 09:00:00', 12, 9),
('Added new product', '2024-07-19 09:00:00', 11, 10),
('Updated price to 100000 VND', '2024-07-20 09:00:00', 12, 10),
('Added new product', '2024-07-21 09:00:00', 11, 11),
('Updated quantity to 300', '2024-07-22 09:00:00', 12, 11),
('Added new product', '2024-07-23 09:00:00', 11, 12),
('Updated description', '2024-07-24 09:00:00', 12, 12),
('Added new product', '2024-07-25 09:00:00', 11, 13),
('Updated price to 250000 VND', '2024-07-26 09:00:00', 12, 13),
('Added new product', '2024-07-27 09:00:00', 11, 14),
('Updated quantity to 110', '2024-07-28 09:00:00', 12, 14),
('Added new product', '2024-07-29 09:00:00', 11, 15),
('Updated description', '2024-07-30 09:00:00', 12, 15),
('Added new product', '2024-07-31 09:00:00', 11, 16),
('Updated price to 500000 VND', '2024-08-01 09:00:00', 12, 16),
('Added new product', '2024-08-02 09:00:00', 11, 17),
('Updated quantity to 100', '2024-08-03 09:00:00', 12, 17),
('Added new product', '2024-08-04 09:00:00', 11, 18),
('Updated description', '2024-08-05 09:00:00', 12, 18);

-- 8. cart
-- Create carts for all 12 users (10 customers + 2 managers), update totals to VND
INSERT INTO cart (total, user_id) VALUES
(0, 1), (0, 2), (0, 3), (0, 4), (0, 5), (0, 6), -- 6 empty carts
(3750000, 7), (750000, 8), (1500000, 9), (625000, 10), (2250000, 11), (5000000, 12); -- 6 with items

-- 9. cart_item
-- 6 users with empty carts (1-6), 6 users with items (7-12), prices in VND
INSERT INTO cart_item (product_price, quantity, cart_id, product_id) VALUES
(3750000, 1, 7, 2), -- User 7: Wireless Earbuds
(750000, 1, 8, 5), -- User 8: Moisturizer
(1500000, 1, 9, 3), -- User 9: Denim Jacket
(625000, 1, 10, 15), -- User 10: Action Figure
(2250000, 1, 11, 4), -- User 11: Sneakers
(5000000, 1, 12, 7); -- User 12: Coffee Table

-- 10. delivery_info
-- Delivery info for 3 users (7, 8, 9)
INSERT INTO delivery_info (address, email, phone_number, province_city, recipient_name, delivery_instruction) VALUES
('123 Main St', 'useracc7@example.com', '555-0101', 'New York', 'John Doe', 'Leave at door'),
('456 Oak Ave', 'useracc8@example.com', '555-0102', 'California', 'Jane Smith', 'Call on arrival'),
('789 Pine Rd', 'useracc9@example.com', '555-0103', 'Texas', 'Alice Johnson', 'Deliver to reception');

-- 11. orders
-- Create 3 orders for users 7, 8, 9, prices in VND, shipping fee 100000 VND
INSERT INTO orders (created_timestamp, discount, is_rush_order, order_status, payment_status, shipping_fee, subtotal, total, delivery_info_id, customer_id) VALUES
('2024-09-01 10:00:00', 0, false, 'PENDING', 'UNPAID', 100000, 3750000, 3850000, 1, 7),
('2024-09-02 10:00:00', 0, false, 'CONFIRMED', 'PAID', 100000, 750000, 850000, 2, 8),
('2024-09-03 10:00:00', 0, false, 'CANCELLED', 'REFUNDED', 100000, 1500000, 1600000, 3, 9);

-- 12. order_item
-- Order items based on cart items for users 7, 8, 9, prices in VND
INSERT INTO order_item (quantity, unit_price, order_id, product_id) VALUES
(1, 3750000, 1, 2), -- Order 1: Wireless Earbuds
(1, 750000, 2, 5), -- Order 2: Moisturizer
(1, 1500000, 3, 3); -- Order 3: Denim Jacket

-- 13. payment_transaction
-- One order paid, one refunded, one pending, amounts in VND
INSERT INTO payment_transaction (payment_method, payment_status, refund_status, total_amount, transaction_code, transaction_content, transaction_timestamp, transaction_type, order_id) VALUES
('VNPAY', 'PAID', 'NOT_REQUESTED', 3850000, 'TXN001', 'Payment for order 1', '2024-09-01 11:00:00', 'PAYMENT', 1),
('VNPAY', 'PAID', 'NOT_REQUESTED', 850000, 'TXN002', 'Payment for order 2', '2024-09-02 11:00:00', 'PAYMENT', 2),
('VNPAY', 'REFUNDED', 'REFUNDED', 1600000, 'TXN003', 'Refund for order 3', '2024-09-03 11:00:00', 'REFUND', 3);

-- 14. audit_log
-- Log actions for various activities
INSERT INTO audit_log (action_type, keyword, role, timestamp, product_id, user_id) VALUES
('LOGIN', 'User login', 0, '2024-09-01 09:00:00', NULL, 1),
('LOGOUT', 'User logout', 0, '2024-09-01 09:30:00', NULL, 1),
('CHANGE_PASSWORD', 'Password changed', 0, '2024-09-01 10:00:00', NULL, 2),
('PLACE_ORDER', 'Order placed', 0, '2024-09-01 10:00:00', NULL, 7),
('VIEW_ORDER', 'Order viewed', 0, '2024-09-01 10:05:00', NULL, 7),
('PAY_ORDER', 'Order paid', 0, '2024-09-01 11:00:00', NULL, 7),
('CANCEL_ORDER', 'Order cancelled', 0, '2024-09-03 10:00:00', NULL, 9),
('SEARCH_PRODUCTS', 'Searched smartphones', 0, '2024-09-01 12:00:00', 1, 1),
('VIEW_PRODUCT', 'Viewed product', 0, '2024-09-01 12:05:00', 1, 1),
('ADD_PRODUCT', 'Product added', 1, '2024-07-01 09:00:00', 1, 11),
('DELETE_PRODUCT', 'Product deleted', 1, '2024-07-02 09:00:00', NULL, 12),
('UPDATE_PRODUCT', 'Product updated', 1, '2024-07-03 09:00:00', 1, 11);
--init data for testing

-- Insert data into user_account
INSERT INTO public.user_account (user_id, create_at, email, password, role, username) VALUES
(1, '2025-05-01 10:00:00', 'john.doe@example.com', 'pass123', 'CUSTOMER', 'johndoe'),
(2, '2025-05-02 12:00:00', 'jane.smith@example.com', 'secure456', 'CUSTOMER', 'janesmith'),
(3, '2025-05-03 14:00:00', 'admin@example.com', 'admin789', 'MANAGER', 'adminuser');

-- Insert data into category
INSERT INTO public.category (category_id, category_description, category_name) VALUES
(1, 'Electronics and gadgets', 'Electronics'),
(2, 'Books and literature', 'Books'),
(3, 'Clothing and apparel', 'Clothing');

-- Insert data into product
INSERT INTO public.product (product_id, product_barcode, product_description, product_name, product_price, product_status, product_specification, stock_quantity, product_value, warehouse_entry_timestamp, category_id) VALUES
(1, 'ABC123456789', 'Smartphone with 128GB storage', 'Smartphone X', 599.99, 'ACTIVE', '6.5" display, 8GB RAM', 50, 499.99, '2025-05-10 09:00:00', 1),
(2, 'DEF987654321', 'Hardcover novel', 'Great Novel', 29.99, 'ACTIVE', '500 pages, hardcover', 100, 24.99, '2025-05-11 10:00:00', 2),
(3, 'GHI456789123', 'Cotton t-shirt', 'Basic Tee', 19.99, 'ACTIVE', '100% cotton, medium size', 200, 15.99, '2025-05-12 11:00:00', 3);

-- Insert data into product_image
INSERT INTO public.product_image (image_id, image_url, product_id) VALUES
(1, 'http://example.com/images/smartphone_x.jpg', 1),
(2, 'http://example.com/images/great_novel.jpg', 2),
(3, 'http://example.com/images/basic_tee.jpg', 3);

-- Insert data into product_review
INSERT INTO public.product_review (review_id, review_comment, review_timestamp, rating, product_id, user_id) VALUES
(1, 'Great phone, fast delivery!', '2025-05-15 15:00:00', 5, 1, 1),
(2, 'Really enjoyed this book.', '2025-05-16 16:00:00', 4, 2, 2),
(3, 'Comfortable t-shirt, good quality.', '2025-05-17 17:00:00', 4, 3, 1);

-- Insert data into product_edit_history
INSERT INTO public.product_edit_history (edit_id, changes_made, edit_timestamp, editor_user_id, product_id) VALUES
(1, 'Updated product price', '2025-05-14 08:00:00', 3, 1),
(2, 'Changed stock quantity', '2025-05-15 09:00:00', 3, 2),
(3, 'Modified product description', '2025-05-16 10:00:00', 3, 3);

-- Insert data into related_product
INSERT INTO public.related_product (relation_id, relation_type, product_id, related_product_id) VALUES
(1, 'SIMILAR', 1, 2),
(2, 'FREQUENTLY_BOUGHT_TOGETHER', 2, 3),
(3, 'ACCESSORY', 1, 3);

-- Insert data into cart
INSERT INTO public.cart (cart_id, total, user_id) VALUES
(1, 619.98, 1),
(2, 29.99, 2),
(3, 0.00, 3);

-- Insert data into cart_item
INSERT INTO public.cart_item (product_price, quantity, cart_id, product_id) VALUES
(599.99, 1, 1, 1),
(19.99, 1, 1, 3),
(29.99, 1, 2, 2);

-- Insert data into delivery_info
INSERT INTO public.delivery_info (delivery_info_id, address, email, phone_number, province_city, recipient_name, shipping_instruction) VALUES
(1, '123 Main St', 'john.doe@example.com', '555-1234', 'New York', 'John Doe', 'Leave at front door'),
(2, '456 Elm St', 'jane.smith@example.com', '555-5678', 'California', 'Jane Smith', 'Ring doorbell'),
(3, '789 Oak St', 'admin@example.com', '555-9012', 'Texas', 'Admin User', 'Call upon arrival');

-- Insert data into orders
INSERT INTO public.orders (order_id, created_timestamp, discount, is_rush_order, order_status, payment_status, shipping_fee, subtotal, total, delivery_info_id, customer_id) VALUES
(1, '2025-05-20 10:00:00', 10.00, false, 'PENDING', 'UNPAID', 5.99, 619.98, 675.97, 1, 1),
(2, '2025-05-21 11:00:00', 0.00, true, 'CONFIRMED', 'PAID', 7.99, 29.99, 40.98, 2, 2),
(3, '2025-05-22 12:00:00', 5.00, false, 'SHIPPED', 'PAID', 4.99, 19.99, 24.48, 3, 1);

-- Insert data into order_item
INSERT INTO public.order_item (order_item_id, quantity, unit_price, order_id, product_id) VALUES
(1, 1, 599.99, 1, 1),
(2, 1, 19.99, 1, 3),
(3, 1, 29.99, 2, 2),
(4, 1, 19.99, 3, 3);

-- Insert data into payment_transaction
INSERT INTO public.payment_transaction (transaction_id, transaction_code, payment_method, payment_status, refund_timestamp, refund_status, total_amount, transaction_content, transaction_timestamp, transaction_type, order_id) VALUES
(1, 'TXN001', 'VNPAY', 'PAID', NULL, 'NOT_REQUESTED', 675.97, 'Payment for order #1', '2025-05-20 10:05:00', 'PAYMENT', 1),
(2, 'TXN002', 'COD', 'PAID', NULL, 'NOT_REQUESTED', 40.98, 'Payment for order #2', '2025-05-21 11:05:00', 'PAYMENT', 2),
(3, 'TXN003', 'PAYPAL', 'PAID', '2025-05-23 09:00:00', 'REQUESTED', 24.48, 'Payment for order #3', '2025-05-22 12:05:00', 'PAYMENT', 3);

-- Insert data into audit_log
INSERT INTO public.audit_log (audit_log_id, action_type, keyword, role, "timestamp", product_id, user_id) VALUES
(1, 'LOGIN', NULL, 0, '2025-05-20 09:55:00', NULL, 1),
(2, 'VIEW_PRODUCT', 'Smartphone', 0, '2025-05-20 10:00:00', 1, 1),
(3, 'PLACE_ORDER', NULL, 0, '2025-05-20 10:05:00', NULL, 1),
(4, 'UPDATE_PRODUCT', 'Price change', 1, '2025-05-21 09:00:00', 1, 3);
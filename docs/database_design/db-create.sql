-- Script tạo bảng cho hệ thống e-commerce của bạn trong PostgreSQL
-- Có các ràng buộc khóa chính, khóa ngoại, ON DELETE phù hợp

CREATE TABLE user_account (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('manager', 'customer')) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    category_description TEXT
);

CREATE TABLE product (
    product_id BIGSERIAL PRIMARY KEY,
    category_id INTEGER NOT NULL REFERENCES category(category_id) ON DELETE RESTRICT,
    product_name VARCHAR(50) NOT NULL,
    product_price DECIMAL(10,2) NOT NULL,
    product_value DECIMAL(10,2) NOT NULL,
    product_barcode VARCHAR(20) UNIQUE NOT NULL,
    product_description TEXT,
    product_specification TEXT,
    stock_quantity INTEGER NOT NULL,
    warehouse_entry_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE product_image (
    image_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE product_review (
    review_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES user_account(user_id) ON DELETE RESTRICT,
    rating SMALLINT CHECK (rating BETWEEN 1 AND 5) NOT NULL,
    review_comment TEXT,
    review_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE product_edit_history (
    edit_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    editor_user_id INTEGER NOT NULL REFERENCES user_account(user_id) ON DELETE RESTRICT,
    edit_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    changes_made TEXT
);

CREATE TABLE related_product (
    relation_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    related_product_id BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    relation_type VARCHAR(20) CHECK (relation_type IN ('similar', 'bundle', 'accessory'))
);

CREATE TABLE delivery_info (
    delivery_info_id BIGSERIAL PRIMARY KEY,
    recipient_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    province_city VARCHAR(50) NOT NULL,
    address TEXT NOT NULL,
    shipping_instruction TEXT
);

CREATE TABLE "order" (
    order_id BIGSERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL REFERENCES user_account(user_id) ON DELETE RESTRICT,
    order_status VARCHAR(20) CHECK (order_status IN ('pending', 'processing', 'shipped', 'delivered', 'cancelled')) NOT NULL,
    is_rush_order BOOLEAN NOT NULL,
    delivery_info_id BIGINT NOT NULL REFERENCES delivery_info(delivery_info_id) ON DELETE RESTRICT,
    subtotal DECIMAL(10,2) NOT NULL,
    shipping_fee DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    discount DECIMAL(10,2),
    payment_status VARCHAR(20) CHECK (payment_status IN ('pending', 'completed', 'failed')) NOT NULL
);

CREATE TABLE order_item (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES "order"(order_id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES product(product_id) ON DELETE RESTRICT,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL
);

CREATE TABLE payment_transaction (
    transaction_id BIGSERIAL PRIMARY KEY,
    transaction_type VARCHAR(10) CHECK (transaction_type IN ('payment', 'refund')) NOT NULL,
    order_id BIGINT NOT NULL REFERENCES "order"(order_id) ON DELETE RESTRICT,
    total_amount DECIMAL(10,2) NOT NULL,
    transaction_content VARCHAR(100),
    transaction_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    payment_method VARCHAR(20) CHECK (payment_method IN ('card', 'paypal', 'bank_transfer')) NOT NULL,
    payment_status VARCHAR(20) CHECK (payment_status IN ('successful', 'failed', 'pending')) NOT NULL,
    refund_status VARCHAR(20) CHECK (refund_status IN ('none', 'pending', 'completed')),
    refund_timestamp TIMESTAMP
);

CREATE TABLE cart (
    user_id INTEGER PRIMARY KEY REFERENCES user_account(user_id) ON DELETE CASCADE,
    total DECIMAL(10,2) NOT NULL
);

CREATE TABLE cart_item (
    user_id INTEGER NOT NULL REFERENCES cart(user_id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL,
    product_price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY(user_id, product_id)
);

CREATE TABLE audit_log (
    audit_log_id BIGSERIAL PRIMARY KEY,
    action_type VARCHAR(20) CHECK (action_type IN (
        'SEARCH', 'VIEW_PRODUCT', 'ADD_PRODUCT', 'DELETE_PRODUCT',
        'UPDATE_PRODUCT', 'PLACE_ORDER', 'PAYMENT', 'CANCEL_ORDER')) NOT NULL,
    user_id INTEGER NOT NULL REFERENCES user_account(user_id) ON DELETE RESTRICT,
    product_id BIGINT REFERENCES product(product_id) ON DELETE SET NULL,
    keyword VARCHAR(100),
    role VARCHAR(10) CHECK (role IN ('manager', 'customer')) NOT NULL,
    action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
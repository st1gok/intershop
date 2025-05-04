DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS carts;

CREATE TABLE products
(
    id SERIAL PRIMARY KEY,
    title            varchar(100)  NOT NULL,
    description      text,
    img_path         text,
    price numeric
);

CREATE TABLE carts
(
    id SERIAL PRIMARY KEY
);

CREATE TABLE cart_items
(
    id SERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    count int NOT NULL
);

CREATE TABLE orders
(
    id SERIAL PRIMARY KEY,
    total_sum numeric
);

CREATE TABLE order_items
(
    id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    price numeric,
    title varchar(100)  NOT NULL,
    img_path    text,
    description      text,
    count int NOT NULL
);


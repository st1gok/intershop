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
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    count int NOT NULL
);

CREATE TABLE orders
(
    id SERIAL PRIMARY KEY
);

CREATE TABLE order_items
(
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    count int NOT NULL
);


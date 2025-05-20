CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    login            varchar(100)  NOT NULL,
    password_hash      varchar(100) NOT NULL
);

CREATE TABLE authority
(
    name varchar(50) PRIMARY KEY
);

CREATE TABLE user_authority
(
    id SERIAL PRIMARY KEY,
    user_id integer NOT NULL,
    authority_name varchar(50) NOT NULL
);

ALTER TABLE cart_items RENAME column cart_id TO user_id;

ALTER TABLE orders add column user_id BIGINT NOT NULL;

INSERT INTO authority (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

INSERT INTO users (id,login,password_hash)
VALUES (1,'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC'),
       (2,'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K');

INSERT INTO user_authority (user_id, authority_name)
VALUES (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');


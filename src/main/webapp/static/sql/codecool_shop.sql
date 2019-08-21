ALTER TABLE IF EXISTS ONLY public.customer DROP CONSTRAINT IF EXISTS pk_customer_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.orders DROP CONSTRAINT IF EXISTS pk_order_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.orders DROP CONSTRAINT IF EXISTS fk_customer_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.order_item DROP CONSTRAINT IF EXISTS pk_order_item_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.order_item DROP CONSTRAINT IF EXISTS fk_order_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.order_item DROP CONSTRAINT IF EXISTS fk_product_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS pk_product_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS fk_category_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS fk_supplier_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.categories DROP CONSTRAINT IF EXISTS pk_category_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.suppliers DROP CONSTRAINT IF EXISTS pk_supplier_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.delivery_information DROP CONSTRAINT IF EXISTS pk_delivery_information_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.delivery_information DROP CONSTRAINT IF EXISTS fk_order_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.delivery_information DROP CONSTRAINT IF EXISTS fk_address_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.addresses DROP CONSTRAINT IF EXISTS pk_addresses_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.addresses DROP CONSTRAINT IF EXISTS fk_delivery_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.addresses DROP CONSTRAINT IF EXISTS fk_delivery_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.customers_information DROP CONSTRAINT IF EXISTS fk_customer_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.customers_information DROP CONSTRAINT IF EXISTS pk_customer_inf_id CASCADE;


DROP TABLE IF EXISTS public.customers;
DROP SEQUENCE IF EXISTS public.customers_id_seq;
CREATE TABLE customers (
    id serial NOT NULL,
    user_name varchar(25),
    password text,
    email varchar(50)
);

DROP TABLE IF EXISTS public.customers_information;
DROP SEQUENCE IF EXISTS public.customers_information_id_seq;
CREATE TABLE customers_information (
    id int NOT NULL,
    customer_id int not null,
    name varchar(25),
    email varchar(50),
    phone_number varchar(25),
    shipping_address_id int,
    billing_address_id int
);

DROP TABLE IF EXISTS public.delivery_information;
DROP SEQUENCE IF EXISTS public.delivery_information_id_seq;
CREATE TABLE delivery_information (
    id int NOT NULL,
    order_id integer,
    name varchar(25),
    email varchar(50),
    phone_number varchar(25),
    shipping_address_id int,
    billing_address_id int
);

DROP TABLE IF EXISTS public.addresses;
DROP SEQUENCE IF EXISTS public.addresses_id_seq;
CREATE TABLE addresses (
    id int NOT NULL,
    country varchar(15),
    city varchar(15),
    zip_code int,
    address varchar(50),
    address_type varchar(15)
);

DROP TABLE IF EXISTS public.orders;
DROP SEQUENCE IF EXISTS public.orders_id_seq;
CREATE TABLE orders (
    id int NOT NULL,
    customer_id integer,
    status varchar(10)
);

DROP TABLE IF EXISTS public.order_item;
DROP SEQUENCE IF EXISTS public.order_item_id_seq;
CREATE TABLE order_item (
    id serial not null,
    order_id integer,
    product_id integer
);

DROP TABLE IF EXISTS public.products;
DROP SEQUENCE IF EXISTS public.products_id_seq;
CREATE TABLE products (
    id int not null,
    name varchar(25),
    description text,
    default_price FLOAT,
    default_currency varchar(4),
    category_id int,
    supplier_id int
);

DROP TABLE IF EXISTS public.categories;
DROP SEQUENCE IF EXISTS public.categories_id_seq;
CREATE TABLE categories (
    id int not null,
    name varchar(15),
    department varchar(15),
    description text
);

DROP TABLE IF EXISTS public.suppliers;
DROP SEQUENCE IF EXISTS public.suppliers_id_seq;
CREATE TABLE suppliers (
    id int not null,
    name varchar(15),
    description text
);

ALTER TABLE ONLY customers ADD CONSTRAINT pk_customer_id PRIMARY KEY (id);
ALTER TABLE ONLY orders ADD CONSTRAINT pk_order_id PRIMARY KEY (id);
ALTER TABLE ONLY order_item ADD CONSTRAINT pk_order_item_id PRIMARY KEY (id);
ALTER TABLE ONLY products ADD CONSTRAINT pk_product_id PRIMARY KEY (id);
ALTER TABLE ONLY categories ADD CONSTRAINT pk_category_id PRIMARY KEY (id);
ALTER TABLE ONLY suppliers ADD CONSTRAINT pk_supplier_id PRIMARY KEY (id);
ALTER TABLE ONLY delivery_information ADD CONSTRAINT pk_delivery_information_id PRIMARY KEY (id);
ALTER TABLE ONLY addresses ADD CONSTRAINT pk_addresses_id PRIMARY KEY (id);
ALTER TABLE ONLY customers_information ADD CONSTRAINT pk_customer_inf_id PRIMARY KEY (id);

ALTER TABLE ONLY orders ADD CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES customers(id);
ALTER TABLE ONLY order_item ADD CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE ONLY order_item ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES products(id);
ALTER TABLE ONLY products ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories(id);
ALTER TABLE ONLY products ADD CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES suppliers(id);
ALTER TABLE ONLY delivery_information ADD CONSTRAINT fk_shipping_address_id FOREIGN KEY (shipping_address_id) REFERENCES addresses(id);
ALTER TABLE ONLY delivery_information ADD CONSTRAINT fk_billing_address_id FOREIGN KEY (billing_address_id) REFERENCES addresses(id) ;
ALTER TABLE ONLY delivery_information ADD CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE ONLY customers_information ADD CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES customers(id);

INSERT INTO categories (id, name, department, description)
VALUES (1, 'Tablet', 'Hardware', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.');
INSERT INTO categories (id, name, department, description)
VALUES (2, 'Laptop', 'Hardware', 'A laptop computer is a small, portable personal computer (PC), typically having a thin LCD or LED computer screen mounted on the inside of the upper lid and an alphanumeric keyboard on the inside of the lower lid.');
INSERT INTO categories (id, name, department, description)
VALUES (3, 'Phone', 'Hardware', 'A mobile phone, cell phone, cellphone, or hand phone, sometimes shortened to simply mobile, cell or just phone, is a portable telephone that can make and receive calls over a radio frequency link while the user is moving within a telephone service area.');
INSERT INTO categories (id, name, department, description)
VALUES (4, 'Desktop', 'Hardware', 'A desktop computer is a personal computer designed for regular use at a single location on or near a desk or table due to its size and power requirements.');

INSERT INTO suppliers (id, name, description)
VALUES (1, 'Amazon', 'Digital content and services');
INSERT INTO suppliers (id, name, description)
VALUES (2, 'Lenovo', 'Computers');
INSERT INTO suppliers (id, name, description)
VALUES (3, 'Asus', 'Computers');
INSERT INTO suppliers (id, name, description)
VALUES (4, 'Apple', 'Hardware products');

INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)
VALUES(1, 'Amazon Fire', 'Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.', 49.9, 'USD', 1, 1);
INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)
VALUES(2, 'Lenovo IdeaPad Miix 700', 'Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.', 479, 'USD', 1, 2);
INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)
VALUES(3, 'Amazon Fire HD 8', 'Amazons latest Fire HD 8 tablet is a great value for media consumption.', 89, 'USD', 1, 1);
INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)
VALUES(4, 'Asus ROG Zephyrus', 'ROG Zephyrus is a revolutionary gaming laptop born from ROG’s persistent dedication to innovation.', 1499, 'USD', 2, 3);
INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)
VALUES(5, 'Apple iPhone X', 'All-screen design. Longest battery life ever in an iPhone. Fastest performance Studio-quality photos.', 1189.9, 'USD', 3, 4);
INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)
VALUES(6, 'ROG Phone', 'Epic performance. Unbeatable visuals. Total control.', 649.9, 'USD', 3, 3);
INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)
VALUES(7, 'Mac Pro (Standard)', 'Power to change everything. Say hello to a Mac that is extreme in every way.', 4999, 'USD', 4, 4);
INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)
VALUES(8, 'Mac Pro (Top-end)', 'Power to change everything. Say hello to a Mac that is extreme in every way.', 44999, 'USD', 4, 4);
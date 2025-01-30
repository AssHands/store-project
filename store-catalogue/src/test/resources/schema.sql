DROP TABLE IF EXISTS category;
create table category (
    id serial primary key,
    parent_id int,
    name varchar
);

DROP TABLE IF EXISTS characteristic;
create table characteristic (
    id serial primary key,
    name varchar,
    is_text boolean
);

DROP TABLE IF EXISTS product;
create table product (
    id serial primary key,
    title varchar,
    description varchar,
    current_price int,
    full_price int,
    discount_percentage int,
    category_id int,
    amount_reviews int,
    grade float,
    foreign key(category_id) REFERENCES category (id)
);

DROP TABLE IF EXISTS product_image;
create table product_image (
    id serial primary key,
    product_id int,
    image_key varchar(60),
    number int,
    foreign key (product_id) references product (id)
);

DROP TABLE IF EXISTS product_characteristic;
create table product_characteristic (
    id serial primary key,
    product_id int,
    characteristic_id int,
    numeric_value int,
    text_value varchar(60),
    foreign key(product_id) REFERENCES product (id),
    foreign key(characteristic_id) REFERENCES characteristic (id)
);

DROP TABLE IF EXISTS category_characteristic;
create table category_characteristic (
    id serial primary key,
    category_id int,
    characteristic_id int,
    foreign key(category_id) REFERENCES category (id),
    foreign key(characteristic_id) REFERENCES characteristic (id)
);

DROP TABLE IF EXISTS range_value;
create table range_value (
    id serial primary key,
    characteristic_id int,
    from_value int,
    to_value int,
    foreign key(characteristic_id) REFERENCES characteristic (id)
);

DROP TABLE IF EXISTS text_value;
CREATE TABLE text_value (
    id SERIAL PRIMARY KEY,
    characteristic_id INT,
    text_value VARCHAR(60),
    FOREIGN KEY (characteristic_id) REFERENCES characteristic (id)
);


ALTER SEQUENCE product_characteristic_id_seq INCREMENT BY 50;
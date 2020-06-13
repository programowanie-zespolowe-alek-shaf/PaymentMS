DROP ALL OBJECTS;

DROP SCHEMA IF EXISTS payment;
CREATE SCHEMA IF NOT EXISTS payment;

USE payment;

create table coupon
(
    ID                   BIGINT unsigned auto_increment PRIMARY KEY,
    code                 VARCHAR(15) unique,
    discount_multiplayer decimal(4, 3),
    amount_left          int
);

create table transaction
(
    ID                       BIGINT unsigned auto_increment PRIMARY KEY,
    shopping_card_id         BIGINT,
    timestamp_of_transaction TIMESTAMP,
    amount                   decimal(15, 2),
    status                   VARCHAR(20),
    method_of_payment        VARCHAR(20),
    coupon_id                BIGINT unsigned,
    FOREIGN KEY (coupon_id) REFERENCES Coupon (ID)
);


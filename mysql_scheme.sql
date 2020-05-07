CREATE SCHEMA IF NOT EXISTS payment;

create user 'payment'@'%' identified by 'payment';
grant all on payment.* to 'payment'@'%';

USE payment;

create table Coupon (
    ID BIGINT unsigned auto_increment PRIMARY KEY UNIQUE,
    code VARCHAR(15),
    discount_multiplayer decimal(4,3),
    amountLeft int
);

create table Transaction (
    ID BIGINT unsigned auto_increment PRIMARY KEY UNIQUE ,
    shopping_card_id BIGINT,
    timestamp_of_transaction TIMESTAMP,
    amount decimal(15,2),
    status  VARCHAR(20),
    method_of_payment VARCHAR(20),
    coupon_id BIGINT unsigned,
    FOREIGN KEY (coupon_id) REFERENCES Coupon (ID)
);


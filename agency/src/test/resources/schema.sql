drop table if exists order_item;
drop table if exists orders;
drop table if exists cart_item;
drop table if exists cart;
drop table if exists user_role;
drop table if exists tour;
drop table if exists app_user;

create table app_user
(
    id             uuid default random_uuid() primary key,
    first_name     varchar(50)  not null,
    last_name      varchar(50)  not null,
    email          varchar(100) not null unique,
    password       varchar(255) not null,
    phone_number   varchar(20),
    account_status boolean      not null default true,
    created_at     timestamp with time zone not null default current_timestamp
);

create table user_role
(
    user_id uuid        not null references app_user (id) on delete cascade,
    role    varchar(20) not null
);

create table tour
(
    id            uuid default random_uuid() primary key,
    title         varchar(150)   not null,
    description   text,
    image_name    varchar(255),
    price         decimal(10, 2) not null,
    arrival_date  date           not null,
    eviction_date date           not null,
    tour_type     varchar(30)    not null,
    hotel_type    varchar(30)    not null,
    transfer_type varchar(30)    not null,
    hot           boolean        not null default false,
    created_at    timestamp      not null default current_timestamp,
    created_by    uuid references app_user (id)
);

create table cart
(
    id         uuid default random_uuid() primary key,
    user_id    uuid not null unique references app_user (id) on delete cascade,
    created_at timestamp with time zone not null default current_timestamp
);

create table cart_item
(
    id             uuid default random_uuid() primary key,
    cart_id        uuid not null references cart (id) on delete cascade,
    tour_id        uuid not null references tour (id),
    reserved_seats int  not null
);

create table orders
(
    id          uuid default random_uuid() primary key,
    user_id     uuid           not null references app_user (id),
    total_price decimal(10, 2) not null,
    status      varchar(20)    not null,
    created_at  timestamp with time zone not null default current_timestamp
);

create table order_item
(
    id             uuid default random_uuid() primary key,
    order_id       uuid           not null references orders (id) on delete cascade,
    tour_id        uuid           not null references tour (id),
    reserved_seats int            not null,
    unit_price     decimal(10, 2) not null
);

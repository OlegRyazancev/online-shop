create table if not exists customers
(
    id      bigserial primary key,
    name    varchar(255) not null,
    email   varchar(255) not null unique,
    balance real         not null
);


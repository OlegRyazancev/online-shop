create table if not exists customers
(
    id       bigserial primary key,
    username varchar(255)     not null,
    email    varchar(255)     not null unique,
    balance  double precision not null
);

-- todo: change size
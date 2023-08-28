
create table customer(
id varchar(255) not null,
name varchar(255) default null,
primary key (id)
);

create table passport(
id varchar(255) not null,
created_date datetime(6) default null,
customer_id varchar(255) not null,
primary key (id),
foreign key(customer_id) references customer(id),
unique index (customer_id)
);


alter table passport drop primary key;
alter table passport drop column id;
alter table passport add primary key (customer_id);

-- here the primary key is the customer_id which is also a FK to id of Customer table.
create table student (
id varchar(255) not null,
name varchar(255) default null,
guide_id varchar(255) not null,
primary key(id),
foreign key (guide_id) references guide (id),
index (guide_id)
);


create table guide (
id varchar(255) not null,
name varchar(255) default null,
salary integer default 0,
primary key(id)
);

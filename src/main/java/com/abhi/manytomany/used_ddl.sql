-- manyToMany association tables.

create table actor_table(
actor_id varchar(255) not null,
actor_name varchar(255) default null,
actor_state varchar(255) default null,
actor_city varchar(255) default null,
primary key(actor_id)
);

create table movie(
movie_id varchar(255) not null,
movie_name varchar(255),
primary key(movie_id)
);

create table actor_movie(
movie_id_fk varchar(255) not null,
actor_id_fk varchar(255) not null,
primary key (movie_id_fk, actor_id_fk),
foreign key (movie_id_fk) references movie(movie_id),
foreign key (actor_id_fk) references actor_table(actor_id)
);

create table movie_genre (
movie_id varchar(255) not null,
genre varchar(255) not null,
primary key(movie_id, genre)
);

alter table user_steps drop constraint FK3du5v4kscjyheme0y8qfkaswu;
drop table if exists user_steps cascade;
drop table if exists users cascade;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start 1 increment 1;
create table user_steps (id int8 not null, created_at timestamp not null, updated_at timestamp not null, current_ranking_steps int8 not null, previous_ranking_steps int8 not null, today_steps int8 not null, user_id int8, primary key (id));
create table users (id int8 not null, created_at timestamp not null, updated_at timestamp not null, avatar_uri varchar(255), current_steps int8, departament varchar(255), name varchar(255), previus_steps int8, refresh_token varchar(255) not null, user_id varchar(255) not null, primary key (id));
alter table users add constraint UK_6efs5vmce86ymf5q7lmvn2uuf unique (user_id);
alter table user_steps add constraint FK3du5v4kscjyheme0y8qfkaswu foreign key (user_id) references users;
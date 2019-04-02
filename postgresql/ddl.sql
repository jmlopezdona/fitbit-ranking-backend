drop table if exists users cascade;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start 1 increment 1;
create table users (id int8 not null, created_at timestamp not null, updated_at timestamp not null, avatar_uri varchar(255), current_steps int8, departament varchar(255), name varchar(255), previus_steps int8, refresh_token varchar(255) not null, user_id varchar(255) not null, primary key (id));
alter table users add constraint UK_6efs5vmce86ymf5q7lmvn2uuf unique (user_id);
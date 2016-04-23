# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table stores_info (
  id                        bigint auto_increment not null,
  store_name                varchar(255),
  pic_url                   varchar(255),
  comment_text              varchar(255),
  created_time              varchar(255),
  constraint pk_stores_info primary key (id))
;

create table user_info (
  id                        bigint auto_increment not null,
  user_name                 varchar(255),
  email                     varchar(255),
  photo_url                 varchar(255),
  constraint pk_user_info primary key (id))
;

create table users_stores (
  id                        bigint auto_increment not null,
  user_name                 varchar(255),
  store_name                varchar(255),
  constraint pk_users_stores primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table stores_info;

drop table user_info;

drop table users_stores;

SET FOREIGN_KEY_CHECKS=1;


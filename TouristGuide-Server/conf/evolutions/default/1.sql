# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table person (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  due_date                  datetime(6),
  constraint pk_person primary key (id))
;

create table search (
  id                        varchar(255) not null,
  keywords                  varchar(255),
  search_type               varchar(255),
  constraint pk_search primary key (id))
;

create table author (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_author primary key (id))
;

create table publication_author (
  id                        bigint auto_increment not null,
  publication_id            bigint,
  author_id                 bigint,
  constraint pk_publication_author primary key (id))
;

create table publication (
  id                        bigint auto_increment not null,
  pubkey                    varchar(255),
  title                     varchar(255),
  editor                    varchar(255),
  year                      varchar(255),
  isbn                      varchar(255),
  url                       varchar(255),
  volume                    varchar(255),
  pages                     varchar(255),
  mdate                     varchar(255),
  series                    varchar(255),
  publisher                 varchar(255),
  booktitle                 varchar(255),
  crossref                  varchar(255),
  ee                        varchar(255),
  constraint pk_publication primary key (id))
;

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

drop table person;

drop table search;

drop table author;

drop table publication_author;

drop table publication;

drop table stores_info;

drop table user_info;

drop table users_stores;

SET FOREIGN_KEY_CHECKS=1;


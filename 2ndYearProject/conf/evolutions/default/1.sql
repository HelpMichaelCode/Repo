# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_category primary key (id)
);

create table product (
  product_id                    bigint auto_increment not null,
  product_name                  varchar(255),
  product_description           varchar(255),
  product_price                 double not null,
  product_qty                   integer not null,
  total_sold                    integer not null,
  overall_rating                double not null,
  category_id                   bigint,
  constraint pk_product primary key (product_id)
);

create table user (
  email                         varchar(255) not null,
  role                          varchar(255),
  username                      varchar(255),
  password                      varchar(255),
  address                       varchar(255),
  mobile_number                 varchar(255),
  constraint pk_user primary key (email)
);

alter table product add constraint fk_product_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_product_category_id on product (category_id);


# --- !Downs

alter table product drop constraint if exists fk_product_category_id;
drop index if exists ix_product_category_id;

drop table if exists category;

drop table if exists product;

drop table if exists user;


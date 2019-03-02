# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  category_id                   bigint auto_increment not null,
  category_name                 varchar(255),
  constraint pk_category primary key (category_id)
);

create table category_product (
  category_category_id          bigint not null,
  product_product_id            bigint not null,
  constraint pk_category_product primary key (category_category_id,product_product_id)
);

create table product (
  product_id                    bigint auto_increment not null,
  product_name                  varchar(255),
  product_description           varchar(255),
  product_price                 double not null,
  product_qty                   integer not null,
  total_sold                    integer not null,
  overall_rating                double not null,
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

alter table category_product add constraint fk_category_product_category foreign key (category_category_id) references category (category_id) on delete restrict on update restrict;
create index ix_category_product_category on category_product (category_category_id);

alter table category_product add constraint fk_category_product_product foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_category_product_product on category_product (product_product_id);


# --- !Downs

alter table category_product drop constraint if exists fk_category_product_category;
drop index if exists ix_category_product_category;

alter table category_product drop constraint if exists fk_category_product_product;
drop index if exists ix_category_product_product;

drop table if exists category;

drop table if exists category_product;

drop table if exists product;

drop table if exists user;


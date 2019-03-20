# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_category primary key (id)
);

create table order_line (
  id                            bigint auto_increment not null,
  quantity                      integer not null,
  price                         double not null,
  order_id                      bigint,
  cart_id                       bigint,
  products_product_id           bigint,
  constraint pk_order_line primary key (id)
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

create table shop_order (
  id                            bigint auto_increment not null,
  order_date                    timestamp,
  user_email                    varchar(255),
  constraint pk_shop_order primary key (id)
);

create table shopping_cart (
  id                            bigint auto_increment not null,
  user_email                    varchar(255),
  constraint uq_shopping_cart_user_email unique (user_email),
  constraint pk_shopping_cart primary key (id)
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

alter table order_line add constraint fk_order_line_order_id foreign key (order_id) references shop_order (id) on delete restrict on update restrict;
create index ix_order_line_order_id on order_line (order_id);

alter table order_line add constraint fk_order_line_cart_id foreign key (cart_id) references shopping_cart (id) on delete restrict on update restrict;
create index ix_order_line_cart_id on order_line (cart_id);

alter table order_line add constraint fk_order_line_products_product_id foreign key (products_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_order_line_products_product_id on order_line (products_product_id);

alter table product add constraint fk_product_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_product_category_id on product (category_id);

alter table shop_order add constraint fk_shop_order_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;
create index ix_shop_order_user_email on shop_order (user_email);

alter table shopping_cart add constraint fk_shopping_cart_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;


# --- !Downs

alter table order_line drop constraint if exists fk_order_line_order_id;
drop index if exists ix_order_line_order_id;

alter table order_line drop constraint if exists fk_order_line_cart_id;
drop index if exists ix_order_line_cart_id;

alter table order_line drop constraint if exists fk_order_line_products_product_id;
drop index if exists ix_order_line_products_product_id;

alter table product drop constraint if exists fk_product_category_id;
drop index if exists ix_product_category_id;

alter table shop_order drop constraint if exists fk_shop_order_user_email;
drop index if exists ix_shop_order_user_email;

alter table shopping_cart drop constraint if exists fk_shopping_cart_user_email;

drop table if exists category;

drop table if exists order_line;

drop table if exists product;

drop table if exists shop_order;

drop table if exists shopping_cart;

drop table if exists user;


-- # --- Created by Ebean DDL
-- # To stop Ebean DDL generation, remove this comment and start using Evolutions

-- # --- !Ups

-- create table category (
--   id                            bigint auto_increment not null,
--   name                          varchar(255),
--   constraint pk_category primary key (id)
-- );

-- create table order_line (
--   id                            bigint auto_increment not null,
--   quantity                      integer not null,
--   price                         double not null,
--   order_id                      bigint,
--   cart_id                       bigint,
--   product_product_id            bigint,
--   constraint pk_order_line primary key (id)
-- );

-- create table product (
--   product_id                    bigint auto_increment not null,
--   product_name                  varchar(255),
--   product_description           varchar(255),
--   product_price                 double not null,
--   product_qty                   integer not null,
--   total_sold                    integer not null,
--   overall_rating                double not null,
--   summed_rating                 double,
--   count_rating                  integer,
--   category_id                   bigint,
--   constraint pk_product primary key (product_id)
-- );

-- create table review (
--   id                            bigint auto_increment not null,
--   body                          varchar(255),
--   rating                        double not null,
--   user_email                    varchar(255),
--   product_product_id            bigint,
--   constraint pk_review primary key (id)
-- );

-- create table shop_order (
--   id                            bigint auto_increment not null,
--   order_date                    timestamp,
--   user_email                    varchar(255),
--   constraint pk_shop_order primary key (id)
-- );

-- create table shopping_cart (
--   id                            bigint auto_increment not null,
--   user_email                    varchar(255),
--   constraint uq_shopping_cart_user_email unique (user_email),
--   constraint pk_shopping_cart primary key (id)
-- );

-- create table user (
--   email                         varchar(255) not null,
--   role                          varchar(255),
--   username                      varchar(255),
--   password                      varchar(255),
--   address                       varchar(255),
--   mobile_number                 varchar(255),
--   constraint pk_user primary key (email)
-- );

-- create table processor(
-- product_id bigint auto_increment not null,
-- manufacturer varchar2(255),
-- name varchar2(255),
-- cores number,
-- clock number,
-- cache number,
-- product_product_id bigint,
-- primary key (product_id)
-- );

-- create table graphics_card (
-- product_id bigint auto_increment not null,
-- manufacturer varchar2(255),
-- name varchar2(255),
-- bus varchar2(255),
-- memory varchar2(255),
-- gpu_clock varchar2(255),
-- memory_clock varchar2(255),
-- product_product_id bigint,
-- primary key (product_id),
-- foreign key(product_product_id) references product(product_id)
-- );

-- create table motherboard (
-- product_id bigint auto_increment not null,
-- manufacturer varchar2(255),
-- name varchar2(255),
-- ram_slots varchar2(255),
-- max_ram varchar2(255),
-- product_product_id bigint,
-- primary key (product_id),
-- foreign key(product_product_id) references product(product_id)
-- );

-- create table ram (
-- product_id bigint auto_increment not null,
-- manufacturer varchar2(255),
-- name varchar2(255),
-- capacity varchar2(255),
-- product_product_id bigint,
-- primary key (product_id),
-- foreign key (product_product_id) references product(product_id)
-- );

-- create table storage (
-- product_id bigint auto_increment not null,
-- manufacturer varchar2(255),
-- name varchar2(255),
-- capacity varchar2(255),
-- product_product_id bigint,
-- primary key (product_id),
-- foreign key (product_product_id) references product(product_id)
-- );

-- create table trending_pc(
-- product_id bigint auto_increment not null,
-- manufacturer varchar2(255),
-- name varchar2(255),
-- cpu bigint,
-- gpu bigint,
-- motherboard bigint,
-- storage bigint,
-- product_product_id bigint,
-- primary key (product_id),
-- foreign key (cpu) references processor (product_id),
-- foreign key (gpu) references graphics_card(product_id),
-- foreign key (motherboard) references motherboard (product_id),
-- foreign key (storage) references storage (product_id),
-- foreign key (product_product_id) references product (product_id)
-- );

-- alter table graphics_card add constraint fk_graphics_card_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
-- create index ix_graphics_card_product_product_id on graphics_card (product_product_id);


-- alter table processor add constraint fk_processor_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
-- create index ix_processor_product_product_id on processor (product_product_id);

-- alter table order_line add constraint fk_order_line_order_id foreign key (order_id) references shop_order (id) on delete restrict on update restrict;
-- create index ix_order_line_order_id on order_line (order_id);

-- alter table order_line add constraint fk_order_line_cart_id foreign key (cart_id) references shopping_cart (id) on delete restrict on update restrict;
-- create index ix_order_line_cart_id on order_line (cart_id);

-- alter table order_line add constraint fk_order_line_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
-- create index ix_order_line_product_product_id on order_line (product_product_id);

-- alter table product add constraint fk_product_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;
-- create index ix_product_category_id on product (category_id);

-- alter table review add constraint fk_review_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;
-- create index ix_review_user_email on review (user_email);

-- alter table review add constraint fk_review_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
-- create index ix_review_product_product_id on review (product_product_id);

-- alter table shop_order add constraint fk_shop_order_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;
-- create index ix_shop_order_user_email on shop_order (user_email);

-- alter table shopping_cart add constraint fk_shopping_cart_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;


-- # --- !Downs

-- alter table order_line drop constraint if exists fk_order_line_order_id;
-- drop index if exists ix_order_line_order_id;

-- alter table order_line drop constraint if exists fk_order_line_cart_id;
-- drop index if exists ix_order_line_cart_id;

-- alter table order_line drop constraint if exists fk_order_line_product_product_id;
-- drop index if exists ix_order_line_product_product_id;

-- alter table product drop constraint if exists fk_product_category_id;
-- drop index if exists ix_product_category_id;

-- alter table review drop constraint if exists fk_review_user_email;
-- drop index if exists ix_review_user_email;

-- alter table review drop constraint if exists fk_review_product_product_id;
-- drop index if exists ix_review_product_product_id;

-- alter table shop_order drop constraint if exists fk_shop_order_user_email;
-- drop index if exists ix_shop_order_user_email;

-- alter table shopping_cart drop constraint if exists fk_shopping_cart_user_email;

-- drop table if exists category;

-- drop table if exists order_line;

-- drop table if exists product;

-- drop table if exists review;

-- drop table if exists shop_order;

-- drop table if exists shopping_cart;

-- drop table if exists user;


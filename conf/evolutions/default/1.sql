# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_category primary key (id)
);

create table graphics_card (
  product_id                    bigint auto_increment not null,
  manufacturer                  varchar(255),
  name                          varchar(255),
  product_product_id            bigint,
  bus                           varchar(255),
  memory                        varchar(255),
  gpu_clock                     varchar(255),
  memory_clock                  varchar(255),
  constraint pk_graphics_card primary key (product_id)
);

create table motherboard (
  product_id                    bigint auto_increment not null,
  manufacturer                  varchar(255),
  name                          varchar(255),
  product_product_id            bigint,
  ram_slots                     varchar(255),
  max_ram                       varchar(255),
  constraint pk_motherboard primary key (product_id)
);

create table order_line (
  id                            bigint auto_increment not null,
  quantity                      integer not null,
  price                         double not null,
  order_id                      bigint,
  cart_id                       bigint,
  product_product_id            bigint,
  constraint pk_order_line primary key (id)
);

create table processor (
  product_id                    bigint auto_increment not null,
  manufacturer                  varchar(255),
  name                          varchar(255),
  product_product_id            bigint,
  cores                         varchar(255),
  clock                         varchar(255),
  cache                         varchar(255),
  constraint pk_processor primary key (product_id)
);

create table product (
  product_id                    bigint auto_increment not null,
  product_name                  varchar(255),
  product_description           varchar(255),
  product_price                 double not null,
  product_qty                   integer not null,
  total_sold                    integer not null,
  overall_rating                double not null,
  summed_rating                 double not null,
  count_rating                  integer not null,
  category_id                   bigint,
  constraint pk_product primary key (product_id)
);

create table ram (
  product_id                    bigint auto_increment not null,
  manufacturer                  varchar(255),
  name                          varchar(255),
  product_product_id            bigint,
  capacity                      varchar(255),
  constraint pk_ram primary key (product_id)
);

create table review (
  id                            bigint auto_increment not null,
  body                          varchar(255),
  rating                        double not null,
  user_email                    varchar(255),
  product_product_id            bigint,
  constraint pk_review primary key (id)
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

create table storage (
  product_id                    bigint auto_increment not null,
  manufacturer                  varchar(255),
  name                          varchar(255),
  product_product_id            bigint,
  capacity                      varchar(255),
  constraint pk_storage primary key (product_id)
);

create table trending_pc (
  product_id                    bigint auto_increment not null,
  manufacturer                  varchar(255),
  name                          varchar(255),
  product_product_id            bigint,
  cpu_product_id                bigint,
  gpu_product_id                bigint,
  motherboard_product_id        bigint,
  ram_qty                       integer not null,
  ram_product_id                bigint,
  storage_product_id            bigint,
  constraint pk_trending_pc primary key (product_id)
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

alter table graphics_card add constraint fk_graphics_card_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_graphics_card_product_product_id on graphics_card (product_product_id);

alter table motherboard add constraint fk_motherboard_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_motherboard_product_product_id on motherboard (product_product_id);

alter table order_line add constraint fk_order_line_order_id foreign key (order_id) references shop_order (id) on delete restrict on update restrict;
create index ix_order_line_order_id on order_line (order_id);

alter table order_line add constraint fk_order_line_cart_id foreign key (cart_id) references shopping_cart (id) on delete restrict on update restrict;
create index ix_order_line_cart_id on order_line (cart_id);

alter table order_line add constraint fk_order_line_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_order_line_product_product_id on order_line (product_product_id);

alter table processor add constraint fk_processor_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_processor_product_product_id on processor (product_product_id);

alter table product add constraint fk_product_category_id foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_product_category_id on product (category_id);

alter table ram add constraint fk_ram_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_ram_product_product_id on ram (product_product_id);

alter table review add constraint fk_review_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;
create index ix_review_user_email on review (user_email);

alter table review add constraint fk_review_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_review_product_product_id on review (product_product_id);

alter table shop_order add constraint fk_shop_order_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;
create index ix_shop_order_user_email on shop_order (user_email);

alter table shopping_cart add constraint fk_shopping_cart_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;

alter table storage add constraint fk_storage_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_storage_product_product_id on storage (product_product_id);

alter table trending_pc add constraint fk_trending_pc_product_product_id foreign key (product_product_id) references product (product_id) on delete restrict on update restrict;
create index ix_trending_pc_product_product_id on trending_pc (product_product_id);

alter table trending_pc add constraint fk_trending_pc_cpu_product_id foreign key (cpu_product_id) references processor (product_id) on delete restrict on update restrict;
create index ix_trending_pc_cpu_product_id on trending_pc (cpu_product_id);

alter table trending_pc add constraint fk_trending_pc_gpu_product_id foreign key (gpu_product_id) references graphics_card (product_id) on delete restrict on update restrict;
create index ix_trending_pc_gpu_product_id on trending_pc (gpu_product_id);

alter table trending_pc add constraint fk_trending_pc_motherboard_product_id foreign key (motherboard_product_id) references motherboard (product_id) on delete restrict on update restrict;
create index ix_trending_pc_motherboard_product_id on trending_pc (motherboard_product_id);

alter table trending_pc add constraint fk_trending_pc_ram_product_id foreign key (ram_product_id) references ram (product_id) on delete restrict on update restrict;
create index ix_trending_pc_ram_product_id on trending_pc (ram_product_id);

alter table trending_pc add constraint fk_trending_pc_storage_product_id foreign key (storage_product_id) references storage (product_id) on delete restrict on update restrict;
create index ix_trending_pc_storage_product_id on trending_pc (storage_product_id);


# --- !Downs

alter table graphics_card drop constraint if exists fk_graphics_card_product_product_id;
drop index if exists ix_graphics_card_product_product_id;

alter table motherboard drop constraint if exists fk_motherboard_product_product_id;
drop index if exists ix_motherboard_product_product_id;

alter table order_line drop constraint if exists fk_order_line_order_id;
drop index if exists ix_order_line_order_id;

alter table order_line drop constraint if exists fk_order_line_cart_id;
drop index if exists ix_order_line_cart_id;

alter table order_line drop constraint if exists fk_order_line_product_product_id;
drop index if exists ix_order_line_product_product_id;

alter table processor drop constraint if exists fk_processor_product_product_id;
drop index if exists ix_processor_product_product_id;

alter table product drop constraint if exists fk_product_category_id;
drop index if exists ix_product_category_id;

alter table ram drop constraint if exists fk_ram_product_product_id;
drop index if exists ix_ram_product_product_id;

alter table review drop constraint if exists fk_review_user_email;
drop index if exists ix_review_user_email;

alter table review drop constraint if exists fk_review_product_product_id;
drop index if exists ix_review_product_product_id;

alter table shop_order drop constraint if exists fk_shop_order_user_email;
drop index if exists ix_shop_order_user_email;

alter table shopping_cart drop constraint if exists fk_shopping_cart_user_email;

alter table storage drop constraint if exists fk_storage_product_product_id;
drop index if exists ix_storage_product_product_id;

alter table trending_pc drop constraint if exists fk_trending_pc_product_product_id;
drop index if exists ix_trending_pc_product_product_id;

alter table trending_pc drop constraint if exists fk_trending_pc_cpu_product_id;
drop index if exists ix_trending_pc_cpu_product_id;

alter table trending_pc drop constraint if exists fk_trending_pc_gpu_product_id;
drop index if exists ix_trending_pc_gpu_product_id;

alter table trending_pc drop constraint if exists fk_trending_pc_motherboard_product_id;
drop index if exists ix_trending_pc_motherboard_product_id;

alter table trending_pc drop constraint if exists fk_trending_pc_ram_product_id;
drop index if exists ix_trending_pc_ram_product_id;

alter table trending_pc drop constraint if exists fk_trending_pc_storage_product_id;
drop index if exists ix_trending_pc_storage_product_id;

drop table if exists category;

drop table if exists graphics_card;

drop table if exists motherboard;

drop table if exists order_line;

drop table if exists processor;

drop table if exists product;

drop table if exists ram;

drop table if exists review;

drop table if exists shop_order;

drop table if exists shopping_cart;

drop table if exists storage;

drop table if exists trending_pc;

drop table if exists user;


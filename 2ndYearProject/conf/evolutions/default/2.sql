# --- Sample dataset

# --- !Ups
delete from Product;
insert into user (email,username,password,address,mobile_number,role) values ( 'michael@play.com', 'Michael Admin', '','122 Michaels Lane','0851234567', 'admin' );
insert into user (email,username,password,address,mobile_number,role) values ( 'pavel@play.com', 'Pavel Admin', '','321 Nitesh Street','0897654321','admin' );
insert into user (email,username,password,address,mobile_number,role) values ( 'nitesh@play.com', 'Nitesh Customer', '','12 Pavel Court','0123456789','customer' );

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating) 
values (100, 'Hardrive', 'This is a test product', 99.99, 17, 3, 5.5);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating) 
values (101, 'SSD', 'This is a test product', 99.99, 18, 2, 4.5);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating) 
values (103, 'USB', 'This is a test product', 99.99, 16, 4, 2.3);


insert into category (id,name) values ( 1,'Electrical and Electronics' );
insert into category (id,name) values ( 2,'Books' );
insert into category (id,name) values ( 3,'Clothes' );
insert into category (id,name) values ( 4,'Household' );
insert into category (id,name) values ( 5,'Musical Instruments' );
insert into category (id,name) values ( 6,'Sports Equipment' );
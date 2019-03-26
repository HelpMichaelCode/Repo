-- # --- Sample dataset

-- # --- !Ups
-- delete from Product;
-- delete from category;
-- delete from user;
-- delete from review;

-- insert into user (email,username,password,address,mobile_number,role) values ( 'admin@play.com', 'Admin', '','122 New Lane','0851234567', 'admin' );
-- insert into user (email,username,password,address,mobile_number,role) values ( 'customer@play.com', 'James Olsen', '','12 Goodwill Court','0866549873','customer' );

-- insert into category (id,name) values ( 1,'Home PCs' );
-- insert into category (id,name) values ( 2,'Top spec PCs' );
-- insert into category (id,name) values ( 3,'Gaming PCs' );
-- insert into category (id,name) values ( 4,'Workstations' );
-- insert into category (id,name) values ( 5,'Home laptops' );
-- insert into category (id,name) values ( 6,'Gaming laptops' );
-- insert into category (id,name) values ( 7,'CPUs' );
-- insert into category (id,name) values ( 8,'Motherboards' );
-- insert into category (id,name) values ( 9,'RAM' );
-- insert into category (id,name) values ( 10,'Graphics cards' );
-- insert into category (id,name) values ( 11,'Storage' );
-- insert into category (id,name) values ( 12,'Power supplies' );

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (100, 'HP Pavilion 590-a0017na AMD A9 Desktop PC - 1 TB HDD, Black',
-- 'The HP Pavilion 590 AMD A9 Desktop PC is part of our Everyday range of simple 
-- and reliable PCs. It''s great for studying, working on essays, streaming TV on 
-- demand, and browsing the web.',
-- 479.99, 11, 0, 0, 0, 0, 1);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (101, 'PC SPECIALIST Vortex Fusion Extreme II Intel® Core™ i7 RTX 2070 Gaming PC – 2 TB HDD & 256 GB SSD',
-- 'With a HyperThreaded Intel® Core™ i7 processor, you can rely on fast performance to get your tasks done with no 
-- lag. Play your favourite FPS game while streaming it to Twitch, edit your latest YouTube video and finish updating 
-- work projects.',
-- 1679.99, 5, 0, 0, 0, 0, 2);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (102, 'ACER Orion 3000 Intel® Core™ i5+ GTX 1050 Ti Gaming PC - 1 TB HDD',
-- 'Take on your favourite games with this gaming PC. 
-- With an 8th generation i5+ processor, your PC is able to run the latest titles everyone''s talking about, 
-- as well as the classic games that helped you hone your skills.',
-- 969.99, 5, 0, 0, 0, 0, 3);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (103, 'ThinkStation P720 Tower Dual Intel® Xeon® NVIDIA® Quadro® GP100',
-- 'This dual-processor 
-- workstation is a tough performer. Ideal for professionals with heavy data-processing 
-- needs, the P720 offers fast processing, massive storage options, and parallel-processing 
-- capability.',
-- 2019.99, 5, 0, 0, 0, 0, 4);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (104, 'HP Stream 11-ak0500sa 11.6" Intel® Celeron™ Laptop - 32 GB eMMC',
-- 'Stay up to date with social media and the latest news headlines with 
-- the HP Stream Laptop. Powered by Intel® Celeron™, your laptop is able to 
-- handle your tasks from ordering groceries online, to writing a letter or chatting 
-- with friends.',
-- 219.99, 10, 0, 0, 0, 0, 5);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (105, 'MSI GF62 8RC 15.6" Intel® Core™ i5 GTX 1050 Gaming Laptop - 1 TB HDD',
-- 'At the core of the MSI GF62 8RC is an 8th generation Intel® Core™ i5 processor, which 
-- provides all the power you need to run your favourite games.', 
-- 959.99, 7, 0, 0, 0, 0, 6);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (106, 'Intel Core™ i7-9700K Unlocked Processor - OEM',
-- 'Overclock easily with the i7-9700K CPU, which has been unlocked so you can tweak the specs 
-- that matter to you. By unlocking your processor you can boost the performance speed 
-- so that everything runs faster when it matters.', 
-- 649.99, 6, 0, 0, 0, 0, 7);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (107, 'ASUS ROG STRIX B450-F GAMING AM4 Motherboard',
-- 'Don''t waste time fussing with the little things when it comes to completing your PC 
-- build. The ASUS B450-F motherboard is designed for easy DIY – the I/O Shield comes pre-mounted, 
-- and you can snap all of your components into place.', 
-- 259.99, 5, 0, 0, 0, 0, 8);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id)  
-- values (108, 'CORSAIR DDR3 1600 MHz PC RAM - 8 GB',
-- 'For everyday use, the 1600 MHz Corsair CMV8GX3M1A1600C11 memory card allows for 
-- faster processing, helping you send emails, load videos and transfer files with greater 
-- efficiency.', 
-- 79.99, 10, 0, 0, 0, 0, 9);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id)  
-- values (109, 'MSI GeForce RTX 2080 Ti 11 GB GAMING X TRIO Turing Graphics Card',
-- 'Powered by the Turing architecture, RTX GPUs are up to 6 times as fast as previous 
-- graphics cards, so you''ll have the power to run any game, get incredible frame rates 
-- and render amazing imagery in next to no time.', 
-- 1589.99, 4, 0, 0, 0, 0, 10);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (110, 'WD Mainstream 3.5” Internal Hard Drive - 3 TB',
-- 'You can expect long-term reliability thanks to No Touch Ramp Load technology. 
-- This ensures the recording head never actually touches the disk media, significantly 
-- reducing wear and providing better protection when mobile.', 
-- 119.99, 8, 0, 0, 0, 0, 11);

-- insert into product (product_ID,product_name,product_description,product_price,product_qty,
-- total_sold,overall_rating, summed_rating, count_rating, category_id) 
-- values (111, 'EVGA SuperNOVA G1+ Gold Modular ATX PSU - 1000 W',
-- 'Keep your case neat and tidy with the fully modular SuperNOVA G1+ Gold ATX PSU. 
-- You''ll be able to add and remove cables whenever you need - not only will your case 
-- be less cluttered, but it will also let air circulate easily to move heat away quickly.', 
-- 189.99, 6, 0, 0, 0, 0, 12);

-- insert into review (id, body, rating, user_email, product_product_id) 
-- values (400, 'Great laptop! Definitely worth buying.', 4.8, 'customer@play.com', 106);
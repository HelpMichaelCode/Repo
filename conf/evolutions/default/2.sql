# --- Sample dataset

# --- !Ups
delete from Product;
delete from category;
delete from user;
delete from review;

insert into user (email,username,real_name,password,address,mobile_number,role) values ( 'admin@play.com', 'Admin', 'Administrator', '','122 New Lane','0851234567', 'admin' );
insert into user (email,username,real_name,password,address,mobile_number,role) values ( 'customer@play.com', 'jolsen', 'James Olsen', '','12 Goodwill Court','0866549873','customer' );
insert into user (email,username,real_name,password,address,mobile_number,role) values ( 'N/A', 'N/A', 'N/A', 'N/A' ,'N/A','N/A','N/A');

insert into shopping_cart (id, user_email) values (1, 'customer@play.com');
insert into shopping_cart (id, user_email) values (2, 'admin@play.com');

insert into category (id,name) values ( 1,'Home PCs' );
insert into category (id,name) values ( 2,'Top spec PCs' );
insert into category (id,name) values ( 3,'Gaming PCs' );
insert into category (id,name) values ( 4,'Workstations' );
insert into category (id,name) values ( 5,'Home laptops' );
insert into category (id,name) values ( 6,'Gaming laptops' );
insert into category (id,name) values ( 7,'CPUs' );
insert into category (id,name) values ( 8,'Motherboards' );
insert into category (id,name) values ( 9,'RAM' );
insert into category (id,name) values ( 10,'Graphics cards' );
insert into category (id,name) values ( 11,'Storage' );
insert into category (id,name) values ( 1000,'None');

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (100, 'HP Pavilion 590-a0017na AMD Ryzen 5 Desktop PC - 1 TB HDD, Black',
'The HP Pavilion 590 AMD Ryzen 5 2600X Desktop PC is part of our everyday range of simple 
and reliable PCs. It''s great for studying, working on essays, streaming TV on 
demand, and browsing the web.',
479.99, 11, 0, 0, 0, 0, 1);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (101, 'PC SPECIALIST Vortex Fusion Extreme II Intel® Core™ i7 RTX 2070 Gaming PC – 2 TB HDD & 256 GB SSD',
'With a HyperThreaded Intel® Core™ i7 processor, you can rely on fast performance to get your tasks done with no 
lag. Play your favourite FPS game while streaming it to Twitch, edit your latest YouTube video and finish updating 
work projects.',
1679.99, 5, 0, 0, 0, 0, 2);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (102, 'ACER Orion 3000 Intel® Core™ i5 GTX 1050 Ti Gaming PC - 1 TB HDD',
'Take on your favourite games with this gaming PC. 
With an 8th generation i5+ processor, your PC is able to run the latest titles everyone''s talking about, 
as well as the classic games that helped you hone your skills.',
969.99, 5, 0, 0, 0, 0, 3);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (103, 'ThinkStation P720 Tower Intel® Xeon® NVIDIA® Quadro® GP100',
'This workstation is a tough performer. Ideal for professionals with heavy data-processing 
needs, the P720 offers fast processing, massive storage options, and parallel-processing 
capability.',
2019.99, 5, 0, 0, 0, 0, 4);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (104, 'HP Stream 11-ak0500sa 11.6" Intel® Celeron™ Laptop - 32 GB eMMC',
'Stay up to date with social media and the latest news headlines with 
the HP Stream Laptop. Powered by Intel® Celeron™, your laptop is able to 
handle your tasks from ordering groceries online, to writing a letter or chatting 
with friends.',
219.99, 10, 0, 0, 0, 0, 5);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (105, 'MSI GF62 8RC 15.6" Intel® Core™ i5 GTX 1050 Gaming Laptop - 1 TB HDD',
'At the core of the MSI GF62 8RC is an 8th generation Intel® Core™ i5 processor, which 
provides all the power you need to run your favourite games.', 
959.99, 7, 0, 0, 0, 0, 6);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (106, 'Intel Core™ i7-9700K Unlocked Processor - OEM',
'Overclock easily with the i7-9700K CPU, which has been unlocked so you can tweak the specs 
that matter to you. By unlocking your processor you can boost the performance speed 
so that everything runs faster when it matters.', 
649.99, 6, 0, 0, 0, 0, 7);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (107, 'ASUS ROG STRIX B450-F GAMING AM4 Motherboard - AMD Chipset',
'Don''t waste time fussing with the little things when it comes to completing your PC 
build. The ASUS B450-F motherboard is designed for easy DIY – the I/O Shield comes pre-mounted, 
and you can snap all of your components into place.', 
259.99, 5, 0, 0, 0, 0, 8);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id)  
values (108, 'CORSAIR DDR3 1600 MHz PC RAM - 8 GB',
'For everyday use, the 1600 MHz Corsair CMV8GX3M1A1600C11 memory card allows for 
faster processing, helping you send emails, load videos and transfer files with greater 
efficiency.', 
79.99, 10, 0, 0, 0, 0, 9);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id)  
values (109, 'MSI GeForce RTX 2070 Ti 11 GB GAMING X TRIO Turing Graphics Card',
'Powered by the Turing architecture, RTX GPUs are up to 6 times as fast as previous 
graphics cards, so you''ll have the power to run any game, get incredible frame rates 
and render amazing imagery in next to no time.', 
1589.99, 4, 0, 0, 0, 0, 10);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (110, 'WD Mainstream 3.5” Internal Hard Drive - 3 TB',
'You can expect long-term reliability thanks to No Touch Ramp Load technology. 
This ensures the recording head never actually touches the disk media, significantly 
reducing wear and providing better protection when mobile.', 
119.99, 8, 0, 0, 0, 0, 11);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (112, 'AMD Ryzen 5 2600X Processor',
'Perfect for everything from finishing off an essay to demanding graphic design work, 
the Ryzen 5 2600X is a great all-round processor for your desktop.', 
259.99, 6, 0, 0, 0, 0, 7); 


insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (113, 'Intel Core™ i5-8400 Processor',
'This 8th generation processor is designed for versatility – pair it with a graphics card to play 
huge games, or just stream online and catch up on emails without being held back.', 
339.99, 6, 0, 0, 0, 0, 7);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (114, 'Intel Xeon™ Gold 5122 Processor',
'Xeon Gold 5122 is a 64-bit quad-core x86 multi-socket high performance server microprocessor 
introduced by Intel in mid-2017. This chip supports up to 4-way multiprocessing.', 
1279.90, 6, 0, 0, 0, 0, 7);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (115, 'Intel® Celeron® Processor N3450',
'Celeron N3450 is a 64-bit two-core chip introduced by Intel. This chip supports up to 2 threads to 
surf, send emails and stream movies with ease.', 
160.00, 6, 0, 0, 0, 0, 7);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (116, 'SEAGATE BarraCuda 3.5" Internal Hard Drive - 1 TB',
'The Seagate BarraCuda 3.5" Internal Hard Drive is ideal if you''re looking for a way 
to store all your family''s work, movies and music. With 1 TB of storage space, you''ll 
be able to store around 250,000 songs along with your important documents.', 
69.99, 8, 0, 0, 0, 0, 11);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (117, 'SEAGATE BarraCuda 3.5" Internal Hard Drive - 2 TB',
'The BarraCuda 3.5" hard disk drive offers a cost-effective way to store your 
large files such as games and software. You''ll love the ability to quickly write graphic-heavy 
open world games to your disk and keep your PC performing consistently.', 
89.99, 8, 0, 0, 0, 0, 11);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id)  
values (118, 'MSI GeForce GTX 1050 Ti 4 GB Graphics Card',
'The GeForce GTX 1050 Ti Graphics Card is built using durable components for a long lifespan 
and impressive performance on every game you play.', 
259.99, 4, 0, 0, 0, 0, 10);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id)  
values (119, 'NVIDIA Quadro GP100 4x DP 1x DVI-D 16 GB PCI Express Professional Graphic Card - Black',
'A single GP100 has incredible horsepower to render photorealistic 
design concepts interactively, create extremely detailed 3D models, run intensive CAE simulations to 
validate designs, and evaluate design prototypes in immersive VR.', 
3504.94, 2, 0, 0, 0, 0, 10);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (120, 'ASUS ROG MAXIMUS XI FORMULA LGA 1151 ATX Motherboard - Intel Chipset',
'The ASUS ROG Z390 gives 
enthusiasts the complete freedom to be as creative as they can be building the best gaming 
PC that leverages the power and efficiency of 8th and 9th generation Intel processors.', 
259.99, 5, 0, 0, 0, 0, 8);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (121, 'GeForce GTX 760 2 GB Graphics card',
'The GTX 760 graphics card is suitable for the casual student. It can keep up with HD streaming 
and casual games with lighter graphics that do not require much processing power.', 
259.99, 5, 0, 0, 0, 0, 10);

insert into product (product_ID,product_name,product_description,product_price,product_qty,
total_sold,overall_rating, summed_rating, count_rating, category_id) 
values (1, 'Unknown - N/A','Unknown - N/A', 0, 0, 0, 0, 0, 0, 1000);

insert into processor (product_id, manufacturer, name, cores, clock, cache, product_product_id)
values (106, 'Intel', 'i7-9700K', 6, '3.2 GHz', '12 MB', 106);

insert into processor (product_id, manufacturer, name, cores, clock, cache, product_product_id)
values (112, 'AMD', 'Ryzen 5 2600X', 6, '3.6 GHz', '16 MB', 112);

insert into processor (product_id, manufacturer, name, cores, clock, cache, product_product_id)
values (113, 'Intel', 'i5-8400', 6, '2.8 GHz', '9 MB', 113);

insert into processor (product_id, manufacturer, name, cores, clock, cache, product_product_id)
values (114, 'Intel', 'Xeon 5122', 4, '3.6 GHz', '16 MB', 114);

insert into processor (product_id, manufacturer, name, cores, clock, cache, product_product_id)
values (115, 'Intel', 'Celeron N3450', 4, '1.1 GHz', '2 MB', 115);

insert into processor (product_id, manufacturer, name, cores, clock, cache, product_product_id)
values (1, 'Unknown - N/A', 'Unknown - N/A', 0, '', '', 1);

insert into storage (product_id, manufacturer, name, capacity, product_product_id)
values (116, 'Seagate', 'BarraCuda 3.5" 1TB', '1 TB', 116);

insert into storage (product_id, manufacturer, name, capacity, product_product_id)
values (117, 'Seagate', 'BarraCuda 3.5" 2TB', '2 TB', 117);

insert into storage (product_id, manufacturer, name, capacity, product_product_id)
values (110, 'Western Design', 'Mainstream 3.5" 3TB', '3 TB', 110);

insert into storage (product_id, manufacturer, name, capacity, product_product_id)
values (1, 'Unknown - N/A', 'Unknown - N/A', '', 1);

insert into graphics_card (product_id, manufacturer, name, bus, memory, gpu_clock, memory_clock, product_product_id)
values (118, 'NVIDIA', 'GeForce GTX 1050 Ti', 'PCIe 3.0 x16', '4 GB', '1291 MHz', '1752 MHz', 118);

insert into graphics_card (product_id, manufacturer, name, bus, memory, gpu_clock, memory_clock, product_product_id)
values (121, 'NVIDIA', 'GeForce GTX 760', 'PCIe 3.0 x16', '2 GB', '980 MHz', '1502 MHz', 121);

insert into graphics_card (product_id, manufacturer, name, bus, memory, gpu_clock, memory_clock, product_product_id)
values (119, 'NVIDIA', 'Quadro GP100', 'PCIe 3.0 x16', '16 GB', '3584 MHz', '717 MHz', 119);

insert into graphics_card (product_id, manufacturer, name, bus, memory, gpu_clock, memory_clock, product_product_id)
values (109, 'NVIDIA', 'RTX 2070', 'PCIe 3.0 x16', '8 GB', '1410 MHz', '1750 MHz', 109);

insert into graphics_card (product_id, manufacturer, name, bus, memory, gpu_clock, memory_clock, product_product_id)
values (1, 'Unknown - N/A', 'Unknown - N/A', '', '', '', '', 1);

insert into ram (product_id, manufacturer, name, capacity, product_product_id)
values (108, 'Corsair', 'DDR3 PC RAM', '8 GB', 108);

insert into ram (product_id, manufacturer, name, capacity, product_product_id)
values (1, 'Unknown - N/A', 'Unknown - N/A', '', 1);

insert into motherboard (product_id, manufacturer, name, ram_slots, max_ram, product_product_id)
values (107, 'Asus', 'ROG STRIX B450-F AM4', '4', '64 GB', 107); --intel

insert into motherboard (product_id, manufacturer, name, ram_slots, max_ram, product_product_id)
values (120, 'Asus', 'ROG Z390 MAXIMUS XI 1151 ATX', '4', '64 GB', 120); --amd

insert into motherboard (product_id, manufacturer, name, ram_slots, max_ram, product_product_id)
values (1, 'Unknown - N/A', 'Unknown - N/A', '', '', 1); --amd

insert into trending_pc (product_id, manufacturer, name, product_product_id, cpu_product_id, 
gpu_product_id, motherboard_product_id, ram_qty, ram_product_id, storage_product_id) values
(100, 'HP', 'Pavilion 590-a0017na', 100, 112, 121, 120, 1, 108, 116);

insert into trending_pc (product_id, manufacturer, name, product_product_id, cpu_product_id, 
gpu_product_id, motherboard_product_id, ram_qty, ram_product_id, storage_product_id) values
(101, 'PCSpecialist', 'Vortex Fusion Extreme II', 101, 106, 109, 107, 2, 108, 117);

insert into trending_pc (product_id, manufacturer, name, product_product_id, cpu_product_id, 
gpu_product_id, motherboard_product_id, ram_qty, ram_product_id, storage_product_id) values
(102, 'ACER', 'Orion 3000', 102, 113, 118, 107, 2, 108, 116);

insert into trending_pc (product_id, manufacturer, name, product_product_id, cpu_product_id, 
gpu_product_id, motherboard_product_id, ram_qty, ram_product_id, storage_product_id) values
(103, 'ThinkStation', 'P720 Tower', 103, 114, 119, 107, 3, 108, 110);

insert into trending_pc (product_id, manufacturer, name, product_product_id, cpu_product_id, 
gpu_product_id, motherboard_product_id, ram_qty, ram_product_id, storage_product_id) values
(104, 'HP', 'Stream 11-ak0500sa', 104, 115, 121, 107, 1, 108, 116);

insert into trending_pc (product_id, manufacturer, name, product_product_id, cpu_product_id, 
gpu_product_id, motherboard_product_id, ram_qty, ram_product_id, storage_product_id) values
(105, 'MSI', 'GF62 8RC 15.6"', 105, 113, 118, 107, 1, 108, 116);

insert into review (id, body, rating, user_email, product_product_id) 
values (400, 'Great laptop! Definitely worth buying.', 4, 'customer@play.com', 105);
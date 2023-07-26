
insert into city_data (country,name,state,timezone,lat,lon) values ('RU','Moscow','Moscow','Europe/Moscow',55.7504461,37.6174943);
insert into city_data (country,name,state,timezone,lat,lon) values ('RU','Borovichi','Novgorod Oblast','Europe/Moscow',58.389009,33.9068275);
insert into city_data (country,name,state,timezone,lat,lon) values ('RU','Saint Petersburg','Saint Petersburg','Europe/Moscow',59.938732,30.316229);

insert into users (current_city_lat,current_city_lon,current_state,is_notif,notification_city_lat,notification_city_lon,notification_days,notification_time,previous_state,user_id) values (55.7504461,37.6174943,0,false,null,null,null,null,0,987654321);
insert into users (current_city_lat,current_city_lon,current_state,is_notif,notification_city_lat,notification_city_lon,notification_days,notification_time,previous_state,user_id) values (null,null,1,true,58.389009,33.9068275,'{1,2,0,0,0,0,0}','09:00:00',0,123456789);
insert into users (current_city_lat,current_city_lon,current_state,is_notif,notification_city_lat,notification_city_lon,notification_days,notification_time,previous_state,user_id) values (55.7504461,37.6174943,2,false,null,null,'{1,2,0,0,0,0,0}','09:00:00',0,111111111);
insert into users (current_city_lat,current_city_lon,current_state,is_notif,notification_city_lat,notification_city_lon,notification_days,notification_time,previous_state,user_id) values (55.7504461,37.6174943,3,true,null,null,'{1,2,0,0,0,0,0}','09:00:00',0,22222222);
insert into users (current_city_lat,current_city_lon,current_state,is_notif,notification_city_lat,notification_city_lon,notification_days,notification_time,previous_state,user_id) values (null,null,0,false,null,null,null,null,0,333333333);

insert into last_three_cities (user_id,last_three_cities_lat,last_three_cities_lon) values (111111111,55.7504461,37.6174943);
insert into last_three_cities (user_id,last_three_cities_lat,last_three_cities_lon) values (111111111,58.389009,33.9068275);
insert into last_three_cities (user_id,last_three_cities_lat,last_three_cities_lon) values (111111111,59.938732,30.316229);
insert into last_three_cities (user_id,last_three_cities_lat,last_three_cities_lon) values (987654321,55.7504461,37.6174943);

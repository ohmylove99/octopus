drop table if exists DEMO_API_JPA_SIMPLE_BOOK;
create table DEMO_API_JPA_SIMPLE_BOOK (
	id bigint not null auto_increment, 
	name varchar(20), 
	price decimal(19,2), 
	publish_date datetime, 
	primary key (id)
) engine=MyISAM;

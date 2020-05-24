create table if not exists ccccc
(
	id int auto_increment
		primary key,
	name varchar(100) null,
	age tinyint null,
	a_id int null,
	amount double(3,2) null,
	create_time datetime null,
	year year null,
	dates date null,
	stamp timestamp null,
	times time null
);

create index idx_mixed
	on ccccc (name, age, amount);


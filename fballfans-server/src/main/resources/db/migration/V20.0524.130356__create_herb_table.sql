create table herb_back
(
	id bigint auto_increment comment '主键ID'
		primary key,
	name varchar(20) null comment '草药名称',
	ripe_duration int null comment '预计成熟所需时间',
	amount bigint null comment '库存数量',
	functions text null comment '药物功效',
	chinese_name varchar(20) null,
	remark text null comment '备注，介绍'
);




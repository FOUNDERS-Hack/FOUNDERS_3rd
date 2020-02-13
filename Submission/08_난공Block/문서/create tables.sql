show databases;
create database mapjd;
use mapjd;

show tables;
drop table user;
create table users (
	userId varChar(45) not null,
	userPw varChar(45) not null,
	userName varChar(45) not null,
	reviewCnt int not null default 0,
	primary key (userId)
);

drop table reviews;
create table reviews (
	reviewId int not null primary key auto_increment,
	userId varchar(45) not null,
    storeId varchar(45) not null,
    reviewText longtext,
    reviewStar int,
    reviewVote float,
    reviewDate Date,
    foreign key(userId) references users(userId) on update cascade on delete cascade
);

create table auths (
	userId varchar(45) not null,
    storeId varchar(45) not null,
    reviewAuth boolean not null default false,
    primary key(userId, storeId)
);

desc auths;
desc users;
desc reviews;
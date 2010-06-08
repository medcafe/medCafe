drop database if exists ovid;

create database ovid;

grant all privileges on ovid.* to 'ovid'@'%' identified by 'ovid';
grant all privileges on ovid.* to 'ovid'@'localhost' identified by 'ovid';

flush privileges;

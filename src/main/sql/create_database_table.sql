-- 连接数据库
-- mysql -uroot -proot -P3306 -h10.170.32.244
CREATE DATABASE IF NOT EXISTS vsse2 DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use vsse2
CREATE TABLE IF NOT EXISTS XSets(
	xSet VARCHAR(400)
);
#
CREATE TABLE IF NOT EXISTS TSets(
	label VARCHAR(120),
	e blob,
	y blob,
	keyword VARCHAR(500)
)
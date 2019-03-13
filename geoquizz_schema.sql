DROP TABLE IF EXISTS partie;
CREATE TABLE partie (
	id varchar(128) NOT NULL,
	token varchar(128) NOT NULL,
	nb_photos bigint DEFAULT 10,
	status varchar(25) DEFAULT 'créée',
	score bigint DEFAULT 0,
	joueur varchar(128) NOT NULL,
	serie_id varchar(128) NOT NULL,
	PRIMARY KEY(id));

DROP TABLE IF EXISTS serie;
CREATE TABLE serie (
	id varchar(128) NOT NULL,
	ville varchar(50) NOT NULL,
	map_lat varchar(50) NOT NULL,
	map_long varchar(50) NOT NULL,
	dist bigint NOT NULL,
	PRIMARY_KEY(id));

DROP TABLE IF EXISTS photo;
CREATE TABLE photo (
	id varchar(128) NOT NULL,
	desc varchar(200) DEFAULT NULL,
	latitude varchar(50) NOT NULL,
	longitude varchar(50) NOT NULL,
	url varchar(200) NOT NULL,
	serie_id varchar(128) NOT NULL,
	PRIMARY KEY(id));

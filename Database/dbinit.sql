

CREATE TABLE users (
	u_identity varchar(256) NOT NULL,
	u_username varchar(25) PRIMARY KEY
	);

CREATE TABLE channels (
	ch_name varchar(50) NOT NULL,
	ch_owner varchar(256) NOT NULL,
	ch_spec INT UNSIGNED  NOT NULL,
	ch_public BOOL NOT NULL,
	FOREIGN KEY (ch_owner) REFERENCES users (u_username),
	PRIMARY KEY (ch_name, ch_owner)
	);

CREATE TABLE channelmods (
	cm_m_username varchar(25) NOT NULL,
	cm_m_name varchar(50) NOT NULL,
	FOREIGN KEY (cm_m_username) REFERENCES users (u_username),
	FOREIGN KEY (cm_m_name) REFERENCES channels (ch_name)
	);

CREATE TABLE channelfavs (
	ch_fav_username varchar(25) NOT NULL,
	ch_fav_chname varchar(50) NOT NULL,
	ch_fav_chowner varchar(25) NOT NULL,
	FOREIGN KEY (ch_fav_username) REFERENCES users (u_username),
	FOREIGN KEY (ch_fav_chname) REFERENCES channels (ch_name),
	FOREIGN KEY (ch_fav_chowner) REFERENCES users (u_username)
	);

CREATE TABLE posts_okenso_march13 (
 p_poster varchar(25) NOT NULL,
 p_number INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
 p_TitleText varchar(100),
 p_time DATETIME,
 p_PictureField varchar(100),
 FOREIGN KEY (p_poster) REFERENCES users (u_username)
 );



source /var/www/okenso.com/cmpt371group2/Database/dbdummydata.sql

/** Sample Table
CREATE TABLE tblname (
	name domain NOT NULL,
	name2 domain2 NOT NULL,
	FOREIGN KEY (nameinthistable) REFERENCES tblname (nameinothertable),
	PRIMARY KEY (name, name2)
);
*/




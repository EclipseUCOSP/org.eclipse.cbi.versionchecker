CREATE TABLE maven_p2
(
	id SERIAL,
	git_repo varchar(255) NOT NULL,
	git_commit varchar(255) NOT NULL,
	git_branch varchar(100),
	project varchar(255),
	git_tag varchar(255),
	p2_version varchar(255),
	maven_version varchar(255),
	PRIMARY KEY (id)
)
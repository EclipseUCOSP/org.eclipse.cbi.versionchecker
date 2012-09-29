DROP TABLE maven_p2;


CREATE TABLE maven_p2
(
  git_commit varchar(255) NOT NULL,
  git_tag varchar(255),
  p2_qualifier varchar(255),
  maven_version varchar(255)
);


DROP TABLE maven_p2;

--TODO: add foreign key from project magement db

CREATE TABLE maven_p2
(
  git_commit varchar NOT NULL,
  git_tag varchar,
  p2_version varchar,
  maven_version varchar
);


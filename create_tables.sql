DROP INDEX git;
DROP TABLE maven_p2;

--TODO: add foreign key from project magement db
CREATE TABLE maven_p2
(
  git_repo varchar NOT NULL,
  git_commit varchar NOT NULL,
  git_branch varchar,
  project varchar(100),
  git_tag varchar,
  p2_version varchar,
  maven_version varchar
);

--this is a btree index. Hash index is preferred, but
--sqlite does not support it.
CREATE INDEX git on maven_p2(git_repo, git_commit);


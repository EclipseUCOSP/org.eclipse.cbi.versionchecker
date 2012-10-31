db
==
Maven P2 Version Matcher
------------------------
- mvn assembly:single will build a standalone jar (for testing)

### Command line syntax
- Command line syntax is "command -opt0 value0 -opt1 value1 ...". 

The recognized commands are:
- add
- find
- update

The recognized options are:
- -repo
- -cmt
- -p
- -gtag
- -p2v
- -mvnv
- -br

- mvn install will build a jar

### Database schema notes:
 
- git_commit is required and at least one of p2_qualifier or maven_version. Currently this is being handled on the application
side because of the limitations of Sqlite.

- We plan is to link our table with a foreign key in the project mamangement datadase on Eclipse's end.

- We are storing both the maven version and the p2 version as single strings. It might be advantageous to break these out to major, minor, qualifier in the future, but for now we cannot see the necessity.

- There are no limits on the size of the maven_p2 table columns. It looks like the max size of each version number is the max size of int*3 plus a string. This can be changed when we get better information.    

### Database Ineterface

- Database Name is hardcoded, but we are planning on changing it to be a config file option to help when writing tests.
- PreparedStatements were used to insert rows because it eliminates the risk of SQL injection.
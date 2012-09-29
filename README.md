db
==

Database schema notes:
 
-git_commit is required and at least one of p2_qualifier or maven_version. Currently only git_commit is required because of the limitations of sqlite. The second rule will have to be handled in the application only for now.

-There is no project name or desciption currenly. The plan is to link with a foreign key in the project mamangement datadase on Eclipse's end.

-We are storing both the maven version and the p2 version as single strings. It might be advantageous to break these out to major, minor, qualifier in the future, but for now we cannot see the necessity.

-There are no limits on the size of the maven_p2 table collums. It looks like the max size of each version number is the max size of int*3 plus a string. This can be changed when we get better information.    

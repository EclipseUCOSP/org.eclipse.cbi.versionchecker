TODO:

* need to remove the my.db from the repo and use one hosted on nexus
* may want to save this until after most db functionality and testing complete, however

Perform the following steps to run the plugin:

* mvn clean install
* mvn org.ucosp.eclipse:mvn-p2-vt-plugin:add-maven-version
* an add entry for this project is sent to MVNP2Util

If you get the error:
Unbound classpath variable: 'M2_REPO/...' in project '...' it means you have not setup your M2_REPO variable as a classpath variable in Eclipse. To fix this:

* Open Eclipse Preferences
* Go to Java -> Build Path -> Classpath Variables
* Add a variable named M2_REPO
* Click Folder... and select your repository folder. This will be in user home directory under .m2, ie /Users/AUser/.m2/repository

You can also do this by running the following maven command:
mvn -Declipse.workspace=<path-to-eclipse-workspace> eclipse:add-maven-repo

You must run 'mvn clean install' at least once AFTER importing this project into eclipse.
Otherwise, the CheckVersionTest class will not work inside of eclipse. This is related
to test-harness-plugin implementation. 

Using this plugin in a build
============================

Insert a <plugin> element like the following into your <plugins> section:

<b></b>

    <plugin>
      <groupId>org.eclipse.cbi.versionchecker</groupId>
      <artifactId>org.eclipse.cbi.versionchecker.maven.plugin</artifactId>
      <version>1.4.0-SNAPSHOT</version>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>add-maven-version</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

Then, refer to the "Configuring MySQL" section.

Configuring MySQL
=================

The plugin requires the following variables in order to run:
* Database Hostname
* Database Name
* Username
* Password

There are two ways to configure this.

In ~/.m2/settings.xml
---------------------

This is preferred if you will be building several different projects on one system, and want all of their information to go into one database.

1. Create a <profiles> section at the end of <settings> if one doesn't already exist.

2. Create a profile similar to the following. Change the values in CAPITALS to match your mysql database configuration. The profile id can also be changed.

        <profile>
          <id>vc_database</id>
          <activation>
            <activeByDefault>false</activeByDefault>
          </activation>
          <properties>
            <cbi.versionchecker.db.host>MYSQL_SERVER_HOSTNAME</cbi.versionchecker.db.host>
            <cbi.versionchecker.db.database>MYSQL_SERVER_DATABASE</cbi.versionchecker.db.database>
            <cbi.versionchecker.db.user>MYSQL_USER</cbi.versionchecker.db.user>
            <cbi.versionchecker.db.password>MYSQL_PASSWORD</cbi.versionchecker.db.password>
          </properties>
        </profile>

3. Enable the profile when using the plugin, i.e. "mvn clean package -P vc_database"

In pom.xml
----------

This is preferred if you want to specify one database for this particular project.
Modify your pom.xml to configure org.eclipse.cbi.versionchecker.maven.plugin with: dbHost, dbDatabase, dbUser, dbPassword. Such as:

    <plugin>
      <groupId>org.eclipse.cbi.versionchecker</groupId>
      <artifactId>org.eclipse.cbi.versionchecker.maven.plugin</artifactId>
      <version>1.4.0-SNAPSHOT</version>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
          <goal>add-maven-version</goal>
            </goals>
        </execution>
      </executions>
      <configuration>
        <repositoryRoot>${basedir}/../.git</repositoryRoot>
        <dbHost>MYSQL_SERVER_HOSTNAME</dbHost>
        <dbDatabase>MYSQL_SERVER_DATABASE</dbDatabase>
        <dbUser>MYSQL_USER</dbUser>
        <dbPassword>MYSQL_PASSWORD</dbPassword>
      </configuration>
    </plugin>

Configuration Options
=====================

TODO: Document all of the options, and why you would want to use them.


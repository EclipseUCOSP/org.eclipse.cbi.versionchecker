Version Checker Eclipse Plugin
=======
Requirement:
------------------------
1. Java 1.6 +

How to run this project:
------------------------
1. Clone the code

        git clone https://github.com/EclipseUCOSP/org.eclipse.cbi.versionchecker.git

2. cd to the directory

        cd org.eclipse.cbi.versionchecker/org.eclipse.cbi.versionchecker.ui

3. Build through mvn (this step will copy all necessary external library jars)

        mvn clean install

4. Import the project into Eclipse as an "Existing Projects into Workspace".
5. Click "Project->Clean"
6. Click "Run" and select "Eclipse Application", if prompted.
7. Click "Help -> Version Checker" on the top menu to boot the plug-in.

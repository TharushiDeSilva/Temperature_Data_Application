mvn clean install
place the temperature-data-application.war in apache tomcat--> webapps
Http://localhost:8080/temperature-data-application
Writes sensor data to a properties file. the output stream writes only one set of data at a time.
This happens because the properties file is read from the server and written to the local machine.
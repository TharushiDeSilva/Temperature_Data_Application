mvn clean install
place the temperature-data-application.war in apache tomcat--> webapps
Http://localhost:8080/temperature-data-application
Writes sensor data to a properties file. the output stream writes only one set of data at a time. this should be handled.
Since the next step is to write data to a sql database, the previous issue was not property handled.
mvn clean install
place the temperature-data-application.war in apache tomcat--> webapps
Http://localhost:8080/temperature-data-application

DHT11 data is handled by the POST method.
Sensor data are written into weatherData.properties that resides within WEB-INF folder. This file can now be read and write.
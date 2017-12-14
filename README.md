mvn clean install
place the temperature-data-application.war in apache tomcat--> webapps
Http://localhost:8080/temperature-data-application/cache-filter

for a full view of a test data set

webapps/static/js/testScript.js ---> uncomment the line 7
        $.get("/temperature-data-application/static/json/mockData.json", function(responseJson){

        and remove the line below (line8)
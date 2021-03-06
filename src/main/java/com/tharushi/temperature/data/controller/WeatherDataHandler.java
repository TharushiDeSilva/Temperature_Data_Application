package com.tharushi.temperature.data.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;

@WebServlet ("/weather-data")
public class WeatherDataHandler extends HttpServlet{
    public int sensorID = 2;
    public String date = "default";
    public String time = "default";
    public String dateTime = "default";
    public Double temperature = 0.00;
    public Double humidity = 0.00;
    public Double heatIndex = 0.00;

    public int logCount = 0;        //Used as an interval to log data.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("sensorID", Integer.toString(sensorID));
        request.setAttribute("date", date);
        request.setAttribute("time", time);
        request.setAttribute("dateTime", date+"-"+time);
        request.setAttribute("temperature", Double.toString(temperature));
        request.setAttribute("humidity", Double.toString(humidity));
        request.setAttribute("heatIndex", Double.toString(heatIndex));
        writeToJSONAsString();

        request.getRequestDispatcher("/ftl/hourlyTemperatureData.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        setParameters(request);
        //Writing to the properties file
        //writeLogData(date,time, sensorID, temperature, humidity, heatIndex);

        //writing to the json file. Do not keep the file weatherData.json empty.
        writeToJSON();
        updateTemperatureFile();
        /*logCount +=1;

        if(logCount == 5) {
            writeToJSONAsString();
            logCount = 0;
        }*/
    }


    // the code below is connected to writing the sensor data into a properties file. ---------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------
    private String createKey(String date, String time){
        String key = date+"//"+time;
        return key;
    }

    private String createValue(int sensorID, Double temperature, Double humidity, Double heatIndex){
        String value = sensorID+"//"+temperature+"//"+humidity+"//"+heatIndex;
        return value;
    }

    protected String readLogData(String key) {
        String value = "";
        Properties properties = new Properties();
        FileInputStream input = null;
        try{
            input = new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/weatherData.properties"));
            properties.load(input);
            value = (String) properties.getProperty(key);
            input.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return value;
    }

    protected void writeLogData(String date, String time, int sensorID, Double temperature, Double humidity, Double heatIndex){
        String key = createKey(date, time);
        String value = createValue(sensorID, temperature, humidity, heatIndex);
        Properties properties = new Properties();
        FileOutputStream fileOut = null;
        FileInputStream fileIn = null;
        try{
            fileIn = new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/weatherData.properties"));
            properties.load(fileIn);
            fileOut = new FileOutputStream(getServletContext().getRealPath("/WEB-INF/classes/weatherData.properties"));
            properties.setProperty(key, value);
            properties.store(fileOut, null);
            fileOut.close();
            fileIn.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    protected String getBody(HttpServletRequest request) throws IOException{
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try{
            InputStream inputStream = request.getInputStream();
            if(inputStream != null){
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while((bytesRead = bufferedReader.read(charBuffer))>0){
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }else{
                stringBuilder.append("");
            }
        }catch (IOException ex){
            throw ex;
        }finally {
            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                }catch (IOException ex){
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
        return body;
    }


    protected void setParameters(HttpServletRequest request) throws IOException{
        String[] paramArray = getBody(request).split("\"");
        this.sensorID = Integer.parseInt(paramArray[3]);
        this.date = paramArray[7];
        this.time = paramArray[11];
        this.temperature = Double.parseDouble(paramArray[15]);
        this.humidity = Double.parseDouble(paramArray[19]);
        this.heatIndex = Double.parseDouble(paramArray[23]);
        this.dateTime = paramArray[7] + "-" + paramArray[11];
    }

    public JSONArray readFromJSON(){
        FileInputStream input = null;
        JSONArray jsonArray = null;
        try {
            input = new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/json/weatherData.json"));
            JSONParser jsonParser = new JSONParser();
            //JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(input, "UTF-8"));  //If there is only one JSON Object in the file
            jsonArray = (JSONArray) jsonParser.parse(new InputStreamReader(input, "UTF-8"));
            //System.out.println(jsonArray);
            input.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
        return jsonArray;
    }
    /*public String prepareDataForCurrentReport(){
        JSONArray dataArray = readFromJSON();
        dataArray.
    }*/

    public void writeToJSON() {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sensorID",sensorID);
        jsonObject.put("date", date);
        jsonObject.put("time", time);
        jsonObject.put("temperature", temperature);
        jsonObject.put("humidity",humidity);
        jsonObject.put("heatIndex", heatIndex);

        try{
            fileInputStream = new FileInputStream(getServletContext().getRealPath("/static/json/weatherData.json"));
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new InputStreamReader(fileInputStream, "UTF-8"));
            jsonArray.add(jsonObject);
            fileOutputStream = new FileOutputStream(getServletContext().getRealPath("/static/json/weatherData.json"));
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.write(jsonArray.toString());
            writer.close();
            fileInputStream.close();
            fileInputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public void updateTemperatureFile() {
        //don't leave this file empty.
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sensorID",sensorID);
        jsonObject.put("dateTime", dateTime);
        jsonObject.put("temperature",temperature);
        //check the date of the parameters set and add the data into the relevant file.

        try{

            fileInputStream = new FileInputStream(getServletContext().getRealPath("/static/json/temperatureData.json"));
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new InputStreamReader(fileInputStream, "UTF-8"));
            jsonArray.add(jsonObject);
            fileOutputStream = new FileOutputStream(getServletContext().getRealPath("/static/json/temperatureData.json"));
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.write(jsonArray.toString());
            writer.close();
            fileInputStream.close();
            fileInputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
    public boolean checkExistence(String fileName) {
        boolean status = false;
        String pathname = "/static/json/"+fileName;
        File f = new File(getServletContext().getRealPath(pathname));
        if(f.isFile()){
            status = true;
        }
        return status;
    }

    public void writeToJSONAsString() {
        String toWrite = "data ='";
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        try {
            fileInputStream = new FileInputStream(getServletContext().getRealPath("static/json/temperatureData.json"));
            toWrite += new Scanner(fileInputStream, "UTF-8").useDelimiter("\\A").next();
            toWrite += "'";
            fileOutputStream = new FileOutputStream(getServletContext().getRealPath("/static/json/temperatureDataString.json"));    //creates an new file if doesn't exist
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.write(toWrite);
            writer.close();
            fileInputStream.close();
            fileOutputStream.close();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

}

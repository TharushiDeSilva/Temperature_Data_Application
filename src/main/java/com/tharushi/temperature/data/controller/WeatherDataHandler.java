package com.tharushi.temperature.data.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;

@WebServlet ("/weather-data")
public class WeatherDataHandler extends HttpServlet{
    public int sensorID = 0;
    public String date = "default";
    public String time = "default";
    public Double temperature = 0.00;
    public Double humidity = 0.00;
    public Double heatIndex = 0.00;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //sending temperature data to this servlet as GET requests and they are processes within this module.
        if(request.getParameterMap().containsKey("sensorID")) {
            this.sensorID = Integer.parseInt(request.getParameter("sensorID"));
        }
        if(request.getParameterMap().containsKey("date")) {
            this.date = request.getParameter("date");
        }
        if(request.getParameterMap().containsKey("time")) {
            this.time = request.getParameter("time");
        }
        if(request.getParameterMap().containsKey("temperature")) {
            this.temperature = Double.parseDouble(request.getParameter("temperature"));
        }
        if(request.getParameterMap().containsKey("humidity")) {
            this.humidity = Double.parseDouble(request.getParameter("humidity"));
        }
        if(request.getParameterMap().containsKey("heatIndex")) {
            this.heatIndex = Double.parseDouble(request.getParameter("heatIndex"));
        }

        request.setAttribute("sensorID", Integer.toString(sensorID));
        request.setAttribute("date", date);
        request.setAttribute("time", time);
        request.setAttribute("temperature", Double.toString(temperature));
        request.setAttribute("humidity", Double.toString(humidity));
        request.setAttribute("heatIndex", Double.toString(heatIndex));
        request.getRequestDispatcher("/ftl/weatherData.ftl").forward(request, response);
        //System.out.println(readLogData("19-09-2017//16-54-00"));
        writeLogData(date,time, sensorID, temperature, humidity, heatIndex);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

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
        Properties prop = new Properties();
        InputStream input = null;
        String value = "not defined";
        try{
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("weather-data.properties");
            prop.load(input);
            value = (String) prop.getProperty(key);
            input.close();
        }catch(IOException io){
            io.printStackTrace();
        }
        return value;
    }
    protected void writeLogData(String date, String time, int sensorID, Double temperature, Double humidity, Double heatIndex){
        String key = createKey(date, time);
        String value = createValue(sensorID, temperature, humidity, heatIndex);
        Properties prop = new Properties();
        InputStream input = null;
        OutputStream output = null;
        try{
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("weather-data.properties");
            prop.load(input);
            output = new FileOutputStream("D:/Temperature_Data_Application/src/main/resources/weather-data.properties");
            prop.setProperty(key, value);
            prop.store(output, null);
            input.close();
            output.close();
        }catch(IOException io){
            io.printStackTrace();
        }
    }

}

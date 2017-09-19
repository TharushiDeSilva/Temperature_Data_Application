package com.tharushi.temperature.data.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

    }

}

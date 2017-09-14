package com.tharushi.temperature.data.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.tharushi.temperature.data.sensor.SensorData;

@WebServlet ("/user-access")
public class UserDataHandler extends HttpServlet {

    //Read the value of an entry given the key

    protected String readUserData(String key) {
        Properties prop = new Properties();
        InputStream input = null;
        String value = "not defined";
        try{
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("user-data.properties");
            prop.load(input);
            value = (String) prop.getProperty(key);
            input.close();
        }catch(IOException io){
            io.printStackTrace();
        }
        return value;
    }

    //adding a new key, value pair
    private static void addNewUser(String key, String value, String comment) {
        try{
            FileInputStream in = new FileInputStream("user-data.properties");
            Properties prop = new Properties();
            prop.load(in);
            in.close();
            FileOutputStream out = new FileOutputStream("user-data.properties");
            prop.setProperty(key, value);
            prop.store(out, comment);
            out.close();
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    //Reading all the data of the properties file and printing them.
    protected String readAllUserData() {
        String outputValue ="";
        Properties prop = new Properties();
        InputStream input = null;
        try{
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("user-data.properties");
            prop.load(input);
            Enumeration<?> enumeration = prop.propertyNames();
            while(enumeration.hasMoreElements()){
                String key = (String) enumeration.nextElement();
                String value = (String) prop.getProperty(key);
                String line = ("username: "+key+" password: "+value);
                outputValue += line;
            }
        }catch(IOException io){
            io.printStackTrace();
        }finally {
            if(input != null){
                try{
                    input.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return outputValue;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.getRequestDispatcher("/ftl/controllerLogin.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameterMap().containsKey("username")) {
            String userName = request.getParameter("username");
            request.setAttribute("username", userName);
            //checking the user permission level defined in the user-data.properties file
            String accessType = readUserData(userName);
            if (accessType == null) {
                request.setAttribute("accessType", "not defined");
            } else {
                request.setAttribute("accessType", accessType);
            }
            request.getRequestDispatcher("/ftl/controllerAccess.ftl").forward(request, response);
        }

        //this means data ara shown to the user.
        else if(request.getParameterMap().containsKey("accessType")) {

            //set an instance of temperature and humidity data and retrieve their values.
            SensorData data = new SensorData();
            String timeStamp = data.getTimeStamp();
            String temperature = Double.toString(data.getTemperature());
            String humidity = Double.toString(data.getHumidity());

            request.setAttribute("timeStamp", timeStamp);
            request.setAttribute("temperature", temperature);
            request.setAttribute("humidity", humidity);

            request.getRequestDispatcher("/ftl/visualData.ftl").forward(request, response);
        }
    }
}

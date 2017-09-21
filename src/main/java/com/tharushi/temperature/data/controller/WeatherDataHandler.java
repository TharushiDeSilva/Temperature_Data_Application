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
        setParameters(request);
        writeLogData(date,time, sensorID, temperature, humidity, heatIndex);


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

    }

}

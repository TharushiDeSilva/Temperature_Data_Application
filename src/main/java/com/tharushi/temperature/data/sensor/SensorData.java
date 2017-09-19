package com.tharushi.temperature.data.sensor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SensorData {
    private int sensorID;
    private String timeStamp;
    private double temperature;
    private double humidity;
    private double heatIndex;
    private String date;
    private String time;

    public SensorData(){
        /*
        getTimeStamp();
        getTemperature();
        getHumidity();
        * */
        this.timeStamp = getDateTime();
        this.temperature = generateRandomTemperature();
        this.humidity = generateRandomHumidity();
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private double generateRandomTemperature(){
        double min_temp = 10.00;
        double max_temp = 35.00;
        Random r = new Random();
        double temporary = min_temp + r.nextDouble() * (max_temp - min_temp);
        double random_temp = Math.round(temporary * 100.00) / 100.00;
        return random_temp;
    }
    private double generateRandomHumidity(){
        double min_humd = 30.00;
        double max_humd = 70.00;
        Random r = new Random();
        double temporary = min_humd + r.nextDouble() * (max_humd - min_humd);
        double random_humd = Math.round(temporary * 100.00) / 100.00;
        return random_humd;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public double getTemperature(){
        return temperature;
    }
    public double getHumidity(){
        return humidity;
    }
}

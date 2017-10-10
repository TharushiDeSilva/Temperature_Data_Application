package com.tharushi.temperature.data.dataSetGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Random;

//this class is used to generate data set for testing purpose
@WebServlet ("/dataset-creator")
public class DataSetGenerator extends HttpServlet{
    String year = "2017";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("Generating the dataset.......");
        String data = generateDataset();
        System.out.println("Finished generating the dataset");
        System.out.println("Writing to File....");
        updateTemperatureFile(data);
        System.out.println("Finished writing to file");
        request.getRequestDispatcher("/ftl/datasetGenerator.ftl").forward(request,response);
    }

    public String intToString(int a){
        String returnString = Integer.toString(a);
        if(returnString.length()==1){
            String tempS = "0"+returnString;
            returnString = tempS;
        }
        return returnString;
    }
    public String generateDataset(){
        String returnString = "[";
        for(int i=9; i<11; i++){            //the months have been limited to September and October
            String month = intToString(i);
            //month is created.
            for(int j=1; j<32; j++){
                String day = intToString(j);

                if(j==31 &&(/*i==2 || i==4 || i==6|| */i==9 /*|| i==11*/)){
                    continue;
                }/*else if(i==2 &&(j==30||j==29)){
                    continue;
                }*/else{

                    for(int k=0; k<24;k++){
                        String hour = intToString(k);
                        for(int l=0; l<60; l++){
                            String minute = intToString(l);
                            for(int m=0; m<60; m+=10){
                                String second = intToString(m);
                                for(int n=1; n<3; n++){
                                    double[] dataset = {18.00,19.00, 20.00, 21.00, 22.00, 23.00, 24.00, 25.00, 26.00, 27.00, 28.00, 29.00, 30.00, 31.00,32.00,33.00};
                                    Random rnd = new Random();
                                    double randomNum = dataset[rnd.nextInt(15)];
                                    String dataString = "{\"dateTime\":\"2017-"+month+"-"+day+"-"+hour+"-"+minute+"-"+second+"\""+","+"\"sensorID\":"+n+",\"temperature\":"+randomNum+"},";
                                    System.out.println(dataString);
                                    returnString+= dataString;
                                }
                            }
                        }
                    }
                }
            }
        }
        returnString += "]";
        return returnString;
    }
    public void updateTemperatureFile(String jsonString) {
        FileOutputStream fileOutputStream;

        try{

            fileOutputStream = new FileOutputStream(getServletContext().getRealPath("/static/json/temperatureData.json"));
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.write(jsonString);
            writer.close();
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

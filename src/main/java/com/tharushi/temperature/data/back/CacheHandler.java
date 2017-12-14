package com.tharushi.temperature.data.back;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebServlet("/cache-handler")
public class CacheHandler extends HttpServlet {

    int sensorID = 1;
    int xCoordinate = 0;
    int yCoordinate = 0;
    String date = "0000.00.00";
    String time = "00.00.00";
    Double temperature = 0.00;

    // --------------------------------------------------------------------------------------------------------------------
    //This is a matrix that contains the existing Temperature sensing devices.
    //when it's necessary to add new devices, just send their x,y coordinates along with the request.
    int[][] locationMatrix = new int[7][6];
    //---------------------------------------------------------------------------------------------------------------------

    ConcurrentMap<Integer, LinkedHashMap> cache = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        String toPublish = convertCacheToJSON().toString();
        //String toPublish = "{\"data\":[{\"values\":[{\"temperature\":25.0,\"time\":\"08.48.02\"},{\"temperature\":25.0,\"time\":\"08.49.09\"}],\"location\":{\"x\":0,\"y\":1},\"id\":8}, {\"values\":[{\"temperature\":25.0,\"time\":\"08.58.02\"},{\"temperature\":26.0,\"time\":\"07.49.50\"}],\"location\":{\"x\":0,\"y\":0},\"id\":1}]}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(toPublish);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        setParameters(request);
        if(temperature != null){
            if(!cache.containsKey(sensorID)){
                locationMatrix[xCoordinate][yCoordinate] = sensorID;
                LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<String, Double>();
                linkedHashMap.put(time, temperature);
                cache.put(sensorID, linkedHashMap);
            }else{
                cache.get(sensorID).put(time, temperature);
            }
        }
        deleteExpiredData();
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
        Date date = new Date();         //create the timestamp at the time of arrival.
        this.date = new SimpleDateFormat("yyyy.MM.dd").format(date);
        this.time = new SimpleDateFormat("HH.mm.ss").format(date);
        String[] paramArray = getBody(request).split("\"");
        this.sensorID = Integer.parseInt(paramArray[3]);
        this.xCoordinate = Integer.parseInt(paramArray[7]);
        this.yCoordinate = Integer.parseInt(paramArray[11]);
        this.temperature = Double.parseDouble(paramArray[15]);
    }

    //Use this method when you have to check the parameters that are received along with the HTTP POST request.
    protected void parameterPositionEvaluator(HttpServletRequest request) throws IOException{
        String[] paramArray = getBody(request).split("\"");
        for(int i=0; i<paramArray.length; i++){
            System.out.print(i+" "+paramArray[i]);
            System.out.println();
        }
    }


    protected void deleteExpiredData(){ //expire data after one hour of arrival
        Integer referenceHour = Integer.parseInt(time.substring(0,2)) - 1;
        if(referenceHour == -1){
            referenceHour = 23;
        }
        String hourInString = referenceHour.toString();
        if(hourInString.length() ==1){
            hourInString = "0"+hourInString;
        }
        String referenceTime = hourInString+time.substring(2,8);    //delete the entries that has timeStamp older than this.

        Set set = cache.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Map map2 = (Map)entry.getValue();
            Set set2 = map2.entrySet();
            Iterator iterator2 = set2.iterator();
            while(iterator2.hasNext()){
                Map.Entry entry2 = (Map.Entry) iterator2.next();
                int comparison = entry2.getKey().toString().compareTo(referenceTime);       //compare the timestamps
                if(comparison>=0){
                    break;
                }else{
                    map2.remove(entry2.getKey());
                }
            }
        }
    }
/*
    private JSONArray convertCacheToJSON(){
        JSONArray jsonArray = new JSONArray();

        Set set1 = cache.entrySet();
        Iterator iterator1 = set1.iterator();
        while(iterator1.hasNext()){
            Map.Entry entry1 = (Map.Entry)iterator1.next();

            JSONObject idObject = new JSONObject();
            idObject.put("sensorID", entry1.getKey());

            int[] location = getSensorLocation((Integer)entry1.getKey());
            JSONObject locationObject = new JSONObject();
            locationObject.put("x", location[0]);
            locationObject.put("y", location[1]);
            idObject.put("location",locationObject);

            JSONArray dataArray = new JSONArray();

            Map map2 = (Map)entry1.getValue();
            Set set2 = map2.entrySet();
            Iterator iterator2 = set2.iterator();
            while(iterator2.hasNext()){
                Map.Entry entry2 = (Map.Entry)iterator2.next();
                JSONObject dataObject = new JSONObject();
                dataObject.put("time",  entry2.getKey());
                dataObject.put("temperature", entry2.getValue());
                dataArray.add(dataObject);
            }

            idObject.put("values", dataArray);
            jsonArray.add(idObject);
        }
        return jsonArray;
    }
*/

    private JSONObject convertCacheToJSON(){
        JSONObject finalObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        Set set1 = cache.entrySet();
        Iterator iterator1 = set1.iterator();
        while(iterator1.hasNext()){
            Map.Entry entry1 = (Map.Entry)iterator1.next();

            JSONObject idObject = new JSONObject();
            idObject.put("sensorID", entry1.getKey());

            int[] location = getSensorLocation((Integer)entry1.getKey());
            JSONObject locationObject = new JSONObject();
            locationObject.put("x", location[0]);
            locationObject.put("y", location[1]);
            idObject.put("location",locationObject);

            JSONArray dataArray = new JSONArray();

            Map map2 = (Map)entry1.getValue();
            Set set2 = map2.entrySet();
            Iterator iterator2 = set2.iterator();
            while(iterator2.hasNext()){
                Map.Entry entry2 = (Map.Entry)iterator2.next();
                JSONObject dataObject = new JSONObject();
                dataObject.put("time",  entry2.getKey());
                dataObject.put("temperature", entry2.getValue());
                dataArray.add(dataObject);
            }

            idObject.put("values", dataArray);
            jsonArray.add(idObject);
        }
        //return jsonArray;
        finalObject.put("data",jsonArray);
        return finalObject;
    }

    private int[] getSensorLocation(int id){
        int[] location = new int[2];
        for(int i=0; i<locationMatrix.length; i++){
            for(int j=0; j<locationMatrix[i].length; j++){
                if(locationMatrix[i][j] == id){
                    location[0] = i;
                    location[1] = j;
                    break;
                }
            }
        }
        return location;
    }
}

package com.tharushi.temperature.data.sensor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@WebServlet ("/cache-test")
public class CacheTest extends HttpServlet{
    //Initiating a cache element.
    LoadingCache<String, String> sensorDataCache = CacheBuilder.newBuilder()
                                                .maximumSize(750)
                                                .expireAfterWrite(60, TimeUnit.MINUTES)
                                                .build(
                                                        new CacheLoader<String, String>() {
                                                            @Override
                                                            public String load(String key) throws Exception {
                                                                return null;
                                                            }
                                                        }
                                                );

    LoadingCache<Integer, Double> mostRecentValueCache = CacheBuilder.newBuilder()
                                                        .maximumSize(3)     //we have only 2 sensors right now. So keeping two values is enough. 3 is for the default value 0. this can be changed with the addition of more sensors.
                                                        .expireAfterWrite(60, TimeUnit.MINUTES)
                                                        .build(
                                                                new CacheLoader<Integer, Double>() {
                                                                    @Override
                                                                    public Double load(Integer sensorID) throws Exception {
                                                                        return null;
                                                                    }
                                                                }
                                                        );
    private Integer sensorID = 0;
    private String date = "default";
    private String dateFormatted = "default";
    private String time = "default";
    private String timeFormatted = "default";
    private String cacheKey = "default";
    private Double temperature = 0.00;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("date",dateFormatted);
        request.setAttribute("time",timeFormatted);
        request.setAttribute("temperature", Double.toString(temperature));
        JSONArray mostRecentDataArray = convertMostRecentDataCacheToJson(mostRecentValueCache.asMap());
        writeJsonAsString(mostRecentDataArray, "displayData","mostRecentDataCacheString.json");
        JSONArray dataArray = convertMapToJson(sensorDataCache.asMap());
        writeJsonAsString(dataArray,"data","cacheDataString.json");
        request.getRequestDispatcher("/ftl/hourlyCache.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        setParameters(request);
        sensorDataCache.put(cacheKey, temperature.toString());
        mostRecentValueCache.put(sensorID, temperature);
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
        String tempDate = paramArray[7];
        this.date = tempDate;
        String tempTime = paramArray[11];
        this.time = tempTime;
        this.temperature = Double.parseDouble(paramArray[15]);
        this.cacheKey = paramArray[7] + "-" + paramArray[11] + "-" + paramArray[3];
        this.dateFormatted = tempDate.substring(8,10)+"/"+tempDate.substring(5,7)+"/"+tempDate.substring(0,4);
        this.timeFormatted = tempTime.substring(0,2)+":"+tempTime.substring(3,5)+":"+tempTime.substring(6,8);
    }


    private JSONArray convertMapToJson(Map map){
        //sample map = {2017-10-10-14-38-00-1=24.0, }
        JSONArray jsonArray = new JSONArray();

        //traversing the map
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dateTime", key.substring(0,19));
            jsonObject.put("sensorID", key.substring(20,21));
            jsonObject.put("temperature", Double.parseDouble(value));
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    private JSONArray convertMostRecentDataCacheToJson(Map map){
        //sample map = {1=24.00, 2=25.00}
        JSONArray jsonArray = new JSONArray();
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sensorID",entry.getKey());
            jsonObject.put("temperature",entry.getValue());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private void writeJsonAsString(JSONArray array, String variableName, String fileName) {
        String toWrite = variableName+" ='"+array+"'";
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream = new FileOutputStream(getServletContext().getRealPath("/static/json/"+fileName));
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.write(toWrite);
            writer.close();
            fileOutputStream.close();
        }catch(IOException io){
            io.printStackTrace();
        }

    }

}

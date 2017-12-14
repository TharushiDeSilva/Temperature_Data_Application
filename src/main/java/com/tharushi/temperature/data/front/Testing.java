package com.tharushi.temperature.data.front;

import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/test")
public class Testing extends HttpServlet {
    private double temperature = 0.00;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setAttribute("temp", temperature);
        request.getRequestDispatcher("/ftl/testInterface.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       setParameters(request);
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
        this.temperature = Double.parseDouble(paramArray[15]);
    }


}

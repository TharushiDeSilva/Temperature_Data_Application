package com.tharushi.temperature.data.front;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebServlet("/user-access")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.getRequestDispatcher("/ftl/login.ftl").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        if(request.getParameterMap().containsKey("accessType")){
            String accessType = request.getParameter("accessType");
            if (accessType.equals("granted")){
                response.sendRedirect("test");
            }
        } else {
            String name = request.getParameter("username");
            String password = request.getParameter("password");
            if (name == "" || password == "") {
                request.setAttribute("message", "missing_parameters");
                request.getRequestDispatcher("/ftl/errorPage.ftl").forward(request, response);
            } else {
                String realPassword = readUserData(name);
                if(realPassword != null) {
                    if (realPassword.equals(password)) {
                        request.getRequestDispatcher("/ftl/accessPage.ftl").forward(request, response);
                    } else {
                        request.setAttribute("message", "password_mismatch");
                        request.getRequestDispatcher("/ftl/errorPage.ftl").forward(request, response);
                    }
                }else{
                    request.setAttribute("message", "unregistered_user");
                    request.getRequestDispatcher("/ftl/errorPage.ftl").forward(request, response);
                }

            }
        }

    }

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
}

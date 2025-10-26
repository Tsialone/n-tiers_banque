package com.example.controllers.utils;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class ServletMethodUtils {
    public static Method getServletMethod(HttpServletRequest req, HttpServlet servlet) {
        String httpMethod = req.getMethod(); // GET, POST, etc.
        try {
            switch (httpMethod) {
                case "GET":
                    return servlet.getClass().getMethod("doGet", HttpServletRequest.class, jakarta.servlet.http.HttpServletResponse.class);
                case "POST":
                    return servlet.getClass().getMethod("doPost", HttpServletRequest.class, jakarta.servlet.http.HttpServletResponse.class);
                case "PUT":
                    return servlet.getClass().getMethod("doPut", HttpServletRequest.class, jakarta.servlet.http.HttpServletResponse.class);
                case "DELETE":
                    return servlet.getClass().getMethod("doDelete", HttpServletRequest.class, jakarta.servlet.http.HttpServletResponse.class);
                default:
                    return null;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.example.controllers.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Flash {
    public static void set(HttpServletRequest request, String name, String value) {
        request.getSession().setAttribute("FLASH_" + name, value);
    }

    public static String get(HttpServletRequest request, String name) {
        HttpSession session = request.getSession();
        String value = (String) session.getAttribute("FLASH_" + name);
        session.removeAttribute("FLASH_" + name); // auto suppression (flash)
        return value;
    }
     public static void loadFlashMessage(HttpServletRequest request) {
        String message = Flash.get(request, "message");
        String messageType = Flash.get(request, "message_type");
        request.setAttribute("message", message);
        request.setAttribute("message_type", messageType);
    }
}

package com.example.controllers.utils;

import java.net.http.HttpRequest;

import com.example.models.ClientCourant;
import com.example.remotes.ClientCourantStatefulServiceRemote;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UserSession {

    public static ClientCourantStatefulServiceRemote getSessionRemote (HttpServletRequest request) throws Exception {
        HttpSession session =  request.getSession();
        return  (ClientCourantStatefulServiceRemote) session.getAttribute("userSession");
    }
    public static void setSessionRemote (HttpServletRequest request , ClientCourantStatefulServiceRemote clientCourantStatefulServiceRemote ) throws Exception{
        HttpSession session =  request.getSession();
        session.setAttribute("userSession", clientCourantStatefulServiceRemote);
        session.setAttribute("client", clientCourantStatefulServiceRemote.getClient());

    } 
     public static void destroySession (HttpServletRequest request){
        HttpSession session =  request.getSession();
        session.removeAttribute("userSession");
        session.removeAttribute("client");

    } 

    public static ClientCourant getClient (HttpServletRequest request) throws Exception {
        return  getSessionRemote(request).getClient();
    }
}

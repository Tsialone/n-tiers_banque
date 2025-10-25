package com.example.controllers.home;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.models.ClientCourant;
import com.example.remotes.ChangeServiceRemote;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.utils.Url;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/test")
public class TestController extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/home.jsp");
        // request.setAttribute("fonctionality", "Home");
        // request.setAttribute("title", "Home bank");
        try {
            ClientCourantStatefulServiceRemote clientCourantStatefulServiceRemote = UserSession
                    .getSessionRemote(request);
            
            
            System.out.println(clientCourantStatefulServiceRemote.getClient().getNom() + " atoo elah");

        } catch (Exception e) {
            Flash.set(request, "message", "Erreur: " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            e.printStackTrace();
        }
        String landingPage = Url.layout + "/layout.jsp";
        request.getRequestDispatcher(landingPage).forward(request, response);
        // request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // @Override
    // protected void doGet(HttpServletRequest request, HttpServletResponse
    // response)
    // throws ServletException, IOException {
    // String page = Url.pages + "/home.jsp";
    // // double montant = Double.parseDouble(request.getParameter("montant"));
    // // String source = request.getParameter("source");
    // // String cible = request.getParameter("cible");

    // // // double resultat = changeService.convertir(montant, source, cible);
    // request.setAttribute("content", page);
    // request.setAttribute("fonctionality", "Home");
    // request.setAttribute("title", "Prolongement-admin");
    // // request.setAttribute("source", source);
    // // request.setAttribute("cible", cible);

    // String landingPage = Url.layout + "/layout.jsp";
    // request.getRequestDispatcher(landingPage).forward(request, response);
    // // request.getRequestDispatcher("/login.jsp").forward(request, response);
    // }
}
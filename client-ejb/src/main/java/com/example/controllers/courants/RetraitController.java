package com.example.controllers.courants;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.remotes.RetraitServiceRemote;
import com.example.remotes.ValidationRetraitServiceRemote;
import com.example.server_dtos.RetraitDto;
import com.example.utils.Url;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/courants/retraits")
public class RetraitController extends HttpServlet {

    @EJB(lookup = "java:global/server-ejb/RetraitService!com.example.remotes.RetraitServiceRemote")
    private RetraitServiceRemote retraitServiceRemote;

    @EJB(lookup = "java:global/server-ejb/ValidationRetraitService!com.example.remotes.ValidationRetraitServiceRemote")
    private ValidationRetraitServiceRemote validationRetraitServiceRemote;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/courants/retrait_liste.jsp");
        request.setAttribute("fonctionality", "Liste des retraits");
        request.setAttribute("title", "Courants - Retraits");

        try {
            int idCompte = Integer.parseInt(request.getParameter("idCompte"));
            List<RetraitDto> retraits = retraitServiceRemote.findByCompte(idCompte);
            request.setAttribute("retraits", retraits);

        } catch (Exception e) {
            Flash.set(request, "message", "Erreur: " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idCompte = Integer.parseInt(request.getParameter("idCompte"));
        String action = request.getParameter("action");

        try {
            int idUtilisateur = UserSession.getClient(request).getIdClient();

            if (request.getParameter("idRetrait") != null) {
                Integer idRetrait = Integer.parseInt(request.getParameter("idRetrait"));
                validationRetraitServiceRemote.saveValidation(idRetrait, null  , null, action , true);
            }

            Flash.set(request, "message", "Succès : retrait " + action + " !");
            Flash.set(request, "message_type", "success");
        } catch (Exception e) {
            Flash.set(request, "message", "Erreur : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/courants/retraits?idCompte=" + idCompte);
    }
}

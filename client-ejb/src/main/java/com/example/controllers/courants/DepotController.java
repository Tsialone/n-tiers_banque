package com.example.controllers.courants;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.remotes.DepotServiceRemote;
import com.example.remotes.ValidationDepotServiceRemote;
import com.example.server_dtos.DepotDto;
import com.example.utils.Url;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/courants/depots")
public class DepotController extends HttpServlet {

    @EJB(lookup = "java:global/server-ejb/DepotService!com.example.remotes.DepotServiceRemote")
    private DepotServiceRemote depotServiceRemote;

    @EJB(lookup = "java:global/server-ejb/ValidationDepotService!com.example.remotes.ValidationDepotServiceRemote")
    private ValidationDepotServiceRemote validationDepotServiceRemote;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/courants/depot_liste.jsp");
        request.setAttribute("fonctionality", "Liste des dépôts");
        request.setAttribute("title", "Courants - Dépôts");

        try {
            int idCompte = Integer.parseInt(request.getParameter("idCompte"));
            List<DepotDto> depots = depotServiceRemote.findByCompte(idCompte);
            request.setAttribute("depots", depots);

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

            if (request.getParameter("idDepot") != null) {
                Integer idDepot = Integer.parseInt(request.getParameter("idDepot"));
                validationDepotServiceRemote.saveValidation(idDepot, null ,  null, action , true);
            }

            Flash.set(request, "message", "Succès : dépôt " + action + " !");
            Flash.set(request, "message_type", "success");
        } catch (Exception e) {
            Flash.set(request, "message", "Erreur : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/courants/depots?idCompte=" + idCompte);
    }
}

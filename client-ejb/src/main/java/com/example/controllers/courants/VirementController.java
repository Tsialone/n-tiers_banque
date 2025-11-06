package com.example.controllers.courants;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.remotes.ValidationVirementServiceRemote;
import com.example.remotes.VirementServiceRemote;
import com.example.server_dtos.VirementDto;
import com.example.utils.Url;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/courants/virements")
public class VirementController extends HttpServlet {

    @EJB(lookup = "java:global/server-ejb/VirementService!com.example.remotes.VirementServiceRemote")
    private VirementServiceRemote virementServiceRemote;

    @EJB(lookup = "java:global/server-ejb/ValidationVirementService!com.example.remotes.ValidationVirementServiceRemote")
    private ValidationVirementServiceRemote validationVirementServiceRemote;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/courants/virement_liste.jsp");
        request.setAttribute("fonctionality", "Liste des virements");
        request.setAttribute("title", "Courants-Virements");

        try {
            int idCompte = Integer.parseInt(request.getParameter("idCompte"));
            List<VirementDto> virements = virementServiceRemote.getVirementsByCompteDebitAndCompteCredit(idCompte);

            request.setAttribute("virements", virements);

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
            int xx = UserSession.getClient(request).getIdClient();

            if (request.getParameter("idVirement") != null) {
                Integer idVirement = Integer.parseInt(request.getParameter("idVirement"));
                // .saveValidation(idVirement, action);
                boolean valider = validationVirementServiceRemote.saveValidation(idVirement,  null, null, xx , action, true);

            }

            Flash.set(request, "message", "Succès : virement " + action + " !");
            Flash.set(request, "message_type", "success");
        } catch (Exception e) {
            Flash.set(request, "message", "Erreur : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/courants/virements?idCompte=" + idCompte);
    }
}

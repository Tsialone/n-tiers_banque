package com.example.controllers.courants;

import com.example.change_dtos.DeviseDto;
import com.example.controllers.utils.DateTimeUtils;
import com.example.controllers.utils.Flash;
import com.example.remotes.ChangeServiceRemote;
import com.example.remotes.DepotServiceRemote;
import com.example.server_dtos.DepotDto;
import com.example.utils.Url;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/courants/depots/form")
public class DepotFormController extends HttpServlet {

    @EJB(lookup = "java:global/server-ejb/DepotService!com.example.remotes.DepotServiceRemote")
    private DepotServiceRemote depotServiceRemote;

    private ChangeServiceRemote changeServiceRemote;

    @PostConstruct
    private void initRemoteEJB() {
        try {
            Hashtable<String, Object> jndiProps = new Hashtable<>();
            jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProps.put(Context.PROVIDER_URL, "remote+http://localhost:9090");
            jndiProps.put(Context.SECURITY_PRINCIPAL, "ejbuser");
            jndiProps.put(Context.SECURITY_CREDENTIALS, "ejbpass");

            Context ctx = new InitialContext(jndiProps);
            changeServiceRemote = (ChangeServiceRemote) ctx
                    .lookup("change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote");
            System.out.println("ChangeServiceRemote initialisé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de ChangeServiceRemote:");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/courants/depot_form.jsp");
        request.setAttribute("fonctionality", "Nouveau Dépôt");
        request.setAttribute("title", "Courants - Dépôt");

        try {
            Integer idCompte = Integer.parseInt(request.getParameter("idCompte"));
            request.setAttribute("idCompte", idCompte);
            request.setAttribute("dateDepot", DateTimeUtils.formatForHtml(LocalDateTime.now()));

            // Récupérer les devises pour le select
            List<DeviseDto> devises = new ArrayList<>(changeServiceRemote.getDistinct());
            request.setAttribute("devises", devises);

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

        Integer idCompte = Integer.parseInt(request.getParameter("idCompte"));
        try {
            Double montant = Double.parseDouble(request.getParameter("montant"));
            LocalDateTime dateDepot = DateTimeUtils.parseDateTime(request.getParameter("dateDepot"));
            String devise = request.getParameter("devise");
            boolean isAriary = devise.equalsIgnoreCase("ar");
            DeviseDto deviseDto = changeServiceRemote.getByDateBtw(dateDepot, devise);
            if (deviseDto == null && !isAriary)
                throw new Exception("Devise non trouvée: " + devise + " à cette date: " + dateDepot);
            if (!isAriary && deviseDto != null)
                montant *= deviseDto.getArriary();

            DepotDto depotDto = new DepotDto();
            depotDto.setIdCompteCredit(idCompte);
            depotDto.setMontant(montant);
            depotDto.setTaux(deviseDto.getArriary());
            depotDto.setDevise(devise); // ajout de la devise-
            // depotDto.setDateDepot(dateDepot);

            depotServiceRemote.create(depotDto);

            Flash.set(request, "message", "Dépôt effectué avec succès !");
            Flash.set(request, "message_type", "success");
            response.sendRedirect(request.getContextPath() + "/courants/depots?idCompte=" + idCompte);

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors du dépôt : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/courants/depots/form?idCompte=" + idCompte);
        }
    }
}

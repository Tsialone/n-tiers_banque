package com.example.controllers.courants;

import com.example.change_dtos.DeviseDto;
import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.remotes.ChangeServiceRemote;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.remotes.TransactionCourantServiceRemote;
import com.example.server_dtos.VirementDto;
import com.example.server_dtos.CompteCourantDto;
import com.example.utils.Url;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/courants/virements/form")
public class VirementCourantFormController extends HttpServlet {

    @EJB(lookup = "java:global/server-ejb/CompteCourantService!com.example.remotes.CompteCourantServiceRemote")
    private CompteCourantServiceRemote compteService;

    @EJB(lookup = "java:global/server-ejb/TransactionCourantService!com.example.remotes.TransactionCourantServiceRemote")
    private TransactionCourantServiceRemote transactionService;

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
        request.setAttribute("content", Url.pages + "/courants/virement_courant_form.jsp");
        request.setAttribute("fonctionality", "Virement form");
        request.setAttribute("title", "Courants-Virement");

        try {
            Integer idCompte = Integer.parseInt(request.getParameter("idCompte"));
            // int clientId = UserSession.getClient(request).getIdClient();

            // Récupérer les comptes du client
            // List<CompteCourantDto> compteCourants = compteService.getComptesByClient(clientId);
            // request.setAttribute("compteCourants", compteCourants);

            // Récupérer les devises
            List<DeviseDto> devises = new ArrayList<>(changeServiceRemote.getDistinct());
            request.setAttribute("devises", devises);

            request.setAttribute("idCompte", idCompte);

        } catch (Exception e) {
            e.printStackTrace();
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

        Integer idCompteDebit = Integer.parseInt(request.getParameter("idCompteDebit"));
        try {
            // Récupérer les paramètres du formulaire
            Integer idCompteCredit = Integer.parseInt(request.getParameter("idCompteCredit"));
            LocalDate dateVirement = LocalDate.parse(request.getParameter("dateVirement"));
            Double montant = Double.parseDouble(request.getParameter("montant"));
            String devise = request.getParameter("devise");

            // Conversion via ChangeService si nécessaire
            boolean isAriary = devise.equalsIgnoreCase("ar");
            DeviseDto deviseDto = changeServiceRemote.getByDateBtw(dateVirement, devise);
            if (deviseDto == null && !isAriary)
                throw new Exception("Devise non trouvée: " + devise + " à cette date: " + dateVirement);
            if (!isAriary && deviseDto != null)
                montant *= deviseDto.getArriary();

            // Création du DTO pour le service
            VirementDto virementDto = new VirementDto();
            virementDto.setIdCompteDebit(idCompteDebit);
            virementDto.setIdCompteCredit(idCompteCredit);
            virementDto.setDateVirement(dateVirement);
            virementDto.setMontant(montant);
            virementDto.setDevise(devise);

            // Appel du service
            transactionService.doVirement(virementDto);

            Flash.set(request, "message", "Virement effectué avec succès !");
            Flash.set(request, "message_type", "success");
            response.sendRedirect(request.getContextPath() + "/courants/transactions?idCompte=" + idCompteDebit);

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors du virement : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/courants/virements/form?idCompte=" + idCompteDebit);
        }
    }
}

package com.example.controllers.courants;

import com.example.change_dtos.DeviseDto;
import com.example.controllers.utils.DateTimeUtils;
import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.remotes.ChangeServiceRemote;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.remotes.TransactionCourantServiceRemote;
import com.example.remotes.VirementServiceRemote;
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
import java.time.LocalDateTime;
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

    @EJB(lookup = "java:global/server-ejb/VirementService!com.example.remotes.VirementServiceRemote")
    private VirementServiceRemote virementServiceRemote;

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
            // List<CompteCourantDto> compteCourants =
            // compteService.getComptesByClient(clientId);
            // request.setAttribute("compteCourants", compteCourants);

            // Récupérer les devises
            List<DeviseDto> devises = new ArrayList<>(changeServiceRemote.getDistinct());
            request.setAttribute("devises", devises);
            request.setAttribute("dateVirement", DateTimeUtils.formatForHtml(LocalDateTime.now()));
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

    // @Override
    // protected void doPost(HttpServletRequest request, HttpServletResponse
    // response)
    // throws ServletException, IOException {

    // Integer idCompteDebit =
    // Integer.parseInt(request.getParameter("idCompteDebit"));
    // try {
    // // Récupérer les paramètres du formulaire
    // Integer idCompteCredit =
    // Integer.parseInt(request.getParameter("idCompteCredit"));
    // LocalDateTime dateVirement =
    // DateTimeUtils.parseDateTime(request.getParameter("dateVirement"));
    // Double montant = Double.parseDouble(request.getParameter("montant"));
    // String devise = request.getParameter("devise");

    // // Conversion via ChangeService si nécessaire
    // boolean isAriary = devise.equalsIgnoreCase("ar");
    // DeviseDto deviseDto = changeServiceRemote.getByDateBtw(dateVirement, devise);
    // if (deviseDto == null && !isAriary)
    // throw new Exception("Devise non trouvée: " + devise + " à cette date: " +
    // dateVirement);
    // if (!isAriary && deviseDto != null)
    // montant *= deviseDto.getArriary();

    // VirementDto virementDto = new VirementDto();
    // virementDto.setIdCompteDebit(idCompteDebit);
    // virementDto.setIdCompteCredit(idCompteCredit);
    // virementDto.setDateVirement(dateVirement);
    // virementDto.setMontant(montant);
    // virementDto.setDevise(devise);

    // virementDto.getFrais();

    // virementServiceRemote.effectuerVirement(virementDto ,
    // deviseDto.getArriary());

    // Flash.set(request, "message", "Virement effectué avec succès !");
    // Flash.set(request, "message_type", "success");
    // response.sendRedirect(request.getContextPath() +
    // "/courants/virements?idCompte=" + idCompteDebit);

    // } catch (Exception e) {
    // e.printStackTrace();
    // Flash.set(request, "message", "Erreur lors du virement : " + e.getMessage());
    // Flash.set(request, "message_type", "danger");
    // response.sendRedirect(request.getContextPath() +
    // "/courants/virements/form?idCompte=" + idCompteDebit);
    // }
    // }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer idCompteDebit = Integer.parseInt(request.getParameter("idCompteDebit"));
        try {
            // ✅ Récupération des paramètres
            String idCompteCreditParam = request.getParameter("idCompteCredit");
            Integer idCompteCredit = (idCompteCreditParam == null || idCompteCreditParam.isEmpty())
                    ? null
                    : Integer.parseInt(idCompteCreditParam);

            LocalDateTime dateVirement = DateTimeUtils.parseDateTime(request.getParameter("dateVirement"));
            Double montant = Double.parseDouble(request.getParameter("montant"));
            String devise = request.getParameter("devise");

            boolean isAriary = devise.equalsIgnoreCase("ar");
            DeviseDto deviseDto = changeServiceRemote.getByDateBtw(dateVirement, devise);
            if (deviseDto == null && !isAriary)
                throw new Exception("Devise non trouvée: " + devise + " à cette date: " + dateVirement);
            if (!isAriary && deviseDto != null)
                montant *= deviseDto.getArriary();

            // ✅ Création de l’objet
            VirementDto virementDto = new VirementDto();
            virementDto.setIdCompteDebit(idCompteDebit);
            virementDto.setIdCompteCredit(idCompteCredit);
            virementDto.setDateVirement(dateVirement);
            virementDto.setMontant(montant);
            virementDto.setDevise(devise);

            


            // ✅ Si aucun compte crédit → afficher frais sans exécuter le virement
            if (idCompteCredit == null) {
                double frais =  virementServiceRemote.alleas(virementDto);
                double tokonyAlefa  = frais + montant;
                double usd = montant;
                String devisestr ="ar";
                if (deviseDto!=null) {
                    devisestr  = deviseDto.getLibelle();
                    usd = tokonyAlefa / deviseDto.getArriary();
                }
                
                Flash.set(request, "message", "Les frais de ce virement sont de : " + frais + " Ar, vous devez donc envoyer " + tokonyAlefa + "Ar en " + devisestr + "= " +  usd) ;
                Flash.set(request, "message_type", "info");
                response.sendRedirect(request.getContextPath() + "/courants/virements/form?idCompte=" + idCompteDebit);
                return;
            }

            // ✅ Sinon on effectue le virement normalement
            virementServiceRemote.effectuerVirement(virementDto, deviseDto.getArriary());

            Flash.set(request, "message", "Virement effectué avec succès !");
            Flash.set(request, "message_type", "success");
            response.sendRedirect(request.getContextPath() + "/courants/virements?idCompte=" + idCompteDebit);

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors du virement : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/courants/virements/form?idCompte=" + idCompteDebit);
        }
    }

}

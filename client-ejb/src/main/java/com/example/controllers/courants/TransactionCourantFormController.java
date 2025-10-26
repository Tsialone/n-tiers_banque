package com.example.controllers.courants;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.dto.DeviseDto;
import com.example.dto.TransactionCourantDto;
import com.example.mappers.TransactionCourantMapper;
import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.models.TransactionCourant;
import com.example.remotes.ChangeServiceRemote;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.remotes.TransactionCourantServiceRemote;
import com.example.utils.Url;
import com.example.views.CompteCourantView;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/courants/transactions/form")
public class TransactionCourantFormController extends HttpServlet {

    // @EJB(lookup =
    // "java:global/server-ejb/ClientCourantService!com.example.remotes.ClientCourantServiceRemote")
    // private ClientCourantServiceRemote clientCourantServiceRemote;

    // @EJB(lookup =
    // "java:global/server-ejb/ChangeService!com.example.remotes.ChangeServiceRemote")
    // @EJB(lookup =
    // "java:global/change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote")
    // private ChangeServiceRemote changeServiceRemote;

    // @EJB(lookup =
    // "java:global/server-ejb/ClientCourantStatefulService!com.example.remotes.ClientCourantStatefulServiceRemote")
    // private ClientCourantStatefulServiceRemote
    // clientCourantStatefulServiceRemote;

    @EJB(lookup = "java:global/server-ejb/CompteCourantService!com.example.remotes.CompteCourantServiceRemote")
    private CompteCourantServiceRemote compteCourantServiceRemote;

    @EJB(lookup = "java:global/server-ejb/TransactionCourantService!com.example.remotes.TransactionCourantServiceRemote")
    private TransactionCourantServiceRemote transactionCourantServiceRemote;

    private ChangeServiceRemote changeServiceRemote;
    @PostConstruct
    private void initRemoteEJB() {
        try {
            Hashtable<String, Object> jndiProps = new Hashtable<>();
            jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProps.put(Context.PROVIDER_URL, "remote+http://localhost:9090");

            // Ajout de l'authentification
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
    // @PostConstruct
    // private void initRemoteEJB() {
    // try {
    // Hashtable<String, Object> jndiProps = new Hashtable<>();
    // jndiProps.put(Context.INITIAL_CONTEXT_FACTORY,
    // "org.wildfly.naming.client.WildFlyInitialContextFactory");
    // jndiProps.put(Context.PROVIDER_URL, "remote+http://localhost:9090");

    // // Ajout de l'authentification
    // jndiProps.put(Context.SECURITY_PRINCIPAL, "ejbuser");
    // jndiProps.put(Context.SECURITY_CREDENTIALS, "ejbpass");

    // Context ctx = new InitialContext(jndiProps);

    // changeServiceRemote = (ChangeServiceRemote) ctx
    // .lookup("change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote");

    // System.out.println("ChangeServiceRemote initialisé avec succès !");
    // } catch (Exception e) {
    // System.err.println("Erreur lors de l'initialisation de
    // ChangeServiceRemote:");
    // e.printStackTrace();
    // }
    // }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/courants/transaction_courant_form.jsp");
        request.setAttribute("fonctionality", "Mouvement form");
        request.setAttribute("title", "Courants-Mvt");

        try {
            // hovaina
            Integer idCompte = Integer.parseInt(request.getParameter("idCompte"));
            // int xx = 1;
            int xx = UserSession.getClient(request).getIdClient() ;

            List<CompteCourant> compteCourants = compteCourantServiceRemote.getComptesByClient(xx);
            List<DeviseDto> deviseDtos = changeServiceRemote.getAllDevises();
            System.out.println("devise taille" + deviseDtos.size());
            request.setAttribute("compteCourants", compteCourants);
            request.setAttribute("devises", deviseDtos);
            request.setAttribute("idCompte", idCompte);

            


        } catch (Exception e) {

            Flash.set(request, "message", "Erreur: " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/home");
            return;

        }

        String landingPage = Url.layout;
        request.getRequestDispatcher(landingPage).forward(request, response);
        // request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

                Integer idCompte = Integer.parseInt(request.getParameter("idCompte"));
        try {
            // Récupérer les paramètres du formulaire
            String dateStr = request.getParameter("dateTransaction");
            LocalDate dateTransaction = dateStr != null && !dateStr.isEmpty()
                    ? LocalDate.parse(dateStr)
                    : LocalDate.now();
            String libelle = request.getParameter("libelle");
            Double montant = Double.parseDouble(request.getParameter("montant"));
            String sens = request.getParameter("sens");
            String devise = request.getParameter("devise");

            boolean isAriary = devise.equalsIgnoreCase("ar");
            DeviseDto deviseDto =  changeServiceRemote.getByDateBtw(dateTransaction, devise);
            if (deviseDto == null && !isAriary ) throw new Exception("devise non trouvé "+ devise +" a cette date " + dateTransaction);
            if (!isAriary && deviseDto != null )  montant*=deviseDto.getArriary();
              
            

            boolean validate = request.getParameter("validate") != null;

            // Créer le DTO ou l'entité TransactionCourant
            TransactionCourantDto transaction = new TransactionCourantDto();
            transaction.setIdCompte(idCompte);
            transaction.setDateTransaction(dateTransaction);
            transaction.setLibelle(libelle);
            transaction.setMontant(montant);
            transaction.setSens(sens);
            // transaction.setDevise(devise);
            transaction.setValidate(validate);
            CompteCourant compteCourant = compteCourantServiceRemote.getCompteById(idCompte);
            TransactionCourant transactionCourant = TransactionCourantMapper.toEntity(transaction, compteCourant);

            // Appel du service pour enregistrer la transaction
            transactionCourantServiceRemote.saveTransaction(transactionCourant);

            // Message flash
            Flash.set(request, "message", "Transaction créée avec succès !");
            Flash.set(request, "message_type", "success");

            // Redirection vers la liste
            response.sendRedirect(request.getContextPath() + "/courants/transactions?idCompte=" + idCompte );

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la création de la transaction : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/courants/transactions/form?idCompte="+idCompte);
        }
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
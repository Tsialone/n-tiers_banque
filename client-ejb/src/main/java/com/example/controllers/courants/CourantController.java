package com.example.controllers.courants;

import com.example.annotations.TablePermission;
import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.remotes.ChangeServiceRemote;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.server_dtos.CompteCourantDto;
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

@WebServlet("/courants")
public class CourantController extends HttpServlet {

    @EJB(lookup = "java:global/server-ejb/ClientCourantService!com.example.remotes.ClientCourantServiceRemote")
    private ClientCourantServiceRemote clientCourantServiceRemote;

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
        request.setAttribute("content", Url.pages + "/courants/courant_liste.jsp");
        request.setAttribute("fonctionality", "Comptes courant");
        request.setAttribute("title", "Courants");

        try {
            // hovaina
            // int xx = 1;
            // UserSession.checkPermission(request, response, "compte_courants", "put");
            int xx = UserSession.getClient(request).getIdClient();

            String dateStr = request.getParameter("date");
            LocalDate date = null;
            if (dateStr != null && !dateStr.isEmpty())
                date = LocalDate.parse(dateStr);

            List<CompteCourantDto> comptes = compteCourantServiceRemote.getComptesByClientAndDate(xx, date);

            List<CompteCourantView> comptes_views = new ArrayList<>();
            for (CompteCourantDto compte_entity : comptes) {
                CompteCourantView temp_view = new CompteCourantView();
                double solde =  compte_entity.getSolde();
                temp_view.setNom(compte_entity.getNom());
                temp_view.setIdCompte(compte_entity.getIdCompte());
                temp_view.setCapital(compte_entity.getCapital());
                temp_view.setDateOuverture(compte_entity.getDateOuverture());
                temp_view.setDecouvertAutorise(compte_entity.getDecouvertAutorise());
                temp_view.setSolde(solde);
                comptes_views.add(temp_view);
            }
            request.setAttribute("comptes", comptes_views);
            // System.out.println("taille>>>> " +
            // changeServiceRemote.getAllDevises().size());
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

    // @Override
    // protected void doPost(HttpServletRequest request, HttpServletResponse
    // response)
    // throws ServletException, IOException {
    // String email = request.getParameter("email");
    // String mdp = request.getParameter("mdp");
    // System.out.println("hellooooo post <<<<<");
    // try {

    // ClientCourant clientCourant = clientCourantStatefulServiceRemote.login(email,
    // mdp);
    // // clientCourant.getDirection().getLibelle();
    // request.setAttribute("content", Url.pages + "/home.jsp");
    // request.setAttribute("fonctionality", "Home");
    // request.setAttribute("title", "Prolongement-admin");
    // // HttpSession session = request.getSession();
    // // session.setAttribute("client", clientCourant);
    // UserSession.setSessionRemote(request, clientCourantStatefulServiceRemote);

    // } catch (Exception e) {

    // Flash.set(request, "message", "Erreur: " + e.getMessage());
    // Flash.set(request, "message_type", "danger");
    // response.sendRedirect(request.getContextPath() + "/");

    // }
    // response.sendRedirect(request.getContextPath() + "/home");
    // }

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
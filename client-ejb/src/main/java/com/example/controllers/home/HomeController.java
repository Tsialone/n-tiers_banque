package com.example.controllers.home;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.remotes.ChangeServiceRemote;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.utils.Url;
import com.example.views.CompteCourantView;
import com.example.views.CompteEpargneView;
import com.example.views.ComptePretView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    private static final String EPARGNE_BASE_URL = "http://localhost:6000/api";
    private static final String PRET_BASE_URL = "http://localhost:5000/api";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @EJB(lookup = "java:global/server-ejb/CompteCourantService!com.example.remotes.CompteCourantServiceRemote")
    private CompteCourantServiceRemote compteCourantServiceRemote;

    @EJB(lookup = "java:global/server-ejb/ClientCourantService!com.example.remotes.ClientCourantServiceRemote")
    private ClientCourantServiceRemote clientCourantServiceRemote;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/home.jsp");
        request.setAttribute("fonctionality", "Home");
        request.setAttribute("title", "Home bank");
        try {

            double courantSoldeGlobal = 0.0;
            double epargneSoldeGlobal = 0.0;
            double pretSoldeGlobal = 0.0;

            // courant global
            int xx = UserSession.getClient(request).getIdClient();
            ClientCourant clientCourant = UserSession.getClient(request);
            LocalDate date = LocalDate.now();
            List<CompteCourant> comptes = compteCourantServiceRemote.getComptesByClientAndDate(xx, date);
            for (CompteCourant compte_entity : comptes) {
                double solde = clientCourantServiceRemote
                        .getSoldeByIdClientAndIdCompte(
                                compte_entity.getIdCompte(),
                                xx,
                                date);
                courantSoldeGlobal += solde;
            }

            // epargne
            String dateParam = date.toString();
            HttpResponse<String> comptesResponse = Unirest.get(EPARGNE_BASE_URL + "/CompteEpargne/byClientSolde")
                    .queryString("idClient", xx)
                    .queryString("date", dateParam)
                    .asString();

            if (comptesResponse.getStatus() == 200) {
                List<CompteEpargneView> comptesEpargne = mapper.readValue(
                        comptesResponse.getBody(),
                        new TypeReference<List<CompteEpargneView>>() {
                        });
                for (CompteEpargneView compteEpargneView : comptesEpargne) {
                    epargneSoldeGlobal += compteEpargneView.getSolde().doubleValue();
                }
            } else {
                throw new Exception("Erreur API: " + comptesResponse.getBody());
            }

            // pret
            HttpResponse<String> apiResponse = Unirest.get(PRET_BASE_URL + "/ComptePret/byClientSolde")
                    .queryString("idClient", xx)
                    .queryString("date", dateParam)
                    .asString();

            if (apiResponse.getStatus() == 200) {
                List<ComptePretView> comptesPret = mapper.readValue(
                        apiResponse.getBody(),
                        new TypeReference<List<ComptePretView>>() {
                        });
                for (ComptePretView comptePretView : comptesPret) {
                    pretSoldeGlobal += comptePretView.getSolde().doubleValue();
                }
                request.setAttribute("comptesPret", comptesPret);
            } else {
                throw new Exception("Erreur API: " + apiResponse.getBody());
            }
            pretSoldeGlobal *= -1;
            double soldeGlobal = courantSoldeGlobal + epargneSoldeGlobal + pretSoldeGlobal;
            request.setAttribute("soldeGlobal", BigDecimal.valueOf(soldeGlobal));
            request.setAttribute("courantSoldeGlobal", BigDecimal.valueOf(courantSoldeGlobal));
            request.setAttribute("epargneSoldeGlobal", BigDecimal.valueOf(epargneSoldeGlobal));
            request.setAttribute("pretSoldeGlobal", BigDecimal.valueOf(pretSoldeGlobal));
            // --- Initialisation des rôles et actions ---
            // List<ClientCourant>  clientCourants = new ArrayList<>();
            request.setAttribute("roles", clientCourant.getClientRoles());

            // ClientCourantStatefulServiceRemote clientCourantStatefulServiceRemote =
            // UserSession
            // .getSessionRemote(request);
            // System.out.println(clientCourantStatefulServiceRemote.getClient().getNom() +
            // " kdjjjjj");

        } catch (Exception e) {
            Flash.set(request, "message", "Erreur: " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/");

            // e.printStackTrace();
        }
        String landingPage = Url.layout;
        System.out.println("page home " + landingPage);
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
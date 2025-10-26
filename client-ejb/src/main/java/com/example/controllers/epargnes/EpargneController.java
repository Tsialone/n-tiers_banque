package com.example.controllers.epargnes;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.dto.TransactionEpargneDto;
import com.example.utils.Url;
import com.example.views.CompteEpargneView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@WebServlet("/epargnes")
public class EpargneController extends HttpServlet {

    // private static final String BASE_URL = "http://172.17.0.1:6000/api";
    private static final String BASE_URL = "http://localhost:6000/api";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/epargnes/epargne_liste.jsp");
        request.setAttribute("fonctionality", "Comptes Epargne");
        request.setAttribute("title", "Épargne");

        try {
            // int xx = 1; // test ou récupéré depuis session
            int xx = UserSession.getClient(request).getIdClient();

            String dateStr = request.getParameter("date");
            String dateParam = dateStr != null && !dateStr.isEmpty() ? dateStr : null;

            // Appel API pour récupérer les comptes épargne
            HttpResponse<String> comptesResponse = Unirest.get(BASE_URL + "/CompteEpargne/byClientSolde")
                    .queryString("idClient", xx)
                    .queryString("date", dateParam)
                    .asString();

            if (comptesResponse.getStatus() == 200) {
                List<CompteEpargneView> comptes = mapper.readValue(
                        comptesResponse.getBody(),
                        new TypeReference<List<CompteEpargneView>>() {
                        });
                request.setAttribute("comptesEpargne", comptes);
            } else {
                throw new Exception("Erreur API: " + comptesResponse.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la récupération des comptes : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idCompteEpargne = Integer.parseInt(request.getParameter("idCompteEpargne"));
            String libelle = request.getParameter("libelle");
            String dateStr = request.getParameter("dateTransaction");
            LocalDate dateTransaction = dateStr != null && !dateStr.isEmpty()
                    ? LocalDate.parse(dateStr)
                    : LocalDate.now();
            String sens = request.getParameter("sens");
            String montantStr = request.getParameter("montant");

            TransactionEpargneDto transaction = new TransactionEpargneDto();
            transaction.setIdCompte(idCompteEpargne);
            transaction.setLibelle(libelle);
            transaction.setDateTransaction(dateTransaction);
            transaction.setSens(sens);
            transaction.setMontant(new java.math.BigDecimal(montantStr));

            // Convertir en JSON et envoyer à l’API C#
            String json = mapper.writeValueAsString(transaction);

            HttpResponse<String> apiResponse = Unirest.post(BASE_URL + "/epargne/transaction")
                    .queryString("idCompteCourant", 1) // id compte courant parent si nécessaire
                    .header("Content-Type", "application/json")
                    .body(json)
                    .asString();

            if (apiResponse.getStatus() == 200 || apiResponse.getStatus() == 201) {
                Flash.set(request, "message", "Transaction créée avec succès !");
                Flash.set(request, "message_type", "success");
            } else {
                Flash.set(request, "message", "Erreur API: " + apiResponse.getBody());
                Flash.set(request, "message_type", "danger");
                response.sendRedirect(request.getContextPath() + "/home");

            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la création : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/home");

        }

        response.sendRedirect(request.getContextPath() + "/epargne");
    }
}

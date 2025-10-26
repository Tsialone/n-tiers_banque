package com.example.controllers.epargnes;

import com.example.controllers.utils.Flash;
import com.example.utils.Url;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/epargnes/transactions/form")
public class TransactionEpargneFormController extends HttpServlet {

    // private static final String BASE_URL = "http://172.17.0.1:6000/api";
    private static final String BASE_URL = "http://localhost:6000/api";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    // Affiche le formulaire pour créer une transaction
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/epargnes/epargne_transaction_form.jsp");
        request.setAttribute("fonctionality", "Nouvelle Transaction");
        request.setAttribute("title", "Transaction Épargne");

        // Récupération du compte cible si fourni
        String idCompte = request.getParameter("idCompte");
        request.setAttribute("idCompte", idCompte);

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }

    // Traite la soumission du formulaire
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int idCompte = Integer.parseInt(request.getParameter("idCompte"));
            String libelle = request.getParameter("libelle");
            BigDecimal montant = new BigDecimal(request.getParameter("montant"));
            String sens = request.getParameter("sens"); // "debit" ou "credit"
            LocalDate dateTransaction = LocalDate.parse(request.getParameter("dateTransaction"));

            // Préparation du JSON pour l'API
            Map<String, Object> transactionMap = new HashMap<>();
            transactionMap.put("idCompte", idCompte);
            transactionMap.put("libelle", libelle);
            transactionMap.put("montant", montant);
            transactionMap.put("sens", sens);
            transactionMap.put("dateTransaction", dateTransaction.toString());

            String jsonBody = mapper.writeValueAsString(transactionMap);

            // Appel API C#
            HttpResponse<String> apiResponse = Unirest.post(BASE_URL + "/TransactionEpargne")
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();

            if (apiResponse.getStatus() == 200 || apiResponse.getStatus() == 201) {
                Flash.set(request, "message", "Transaction créée avec succès !");
                Flash.set(request, "message_type", "success");
            } else {
                throw new Exception("Erreur API: " + apiResponse.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la création de la transaction : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/epargnes"); // Retour à la liste des comptes
    }
}

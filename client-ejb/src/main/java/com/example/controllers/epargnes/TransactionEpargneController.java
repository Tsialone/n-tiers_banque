package com.example.controllers.epargnes;

import com.example.controllers.utils.Flash;
import com.example.server_dtos.TransactionEpargneDto;
import com.example.utils.Url;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/epargnes/transactions")
public class TransactionEpargneController extends HttpServlet {

    // private static final String BASE_URL = "http://172.17.0.1:6000/api";
    private static final String BASE_URL = "http://localhost:6000/api";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        int idEpargne = Integer.parseInt(request.getParameter("idEpargne"));
        try {

            HttpResponse<String> apiResponse = Unirest.get(BASE_URL + "/TransactionEpargne/byEpargne")
                    .queryString("idEpargne", idEpargne)
                    .asString();

            if (apiResponse.getStatus() != 200) {
                throw new Exception("Erreur API C#: " + apiResponse.getBody());
            }

            // Convertir JSON en liste Java
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            List<TransactionEpargneDto> transactions = mapper.readValue(
                    apiResponse.getBody(),
                    new TypeReference<List<TransactionEpargneDto>>() {
                    });

            request.setAttribute("transactions", transactions);
            request.setAttribute("content", Url.pages + "/epargnes/epargne_transaction_liste.jsp");
            request.setAttribute("fonctionality", "Transactions Épargne");
            request.setAttribute("title", "Transactions Épargne");

        } catch (Exception e) {
            Flash.set(request, "message", "Erreur lors de la récupération des transactions : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/epargnes");

        }

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }
}

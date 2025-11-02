package com.example.controllers.epargnes;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.server_dtos.CompteEpargneDto;
import com.example.utils.Url;
import com.example.views.CompteEpargneView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/epargnes/form")
public class EpargneFormController extends HttpServlet {

    private static final String BASE_URL = "http://172.17.0.1:6000/api";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        // Configuration de Jackson pour gérer LocalDate
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pré-remplir le formulaire
        // int xx = 1; // xx = 1 pour test

        try {
            int xx = UserSession.getClient(request).getIdClient();
            request.setAttribute("idClient", xx);
            request.setAttribute("dateOuverture", LocalDate.now());

            request.setAttribute("content", Url.pages + "/epargnes/epargne_form.jsp");
            request.setAttribute("fonctionality", "Créer un compte épargne");
            request.setAttribute("title", "Nouvel épargne");
        } catch (Exception e) {
            Flash.set(request, "message", "Erreur: " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/home");
        }

        Flash.loadFlashMessage(request);

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Récupération des paramètres du formulaire
            String libelle = request.getParameter("libelle");
            String capitalStr = request.getParameter("capital");
            String idClientStr = request.getParameter("idClient");

            CompteEpargneDto dto = new CompteEpargneDto();
            dto.setLibelle(libelle);
            dto.setCapitalEpargne(capitalStr != null ? Double.parseDouble(capitalStr) : 0.0);
            dto.setIdClient(idClientStr != null ? Integer.parseInt(idClientStr) : 1);

            // Envoi au serveur C#
            String json = mapper.writeValueAsString(dto);
            HttpResponse<String> apiResponse = Unirest.post(BASE_URL + "/CompteEpargne")
                    .header("Content-Type", "application/json")
                    .body(json)
                    .asString();

            if (apiResponse.getStatus() == 200 || apiResponse.getStatus() == 201) {
                Flash.set(request, "message", "Compte épargne créé avec succès !");
                Flash.set(request, "message_type", "success");
            } else {
                Flash.set(request, "message", "Erreur API : " + apiResponse.getBody());
                Flash.set(request, "message_type", "danger");
                response.sendRedirect(request.getContextPath() + "/epargnes/form");

            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/epargnes/form");

        }

        response.sendRedirect(request.getContextPath() + "/epargnes");
    }
}

package com.example.controllers.prets;

import com.example.controllers.utils.Flash;
import com.example.dto.ComptePretDto;
import com.example.utils.Url;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import java.math.BigDecimal;
import java.time.LocalDate;

@WebServlet("/prets/form")
public class PretFormController extends HttpServlet {

    private static final String BASE_URL = "http://localhost:5000/api";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd"));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    // Affichage du formulaire
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);

        // Pré-remplissage test (idClient=1)
        request.setAttribute("idClient", 1);
        request.setAttribute("dateOuverture", java.time.LocalDate.now());

        request.setAttribute("content", Url.pages + "/prets/pret_form.jsp");
        request.setAttribute("fonctionality", "Demande de prêt");
        request.setAttribute("title", "Nouvelle demande de prêt");

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }

    // Traitement du formulaire
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            ComptePretDto dto = new ComptePretDto();
            dto.setIdClient(Integer.parseInt(request.getParameter("idClient")));
            dto.setLibelle(request.getParameter("libelle"));
            dto.setCapitalEmprunte(new BigDecimal(request.getParameter("capitalEmprunte")));
            dto.setTauxInteret(new BigDecimal(request.getParameter("tauxInteret")));
            dto.setDureeMois(Integer.parseInt(request.getParameter("dureeMois")));

            // Conversion de la date
            String dateStr = request.getParameter("dateOuverture");
            if (dateStr != null && !dateStr.isEmpty()) {
                dto.setDateOuverture(LocalDate.parse(dateStr));
            }

            // Sérialisation JSON
            String jsonBody = mapper.writeValueAsString(dto);

            // Appel API C#
            HttpResponse<String> apiResponse = Unirest.post(BASE_URL + "/ComptePret/askPret")
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();

            if (apiResponse.getStatus() == 200 || apiResponse.getStatus() == 201) {
                Flash.set(request, "message", "Demande de prêt créée avec succès !");
                Flash.set(request, "message_type", "success");
            } else {
                Flash.set(request, "message", "Erreur API : " + apiResponse.getBody());
                Flash.set(request, "message_type", "danger");
                response.sendRedirect(request.getContextPath() + "/prets/form");

            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la création du prêt : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/prets/form");

        }

        response.sendRedirect(request.getContextPath() + "/prets");
    }
}

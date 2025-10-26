package com.example.controllers.prets;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.utils.Url;
import com.example.views.ComptePretView;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;

@WebServlet("/prets")
public class PretController extends HttpServlet {

    private static final String BASE_URL = "http://localhost:5000/api"; // API Pret
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        // Pour gérer LocalDate/DateOnly
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/prets/pret_liste.jsp");
        request.setAttribute("fonctionality", "Comptes Prêt");
        request.setAttribute("title", "Prêts");

        try {
            // int xx = 1; // xx = 1 pour test ou récupération depuis session
            int xx = UserSession.getClient(request).getIdClient();
            String dateStr = request.getParameter("date");
            String dateParam = dateStr != null && !dateStr.isEmpty() ? dateStr : null;

            // Appel API pour récupérer les prêts
            HttpResponse<String> apiResponse = Unirest.get(BASE_URL + "/ComptePret/byClientSolde")
                    .queryString("idClient", xx)
                    .queryString("date", dateParam)
                    .asString();

            if (apiResponse.getStatus() == 200) {
                List<ComptePretView> comptesPret = mapper.readValue(
                        apiResponse.getBody(),
                        new TypeReference<List<ComptePretView>>() {
                        });
                request.setAttribute("comptesPret", comptesPret);
            } else {
                throw new Exception("Erreur API: " + apiResponse.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la récupération des prêts : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }
}

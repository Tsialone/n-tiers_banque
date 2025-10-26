package com.example.controllers.prets;

import com.example.controllers.utils.Flash;
import com.example.dto.AmortissementDto;
import com.example.utils.Url;
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

@WebServlet("/prets/amortissements")
public class AmortissementController extends HttpServlet {

    private static final String BASE_URL = "http://localhost:5000/api";
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

        try {
            String idCompteStr = request.getParameter("idCompte");
            if (idCompteStr == null || idCompteStr.isEmpty()) {
                throw new Exception("ID du compte manquant");
            }
            int idCompte = Integer.parseInt(idCompteStr);

            HttpResponse<String> apiResponse = Unirest.get(BASE_URL + "/Amortissement/byCompte")
                    .queryString("idCompte", idCompte)
                    .asString();

            if (apiResponse.getStatus() == 200) {
                List<AmortissementDto> amortissements = mapper.readValue(
                        apiResponse.getBody(),
                        new TypeReference<List<AmortissementDto>>() {}
                );
                request.setAttribute("amortissements", amortissements);
                request.setAttribute("idCompte", idCompte);
            } else {
                throw new Exception("Erreur API : " + apiResponse.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la récupération des amortissements : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        request.setAttribute("content", Url.pages + "/prets/amortissement_liste.jsp");
        request.setAttribute("fonctionality", "Amortissements");
        request.setAttribute("title", "Amortissements");
        request.getRequestDispatcher(Url.layout).forward(request, response);
    }
}

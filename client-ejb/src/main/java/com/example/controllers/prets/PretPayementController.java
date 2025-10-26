package com.example.controllers.prets;

import com.example.controllers.utils.Flash;
import com.example.utils.Url;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/prets/payement")
public class PretPayementController extends HttpServlet {

    private static final String BASE_URL = "http://localhost:5000/api";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

                int idComptePret = Integer.parseInt(request.getParameter("idComptePret"));
        try {

            // Appel API C# pour effectuer le remboursement
            HttpResponse<String> apiResponse = Unirest.get(BASE_URL + "/Amortissement/rembourssement")
                    .queryString("idComptePret", idComptePret)
                    .asString();

            if (apiResponse.getStatus() == 200) {
                Flash.set(request, "message", "Remboursement effectué avec succès !");
                Flash.set(request, "message_type", "success");
            } else {
                Flash.set(request, "message", "Erreur API : " + apiResponse.getBody());
                Flash.set(request, "message_type", "danger");
                response.sendRedirect(request.getContextPath() + "/prets");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors du remboursement : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/prets");

        }

        // Redirection vers la liste des prêts
        response.sendRedirect(request.getContextPath() + "/prets/amortissements?idCompte="+idComptePret);
    }
}

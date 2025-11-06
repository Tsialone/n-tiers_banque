package com.example.controllers.courants;

import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.remotes.TypeCompteServiceRemote;
import com.example.server_dtos.CompteCourantDto;
import com.example.server_dtos.TypeCompteDto;
import com.example.utils.Url;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/courants/form")
public class CourantFormController extends HttpServlet {

    @EJB(lookup = "java:global/server-ejb/CompteCourantService!com.example.remotes.CompteCourantServiceRemote")
    private CompteCourantServiceRemote compteCourantServiceRemote;

    @EJB(lookup = "java:global/server-ejb/ClientCourantService!com.example.remotes.ClientCourantServiceRemote")
    private ClientCourantServiceRemote clientCourantServiceRemote;

     @EJB(lookup = "java:global/server-ejb/TypeCompteService!com.example.remotes.TypeCompteServiceRemote")
    private TypeCompteServiceRemote typeCompteServiceRemote;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
        request.setAttribute("content", Url.pages + "/courants/courant_form.jsp");
        request.setAttribute("fonctionality", "Créer un compte courant");
        request.setAttribute("title", "Courants - Création");

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // int xx =1;
            int xx = UserSession.getClient(request).getIdClient();

            String nom = request.getParameter("nom");
            Double capital = Double.parseDouble(request.getParameter("capital"));
            Double decouvert = Double.parseDouble(request.getParameter("decouvertAutorise"));
            LocalDate dateOuverture = LocalDate.now();

            TypeCompteDto typeCompteDto = typeCompteServiceRemote.findById(1);
            CompteCourantDto dto = new CompteCourantDto();
            dto.setNom(nom);
            dto.setCapital(capital);
            dto.setDecouvertAutorise(decouvert);
            dto.setDateOuverture(dateOuverture);
            dto.setTypeCompte(typeCompteDto);
            dto.setIdClient(xx); // client fixe xx = 1

            // CompteCourant compte = CompteCourantMapper.toEntity(dto, clientCourantServiceRemote.getClientById(xx));

            compteCourantServiceRemote.saveCompte(dto);

            Flash.set(request, "message", "Compte courant créé avec succès !");
            Flash.set(request, "message_type", "success");

            response.sendRedirect(request.getContextPath() + "/courants");
        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la création du compte : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/courants/form");
        }
    }
}

package com.example.controllers.devises;

import com.example.change_dtos.DeviseDto;
import com.example.controllers.utils.Flash;
import com.example.remotes.ChangeServiceRemote;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/devises/valider")
public class DeviseValidationController extends HttpServlet {

    private ChangeServiceRemote changeServiceRemote;

    @PostConstruct
    private void initRemoteEJB() {
        try {
            Hashtable<String, Object> jndiProps = new Hashtable<>();
            jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProps.put(Context.PROVIDER_URL, "remote+http://localhost:9090");
            jndiProps.put(Context.SECURITY_PRINCIPAL, "ejbuser");
            jndiProps.put(Context.SECURITY_CREDENTIALS, "ejbpass");

            Context ctx = new InitialContext(jndiProps);
            changeServiceRemote = (ChangeServiceRemote) ctx.lookup(
                    "change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote");

            System.out.println("ChangeServiceRemote initialisé pour validation !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de ChangeServiceRemote:");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            Flash.set(request, "message", "ID de la devise manquant.");
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/devises");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            DeviseDto devise = changeServiceRemote.getById(id);

            if (devise == null) {
                Flash.set(request, "message", "Devise introuvable !");
                Flash.set(request, "message_type", "danger");
            } else {
                changeServiceRemote.validerDevise(id);
                Flash.set(request, "message", "Devise validée avec succès !");
                Flash.set(request, "message_type", "success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Flash.set(request, "message", "Erreur lors de la validation : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/devises");
    }
}

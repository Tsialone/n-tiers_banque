package com.example.controllers.devises;

import com.example.change_dtos.DeviseDto;
import com.example.controllers.utils.Flash;
import com.example.remotes.ChangeServiceRemote;
import com.example.utils.Url;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/devises")
public class DeviseController extends HttpServlet {

    private ChangeServiceRemote changeServiceRemote;

    @PostConstruct
    private void initRemoteEJB() {
        try {
            Hashtable<String, Object> jndiProps = new Hashtable<>();
            jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProps.put(Context.PROVIDER_URL, "remote+http://localhost:9090");

            // Ajout de l'authentification
            jndiProps.put(Context.SECURITY_PRINCIPAL, "ejbuser");
            jndiProps.put(Context.SECURITY_CREDENTIALS, "ejbpass");

            Context ctx = new InitialContext(jndiProps);

            changeServiceRemote = (ChangeServiceRemote) ctx
                    .lookup("change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote");

            System.out.println("ChangeServiceRemote initialisé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de ChangeServiceRemote:");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Flash.loadFlashMessage(request);
          request.setAttribute("fonctionality", "Liste devises");
        request.setAttribute("title", "Devise - Liste");

        try {
            List<DeviseDto> devises = changeServiceRemote.getAllDevises();
            request.setAttribute("devises", devises);
        } catch (Exception e) {
            Flash.set(request, "message", "Erreur lors du chargement des devises : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
        }

        request.setAttribute("content", Url.pages + "/devises/devise_liste.jsp");
        request.setAttribute("title", "Liste des devises");

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }
}

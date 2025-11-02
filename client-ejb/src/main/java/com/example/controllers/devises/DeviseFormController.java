package com.example.controllers.devises;

import com.example.change_dtos.DeviseDto;
import com.example.controllers.utils.Flash;
import com.example.remotes.ChangeServiceRemote;
import com.example.remotes.TransactionCourantServiceRemote;
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

import javax.naming.Context;
import javax.naming.InitialContext;

@WebServlet("/devises/form")
public class DeviseFormController extends HttpServlet {

    @EJB(lookup = "java:global/server-ejb/TransactionCourantService!com.example.remotes.TransactionCourantServiceRemote")
    private TransactionCourantServiceRemote transactionCourantServiceRemote;

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
        request.setAttribute("fonctionality", "Créer d'une devise");
        request.setAttribute("title", "Devise - Création");

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Long id = Long.parseLong(idParam);
                DeviseDto devise = changeServiceRemote.getById(id);
                request.setAttribute("devise", devise);
            } catch (Exception e) {
                Flash.set(request, "message", "Devise non trouvée : " + e.getMessage());
                Flash.set(request, "message_type", "danger");
            }
        }

        request.setAttribute("content", Url.pages + "/devises/devise_form.jsp");
        request.setAttribute("title", "Formulaire Devise");

        request.getRequestDispatcher(Url.layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String libelle = request.getParameter("libelle");
            String dateDebut = request.getParameter("dateDebut");
            String dateFin = request.getParameter("dateFin");
            double arriary = Double.parseDouble(request.getParameter("arriary"));

            Integer newId = changeServiceRemote.getAllDevises().size() + 1;
            DeviseDto dto = new DeviseDto();
            dto.setId(newId.longValue());
            dto.setLibelle(libelle);
            dto.setDateDebut(dateDebut);
            dto.setDateFin(dateFin);
            dto.setArriary(arriary);

            String idParam = request.getParameter("id");

            if (idParam != null && !idParam.isEmpty()) {
                Long id = Long.parseLong(idParam);
                dto.setId(id);
                DeviseDto updated = changeServiceRemote.updateDevise(id, dto);
                transactionCourantServiceRemote.updateAllOperations(dto);
                if (updated == null) {
                    throw new Exception("Impossible de mettre à jour la devise avec id " + id);
                }
            } else {
                changeServiceRemote.addDevise(dto);
            }

            Flash.set(request, "message", "Devise enregistrée avec succès !");
            Flash.set(request, "message_type", "success");
            response.sendRedirect(request.getContextPath() + "/devises");

        } catch (Exception e) {
            Flash.set(request, "message", "Erreur lors de l'enregistrement : " + e.getMessage());
            Flash.set(request, "message_type", "danger");
            response.sendRedirect(request.getContextPath() + "/devises/form");
        }
    }

}

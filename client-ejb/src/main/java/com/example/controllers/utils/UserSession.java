package com.example.controllers.utils;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.wildfly.security.http.HttpServerResponse;

import com.example.models.ActionRole;
import com.example.models.ClientCourant;
import com.example.models.ClientRole;
import com.example.models.Role;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.utils.Url;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.container.ContainerRequestContext;

public class UserSession {

    public static ClientCourantStatefulServiceRemote getSessionRemote(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        return (ClientCourantStatefulServiceRemote) session.getAttribute("userSession");
    }

    public static void setSessionRemote(HttpServletRequest request,
            ClientCourantStatefulServiceRemote clientCourantStatefulServiceRemote) throws Exception {
        HttpSession session = request.getSession();
        session.setAttribute("userSession", clientCourantStatefulServiceRemote);
        session.setAttribute("client", clientCourantStatefulServiceRemote.getClient());

    }

    public static void destroySession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        // getSessionRemote(request).logout();
        session.removeAttribute("userSession");
        session.removeAttribute("client");
        session.invalidate();

    }

    public static ClientCourant getClient(HttpServletRequest request) throws Exception {
        return getSessionRemote(request).getClient();
    }

    public static boolean checkPermission(HttpServletRequest request, HttpServletResponse response, String table,
            String httpMethod)
            throws Exception {
        ClientCourant clientCourant = getClient(request);

        System.out.println("L'utilisateur: " + clientCourant.getNom());
        System.out.println("Ses roles: ");
        for (ClientRole clientRole : clientCourant.getClientRoles()) {
            Role role = clientRole.getRole();
            System.out.println("role: " + role);
            System.out.println("------ses actions: ");
            for (ActionRole actionRole : role.getActionRoles()) {
                if (actionRole.getNomTable().equalsIgnoreCase(table) &&
                        actionRole.getAction().getLibelle().equalsIgnoreCase(httpMethod)) {
                    System.out.println(actionRole);
                    return true;
                }
            }
        }
        // Ici, tu peux utiliser userSession ou un service externe pour vérifier les
        // droits
        // Exemple :
        // User user = (User) requestContext.getProperty("user");
        // return permissionService.hasPermission(user, table, httpMethod);
        String message = "Vous n'avez pas la permission pour accéder à cette table: " + table
                + "<br>action: " + httpMethod;
        Flash.set(request, "message", "Erreur: " + message);
        Flash.set(request, "message_type", "danger");
        response.sendRedirect(request.getContextPath() + "/home");
        return false;
    }
}

package com.example.controllers.utils;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.wildfly.security.http.HttpServerResponse;

import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.server_dtos.ActionRoleDto;
import com.example.server_dtos.ClientCourantDto;
import com.example.server_dtos.DirectionDto;
import com.example.utils.Url;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.Action;

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
        session.setAttribute("direction", clientCourantStatefulServiceRemote.getDirection());
        // session.setAttribute("actionRoles",
        // clientCourantStatefulServiceRemote.getActionRoles());

    }

    public static DirectionDto getDirection(HttpServletRequest request) throws Exception {
        return getSessionRemote(request).getDirection();
    }

    public static List<ActionRoleDto> getActionRoleDto(HttpServletRequest request) throws Exception {
        return getSessionRemote(request).getActionRoles();
    }

    public static void destroySession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        getSessionRemote(request).logout();
        session.removeAttribute("userSession");
        session.removeAttribute("client");
        session.removeAttribute("direction");

        session.invalidate();

    }

    public static ClientCourantDto getClient(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        return  (ClientCourantDto) session.getAttribute("client");
    }

    public static boolean checkPermission(HttpServletRequest request,
            HttpServletResponse response, String table,
            String httpMethod)
            throws Exception {
        ClientCourantDto clientCourant = getClient(request);
        List<ActionRoleDto> actionRoleDtos = getActionRoleDto(request);

        System.out.println("L'utilisateur: " + clientCourant.getNom());
        System.out.println("Ses roles: ");
        for (ActionRoleDto actionRole : actionRoleDtos) {
            // Role role = clientRole.getRole();
            System.out.println("role: " + actionRole.getLibelleRole() + " action: " + actionRole.getLibelleAction());
            // System.out.println("------ses actions: ");
            if (actionRole.getNomTable().equalsIgnoreCase(table) &&
                    actionRole.getLibelleAction().equalsIgnoreCase(httpMethod)) {
                System.out.println(actionRole);
                return true;
            }

            // for (ActionRole actionRole : role.getActionRoles()) {

            // }
        }
        // Ici, tu peux utiliser userSession ou un service externe pour vérifier les
        // droits
        // Exemple :
        // User user = (User) requestContext.getProperty("user");
        // return permissionService.hasPermission(user, table, httpMethod);
        String message = "Vous n'avez pas la permission pour accéder à cette table: "
                + table
                + "<br>action: " + httpMethod;
        Flash.set(request, "message", "Erreur: " + message);
        Flash.set(request, "message_type", "danger");
        response.sendRedirect(request.getContextPath() + "/home");
        return false;
    }
}

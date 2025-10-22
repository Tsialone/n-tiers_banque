package com.example.filters;

import com.example.annotations.TablePermission;
import com.example.models.ActionRole;
import com.example.models.ClientCourant;
import com.example.models.ClientRole;
import com.example.models.Role;
import com.example.service.UserSession;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Provider
public class PermissionFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo; // Permet d'accéder au contrôleur et à la méthode

    @Inject
    UserSession userSession;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        // if (userSession.getClient() == null || userSession == null) {
        // requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
        // .entity(Map.of("error", "Vous devez vous reconnceter stp")).build());
        // }

        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            System.out.println("d,j;ytreghj,,gfdsfghj,bdgbh,jngf");
            return;
        }
        System.out.println("the methode " + method);
        if (method != null) {
            TablePermission tablePermission = method.getAnnotation(TablePermission.class);
            System.out.println("methode  " + method + " but " + tablePermission);
            if (tablePermission != null) {
                String table = tablePermission.table();
                String httpMethod = requestContext.getMethod(); // GET, POST, etc.

                System.out.println("Vérification permission pour table : " + table + ", méthode HTTP : " + httpMethod);

                // Exemple pseudo-code de vérification
                try {
                    boolean hasPermission = checkPermission(requestContext, table, httpMethod);
                    if (!hasPermission) {
                        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                                .entity(Map.of("error",
                                        "Vous n'avez pas la permission pour accéder à cette ressource: " + "table: "
                                                + table + "\naction: " + httpMethod))
                                .build());
                    }
                } catch (Exception e) {
                     requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                                .entity(Map.of("error",
                                        "session expiré " + e.getMessage()))
                                .build());
                }

            }
        } else {
            System.out.println("nulllllllllllllllllll");
        }
    }

    private boolean checkPermission(ContainerRequestContext requestContext, String table, String httpMethod) {
        ClientCourant clientCourant = userSession.getClient();

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
        return false; // temporaire
    }
}

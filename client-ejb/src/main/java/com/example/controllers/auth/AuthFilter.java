package com.example.controllers.auth;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;

import com.example.annotations.TablePermission;
import com.example.controllers.utils.Flash;
import com.example.controllers.utils.UserSession;
import com.example.server_dtos.ClientCourantDto;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si besoin
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        // chain.doFilter(request, response);
        // return;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/assets/") || path.startsWith("/css/") || path.startsWith("/js/")) {
            chain.doFilter(request, response);
            return;
        }

        if (path.equals("") || path.equals("/")) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (path.equals("/login") || path.equals("/register")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            ClientCourantDto clientCourant = (ClientCourantDto) session.getAttribute("client");
            if (clientCourant == null) {
                Flash.set(req, "message", "Erreur: " + "session expiréé, veuillez vous reconnecter");
                Flash.set(req, "message_type", "danger");
                res.sendRedirect(req.getContextPath() + "/");
                return;
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            Flash.set(req, "message", "Erreur: " + e.getMessage());
            Flash.set(req, "message_type", "danger");
            res.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // if (session != null && session.getAttribute("user") != null) {
        // chain.doFilter(request, response); // Utilisateur connecté
        // } else {
        // res.sendRedirect(req.getContextPath() + "/login"); // Non connecté → login
        // }
    }

    @Override
    public void destroy() {
        // Rien à nettoyer
    }
}

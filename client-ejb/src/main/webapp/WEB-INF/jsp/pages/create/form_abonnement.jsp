<%@ page import="s4.biblio.models.Utilisateur" %>
<%@ page import="s4.biblio.models.Categorie" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur"); 
String message = (String) request.getAttribute("message");
String message_type = (String) request.getAttribute("message_type");
String alertClass = "alert-" + (message_type != null ? message_type : "info");

%>

<div class="container mt-4">
    <h4 class="mb-4">Formulaire d'abonnement</h4>
    <% if (message != null) { %>
        <div class="alert <%= alertClass %> alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fermer"></button>
        </div>
    <% } %>
    <form method="post" action="/abonnement/save">
    <input type="hidden" name="adherant" value="<%= utilisateur.getId() %>" >
        <div class="mb-3">
            <label for="dateDebut" class="form-label">Date de début</label>
            <input type="date" class="form-control" id="dateDebut" name="dateDebut" required>
        </div>

        <div class="mb-3">
            <label for="dateFin" class="form-label">Date de fin</label>
            <input type="date" class="form-control" id="dateFin" name="dateFin" required>
        </div>

        <button type="submit" class="btn btn-primary">S'abonner</button>
    </form>
</div>

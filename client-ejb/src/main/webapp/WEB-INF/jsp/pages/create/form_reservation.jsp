<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="s4.biblio.models.Exemplaire" %>
<%@ page import="s4.biblio.models.Categorie" %>

<%@ page import="java.time.LocalDate" %>

<%
    List<Exemplaire> exemplaires = (List<Exemplaire>) request.getAttribute("exemplaires");
    List<Categorie> categoriesPret = (List<Categorie>) request.getAttribute("categoriesPret");
    
    LocalDate today = LocalDate.now();
    LocalDate defaultEnd = today.plusDays(7);

    String message = (String) request.getAttribute("message");
    String message_type = (String) request.getAttribute("message_type");
    String alertClass = "alert-" + (message_type != null ? message_type : "info");
%>

<div class="container mt-4">
    <h4 class="mb-3">Faire une réservation</h4>
    <% if (message != null) { %>
        <div class="alert <%= alertClass %> alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fermer"></button>
        </div>
    <% } %>


    <form action="/reservation/save" method="post">

    
        <div class="mb-3">
            <label for="exemplaireId" class="form-label">Choisir un exemplaire</label>
            <select name="exemplaire" id="exemplaireId" class="form-select" required>
                <option value="">-- Sélectionner un exemplaire --</option>
                <% for (Exemplaire ex : exemplaires) { %>
                    <option value="<%= ex.getId() %>">
                        <%= ex.getLivre().getTitre() %> (ID: <%= ex.getId() %>)
                    </option>
                <% } %>
            </select>
        </div>


        <div class="mb-3">
            <label for="categoriePretId" class="form-label">Choisir le Type de pret</label>
            <select name="categoriePret" id="exemplaireId" class="form-select" required>
                <option value="">-- Sélectionner un type de pret --</option>
                <% for (Categorie  categ_pret : categoriesPret) { %>
                    <option value="<%= categ_pret.getId() %>">
                        <%= categ_pret.getLibelle() %>
                    </option>
                <% } %>
            </select>
        </div>

        <div class="mb-3">
            <label for="dateDebut" class="form-label">Date de début</label>
            <input type="date" id="dateDebut" name="dateDebut" class="form-control" value="<%= today %>" required />
        </div>

        <button type="submit" class="btn btn-primary">Réserver</button>
    </form>
</div>

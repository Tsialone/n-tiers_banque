<%@ page import="s4.biblio.models.Exemplaire" %>
<%@ page import="s4.biblio.models.Utilisateur" %>
<%@ page import="s4.biblio.models.Categorie" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Exemplaire> exemplaires = (List<Exemplaire>) request.getAttribute("exemplaires");
    List<Utilisateur> utilisateurs = (List<Utilisateur>) request.getAttribute("utilisateurs");
    List<Categorie> categories = (List<Categorie>) request.getAttribute("categories");

    Utilisateur utilisateurCourant = (Utilisateur) session.getAttribute("utilisateur");
    String message = (String) request.getAttribute("message");
    String message_type = (String) request.getAttribute("message_type");
    String alertClass = "alert-" + (message_type != null ? message_type : "info");


%>

<div class="container mt-4">
    <h4 class="mb-4">Formulaire de prêt</h4>

    <% if (message != null) { %>
        <div class="alert <%= alertClass %> alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fermer"></button>
        </div>
    <% } %>

    <form method="post" action="/pret/save">

        <!-- Adhérant utilisateur en datalist -->
        <div class="mb-3">
            <label for="adherantInput" class="form-label">Referance Adhérant (Utilisateur)</label>
            <input list="utilisateurOptions" class="form-control" id="adherantInput" name="adherant" placeholder="Choisir un utilisateur..." required>
            <datalist id="utilisateurOptions">
                <% for (Utilisateur u : utilisateurs) { %>
                    <option value="<%= u.getId() %>"><%= u.getNom() %></option>
                <% } %>
            </datalist>
        </div>

        <!-- Exemplaire en datalist -->
        <div class="mb-3">
            <label for="exemplaireInput" class="form-label">Exemplaire</label>
            <input list="exemplaireOptions" class="form-control" id="exemplaireInput" name="exemplaire" placeholder="Choisir un exemplaire..." required>
            <datalist id="exemplaireOptions">
                <% for (Exemplaire e : exemplaires) { %>
                    <option value="<%= e.getId() %>"><%= e.getLivre().getTitre() %></option>
                <% } %>
            </datalist>
        </div>

        <!-- Catégorie en select -->
        <div class="mb-3">
            <label for="categorie" class="form-label">Catégorie</label>
            <select class="form-control" id="categorie" name="categorie" required>
                <option value="">-- Sélectionner une catégorie --</option>
                <% for (Categorie c : categories) { %>
                    <option value="<%= c.getId() %>"><%= c.getLibelle() %></option>
                <% } %>
            </select>
        </div>

        <!-- Date de début -->
        <div class="mb-3">
            <label for="dateDebut" class="form-label">Date de début</label>
            <input type="date" class="form-control" id="dateDebut" name="dateDebut" required>
        </div>

        <button type="submit" class="btn btn-primary">Faire le pret</button>
    </form>
</div>

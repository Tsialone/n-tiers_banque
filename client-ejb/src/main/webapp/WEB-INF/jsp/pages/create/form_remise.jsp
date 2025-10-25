<%@ page import="s4.biblio.models.Exemplaire" %>
<%@ page import="s4.biblio.models.Utilisateur" %>
<%@ page import="s4.biblio.models.Pret" %>
<%@ page import="s4.biblio.models.Categorie" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Pret> prets = (List<Pret>) request.getAttribute("prets");
    String message = (String) request.getAttribute("message");
    String message_type = (String) request.getAttribute("message_type");
    String alertClass = "alert-" + (message_type != null ? message_type : "info");
%>

<div class="container mt-4">
    <h4 class="mb-4">Formulaire de remise de pret</h4>

    <% if (message != null) { %>
        <div class="alert <%= alertClass %> alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fermer"></button>
        </div>
    <% } %>

    <form method="post" action="/pret/remise/save">
        <div class="mb-3">
            <label for="adherantInput" class="form-label">Referance Pret </label>
            <input list="utilisateurOptions" class="form-control" id="adherantInput" name="pret" placeholder="Choisir un le pret..." required>
            <datalist id="utilisateurOptions">
                <% for (Pret p : prets) { %>
                    <option value="<%= p.getId() %>"><%= p.getExemplaire().getLivre().getTitre() %></option>
                <% } %>
            </datalist>
        </div>
        <!-- Date de début -->
        <div class="mb-3">
            <label for="dateDebut" class="form-label">Date de remise ne pas remplir pour ne pas faire une remise</label>
            <input type="date" class="form-control" id="dateDebut" name="dateRemise" >
        </div>
        <button type="submit" class="btn btn-primary">Faire la remise</button>
    </form>
</div>

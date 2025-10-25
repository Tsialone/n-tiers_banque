<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="s4.biblio.models.Pret" %>
<%@ page import="s4.biblio.models.Exemplaire" %>
<%@ page import="s4.biblio.models.Utilisateur" %>
<%@ page import="s4.biblio.models.Prolongement" %>
<%@ page import="s4.biblio.models.Statut" %>
<%@ page import="s4.biblio.models.Categorie" %>

<%
    List<Pret> prets = (List<Pret>) request.getAttribute("prets");
    String message = (String) request.getAttribute("message");
    String message_type = (String) request.getAttribute("message_type");
    String alertClass = "alert-" + (message_type != null ? message_type : "info");

%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des prêts</h4>
    <% if (message != null) { %>
        <div class="alert <%= alertClass %> alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Fermer"></button>
        </div>
    <% } %>
    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>#Exemplaire</th>
                <th>#Pret</th>
                <th>Libelle d'exemplaire</th>
                <th>Catégorie</th>
                <th>Date début</th>
                <th>Date fin prévue</th>
                <th>Fonctionnalite</th>
                <th>Statut Prolengement</th>
            </tr>
        </thead>
        <tbody>
            <% if (prets != null && !prets.isEmpty()) {
                int i = 1;
                for (Pret pret : prets) {
            %>
            <tr>
                <td><%= pret.getExemplaire().getId() %></td>
                <td><%= pret.getId() %></td>
                <td><%= pret.getExemplaire().getLivre().getTitre() %></td>
                <td><%= pret.getCategorie().getLibelle() %></td>
                <td><%= pret.getDateDebut() %></td>
                <td><%= pret.getDateFin() %></td>
                <td>
                    <a href="/prolongement/demande?id=<%= pret.getId() %>" class="btn btn-sm btn-outline-primary">Prolonger</a>
                </td>
                <td>
                    <%= pret.getLatestProlongement() != null ? pret.getLatestProlongement().getStatut().getLibelle()  : "Aucune transaction" %>
                </td>
            </tr>
            <%   }
               } else { %>
            <tr>
                <td colspan="8" class="text-center">Aucun prêt trouvé.</td>
            </tr>
            <% } %>
        </tbody>
    </table>
</div>

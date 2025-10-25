<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="s4.biblio.models.Reservation" %>
<%@ page import="s4.biblio.models.Exemplaire" %>
<%@ page import="s4.biblio.models.Utilisateur" %>
<%@ page import="s4.biblio.models.Categorie" %>

<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    String message = (String) request.getAttribute("message");
    String message_type = (String) request.getAttribute("message_type");
    String alertClass = "alert-" + (message_type != null ? message_type : "info");

%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des reservations</h4>
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
                <th>Libelle d'exemplaire</th>
                <th>Type de pret</th>
                <th>Date début</th>
                <th>Date fin prévue</th>
                <th>Statut</th>
                <th>Fonctionnalite</th>



            </tr>
        </thead>
        <tbody>
            <% if (reservations != null && !reservations.isEmpty()) {
                int i = 1;
                for (Reservation reservation : reservations) {
            %>
            <tr>
                <td><%= reservation.getExemplaire().getId()%></td>
                <td><%= reservation.getExemplaire().getLivre().getTitre() %></td>
                <td><%= reservation.getCategoriePret().getLibelle() %></td>
                <td><%= reservation.getDateDebut() %></td>
                <td><%= reservation.getDateFin() %></td>
                <td><%= reservation.getStatut().getLibelle() %></td>
                <td>
                    <a href="/reservation/valider?id=<%= reservation.getId() %>" class="btn btn-sm btn-outline-primary">Valider</a>
                    <a href="/reservation/refuser?id=<%= reservation.getId() %>" class="btn btn-sm btn-outline-danger">Annulée</a>
                </td>

            </tr>
            <%   }
               } else { %>
            <tr>
                <td colspan="8" class="text-center">Aucune reservation trouvée.</td>
            </tr>
            <% } %>
        </tbody>
    </table>
</div>

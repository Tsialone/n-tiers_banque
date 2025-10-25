<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="s4.biblio.models.Reservation" %>
<%@ page import="s4.biblio.models.Exemplaire" %>
<%@ page import="s4.biblio.models.Utilisateur" %>
<%@ page import="s4.biblio.models.Categorie" %>

<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des reservations</h4>

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>#Exemplaire</th>
                <th>Libelle d'exemplaire</th>
                <th>Type de pret</th>
                <th>Date début</th>
                <th>Date fin prévue</th>
                <th>Statut</th>

            </tr>
        </thead>
        <tbody>
            <% if (reservations != null && !reservations.isEmpty()) {
                int i = 1;
                for (Reservation reservation : reservations) {
            %>
            <tr>
                <td><%= reservation.getId() %></td>
                <td><%= reservation.getExemplaire().getLivre().getTitre() %></td>
                <td><%= reservation.getCategoriePret().getLibelle() %></td>
                <td><%= reservation.getDateDebut() %></td>
                <td><%= reservation.getDateFin() %></td>
                <td><%= reservation.getStatut().getLibelle() %></td>
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

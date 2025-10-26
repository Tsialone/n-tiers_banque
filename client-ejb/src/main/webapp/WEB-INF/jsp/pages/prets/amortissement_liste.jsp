<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.dto.AmortissementDto" %>

<%
    List<AmortissementDto> amortissements = (List<AmortissementDto>) request.getAttribute("amortissements");
    String idCompte = String.valueOf(request.getAttribute("idCompte"));
%>

<div class="container mt-4">
    <h4 class="mb-3">Amortissements du compte n° <%= idCompte %></h4>

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>Mois</th>
                <th>Mensualité</th>
                <th>Intérêt</th>
                <th>Capital</th>
                <th>Reste dû</th>
                <th>Date</th>
                <th>Statut</th>
            </tr>
        </thead>
        <tbody>
            <% if (amortissements != null && !amortissements.isEmpty()) {
                   for (AmortissementDto a : amortissements) { %>
                <tr>
                    <td><%= a.getMois() %></td>
                    <td><%= a.getMensualite() %></td>
                    <td><%= a.getInteret() %></td>
                    <td><%= a.getCapital() %></td>
                    <td><%= a.getResteDu() %></td>
                    <td><%= a.getCreatedAt() %></td>
                    <td><%= a.getStatut() %></td>
                </tr>
            <%   }
               } else { %>
                <tr>
                    <td colspan="7" class="text-center">Aucun amortissement trouvé.</td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

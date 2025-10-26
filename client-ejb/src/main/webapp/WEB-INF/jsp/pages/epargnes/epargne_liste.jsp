<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.views.CompteEpargneView" %>

<%
    List<CompteEpargneView> comptesEpargne = (List<CompteEpargneView>) request.getAttribute("comptesEpargne");
    String dateParam = request.getParameter("date");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des Comptes Épargne</h4>

    <!-- Formulaire de filtrage par date -->
    <form method="get" class="mb-3">
        <label for="date" class="form-label">Filtrer par date :</label>
        <input type="date" id="date" name="date" class="form-control" value="<%= dateParam != null ? dateParam : "" %>">
        <button type="submit" class="btn btn-primary mt-2">Filtrer</button>
    </form>

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Libellé</th>
                <th>Capital Initial</th>
                <th>Solde</th>
                <th>Taux d'intérêt (%)</th>
                <th>Retrait Autorisé (%)</th>
                <th>Date d'ouverture</th>
                 <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% if (comptesEpargne != null && !comptesEpargne.isEmpty()) {
                   for (CompteEpargneView c : comptesEpargne) { %>
                <tr>
                    <td><%= c.getIdCompte() %></td>
                    <td><%= c.getLibelle() %></td>
                    <td><%= c.getCapitalEpargne() %></td>
                    <td><%= c.getSolde() %></td>
                    <td><%= c.getTauxInteret() %></td>
                    <td><%= c.getRetrait() %></td>
                    <td><%= c.getDateOuverture() %></td>
                    <td>
                        <a href="<%= request.getContextPath() %>/epargnes/transactions/form?idCompte=<%= c.getIdCompte() %>"
                           class="btn btn-primary btn-sm">add mvt</a>
                            <a href="<%= request.getContextPath() %>/epargnes/transactions?idEpargne=<%= c.getIdCompte() %>"
                           class="btn btn-primary btn-sm">voir mvt</a>
                    </td>
                </tr>
            <%   }
               } else { %>
                <tr>
                    <td colspan="7" class="text-center">Aucun compte épargne trouvé.</td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

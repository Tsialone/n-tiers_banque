<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.views.ComptePretView" %>

<%
    List<ComptePretView> comptesPret = (List<ComptePretView>) request.getAttribute("comptesPret");
    String dateParam = request.getParameter("date");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des Comptes Prêt</h4>

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
                <th>Capital Emprunté</th>
                <th>Solde</th>
                <th>Taux d'intérêt (%)</th>
                <th>Durée (mois)</th>
                <th>Date ouverture</th>
                <th>Date échéance</th>
                <th>Statut</th>
                 <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% if (comptesPret != null && !comptesPret.isEmpty()) {
                   for (ComptePretView c : comptesPret) { %>
                <tr>
                    <td><%= c.getIdCompte() %></td>
                    <td><%= c.getLibelle() %></td>
                    <td><%= c.getCapitalEmprunte() %></td>
                    <td><%= c.getSolde() %></td>
                    <td><%= c.getTauxInteret() %></td>
                    <td><%= c.getDureeMois() %></td>
                    <td><%= c.getDateOuverture() %></td>
                    <td><%= c.getDateEcheance() != null ? c.getDateEcheance() : "-" %></td>
                    <td><%= c.getStatut() %></td>
                    <td>
                        <a href="<%= request.getContextPath() %>/prets/amortissements?idCompte=<%= c.getIdCompte() %>" 
                           class="btn btn-sm btn-info">
                            Voir Amortissements
                        </a>
                         <a href="<%= request.getContextPath() %>/prets/payement?idComptePret=<%= c.getIdCompte() %>" 
                           class="btn btn-sm btn-success">
                            Rembourser
                        </a>
                    </td>
                </tr>
            <%   }
               } else { %>
                <tr>
                    <td colspan="9" class="text-center">Aucun compte prêt trouvé.</td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

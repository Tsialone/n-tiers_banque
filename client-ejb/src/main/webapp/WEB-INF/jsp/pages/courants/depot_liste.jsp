<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.server_dtos.DepotDto" %>
<%@ page import="com.example.server_dtos.ValidationDepotDto" %>

<%
    List<DepotDto> depots = (List<DepotDto>) request.getAttribute("depots");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des Dépôts</h4>

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Compte Crédité</th>
                <th>Montant</th>
                <th>État</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% if (depots != null && !depots.isEmpty()) {
                   for (DepotDto d : depots) { %>
                <tr>
                    <td><%= d.getIdDepot() %></td>
                    <td><%= d.getIdCompteCredit() %></td>
                    <td><%= d.getMontant() %> Ar</td>
                    <td>
                        <%= (d.getLastValidation() != null)
                            ? d.getLastValidation().getEtat().getLibelle()
                            : "en_attente" %>
                    </td>
                    <td>
                        <form method="post" action="<%= request.getContextPath() %>/courants/depots">
                            <input type="hidden" name="idDepot" value="<%= d.getIdDepot() %>" />
                            <input type="hidden" name="idCompte" value="<%= d.getIdCompteCredit() %>" />
                            <button type="submit" name="action" value="valider" class="btn btn-success btn-sm">Valider</button>
                            <button type="submit" name="action" value="annuler" class="btn btn-danger btn-sm">Annuler</button>
                        </form>
                    </td>
                </tr>
            <% } } else { %>
                <tr><td colspan="5" class="text-center">Aucun dépôt trouvé.</td></tr>
            <% } %>
        </tbody>
    </table>
</div>

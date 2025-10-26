<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.dto.TransactionEpargneDto" %>

<%
    List<TransactionEpargneDto> transactions = (List<TransactionEpargneDto>) request.getAttribute("transactions");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des Transactions Épargne</h4>

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID Transaction</th>
                <th>ID Compte</th>
                <th>Date</th>
                <th>Libellé</th>
                <th>Montant</th>
                <th>Sens</th>
            </tr>
        </thead>
        <tbody>
            <% if (transactions != null && !transactions.isEmpty()) {
                   for (TransactionEpargneDto t : transactions) { %>
                <tr>
                    <td><%= t.getIdTransaction() %></td>
                    <td><%= t.getIdCompte() %></td>
                    <td><%= t.getDateTransaction() %></td>
                    <td><%= t.getLibelle() %></td>
                    <td><%= t.getMontant() %></td>
                    <td><%= t.getSens() %></td>
                </tr>
            <%   } 
               } else { %>
                <tr>
                    <td colspan="6" class="text-center">Aucune transaction trouvée.</td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

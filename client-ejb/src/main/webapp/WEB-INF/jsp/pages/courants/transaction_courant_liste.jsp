<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.server_dtos.TransactionCourantDto" %>

<%
    List<TransactionCourantDto> transactions = (List<TransactionCourantDto>) request.getAttribute("transactionCourants");
    String message = (String) request.getAttribute("message");
    String message_type = (String) request.getAttribute("message_type");
    String alertClass = "alert-" + (message_type != null ? message_type : "info");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des Transactions Courantes</h4>

   

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Source</th>
                <th>Date</th>
                <th>Libellé</th>
                <th>Montant</th>
                <th>Sens</th>
                <th>Devise</th>
                <th>Etat</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% if (transactions != null && !transactions.isEmpty()) {
                   for (TransactionCourantDto t : transactions) { %>
                <tr>
                    <td><%= t.getIdTransaction() %></td>
                    <td><%= t.getSource() %></td>
                    <td><%= t.getDateTransaction() %></td>
                    <td><%= t.getLibelle() %></td>
                    <td><%= t.getMontant() %>Ar</td>
                    <td><%= t.getSens() %></td>
                    <td><%= t.getDevise() %></td>
                    <td>
                        <%= (t.getLastValidation() != null) ? t.getLastValidation().getEtat().getLibelle() : "En attente" %>
                    </td>
                    <td>
                           <form method="post" action="<%= request.getContextPath() %>/courants/transactions">
                                <input type="hidden" name="idTransactionCourant" value="<%= t.getIdTransaction() %>" />
                                <input type="hidden" name="idCompte" value="<%= request.getAttribute("idCompte") %>" />
                                <button type="submit" name="action" value="valider" class="btn btn-primary btn-sm">Valider</button>
                                <button type="submit" name="action" value="annuler" class="btn btn-danger btn-sm">Annuler</button>
                            </form>
                    </td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="9" class="text-center">Aucune transaction trouvée.</td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>
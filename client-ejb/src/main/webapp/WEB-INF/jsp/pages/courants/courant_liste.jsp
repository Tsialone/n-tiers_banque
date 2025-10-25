<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.views.CompteCourantView" %>

<%
    List<CompteCourantView> comptes = (List<CompteCourantView>) request.getAttribute("comptes");
    String message = (String) request.getAttribute("message");
    String message_type = (String) request.getAttribute("message_type");
    String alertClass = "alert-" + (message_type != null ? message_type : "info");
%>

<div class="container mt-4">
    <h4 class="mb-3">Mes Comptes Courants</h4>

    <form method="get" action="${pageContext.request.contextPath}/courants" class="mb-3">
        <label for="date" class="form-label">Voir états au :</label>
        <input type="date" id="date" name="date" class="form-control" style="max-width: 300px;">
        <button type="submit" class="btn btn-primary mt-2">Filtrer</button>
    </form>

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID Compte</th>
                <th>Nom</th>
                <th>Capital</th>
                <th>Découvert Autorisé</th>
                <th>Date Ouverture</th>
                <th>Solde</th>
            </tr>
        </thead>
        <tbody>
            <% if (comptes != null && !comptes.isEmpty()) {
                   for (CompteCourantView c : comptes) { %>
                <tr>
                    <td><%= c.getIdCompte() %></td>
                    <td><%= c.getNom() %></td>
                    <td><%= c.getCapital() %> Ar</td>
                    <td><%= c.getDecouvertAutorise() %> Ar</td>
                    <td><%= c.getDateOuverture() %></td>
                    <td><%= c.getSolde() %> Ar</td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="6" class="text-center">Aucun compte trouvé.</td>
                </tr>
            <% } %>
        </tbody>
    </table> 
</div>

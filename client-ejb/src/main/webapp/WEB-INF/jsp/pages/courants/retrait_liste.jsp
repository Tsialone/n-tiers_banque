<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.server_dtos.RetraitDto" %>
<%@ page import="com.example.server_dtos.ValidationRetraitDto" %>

<%
    List<RetraitDto> retraits = (List<RetraitDto>) request.getAttribute("retraits");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des Retraits</h4>

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Compte Débité</th>
                <th>Montant</th>
                <th>État</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% if (retraits != null && !retraits.isEmpty()) {
                   for (RetraitDto r : retraits) { %>
                <tr>
                    <td><%= r.getIdRetrait() %></td>
                    <td><%= r.getIdCompteDebit() %></td>
                    <td><%= r.getMontant() %> Ar</td>
                    <td>
                        <%= (r.getLastValidation() != null)
                            ? r.getLastValidation().getEtat().getLibelle()
                            : "en_attente" %>
                    </td>
                    <td>
                        <form method="post" action="<%= request.getContextPath() %>/courants/retraits">
                            <input type="hidden" name="idRetrait" value="<%= r.getIdRetrait() %>" />
                            <input type="hidden" name="idCompte" value="<%= r.getIdCompteDebit() %>" />
                            <button type="submit" name="action" value="valider" class="btn btn-success btn-sm">Valider</button>
                            <button type="submit" name="action" value="annuler" class="btn btn-danger btn-sm">Annuler</button>
                        </form>
                    </td>
                </tr>
            <% } } else { %>
                <tr><td colspan="5" class="text-center">Aucun retrait trouvé.</td></tr>
            <% } %>
        </tbody>
    </table>
</div>

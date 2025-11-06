<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.server_dtos.VirementDto" %>

<%
    List<VirementDto> virements = (List<VirementDto>) request.getAttribute("virements");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des Virements</h4>

    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Compte Débit</th>
                <th>Compte Crédit</th>
                <th>Date</th>
                <th>Montant</th>
                <th>Devise</th>
                <th>État</th>
                <th>Frais necessaire</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% if (virements != null && !virements.isEmpty()) {
                   for (VirementDto v : virements) { %>
                <tr>
                    <td><%= v.getIdVirement() %></td>
                    <td><%= v.getIdCompteDebit() %></td>
                    <td><%= v.getIdCompteCredit() %></td>
                    <td><%= v.getDateVirement() %></td>
                    <td><%= v.getMontant() %> Ar</td>
                    <td><%= v.getDevise() %></td>
                    <td>
                        <%= (v.getLastValidation() != null)
                            ? v.getLastValidation().getEtat().getLibelle()
                            : "en_attente" %>
                    </td>
                    <td><%= v.getFrais() %>Ar</td>
                    <td>
                        <form method="post" action="<%= request.getContextPath() %>/courants/virements">
                            <input type="hidden" name="idVirement" value="<%= v.getIdVirement() %>" />
                            <input type="hidden" name="idCompte" value="<%= v.getIdCompteDebit() %>" />
                            <button type="submit" name="action" value="valider" class="btn btn-primary btn-sm">Valider</button>
                            <button type="submit" name="action" value="annuler" class="btn btn-danger btn-sm">Annuler</button>
                        </form>
                    </td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="9" class="text-center">Aucun virement trouvé.</td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

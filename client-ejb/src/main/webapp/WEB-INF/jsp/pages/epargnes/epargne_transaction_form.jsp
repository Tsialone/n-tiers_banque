<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String idCompte = request.getAttribute("idCompte") != null ? request.getAttribute("idCompte").toString() : "";
%>

<div class="container mt-4">
    <h4 class="mb-3">Nouvelle Transaction Épargne</h4>

    <form method="post" action="<%= request.getContextPath() %>/epargnes/transactions/form">
        <input type="hidden" name="idCompte" value="<%= idCompte %>">

        <div class="mb-3">
            <label for="libelle" class="form-label">Libellé</label>
            <input type="text" class="form-control" id="libelle" name="libelle" required>
        </div>

        <div class="mb-3">
            <label for="montant" class="form-label">Montant</label>
            <input type="number" step="0.01" class="form-control" id="montant" name="montant" required>
        </div>

        <div class="mb-3">  
            <label for="sens" class="form-label">Type</label>
            <select id="sens" name="sens" class="form-select">
                <option value="credit">Crédit</option>
                <option value="debit">Débit</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="dateTransaction" class="form-label">Date</label>
            <input type="date" id="dateTransaction" name="dateTransaction" class="form-control" value="<%= request.getAttribute("dateTransaction") %>" required>
        </div>

        <button type="submit" class="btn btn-primary">Enregistrer</button>
        <a href="<%= request.getContextPath() %>/epargnes" class="btn btn-secondary">Annuler</a>
    </form>
</div>

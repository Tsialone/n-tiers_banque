<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    int idClient = request.getAttribute("idClient") != null ? (int) request.getAttribute("idClient") : 1;
    java.time.LocalDate dateOuverture = request.getAttribute("dateOuverture") != null
            ? (java.time.LocalDate) request.getAttribute("dateOuverture")
            : java.time.LocalDate.now();
%>

<div class="container mt-4">
    <h4>Créer un compte épargne</h4>

    <form method="post">
        <input type="hidden" name="idClient" value="<%= idClient %>">

        <div class="mb-3">
            <label for="libelle" class="form-label">Libellé</label>
            <input type="text" class="form-control" id="libelle" name="libelle" required>
        </div>

        <div class="mb-3">
            <label for="capital" class="form-label">Capital initial</label>
            <input type="number" class="form-control" id="capital" name="capital" step="0.01" required>
        </div>

        <div class="mb-3">
            <label for="tauxInteret" class="form-label">Taux d'intérêt (%)</label>
            <input type="number" class="form-control" id="tauxInteret" name="tauxInteret" step="0.01" required>
        </div>

        <div class="mb-3">
            <label for="retrait" class="form-label">Retrait autorisé</label>
            <input type="number" class="form-control" id="retrait" name="retrait" step="0.01" value="50.0" required>
        </div>

        <div class="mb-3">
            <label for="dateOuverture" class="form-label">Date d'ouverture</label>
            <input type="date" class="form-control" id="dateOuverture" name="dateOuverture"
                   value="<%= dateOuverture %>" >
        </div>

        <button type="submit" class="btn btn-success">Créer</button>
        <a href="<%= request.getContextPath() %>/epargnes" class="btn btn-secondary">Annuler</a>
    </form>
</div>

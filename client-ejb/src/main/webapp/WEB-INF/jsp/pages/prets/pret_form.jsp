<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container mt-4">
    <h4 class="mb-3">Demande de prêt</h4>

    <form method="post" action="<%= request.getContextPath() %>/prets/form">
        <input type="hidden" name="idClient" value="<%= request.getAttribute("idClient") %>">

        <div class="mb-3">
            <label for="libelle" class="form-label">Libellé du prêt</label>
            <input type="text" class="form-control" id="libelle" name="libelle" required>
        </div>

        <div class="mb-3">
            <label for="capitalEmprunte" class="form-label">Capital emprunté</label>
            <input type="number" step="0.01" class="form-control" id="capitalEmprunte" name="capitalEmprunte" required>
        </div>

        <div class="mb-3">
            <label for="tauxInteret" class="form-label">Taux d'intérêt (%)</label>
            <input type="number" step="0.01" class="form-control" id="tauxInteret" name="tauxInteret" required>
        </div>

        <div class="mb-3">
            <label for="dureeMois" class="form-label">Durée (mois)</label>
            <input type="number" class="form-control" id="dureeMois" name="dureeMois" required>
        </div>

        <div class="mb-3">
            <label for="dateOuverture" class="form-label">Date d'ouverture</label>
            <input type="date" class="form-control" id="dateOuverture" name="dateOuverture"
                   value="<%= request.getAttribute("dateOuverture") %>" required>
        </div>

        <button type="submit" class="btn btn-primary">Envoyer la demande</button>
    </form>
</div>

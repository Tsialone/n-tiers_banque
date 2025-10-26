<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>

<div class="container mt-4">
    <h4 class="mb-3">Créer un Compte Courant</h4>

    <% 
        String message = (String) request.getAttribute("message");
        String message_type = (String) request.getAttribute("message_type");
        String alertClass = "alert-" + (message_type != null ? message_type : "info");
    %>


    <form method="post" action="<%= request.getContextPath() %>/courants/form">
        <div class="mb-3">
            <label for="nom" class="form-label">Nom du compte</label>
            <input type="text" id="nom" name="nom" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="capital" class="form-label">Capital</label>
            <input type="number" step="0.01" id="capital" name="capital" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="decouvertAutorise" class="form-label">Découvert autorisé</label>
            <input type="number" step="0.01" id="decouvertAutorise" name="decouvertAutorise" class="form-control" value="0.0" required>
        </div>

        <div class="mb-3">
            <label for="dateOuverture" class="form-label">Date d'ouverture</label>
            <input type="date" id="dateOuverture" name="dateOuverture" class="form-control" value="<%= LocalDate.now() %>" readonly>
        </div>

        <button type="submit" class="btn btn-success">Créer le compte</button>
    </form>
</div>

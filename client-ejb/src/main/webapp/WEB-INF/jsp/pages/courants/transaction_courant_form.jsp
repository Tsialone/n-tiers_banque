<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.dto.TransactionCourantDto" %>
<%@ page import="com.example.dto.DeviseDto" %>
<%@ page import="com.example.models.CompteCourant" %>

<%
    List<CompteCourant> comptes = (List<CompteCourant>) request.getAttribute("compteCourants");
    List<DeviseDto> devises = (List<DeviseDto>) request.getAttribute("devises");

%>

<div class="container mt-4">
    <h4 class="mb-3">Créer une Nouvelle Transaction Courante</h4>

    <form method="post" action="<%= request.getContextPath() %>/transaction_courants/form">
         <div class="mb-3">
            <label for="idCompte" class="form-label">Compte</label>
            <select id="idCompte" name="idCompte" class="form-select" required>
                <option value="">-- Sélectionnez un compte --</option>
                <% if (comptes != null) {
                       for (CompteCourant c : comptes) { %>
                    <option value="<%= c.getIdCompte() %>">
                        <%= c.getNom() %> (ID: <%= c.getIdCompte() %>)
                    </option>
                <%   } 
                   } %>
            </select>
        </div>

        <div class="mb-3">
            <label for="dateTransaction" class="form-label">Date de Transaction</label>
            <input type="date" id="dateTransaction" name="dateTransaction" class="form-control" value="<%= java.time.LocalDate.now() %>" required>
        </div>

        <div class="mb-3">
            <label for="libelle" class="form-label">Libellé</label>
            <input type="text" id="libelle" name="libelle" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="montant" class="form-label">Montant</label>
            <input type="number" step="0.01" id="montant" name="montant" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="sens" class="form-label">Sens</label>
            <select id="sens" name="sens" class="form-select" required>
                <option value="debit">Débit</option>
                <option value="credit">Crédit</option>
            </select>
        </div>

        <div class="mb-3">
        
            <label for="devise" class="form-label">Devise</label>
            <%-- <input type="text" id="devise" name="devise" class="form-control" value="Ar" required> --%>
            <select id="idDevise" name="idDevise" class="form-select" required>
                <option value="">-- Sélectionnez une devise --</option>
                <% if (devises != null) {
                       for (DeviseDto c : devises) { %>
                    <option value="<%= c.getLibelle() %>">
                        <%= c.getLibelle() %>
                    </option>
                <%   } 
                   } %>
            </select>
        </div>

        <div class="mb-3 form-check">
            <input type="checkbox" id="validate" name="validate" class="form-check-input">
            <label for="validate" class="form-check-label">Valider immédiatement</label>
        </div>

        <button type="submit" class="btn btn-success">Créer la Transaction</button>
    </form>
</div>

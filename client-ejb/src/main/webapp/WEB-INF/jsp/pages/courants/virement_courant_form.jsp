<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.change_dtos.DeviseDto" %>

<%
    List<DeviseDto> devises = (List<DeviseDto>) request.getAttribute("devises");
    String idCompteDebit = request.getAttribute("idCompte") != null ? request.getAttribute("idCompte").toString() : "";
%>

<div class="container mt-4">
    <h4 class="mb-3">Effectuer un Virement Courant</h4>

    <form method="post" action="<%= request.getContextPath() %>/courants/virements/form">

        <!-- Compte débiteur (hidden) -->
        <input type="hidden" id="idCompteDebit" name="idCompteDebit" value="<%= idCompteDebit %>">

        <!-- Compte créditeur -->
        <div class="mb-3">
            <label for="idCompteCredit" class="form-label">Numéro de Compte Créditeur</label>
            <input type="text" id="idCompteCredit" name="idCompteCredit" class="form-control" placeholder="Ex: 20014578" >
        </div>

        <!-- Date du virement -->
        <div class="mb-3">
            <label for="dateVirement" class="form-label">Date du Virement</label>
            <input type="datetime-local" id="dateVirement" name="dateVirement" class="form-control" value="<%= request.getAttribute("dateVirement") %>" >
        </div>

        <!-- Montant -->
        <div class="mb-3">
            <label for="montant" class="form-label">Montant</label>
            <input type="number" step="0.01" id="montant" name="montant" class="form-control" >
        </div>

        <!-- Devise -->
        <div class="mb-3">
            <label for="devise" class="form-label">Devise</label>
            <select id="devise" name="devise" class="form-select" >
                <option value="ar">-- Sélectionnez une devise --</option>
                <% if (devises != null) {
                       for (DeviseDto d : devises) { %>
                    <option value="<%= d.getLibelle() %>"><%= d.getLibelle() %></option>
                <%   }
                   } %>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Effectuer le Virement</button>
    </form>
</div>

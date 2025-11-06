<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.change_dtos.DeviseDto" %>

<%
    List<DeviseDto> devises = (List<DeviseDto>) request.getAttribute("devises");
    String idCompteDebit = request.getAttribute("idCompte") != null ? request.getAttribute("idCompte").toString() : "";
%>

<div class="container mt-4">
    <h4 class="mb-3">Nouveau Retrait</h4>

    <form method="post" action="<%= request.getContextPath() %>/courants/retraits/form">

        <!-- Compte débiteur (hidden) -->
        <input type="hidden" id="idCompteDebit" name="idCompte" value="<%= idCompteDebit %>">

        <!-- Date du retrait -->
        <div class="mb-3">
            <label for="dateRetrait" class="form-label">Date du Retrait</label>
            <input type="datetime-local" id="dateRetrait" name="dateRetrait" class="form-control"
                   value="<%= request.getAttribute("dateRetrait") %>" required />
        </div>

        <!-- Montant -->
        <div class="mb-3">
            <label for="montant" class="form-label">Montant</label>
            <input type="number" step="0.01" id="montant" name="montant" class="form-control" required />
        </div>

        <!-- Devise -->
        <div class="mb-3">
            <label for="devise" class="form-label">Devise</label>
            <select id="devise" name="devise" class="form-select">
                <option value="ar">-- Sélectionnez une devise --</option>
                <% if (devises != null) {
                       for (DeviseDto d : devises) { %>
                    <option value="<%= d.getLibelle() %>"><%= d.getLibelle() %></option>
                <%   }
                   } %>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Effectuer le Retrait</button>
    </form>
</div>

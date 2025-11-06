<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.change_dtos.DeviseDto" %>

<%
    List<DeviseDto> devises = (List<DeviseDto>) request.getAttribute("devises");
    String idCompte = request.getAttribute("idCompte") != null ? request.getAttribute("idCompte").toString() : "";
%>

<div class="container mt-4">
    <h4 class="mb-3">Nouveau Dépôt</h4>

    <form method="post" action="<%= request.getContextPath() %>/courants/depots/form">
        <input type="hidden" name="idCompte" value="<%= idCompte %>" />

        <div class="mb-3">
            <label for="dateDepot" class="form-label">Date du Dépôt</label>
            <input type="datetime-local" id="dateDepot" name="dateDepot" class="form-control"
                   value="<%= request.getAttribute("dateDepot") %>" required />
        </div>

        <div class="mb-3">
            <label for="montant" class="form-label">Montant</label>
            <input type="number" step="0.01" id="montant" name="montant" class="form-control" required />
        </div>

        <div class="mb-3">
            <label for="devise" class="form-label">Devise</label>
            <select id="devise" name="devise" class="form-select" required>
                <option value="ar">-- Sélectionnez une devise --</option>
                <% if (devises != null) {
                       for (DeviseDto d : devises) { %>
                    <option value="<%= d.getLibelle() %>"><%= d.getLibelle() %></option>
                <%   }
                   } %>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Enregistrer le dépôt</button>
    </form>
</div>

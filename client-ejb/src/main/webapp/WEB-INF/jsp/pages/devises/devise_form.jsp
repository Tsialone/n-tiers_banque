<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.change_dtos.DeviseDto" %>

<%
    DeviseDto devise = (DeviseDto) request.getAttribute("devise");
    if (devise == null) {
        devise = new DeviseDto();
    }

%>

<div class="container mt-4">
    <h4 class="mb-3"><%= devise.getId() != null ? "Modifier Devise" : "Nouvelle Devise" %></h4>

   

    <form method="post" action="<%= request.getContextPath() %>/devises/form">
        <input type="hidden" name="id" value="<%= devise.getId() != null ? devise.getId() : "" %>" />

        <div class="mb-2">
            <label>Libellé</label>
            <input type="text" name="libelle" class="form-control" 
                   value="<%= devise.getLibelle() != null ? devise.getLibelle() : "" %>" />
        </div>

        <div class="mb-2">
            <label>Date début</label>
            <input type="date" name="dateDebut" class="form-control" 
                   value="<%= devise.getDateDebut() != null ? devise.getDateDebut() : "" %>" />
        </div>

        <div class="mb-2">
            <label>Date fin</label>
            <input type="date" name="dateFin" class="form-control"
                   value="<%= devise.getDateFin() != null ? devise.getDateFin() : "" %>" />
        </div>

        <div class="mb-3">
            <label>Arriary</label>
            <input type="number" step="0.01" name="arriary" class="form-control"
                   value="<%= devise.getArriary() %>"  />
        </div>

        <button type="submit" class="btn btn-success">Enregistrer</button>
        <a href="<%= request.getContextPath() %>/devises" class="btn btn-secondary">Annuler</a>
    </form>
</div>

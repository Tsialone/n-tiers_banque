<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.change_dtos.DeviseDto" %>

<%
    List<DeviseDto> devises = (List<DeviseDto>) request.getAttribute("devises");
%>

<div class="container mt-4">
    <h4 class="mb-3">Liste des devises</h4>


    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Libellé</th>
                <th>Date début</th>
                <th>Date fin</th>
                <th>Arriary</th>
                <th>Valider</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% if (devises != null && !devises.isEmpty()) {
                   for (DeviseDto d : devises) { %>
                <tr>
                    <td><%= d.getId() %></td>
                    <td><%= d.getLibelle() %></td>
                    <td><%= d.getDateDebut() %></td>
                    <td><%= d.getDateFin() != null ? d.getDateFin() : "" %></td>
                    <td><%= d.getArriary() %></td>
                    <td><%= d.getEtat() %></td>
                    <td>
                        <a href="<%= request.getContextPath() %>/devises/form?id=<%= d.getId() %>"
                           class="btn btn-primary btn-sm">Modifier</a>
                           <a href="<%= request.getContextPath() %>/devises/valider?id=<%= d.getId() %>"
                           class="btn btn-success btn-sm" >Valider</a>
                        <a href="<%= request.getContextPath() %>/devises/annuler?id=<%= d.getId() %>"
                           class="btn btn-danger btn-sm" >Annuler</a>
                    </td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="7" class="text-center">Aucune devise trouvée.</td>
                </tr>
            <% } %>
        </tbody>
    </table>

    <a href="<%= request.getContextPath() %>/devises/form" class="btn btn-success">Nouvelle devise</a>
</div>

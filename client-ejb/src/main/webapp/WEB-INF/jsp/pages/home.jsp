<%@page import="com.example.server_dtos.ClientCourantDto"  %>
<%@page import="com.example.server_dtos.ActionRoleDto"  %>



<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% 

ClientCourantDto clientCourant = (ClientCourantDto)session.getAttribute("client");

List<ActionRoleDto> roles = (List<ActionRoleDto>) request.getAttribute("actionRoles");

%>
<div class="container my-5">
  <div class="card shadow-sm">
    <div class="card-body">
      <%-- <h2 class="card-title mb-3">Bonjour, son nom</h2> --%>
      <h2 class="card-title mb-3">Bonjour, <%= clientCourant.getNom() %> </h2>
      <%-- <h5 class="text-muted mb-4">Utilisateur: <%= clientCourant.getDirection().getLibelle() %> </h5> --%>
      <h5 class="text-muted mb-4">Utilisateur: sa direction </h5>
      <p class="text-secondary">Bienvenue dans notre page de Banque.</p>
        <ul>
               <ul>
                  <% if (roles != null) {
                      for (ActionRoleDto ar : roles) { %>
                  <li>
                      <strong><%= ar.getLibelleRole() %></strong> →
                      <%= ar.getLibelleAction() %> (<%= ar.getNomTable() %>)
                  </li>
                <%  }
                    } else { %>
                        <li>Aucune action disponible</li>
                  <% } %>
</ul>
        </ul>
    </div>
  </div>

  <!-- ===== SOLDE GLOBAL ===== -->
  <div class="row mt-4">
    <div class="col-md-4">
      <div class="card text-bg-primary mb-3">
        <div class="card-header">Solde Courant</div>
        <div class="card-body">
          <h4 class="card-title">
            <%= request.getAttribute("courantSoldeGlobal") != null ? request.getAttribute("courantSoldeGlobal") : "0.0" %> Ar
          </h4>
        </div>
      </div>
    </div>

    <div class="col-md-4">
      <div class="card text-bg-success mb-3">
        <div class="card-header">Solde Épargne</div>
        <div class="card-body">
          <h4 class="card-title">
            <%= request.getAttribute("epargneSoldeGlobal") != null ? request.getAttribute("epargneSoldeGlobal") : "0.0" %> Ar
          </h4>
        </div>
      </div>
    </div>
     <div class="col-md-4">
      <div class="card text-bg-success mb-3">
        <div class="card-header">Solde Pret</div>
        <div class="card-body">
          <h4 class="card-title">
            <%= request.getAttribute("pretSoldeGlobal") != null ? request.getAttribute("pretSoldeGlobal") : "0.0" %> Ar
          </h4>
        </div>
      </div>
    </div>

    <div class="col-md-4">
      <div class="card text-bg-warning mb-3">
        <div class="card-header">Solde Global</div>
        <div class="card-body">
          <h4 class="card-title">
            <%= request.getAttribute("soldeGlobal") != null ? request.getAttribute("soldeGlobal") : "0.0" %> Ar
          </h4>
        </div>
      </div>
    </div>
  </div>
</div>


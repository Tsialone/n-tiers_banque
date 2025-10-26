<%@page import="com.example.models.ClientCourant"  %>
<%@page import="com.example.models.ClientRole"  %>
<%@page import="com.example.models.Role"  %>
<%@page import="com.example.models.ActionRole"  %>

<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% 

ClientCourant clientCourant = (ClientCourant)session.getAttribute("client");
List<ClientRole> roles = (List<ClientRole>) request.getAttribute("roles");

%>
<div class="container my-5">
  <div class="card shadow-sm">
    <div class="card-body">
      <%-- <h2 class="card-title mb-3">Bonjour, son nom</h2> --%>
      <h2 class="card-title mb-3">Bonjour, <%= clientCourant.getNom() %> </h2>
      <h5 class="text-muted mb-4">Utilisateur: <%= clientCourant.getDirection().getLibelle() %> </h5>
      <%-- <h5 class="text-muted mb-4">Utilisateur: sa direction </h5> --%>
      <p class="text-secondary">Bienvenue dans notre page de Banque.</p>
       <ul>
                <% for (ClientRole clientRole : roles) { 
                        Role role = clientRole.getRole();
                %>
                <li>
                    <strong>Role:</strong> <%= role.getLibelle() %>
                    <ul>
                        <% for (ActionRole actionRole : role.getActionRoles()) { %>
                        <li><%= actionRole.getNomTable() %> - <%= actionRole.getAction().getLibelle() %></li>
                        <% } %>
                    </ul>
                </li>
                <% } %>
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


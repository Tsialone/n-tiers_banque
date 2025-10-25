<%-- <%@ page import="s4.biblio.models.Utilisateur" %> --%>
<%-- <%@ page import="s4.biblio.models.E_TypeCategorie" %> --%>
<%@page import="com.example.models.ClientCourant"  %>
<%@ page import="java.util.List" %>
<%-- <%@ page import="s4.biblio.views.V_Abonnement" %> --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
// ClientCourant clientCourant = (ClientCourant)session.getAttribute("client");
%> 

<div class="container my-5">
  <div class="card shadow-sm">
    <div class="card-body">
      <%-- <h2 class="card-title mb-3">Bonjour, <%= clientCourant.getNom() %> </h2> --%>
      <h2 class="card-title mb-3">Bonjour, son nom</h2>

      <%-- <h5 class="text-muted mb-4">Utilisateur: <%= clientCourant.getDirection().getLibelle() %> </h5> --%>
      <h5 class="text-muted mb-4">Utilisateur: sa direction </h5>

      <p class="text-secondary">Bienvenue dans notre page de Banque.</p>
      
    </div>
  </div>
</div>

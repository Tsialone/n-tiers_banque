<%@page import="java.util.List"  %>
<%@page import="com.example.models.ClientCourant"  %>
<%@page import="java.time.LocalDate"  %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- <%@ page import="s4.biblio.models.Utilisateur" %> --%>
<%
  String fonctionality = request.getAttribute("fonctionality").toString();
  // ClientCourant clientCourant = (ClientCourant)session.getAttribute("client");
%>

<header class="bg-white shadow-sm px-4 py-3 d-flex justify-content-between align-items-center border-bottom">
  <h1 class="h4 fw-bold mb-0 text-primary"><%= fonctionality %></h1>
  
  <div class="d-flex align-items-center gap-3">
    <span class="text-dark">
      <%-- <span class="badge bg-warning text-dark ms-5"><%= clientCourant.getNom() %></span> --%>
      <span class="badge bg-warning text-dark ms-5">son nom</span>

      |
      <%-- <span class="badge bg-info text-dark ms-2"><%= clientCourant.getDirection().getLibelle() %></span> --%>
      <span class="badge bg-info text-dark ms-2">direction</span>

    </span>
    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Se déconnecter</a>
  </div>
</header>

<%-- <%@ page import="s4.biblio.models.Categorie" %> --%>
<%-- <%@ page import="s4.biblio.models.Utilisateur" %> --%>
<%-- <%@ page import="s4.biblio.models.E_TypeCategorie" %> --%>

<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- <%  Utilisateur utilisateur = (Utilisateur)session.getAttribute("utilisateur"); 
%> --%>

<aside class="bg-white shadow-sm d-none d-md-block" style="width: 16rem;">
  <div class="p-4 border-bottom fw-bold fs-5">
    <a href="${pageContext.request.contextPath}/home" class="text-decoration-none text-dark">Banky</a>
  </div>

  <nav class="p-3">
  <div class="mb-4">
        <h3 class="text-muted text-uppercase small fw-semibold mb-2">Devise(s)</h3>
        <a href="${pageContext.request.contextPath}/devises/form" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Creation</a>
        <a href="${pageContext.request.contextPath}/devises" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Changes</a>

        <%-- <a href="${pageContext.request.contextPath}/epargnes/transactions" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Mouvements</a> --%>

      </div>
      <%-- <a href="/user/client/filter" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Prolonge</a> --%>
     <div class="mb-4">
      <h3 class="text-muted text-uppercase small fw-semibold mb-2">Courant(s)</h3>
      <a href="${pageContext.request.contextPath}/courants" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Comptes</a>
      <a href="${pageContext.request.contextPath}/courants/form" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Creation</a>
      <%-- <a href="${pageContext.request.contextPath}/transaction_courants" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Mouvements</a> --%>
      <%-- <a href="${pageContext.request.contextPath}/transaction_courants/form" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Ajout mouvement</a> --%>


     </div>
     <div class="mb-4">
      <h3 class="text-muted text-uppercase small fw-semibold mb-2">Pret(s)</h3>
      <a href="${pageContext.request.contextPath}/prets" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Comptes</a>
      <a href="${pageContext.request.contextPath}/prets/form" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Ouverture</a>
      <%-- <a href="/reservation/list" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Mouvements</a> --%>
     </div>
      <div class="mb-4">
        <h3 class="text-muted text-uppercase small fw-semibold mb-2">Epargne(s)</h3>
        <a href="${pageContext.request.contextPath}/epargnes" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Comptes</a>
        <%-- <a href="${pageContext.request.contextPath}/epargnes/transactions" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Mouvements</a> --%>
        <a href="${pageContext.request.contextPath}/epargnes/form" class="d-block p-2 rounded text-decoration-none text-body hover-bg-light">Creations</a>

      </div>

      
    </div>
  </nav>
</aside>

<style>
  .hover-bg-light:hover {
    background-color: #f8f9fa; /* équivalent bg-gray-200 en Tailwind */
  }
</style>

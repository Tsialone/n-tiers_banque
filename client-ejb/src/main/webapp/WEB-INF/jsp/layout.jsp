<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String title = (String) request.getAttribute("title");
    String the_page = (String) request.getAttribute("content");

    // Gestion du message centralisée
    String message = (String) request.getAttribute("message");
    String message_type = (String) request.getAttribute("message_type");
    String alertClass = "alert-" + (message_type != null ? message_type : "info");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title><%= title %></title>
  <link href="${pageContext.request.contextPath}/assets/bootstrap-5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex min-vh-100">

  <!-- Sidebar -->
  <jsp:include page="layout/sidebar.jsp" />

  <!-- Main Content Area -->
  <div class="flex-grow-1 d-flex flex-column">

    <!-- Header -->
    <jsp:include page="layout/header.jsp" />

    <!-- Main -->
    <main class="p-4">

      <%-- Gestion centralisée des messages --%>
      <% if (message != null && !message.isEmpty()) { %>
      <div class="container my-5">
          <div class="alert <%= alertClass %> alert-dismissible fade show" role="alert">
              <%= message %>
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
         </div>
      <% } %>

      <%-- Contenu dynamique de la page --%>
      <jsp:include page="<%= the_page %>" />
      
      


    </main>
  </div>

  <script src="${pageContext.request.contextPath}/assets/bootstrap-5.0.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>

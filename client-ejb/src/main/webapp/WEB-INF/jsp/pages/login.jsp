<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String message = (String) request.getAttribute("message");
String message_type = (String) request.getAttribute("message_type");
String alertClass = "alert-" + (message_type != null ? message_type : "info");

%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Connexion</title>
  <link rel="icon" type="image/png" href="../../assets/images/my.png">
  <%-- <link href="../../assets/bootstrap-5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" /> --%>
  <link href="${pageContext.request.contextPath}/assets/bootstrap-5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
<%-- <link href="/client-ejb/assets/bootstrap-5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"> --%>

</head>
<body class="bg-light d-flex align-items-center justify-content-center min-vh-100">

  <div class="bg-white p-4 p-md-5 rounded shadow-sm w-100" style="max-width: 400px;">
    <h2 class="text-center fw-bold mb-4 text-secondary fs-4">Connexion</h2>
    <form action="${pageContext.request.contextPath}/login" method="POST" class="needs-validation" novalidate>
      
      <div class="mb-3">
        <label for="email" class="form-label fw-medium">Email</label>
        <input
          type="email"
          id="email"
          name="email"
          class="form-control"
          placeholder="Entrez votre email"
          value="@gmail"
          required
        />
        <div class="invalid-feedback">
          Veuillez entrer votre email.
        </div>
      </div>
      <div class="mb-4">
        <label for="password" class="form-label fw-medium">Mot de passe</label>
        <input
          type="password"
          id="password"
          name="mdp"
          class="form-control"
          placeholder="Entrez votre mot de passe"
          value="1234"
          required
        />
        <div class="invalid-feedback">
          Veuillez entrer votre mot de passe.
        </div>
      </div>
      
      <div class="text-center mt-3">
        <span>Pas encore inscrit ?</span>
        <a href="/utilisateur/inscription" class="text-primary fw-semibold">Inscription</a>
      </div>
      <br/>
  <% if (message != null) { %>
        <div class="alert <%= alertClass %> alert-dismissible fade show">
            <%= message %>
        </div>
    <% } %>
      <button type="submit" class="btn btn-primary w-100 fw-semibold">
        Se connecter
      </button>
     
    </form>
  </div>

  <%-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script> --%>
  <%-- <script src="../../assets/bootstrap-5.0.2/dist/js/bootstrap.bundle.min.js"></script> --%>
  <script src="${pageContext.request.contextPath}/assets/bootstrap-5.0.2/dist/js/bootstrap.bundle.min.js"></script>


</body>
</html>

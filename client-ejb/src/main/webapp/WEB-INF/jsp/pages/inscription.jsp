<%@ page import="s4.biblio.models.Categorie" %>
<%@ page import="s4.biblio.models.E_TypeCategorie" %>

<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% 
List<Categorie> categorie_utilisateur = (List<Categorie>)request.getAttribute("categorie_utilisateur");
String message = (String) request.getAttribute("message");
String message_type = (String) request.getAttribute("message_type");
String alertClass = "alert-" + (message_type != null ? message_type : "info");

%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Inscription</title>
  <link rel="icon" type="image/png" href="../../assets/images/my.png">
  <link href="../../assets/bootstrap-5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light d-flex align-items-center justify-content-center min-vh-100">

  <div class="bg-white p-4 p-md-5 rounded shadow-sm w-100" style="max-width: 600px;">
  <h2 class="text-center fw-bold mb-4 text-secondary fs-4">Inscription</h2>

  <form action="/utilisateur/inscription/save" method="POST" class="needs-validation" novalidate>

    <div class="row g-3">
      <div class="col-md-6">
        <label for="nom" class="form-label fw-medium">Nom</label>
        <input type="text" id="nom" name="nom" class="form-control" placeholder="Ex: Bobou" required />
        <div class="invalid-feedback">Veuillez entrer votre nom.</div>
      </div>

      <div class="col-md-6">
        <label for="prenom" class="form-label fw-medium">Prénom</label>
        <input type="text" id="prenom" name="prenom" class="form-control" placeholder="Ex: Babakoto" required />
        <div class="invalid-feedback">Veuillez entrer votre prénom.</div>
      </div>

      <div class="col-md-6">
        <label for="email" class="form-label fw-medium">Email</label>
        <input type="email" id="email" name="email" class="form-control" placeholder="Ex: tsialone1902@gmail.com" required />
        <div class="invalid-feedback">Veuillez entrer votre email.</div>
      </div>

      <div class="col-md-6">
        <label for="tel" class="form-label fw-medium">Téléphone</label>
        <input type="tel" id="tel" name="tel" class="form-control" placeholder="Ex: 0387588451" required />
        <div class="invalid-feedback">Veuillez entrer votre numéro de téléphone.</div>
      </div>

      <div class="col-12">
        <label for="adresse" class="form-label fw-medium">Adresse</label>
        <input type="text" id="adresse" name="adresse" class="form-control" placeholder="Ex: Analamanga" required />
        <div class="invalid-feedback">Veuillez entrer votre adresse.</div>
      </div>

       <div class="col-12">
        <label for="dateNaissance" class="form-label fw-medium">Date de naissance</label>
        <input type="date" id="dateNaissance" name="dateNaissance" class="form-control" placeholder="Ex: Analamanga" required />
        <div class="invalid-feedback">Veuillez entrer votre date de naissance.</div>
      </div>

     <select id="categorie" name="categorie" class="form-select" required>
        <option value="">-- Sélectionnez une catégorie --</option>
        <optgroup label="Adhérant">
          <% for (Categorie cat : categorie_utilisateur) {
              if (cat.getType() == E_TypeCategorie.adherant) { %>
            <option value="<%= cat.getId() %>"><%= cat.getLibelle() %></option>
          <% }} %>
        </optgroup>
        <optgroup label="Admin">
          <% for (Categorie cat : categorie_utilisateur) {
              if (cat.getType() == E_TypeCategorie.admin) { %>
            <option value="<%= cat.getId() %>"><%= cat.getLibelle() %></option>
          <% }} %>
        </optgroup>
    </select>

      <div class="col-12">
        <label for="password" class="form-label fw-medium">Mot de passe</label>
        <input type="password" id="password" name="mdp" class="form-control" placeholder="Votre mot de passe" required />
        <div class="invalid-feedback">Veuillez entrer votre mot de passe.</div>
      </div>
    </div>

    <button type="submit" class="btn btn-primary w-100 fw-semibold mt-4">S'inscrire</button>

    <div class="text-center mt-3">
      <span>Déjà inscrit ?</span>
      <a href="/utilisateur/login" class="text-primary fw-semibold">Connexion</a>
    </div>
  <% if (message != null) { %>
        <div class="alert <%= alertClass %> alert-dismissible fade show">
            <%= message %>
        </div>
    <% } %>
  </form>
</div>


  <script src="../../assets/bootstrap-5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

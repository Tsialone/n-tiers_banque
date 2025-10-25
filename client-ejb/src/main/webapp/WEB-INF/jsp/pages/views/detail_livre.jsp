<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="s4.biblio.models.Pret" %>
<%@ page import="s4.biblio.models.Exemplaire" %>
<%@ page import="s4.biblio.models.Utilisateur" %>
<%@ page import="s4.biblio.models.Prolongement" %>
<%@ page import="s4.biblio.models.Statut" %>
<%@ page import="s4.biblio.models.Categorie" %>



<div class="container mt-5">
    <h4>Rechercher les exemplaires d’un livre</h4>

    <form id="livreForm" class="mb-3">
        <div class="input-group">
            <input type="number" id="id_livre" name="id_livre" class="form-control" placeholder="ID du livre" required>
            <input type="date" id="date_exemplaire" name="date_exemplaire" class="form-control" placeholder="Date (optionnelle)">
            <button type="submit" class="btn btn-primary">Rechercher</button>
        </div>
    </form>


    <table class="table table-bordered" id="resultTable" style="display: none;">
        <thead>
            <tr>
                <th>ID Livre</th>
                <th>Titre</th>
                <th>Description</th>
                <th>Auteur</th>
                <th>ID Exemplaire</th>
                <th>Disponible</th>
            </tr>
        </thead>
        <tbody id="resultBody">
        </tbody>
    </table>
</div>

<script>
document.getElementById("livreForm").addEventListener("submit", async function(e) {
    e.preventDefault();
    const id = document.getElementById("id_livre").value;
    const dateExemplaire = document.getElementById("date_exemplaire").value;

    let url = "/api/livre?id=" + id;
    if (dateExemplaire) {
        url += "&date=" + encodeURIComponent(dateExemplaire);
    }

    const response = await fetch(url);
    const data = await response.json();

    const table = document.getElementById("resultTable");
    const tbody = document.getElementById("resultBody");
    tbody.innerHTML = ""; 

    if (data.length === 0) {
        tbody.innerHTML = "<tr><td colspan='6' class='text-center'>Aucun exemplaire trouvé</td></tr>";
    } else {
        data.forEach(ex => {
            const row = document.createElement("tr");
            row.innerHTML = 
            "<td>" + (ex.idLivre ?? "N/A") + "</td>" +
            "<td>" + (ex.titre ?? "N/A") + "</td>" +
            "<td>" + (ex.description ?? "N/A") + "</td>" +
            "<td>" + (ex.auteurName ?? "N/A") + "</td>" +
            "<td>" + (ex.idExemplaire ?? "N/A") + "</td>" +
            "<td>" + (ex.disponible ? "Oui" : "Non") + "</td>";
            tbody.appendChild(row);
        });
    }

    table.style.display = "table";
});

</script>


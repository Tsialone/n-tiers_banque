<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="s4.biblio.views.V_AdherantDetail" %>

<div class="container mt-5">
    <h4>Rechercher un adhérant</h4>

    <form id="adherantForm" class="mb-3">
        <div class="input-group">
            <input type="number" id="id_adherant" name="id_adherant" class="form-control" placeholder="ID adhérant" required>
            <input type="date" id="date_adherant" name="date_adherant" class="form-control" placeholder="Date (optionnelle)">
            <button type="submit" class="btn btn-primary">Rechercher</button>
        </div>
    </form>

    <table class="table table-bordered" id="adherantTable" style="display: none;">
        <thead>
            <tr>
                <th>ID Adhérant</th>
                <th>Nom</th>
                <th>Prêts Restants</th>
                <th>Prêts Max</th>
                <th>Durée Prêt Max en jour</th>
                <th>Est Abonné</th>
                <th>Est Pénalisé</th>
            </tr>
        </thead>
        <tbody id="adherantBody">
        </tbody>
    </table>
</div>

<script>
document.getElementById("adherantForm").addEventListener("submit", async function(e) {
    e.preventDefault();
    const id = document.getElementById("id_adherant").value;
    const dateAdherant = document.getElementById("date_adherant").value;

    let url = "/api/adherant?id=" + id;
    if (dateAdherant) {
        url += "&date=" + encodeURIComponent(dateAdherant);
    }

    const response = await fetch(url);
    const data = await response.json();

    const table = document.getElementById("adherantTable");
    const tbody = document.getElementById("adherantBody");
    tbody.innerHTML = "";

    if (data.length === 0) {
        tbody.innerHTML = "<tr><td colspan='7' class='text-center'>Aucun adhérant trouvé</td></tr>";
    } else {
        console.log(data);
        data.forEach(ex => {
            const row = document.createElement("tr");
            row.innerHTML = 
                "<td>" + (ex.idAdherant ?? "N/A") + "</td>" +
                "<td>" + (ex.nom ?? "N/A") + "</td>" +
                "<td>" + (ex.pretRestant ?? "N/A") + "</td>" +
                "<td>" + (ex.pretMax ?? "N/A") + "</td>" +
                "<td>" + (ex.durreeMax ?? "N/A") + "</td>" +
                "<td>" + (ex.estAbonne ? "Oui" : "Non") + "</td>" +
                "<td>" + (ex.estPenalise ? "Oui" : "Non") + "</td>";
            tbody.appendChild(row);
        });
    }

    table.style.display = "table";
});
</script>


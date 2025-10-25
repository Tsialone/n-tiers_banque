<%@ page import="s4.biblio.models.Utilisateur" %>
<%@ page import="s4.biblio.views.VExemplaireEmprunt" %>
<%@ page import="s4.biblio.models.StatPenaliteAdherantView" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<VExemplaireEmprunt> exemplaires = (List<VExemplaireEmprunt>) request.getAttribute("exemplaires");
    List<StatPenaliteAdherantView> stat_user = (List<StatPenaliteAdherantView>) request.getAttribute("stat_user");

    int  nbr_adherant_penalise = (int)request.getAttribute("nbr_adherant_penalise");
    double  duree_moyenne_pret = (double)request.getAttribute("duree_moyenne_pret");
    double  duree_moyenne_penalite = (double)request.getAttribute("duree_moyenne_penalite");
%>

<!-- Chart.js CDN -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="container mt-4">
    <h4 class="mb-4">Statistiques</h4>

    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card text-white bg-danger mb-3 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Nombre fois Adhérents pénalisés</h5>
                    <p class="card-text" style="font-size: 1.5rem;"><strong><%=  nbr_adherant_penalise %></strong></p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-info mb-3 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Durée moyenne des prêts</h5>
                    <p class="card-text" style="font-size: 1.5rem;"><strong><%= duree_moyenne_pret %></strong></p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-success mb-3 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Durée moyenne des pénalités</h5>
                    <p class="card-text" style="font-size: 1.5rem;"><strong><%= duree_moyenne_penalite %></strong></p>
                </div>
            </div>
        </div>
    </div>

    <%-- TABLE DES PENALITÉS PAR ADHÉRANT (GARDÉE) --%>
    <% if (stat_user != null && !stat_user.isEmpty()) { %>
        <table class="table table-sm table-bordered table-striped">
            <thead class="thead-light">
                <tr>
                    <th>adherantId</th>
                    <th>nomComplet</th>
                    <th>categorieAdherant</th>
                    <th>nbrPenalites</th>
                    <th>quotaMax</th>
                    <th>nbrPretsEnCours</th>
                    <th>nbrPretDispo</th>
                </tr>
            </thead>
            <tbody>
                <% for (StatPenaliteAdherantView ex : stat_user) { %>
                    <tr>
                        <td><%= ex.getAdherantId() %></td>
                        <td><%= ex.getNomComplet() %></td>
                        <td><%= ex.getCategorieAdherant() %></td>
                        <td><%= ex.getNbrPenalites() %></td>
                        <td><%= ex.getQuotaMax() %></td>
                        <td><%= ex.getNbrPretsEnCours() %></td>
                        <td><%= ex.getNbrPretDispo() %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else { %>
        <div class="alert alert-info">
            Aucune donnée trouvée pour les pénalités.
        </div>
    <% } %>

    <%-- CHART DES EXEMPLAIRES EMPRUNTÉS --%>
    <% if (exemplaires != null && !exemplaires.isEmpty()) { %>
        <h5 class="mt-5">Nombre d'emprunts par exemplaire</h5>
        <canvas id="exemplaireChart" height="100"></canvas>
    <% } else { %>
        <div class="alert alert-info">
            Aucune donnée trouvée pour les emprunts.
        </div>
    <% } %>
</div>

<script>
    <% if (exemplaires != null && !exemplaires.isEmpty()) { %>
        const labels = [
            <% for (VExemplaireEmprunt ex : exemplaires) { %>
                "Ex.<%= ex.getId_exemplaire() %>",
            <% } %>
        ];

        const data = [
            <% for (VExemplaireEmprunt ex : exemplaires) { %>
                <%= ex.getNbr_emprunt() %>,
            <% } %>
        ];

        const ctx = document.getElementById('exemplaireChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Nombre d\'emprunts',
                    data: data,
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                }
            }
        });
    <% } %>
</script>

<section class="px-6 py-8 max-w-3xl mx-auto">
  <div class="bg-white shadow-md rounded-lg p-6">

    <form action="ajouter-ticket" method="post" class="space-y-6">

      <!-- Titre -->
      <div>
        <label for="titre" class="block text-sm font-medium text-gray-700">Titre du ticket</label>
        <input type="text" id="titre" name="titre" required
               class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
      </div>

      <!-- Description -->
      <div>
        <label for="description" class="block text-sm font-medium text-gray-700">Description</label>
        <textarea id="description" name="description" rows="4" required
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>
      </div>

      <!-- Priorité -->
      <div>
        <label for="priorite" class="block text-sm font-medium text-gray-700">Priorite</label>
        <select id="priorite" name="priorite" required
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
          <option value="Basse">Basse</option>
          <option value="Moyenne">Moyenne</option>
          <option value="Élevée">Élevée</option>
        </select>
      </div>

      <!-- Statut -->
      <div>
        <label for="statut" class="block text-sm font-medium text-gray-700">Statut</label>
        <select id="statut" name="statut" required
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500">
          <option value="Nouveau">Nouveau</option>
          <option value="En cours">En cours</option>
          <option value="Résolu">Résolu</option>
        </select>
      </div>

      <!-- Client (optionnel si c’est prérempli) -->
      <div>
        <label for="client" class="block text-sm font-medium text-gray-700">Client</label>
        <input type="text" id="client" name="client" value="Jean Dupont"
               class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
      </div>

      <!-- Soumettre -->
      <div class="text-right">
        <button type="submit"
                class="bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700 transition">
          Creer le ticket
        </button>
      </div>

    </form>
  </div>
</section>

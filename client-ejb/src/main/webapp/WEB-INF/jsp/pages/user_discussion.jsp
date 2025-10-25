<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="bg-white rounded-lg shadow-md p-20 h-full flex flex-col">
  
  <div class="flex flex-col flex-1">
   
    
    <div class="flex-1 flex flex-col">
      <!-- Messages -->
      <div class="flex-1 overflow-y-auto mb-4 space-y-4">
        <div class="flex items-start">
          <div class="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center mr-3">
            <span class="text-green-600 font-medium">MS</span>
          </div>
          <div>
            <p class="font-medium text-gray-800 mb-1">Marie Smith</p>
            <div class="max-w-xs lg:max-w-md bg-gray-100 rounded-lg p-3">
              <p class="text-gray-800">Bonjour, je rencontre un problème avec ma commande #12345. Le statut indique "livré" mais je n'ai rien reçu.</p>
              <p class="text-xs text-gray-500 mt-1">10:24</p>
            </div>
          </div>
        </div>
        
        <!-- Message de l'agent -->
        <div class="flex items-start justify-end">
          <div class="text-right mr-3">
            <p class="font-medium text-gray-800 mb-1">Vous</p>
            <div class="max-w-xs lg:max-w-md bg-blue-500 text-white rounded-lg p-3">
              <p>Bonjour Marie, je suis désolé pour ce désagrément. Je vais vérifier immédiatement le statut de votre commande avec notre service logistique.</p>
              <p class="text-xs text-blue-100 mt-1">10:30</p>
            </div>
          </div>
          <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center">
            <span class="text-blue-600 font-medium">VS</span>
          </div>
        </div>
        
        <!-- Message de l'agent -->
        <div class="flex items-start justify-end">
          <div class="text-right mr-3">
            <p class="font-medium text-gray-800 mb-1">Vous</p>
            <div class="max-w-xs lg:max-w-md bg-blue-500 text-white rounded-lg p-3">
              <p>Pourriez-vous me confirmer votre adresse de livraison? Je veux m'assurer qu'il n'y a pas eu d'erreur.</p>
              <p class="text-xs text-blue-100 mt-1">10:31</p>
            </div>
          </div>
          <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center">
            <span class="text-blue-600 font-medium">VS</span>
          </div>
        </div>
        
        <!-- Message du client -->
        <div class="flex items-start">
          <div class="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center mr-3">
            <span class="text-green-600 font-medium">MS</span>
          </div>
          <div>
            <p class="font-medium text-gray-800 mb-1">Marie Smith</p>
            <div class="max-w-xs lg:max-w-md bg-gray-100 rounded-lg p-3">
              <p class="text-gray-800">Bien sûr, c'est 12 Rue des Fleurs, 75001 Paris.</p>
              <p class="text-xs text-gray-500 mt-1">10:35</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Zone de saisie -->
      <div class="border-t pt-4">
        <form class="flex items-center">
          <input type="text" placeholder="Écrivez votre message..." class="flex-1 border border-gray-300 rounded-l-lg py-2 px-4 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
          <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-r-lg transition duration-200">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-8.707l-3-3a1 1 0 00-1.414 1.414L10.586 9H7a1 1 0 100 2h3.586l-1.293 1.293a1 1 0 101.414 1.414l3-3a1 1 0 000-1.414z" clip-rule="evenodd" />
            </svg>
          </button>
        </form>
      </div>
    </div>
  </div>
</div>
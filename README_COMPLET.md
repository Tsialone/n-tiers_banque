# 🏦 N-TIERS BANQUE - Simulation d'Application Bancaire Multi-Services

> **Projet académique** : Simulation complète d'une application bancaire multi-tiers démontrant une architecture distribuée avec gestion de comptes, transactions, épargne et emprunts.

---

## 📋 Table des Matières

1. [Vue d'ensemble](#vue-densemble)
2. [Architecture Globale](#architecture-globale)
3. [Services Principaux](#services-principaux)
4. [Structure Technique](#structure-technique)
5. [Modèles de Données](#modèles-de-données)
6. [Workflow et Flux Opérationnels](#workflow-et-flux-opérationnels)
7. [API Endpoints](#api-endpoints)
8. [Installation et Déploiement](#installation-et-déploiement)
9. [Utilisation](#utilisation)
10. [Technologies et Stack](#technologies-et-stack)

---

## 🎯 Vue d'Ensemble

Ce projet simule une **plateforme bancaire complète** organisée en trois services principaux:

| Service | Port | Technologie | Base de Données |
|---------|------|-------------|-----------------|
| **Compte Courant** (EJB) | 9090 | Java Jakarta EE / WildFly | PostgreSQL (port 5434) |
| **Épargne** | 6000 | .NET 8 / C# / ASP.NET | PostgreSQL (port 5432) |
| **Prêt** | 5000 | .NET 8 / C# / ASP.NET | PostgreSQL (port 5433) |
| **Gestion Devises** (Change) | 9090 | Java Jakarta EE / WildFly | Collections en mémoire |

### Fonctionnalités Principales

✅ **Gestion de Comptes Courants**
- Création/modification de comptes
- Gestion des clients et des rôles (validation, consultation, insertion)
- Directions et hiérarchies de clients
- Historiques des opérations

✅ **Gestion de l'Épargne**
- Comptes d'épargne avec taux d'intérêt
- Dépôts et retraits
- Calcul automatique des intérêts
- Suivi des transactions

✅ **Gestion des Prêts**
- Création de contrats de prêt
- Calcul automatique des amortissements
- Versements et remboursements
- Suivi du statut des prêts

✅ **Services Transversaux**
- Conversion de devises
- Virements entre comptes
- Validation multi-niveaux des opérations
- Intégration inter-services via APIs

---

## 🏗️ Architecture Globale

### Diagramme Architecture Générale

a remplire

### Architecture Modulaire (Maven)

```
n-tiers_banque/
├── change-api/           (Interfaces EJB Remote + DTOs pour Change)
├── change-ejb/           (Service Change - EJB Singleton + REST endpoints)
├── server-api/           (Interfaces EJB Remote du compte courant)
├── server-ejb/           (Service EJB compte courant)
├── client-ejb/           (Client EJB)
├── epargne/              (Service microservice .NET)
├── pret/                 (Service microservice .NET)
├── sql/                  (Schémas BD)
│   ├── courant/
│   ├── epargne/
│   └── pret/
└── n-tiers api/          (Collections Postman/Bruno)
```

---

## 🔧 Services Principaux

### 1️⃣ Service Compte Courant (EJB) - Port 8080

**Description**: Gère les comptes courants classiques avec validation multi-niveaux

**Responsabilités**:
- Gestion des clients et authentification
- Gestion des comptes courants
- Dépôts, retraits et virements
- Frais bancaires
- Validation des opérations
- Historiques détaillés

**Packages Java** (`server-ejb`):
```
com.example/
├── models/            (45+ entités JPA)
│   ├── ClientCourant, CompteCourant, TransactionCourant
│   ├── Depot, Retrait, Virement
│   ├── ValidationDepot, ValidationRetrait, ValidationVirement
│   └── Direction, Role, Action...
├── repositories/      (18+ DAOs)
│   └── Interfaces pour accès données
├── service/           (15+ services métier)
│   ├── ClientCourantService
│   ├── DepotService, RetraitService, VirementService
│   ├── ValidationService, ValidationDepotService...
│   └── CompteCourantService
└── mappers/           (17+ mappers DTO)
    └── Conversion entité <-> DTO
```

**Framework**: Jakarta EE 9.1 / WildFly 19.0.1

---

### 2️⃣ Service Épargne (.NET) - Port 6000

**Description**: Gère les comptes d'épargne avec intérêts

**Responsabilités**:
- Création de comptes d'épargne
- Calcul des intérêts
- Dépôts et retraits contrôlés
- Transactions d'épargne
- Intégration API avec compte courant

**Structure C#** (`epargne/`):
```
Controllers/
├── CompteEpargneController      (CRUD comptes)
├── TransactionEpargneController (Transactions)
└── PersonController             (Clients)

Models/
├── CompteEpargne      (Compte d'épargne)
├── TransactionEpargne (Historique transactions)
└── Person             (Client épargne)

Services/
├── CompteEpargneService           (Logique métier)
├── TransactionEpargneService      (Transactions)
├── PersonService                  (Gestion clients)
└── CompteCourantApiClient         (Intégration EJB)

DTO/
├── CompteEpargneDto
├── TransactionEpargneDto
└── CompteEpargneCreateDto
```

**Framework**: ASP.NET Core 8 / Entity Framework Core / PostgreSQL

---

### 3️⃣ Service Prêt (.NET) - Port 5000

**Description**: Gère les contrats de prêt avec amortissement

**Responsabilités**:
- Création de forfaits de prêt
- Calcul automatique des amortissements
- Versement du prêt
- Suivi des remboursements
- Intégration avec compte courant

**Structure C#** (`pret/`):
```
Controllers/
├── ComptePretController        (Gestion prêts)
├── AmortissementController     (Amortissements)
└── TransactionPretController   (Transactions)

Models/
├── ComptePret       (Contrat de prêt)
├── Amortissement    (Table amortissement)
└── TransactionPret  (Mouvements de prêt)

Services/
├── ComptePretService           (Logique prêt)
├── AmortissementService        (Calcul amortissements)
├── TransactionPretService      (Transactions)
└── CompteCourantApiClient      (Intégration EJB)

DTO/
├── ComptePretDto
├── AmortissementDto
└── TransactionPretDto
```

**Framework**: ASP.NET Core 8 / Entity Framework Core / PostgreSQL

---

### 4️⃣ Service Change (Devises) - Port 9090

**Description**: Gère les devises avec chargement depuis fichier JSON en EJB Singleton

**Architecture**:
- **change-api** (JAR): Interfaces EJB Remote + DTOs
  - `ChangeServiceRemote`: Interface EJB pour accès distant
  - `DeviseDto`: Modèle devise (id, libelle, dateDebut, dateFin, arriary, valide, dateValidation)

- **change-ejb** (WAR): Implémentation avec fichier JSON
  - `ChangeService`: EJB Singleton @Startup qui charge `/opt/devises.json` en mémoire au démarrage
  - `ChangeRestController`: Endpoints REST (@Path("/devises"))
  - `devises.json`: Fichier JSON avec données initiales

**Responsabilités**:
- Chargement des devises depuis `devises.json` au démarrage du conteneur
- Stockage en mémoire via EJB Singleton (persiste pendant runtime)
- Gestion des devises: validation, annulation, modifications
- Historique des modifications (dateValidation, états)
- REST API pour accès aux devises

**Endpoints REST** (`/change-ejb/api/devises`):
```http
GET    /api/devises              → Toutes devises
GET    /api/devises/{id}         → Devise par ID
POST   /api/devises              → Ajouter devise
PUT    /api/devises/{id}         → Modifier devise
DELETE /api/devises/{id}         → Supprimer devise
```

**Format JSON** (devises.json):
```json
[
  {
    "id": 1,
    "libelle": "USD",
    "dateDebut": "2025-11-06",
    "dateFin": "2025-11-07",
    "arriary": 4500,
    "valide": true,
    "dateValidation": "2024-06-30"
  }
]
```

**Framework**: Jakarta EE 9.1 / WildFly 19.0.1 / JAX-RS / EJB Singleton @Startup

---

## 📊 Structure Technique

### 🗂️ Arborescence Complète

```
n-tiers_banque/
│
├── 📄 pom.xml                 (POM parent Maven)
├── 📄 n-tiers.sln             (Solution Visual Studio)
├── 🐳 docker-compose.yml      (Orchestration conteneurs)
│
├── 🔵 server-ejb/             (Compte courant - EJB)
│   ├── pom.xml
│   ├── Dockerfile
│   ├── module.xml
│   └── src/main/java/
│       └── com/example/
│           ├── models/        (45+ entités)
│           ├── repositories/  (18+ DAOs)
│           ├── service/       (15+ services)
│           └── mappers/       (17+ mappers)
│
├── 🟢 epargne/                (Service Épargne - .NET)
│   ├── epargne.csproj
│   ├── Dockerfile
│   ├── Program.cs             (Configuration)
│   ├── Controllers/           (3 contrôleurs)
│   ├── Services/              (Services métier)
│   ├── Models/                (Modèles BD)
│   ├── DTO/                   (DTOs API)
│   ├── ExternalApi/           (Clients HTTP)
│   ├── Mappers/               (Mappers)
│   └── Data/
│       └── AppDbContext.cs    (DbContext EF Core)
│
├── 🟡 pret/                   (Service Prêt - .NET)
│   ├── pret.csproj
│   ├── Dockerfile
│   ├── Program.cs             (Configuration)
│   ├── Controllers/           (3 contrôleurs)
│   ├── Services/              (Services métier)
│   ├── Models/                (Modèles BD)
│   ├── DTO/                   (DTOs API)
│   ├── ExternalApi/           (Clients HTTP)
│   ├── Mappers/               (Mappers)
│   └── Data/
│       └── AppDbContext.cs    (DbContext EF Core)
│
├── � change-api/             (Interfaces & DTOs - Change)
│   ├── pom.xml
│   └── src/main/java/com/example/
│       ├── remotes/
│       │   └── ChangeServiceRemote.java    (Interface EJB Remote)
│       ├── change_dtos/
│       │   └── DeviseDto.java              (DTO Devise)
│       └── utils/
│           └── DateUtils.java
│
├── 🟣 change-ejb/             (Implémentation Change - EJB)
│   ├── pom.xml
│   ├── Dockerfile
│   ├── devises.json           (Fichier données devises JSON)
│   └── src/main/java/com/example/
│       ├── services/
│       │   └── ChangeService.java          (EJB Singleton @Startup)
│       ├── controllers/
│       │   └── ChangeRestController.java   (REST endpoints @Path)
│       └── api/
│           └── RestApplication.java        (Config JAX-RS)
│
├── 🗄️ sql/                     (Schémas BD initiaux)
│   ├── courant/
│   │   └── compte_courant.sql (Tables + données test)
│   ├── epargne/
│   │   └── compte_epargne.sql (Tables + données test)
│   └── pret/
│       └── compte_pret.sql    (Tables + données test)
│
├── 📚 n-tiers api/            (Collections API)
│   ├── bruno.json
│   ├── collection.bru
│   ├── central/               (Endpoints EJB)
│   ├── compte_courant/
│   ├── epargne/
│   └── pret/                  (Endpoints prêt)
│
├── 🔨 Scripts de déploiement
│   ├── create.sh              (Créer images)
│   ├── deploy.sh              (Déployer)
│   ├── run.sh                 (Lancer conteneurs)
│   ├── restart.sh             (Redémarrer)
│   └── stop.sh                (Arrêter)
│
└── 📖 Documentation
    └── README.md / README_COMPLET.md
```

---

## 📈 Modèles de Données

### 1. Schéma Compte Courant (PostgreSQL - ejb_db)

![Schéma Compte Courant ERD](docs/images/compte_pret.jpg)

**Données d'exemple**: 
- 1 client par direction
- 3-4 comptes par client
- 5+ transactions par compte

---

### 2. Schéma Épargne (PostgreSQL - epargne_db)

![Schéma Épargne ERD](docs/images/epargne.jpg)

**Données d'exemple**:
- 4 comptes d'épargne (vacance, moto, rancard, ferrari)
- Capital initial: 200 à 20 000 EUR
- Taux intérêt: 2.75% à 4.00%
- 5 transactions test

---

### 3. Schéma Prêt (PostgreSQL - pret_db)

![Schéma Prêt ERD](docs/images/pret.jpg)

**Données d'exemple**:
- 1 prêt immobilier: 1000 EUR
- Taux: 10% annuel
- Durée: 4 mois
- Amortissements calculés pour 4 mois: 260 EUR/mois

---

## 🔄 Workflow et Flux Opérationnels

### Flux 1️⃣: Création Compte et Dépôt Épargne

```mermaid
sequenceDiagram
    participant Client as 👤 Client/Postman
    participant Epargne as 🟢 Service Épargne
    participant DB_Epargne as 🗄️ PostgreSQL Epargne
    participant EJB as 🔵 Service EJB

    Client->>Epargne: 1. POST /api/comptes-epargne/create
    activate Epargne
    Epargne->>Epargne: Valider données
    Epargne->>DB_Epargne: INSERT compte_epargne
    activate DB_Epargne
    DB_Epargne-->>Epargne: Retour ID compte
    deactivate DB_Epargne
    
    Epargne->>Epargne: Créer transaction
    Epargne->>DB_Epargne: INSERT transaction (credit)
    activate DB_Epargne
    DB_Epargne-->>Epargne: ✅ OK
    deactivate DB_Epargne
    
    Epargne->>EJB: 2. Appel HTTP (notifier)
    activate EJB
    EJB-->>Epargne: Accusé réception
    deactivate EJB
    
    Epargne-->>Client: { id: 1, capital: 1000, taux: 3.5 }
    deactivate Epargne
```

**Étapes**:
1. Validation des données du compte
2. Création compte avec capital initial
3. Insertion transaction de dépôt
4. Appel API vers EJB pour notification
5. Retour JSON au client

**Ports**: Port 6000 (Épargne) → Port 9090 (EJB)

---

### Flux 2️⃣: Retrait Épargne avec Limite

```mermaid
sequenceDiagram
    participant Client as 👤 Client/Postman
    participant Epargne as 🟢 Service Épargne
    participant DB_Epargne as 🗄️ PostgreSQL Epargne

    Client->>Epargne: DELETE /api/comptes-epargne/{id}?montant=X
    activate Epargne
    
    Epargne->>DB_Epargne: SELECT compte WHERE id={id}
    activate DB_Epargne
    DB_Epargne-->>Epargne: { capital: 1000, retrait: 50 }
    deactivate DB_Epargne
    
    alt Montant ≤ Limite Retrait
        Epargne->>Epargne: Valider montant
        Epargne->>DB_Epargne: UPDATE capital - montant
        Epargne->>DB_Epargne: INSERT transaction (debit)
        Epargne-->>Client: ✅ Retrait effectué
    else Montant > Limite
        Epargne-->>Client: ❌ Erreur: Dépassement limite
    end
    
    deactivate Epargne
```

**Règles**:
- Limite de retrait: 50 EUR par défaut
- Solde ne peut pas devenir négatif
- Chaque retrait crée une transaction

---

### Flux 3️⃣: Emprunt Prêt avec Amortissement

```mermaid
sequenceDiagram
    participant Client as 👤 Client/Postman
    participant Pret as 🟡 Service Prêt
    participant DB_Pret as 🗄️ PostgreSQL Prêt
    participant Amort as 📊 Service Amortissement
    participant EJB as 🔵 Service EJB

    Client->>Pret: 1. POST /api/comptes-pret/create
    activate Pret
    Note over Pret: Données: capital=1000, taux=10%, durée=4 mois
    
    Pret->>Amort: 2. Calculer amortissements
    activate Amort
    Amort-->>Amort: Générer table amortissements
    Amort-->>Pret: [Mois1..Mois4] avec mensualités
    deactivate Amort
    
    Pret->>DB_Pret: 3. INSERT compte_pret
    Pret->>DB_Pret: 4. INSERT amortissements (4 lignes)
    activate DB_Pret
    DB_Pret-->>Pret: ✅ IDs créés
    deactivate DB_Pret
    
    Pret->>DB_Pret: 5. INSERT transaction (décaissement)
    activate DB_Pret
    DB_Pret-->>Pret: ✅ OK
    deactivate DB_Pret
    
    Pret->>EJB: 6. Appel HTTP (débiter compte courant)
    activate EJB
    EJB-->>Pret: Montant débité
    deactivate EJB
    
    Pret-->>Client: { id: 1, capital: 1000, amortissements: [...] }
    deactivate Pret
```

**Étapes**:
1. Création contrat prêt
2. Calcul amortissements (table d'amortissement)
3. Insertion compte + amortissements BD
4. Insertion transaction décaissement
5. Débit du compte courant via API EJB
6. Retour des détails prêt

**Formules**:
- Mensualité = Capital × [r(1+r)^n] / [(1+r)^n - 1]
- r = taux mensuel = taux annuel / 12
- n = nombre de mois

---

### Flux 4️⃣: Remboursement Prêt Mensuel

```mermaid
sequenceDiagram
    participant Client as 👤 Client/Postman
    participant Pret as 🟡 Service Prêt
    participant DB_Pret as 🗄️ PostgreSQL Prêt

    Client->>Pret: PUT /api/amortissements/{id}/payer
    activate Pret
    
    Pret->>DB_Pret: SELECT amortissement WHERE id={id}
    activate DB_Pret
    DB_Pret-->>Pret: { mois: 1, mensualite: 260 }
    deactivate DB_Pret
    
    alt Statut = en_attente
        Pret->>Pret: Valider montant = mensualité
        Pret->>DB_Pret: UPDATE amortissement statut = payee
        Pret->>DB_Pret: INSERT transaction (remboursement)
        Pret-->>Client: ✅ Mois X remboursé
    else Statut = payee
        Pret-->>Client: ⚠️ Déjà remboursé
    end
    
    deactivate Pret
```

**Règles**:
- Chaque échéance peut être payée une fois
- Montant = exactement la mensualité
- Historique des remboursements conservé

---

### Flux 5️⃣: Virement Inter-Comptes Courant

```mermaid
sequenceDiagram
    participant Client as 👤 Client/Postman
    participant EJB as 🔵 Service EJB
    participant DB_EJB as 🗄️ PostgreSQL EJB

    Client->>EJB: POST /api/virements/creer
    activate EJB
    Note over EJB: compteSource, compteDest, montant, libelle
    
    EJB->>EJB: Valider comptes existence
    EJB->>EJB: Valider solde suffisant
    
    alt Validation OK
        EJB->>DB_EJB: UPDATE source.solde -= montant
        EJB->>DB_EJB: UPDATE dest.solde += montant
        EJB->>DB_EJB: INSERT virement
        EJB->>DB_EJB: INSERT 2 transactions
        EJB-->>Client: ✅ Virement effectué
    else Erreur
        EJB-->>Client: ❌ Erreur validation
    end
    
    deactivate EJB
```

---

## 📡 API Endpoints

### A. Service Compte Courant (EJB) - Port 9090

#### Gestion Clients
```http
GET    /server-ejb/api/clients                    → Liste tous clients
GET    /server-ejb/api/clients/{id}               → Client par ID
POST   /server-ejb/api/clients                    → Créer client
PUT    /server-ejb/api/clients/{id}               → Modifier client
DELETE /server-ejb/api/clients/{id}               → Supprimer client
```

#### Gestion Comptes Courant
```http
GET    /server-ejb/api/comptes-courants           → Tous comptes
GET    /server-ejb/api/comptes-courants/{id}      → Compte par ID
POST   /server-ejb/api/comptes-courants           → Créer compte
PUT    /server-ejb/api/comptes-courants/{id}      → Modifier compte
DELETE /server-ejb/api/comptes-courants/{id}      → Supprimer compte
```

#### Dépôts & Retraits
```http
POST   /server-ejb/api/depots                     → Effectuer dépôt
POST   /server-ejb/api/retraits                   → Effectuer retrait
GET    /server-ejb/api/historique-depots/{id}    → Historique dépôts
GET    /server-ejb/api/historique-retraits/{id}  → Historique retraits
```

#### Virements
```http
POST   /server-ejb/api/virements/creer            → Créer virement
GET    /server-ejb/api/virements/{id}             → Détails virement
GET    /server-ejb/api/historique-virements/{id} → Historique virements
```

---

### B. Service Change (Devises) - Port 9090

#### Gestion Devises
```http
GET    /change-ejb/api/devises                    → Toutes devises
GET    /change-ejb/api/devises/{id}               → Devise par ID
POST   /change-ejb/api/devises                    → Ajouter devise
PUT    /change-ejb/api/devises/{id}               → Modifier devise
DELETE /change-ejb/api/devises/{id}               → Supprimer devise
```

**Données**: Chargées depuis `/opt/devises.json` au démarrage du service
**Type**: EJB Singleton REST via JAX-RS

---

### C. Service Épargne (.NET) - Port 6000

#### Gestion Comptes Épargne
```http
GET    /api/comptes-epargne                       → Tous comptes épargne
GET    /api/comptes-epargne/{id}                  → Compte par ID
POST   /api/comptes-epargne                       → Créer compte
POST   /api/comptes-epargne/create                → Créer avec transaction
PUT    /api/comptes-epargne/{id}                  → Modifier compte
DELETE /api/comptes-epargne/{id}                  → Retrait/Suppression
```

#### Transactions Épargne
```http
GET    /api/transactions-epargne/{idCompte}      → Historique compte
POST   /api/transactions-epargne                  → Créer transaction
GET    /api/transactions-epargne/{id}             → Détails transaction
```

#### Gestion Clients
```http
GET    /api/persons                               → Liste clients
POST   /api/persons                               → Créer client
GET    /api/persons/{id}                          → Client par ID
```

---

### C. Service Prêt (.NET) - Port 5000

#### Gestion Prêts
```http
GET    /api/comptes-pret                          → Tous prêts
GET    /api/comptes-pret/{id}                     → Prêt par ID
POST   /api/comptes-pret                          → Créer prêt
PUT    /api/comptes-pret/{id}                     → Modifier
DELETE /api/comptes-pret/{id}                     → Supprimer
```

#### Amortissements
```http
GET    /api/amortissements/{idPret}               → Table amortissement
GET    /api/amortissements/{id}                   → Ligne amortissement
PUT    /api/amortissements/{id}/payer             → Payer échéance
GET    /api/amortissements/{id}/statut            → Statut remboursement
```

#### Transactions Prêt
```http
GET    /api/transactions-pret/{idPret}            → Historique
POST   /api/transactions-pret                     → Enregistrer transaction
```

---

## 🚀 Installation et Déploiement

### Prérequis

| Outil | Version | Utilisé pour |
|-------|---------|-------------|
| **Docker** | 20.10+ | Conteneurisation et orchestration |
| **Docker Compose** | 2.0+ | Orchestration multi-conteneurs |
| **Git** | 2.30+ | Cloner le projet |

**Note**: Les autres outils (Java, .NET, Maven, PostgreSQL) sont inclus dans les conteneurs Docker.

> **Si tu n'as pas WildFly installé localement**: Le serveur WildFly Core 19.0.1.Final est automatiquement inclus dans le conteneur `change-server`. Pas besoin de l'installer si tu utilises Docker!
> 
> Si tu as besoin de WildFly en standalone: [Télécharger WildFly Core 19.0.1.Final](https://www.wildfly.org/downloads/)

---

### Déploiement Rapide avec Docker (Seule option recommandée)

#### Étape 1: Cloner le projet
```bash
git clone <repository-url>
cd n-tiers_banque
```

#### Étape 2: Lancer le déploiement
```bash
# Option A: Exécuter le script de déploiement
./deploy.sh

# Option B: Lancer directement avec Docker Compose
docker-compose up -d
```

**Services démarrés automatiquement**:
- ✅ WildFly (EJB) - Change + Server - Port 9090
- ✅ Service Épargne (.NET) - Port 6000
- ✅ Service Prêt (.NET) - Port 5000
- ✅ PostgreSQL (Compte Courant) - Port 5434
- ✅ PostgreSQL (Épargne) - Port 5432
- ✅ PostgreSQL (Prêt) - Port 5433

#### Étape 3: Vérifier le déploiement
```bash
# Voir les conteneurs actifs
docker ps

# Voir les logs d'un service
docker logs change-server    # WildFly + Change + Server
docker logs epargne          # Service Épargne
docker logs pret             # Service Prêt
docker logs postgres-ejb     # BD Courant

# Voir les logs en temps réel
docker-compose logs -f
docker-compose logs -f epargne
docker-compose logs -f pret
```


## 💻 Utilisation

### 1️⃣ Collection Postman/Bruno

Des collections API sont disponibles dans `n-tiers api/`:

```bash
# Ouvrir dans Bruno (alternative Postman)
bruno collection.bru

# Ou importer dans Postman
# Fichier: n-tiers api/collection.bru
```

**Collections disponibles**:
- `central/` → Endpoints EJB
- `epargne/` → Endpoints Épargne
- `pret/` → Endpoints Prêt

---

### 2️⃣ Scénarios de Test Complet sur Interface Web (client-ejb)

**URL Base**: `http://localhost:9090/client-ejb/`

#### 🔐 Scénario 0: Se Connecter (Login)

1. Accéder à: `http://localhost:9090/client-ejb/login`
2. **Email**: `@gmail` (pré-rempli)
3. **Mot de passe**: `1234` (pré-rempli)
4. Cliquer sur **"Se connecter"**
5. Redirection vers la page d'accueil

**Screenshot 1 - Page de Login**:

![Page de Login](docs/images/login.jpg)

**Screenshot 2 - Page d'Accueil (Après Login)**:

![Page d'Accueil](docs/images/home.jpg)

---

#### 💰 Scénario 1: Effectuer un Dépôt sur Compte Courant

**Étapes**:

1. Se connecter (voir Scénario 0)
2. Accéder à: `/courants/depot_liste.jsp`
3. Voir la liste des dépôts existants
4. Cliquer sur **"Nouveau Dépôt"** ou accéder `/courants/depot_form.jsp`
5. Remplir le formulaire:
   - **Compte**: Sélectionner un compte dans la liste
   - **Montant**: Entrer montant (ex: 500.00)
   - **Description**: Entrer description (ex: "Salaire mois de Janvier")
6. Cliquer **"Valider"**
7. Confirmation du dépôt avec numéro de transaction

**Screenshots à capturer**:

**Screenshot 3 - Liste des Dépôts**:

![Liste des Dépôts](docs/images/faire_depot.jpg)

**Screenshot 4 - Formulaire Dépôt**:

![Formulaire Dépôt](docs/images/depot_form.jpg)

**Screenshot 5 - Confirmation Dépôt**:

![Confirmation Dépôt](docs/images/depot_confirmation.jpg)

---

#### 🏧 Scénario 2: Effectuer un Retrait

**Étapes**:

1. Depuis la page d'accueil, accéder à: `/courants/retrait_liste.jsp`
2. Voir la liste des retraits existants
3. Cliquer sur **"Nouveau Retrait"** ou accéder `/courants/retrait_form.jsp`
4. Remplir le formulaire:
   - **Compte**: Sélectionner un compte courant
   - **Montant**: Entrer le montant (ex: 200.00)
   - **Description**: Raison du retrait
5. Cliquer **"Valider"**
6. Le système valide le solde suffisant
7. Confirmation avec mise à jour du solde

**Screenshots à capturer**:

**Screenshot 6 - Liste Retraits**:

![Liste Retraits](docs/images/retrait_liste.jpg)

**Screenshot 7 - Formulaire Retrait**:

![Formulaire Retrait](docs/images/retrait_form.jpg)

**Screenshot 8 - Confirmation Retrait**:

![Confirmation Retrait](docs/images/retrait_confirmation.jpg)

---

#### 💸 Scénario 3: Effectuer un Virement Inter-Comptes

**Étapes**:

1. Accéder à: `/courants/virement_liste.jsp`
2. Voir l'historique des virements
3. Cliquer **"Nouveau Virement"** ou accéder `/courants/virement_courant_form.jsp`
4. Remplir le formulaire:
   - **Compte Source**: Sélectionner compte à débiter
   - **Compte Destination**: Sélectionner compte à créditer
   - **Montant**: Montant à virer (ex: 150.00)
   - **Description**: Motif du virement
5. Cliquer **"Valider"**
6. Confirmation avec mise à jour des deux comptes

**Screenshots à capturer**:

**Screenshot 9 - Liste Virements**:

![Liste Virements](docs/images/virement_liste.jpg)

**Screenshot 10 - Formulaire Virement**:

![Formulaire Virement](docs/images/virement_form.jpg)

**Screenshot 11 - Confirmation Virement**:

![Confirmation Virement](docs/images/virement_confirmation.jpg)

---

#### 📊 Scénario 4: Consulter Transactions d'un Compte

**Étapes**:

1. Accéder à: `/courants/transaction_courant_liste.jsp`
2. Optionnel: Filtrer par compte (dropdown)
3. Voir le tableau complet avec toutes les transactions:
   - Dépôts (sens: Crédit)
   - Retraits (sens: Débit)
   - Virements (Sortie/Entrée)
4. Consulter détails: Date, Montant, Type, Description, Ancien/Nouveau Solde

**Screenshots à capturer**:

**Screenshot 12 - Liste Transactions**:

![Liste Transactions](docs/images/transaction_courant_liste.jpg)

**Screenshot 13 - Détail Transaction**:

![Détail Transaction](docs/images/transaction_courant_detail.jpg)

---

#### 💱 Scénario 5: Gérer les Devises

**Étapes**:

1. Accéder à: `/devises/devise_liste.jsp`
2. Voir la liste des devises disponibles:
   - USD, EUR, GBP, JPY, etc.
   - Colonnes: Code, Nom, Taux de Change, Date de Validation
3. Cliquer **"Ajouter Devise"** ou accéder `/devises/devise_form.jsp`
4. Remplir le formulaire:
   - **Code**: Code devise (ex: CHF)
   - **Nom**: Nom complet (ex: Franc Suisse)
   - **Taux**: Valeur en Ariary (ex: 4200.50)
   - **Date Début**: Date à partir de laquelle valide
5. Cliquer **"Enregistrer"**
6. Confirmation d'ajout avec apparition dans la liste

**Screenshots à capturer**:

**Screenshot 14 - Liste Devises**:
```
[À AJOUTER: Capture de devise_liste.jsp]
- Tableau des devises
- Colonnes: Code, Libelle, Arriary (Taux), Valide, Date Validation
- Bouton "Ajouter Devise"
- Au moins 5 devises listées
```

**Screenshot 15 - Formulaire Ajout Devise**:
```
[À AJOUTER: Capture de devise_form.jsp]
- Champs: Code (CHF), Libelle, Arriary, Date Début, Date Fin
- Case à cocher "Valide"
- Bouton "Enregistrer"
```

**Screenshot 16 - Confirmation Devise Ajoutée**:
```
[À AJOUTER: Confirmation d'ajout]
- Message: "Devise ajoutée avec succès"
- Nouvelle devise visible dans liste_devise.jsp
```

---

#### 📋 Scénario 6: Créer un Nouveau Prêt

**Étapes**:

1. Accéder à: `/create/form_pret.jsp`
2. Remplir le formulaire de création de prêt:
   - **Client**: Sélectionner client
   - **Montant Emprunté**: Capital (ex: 5000.00)
   - **Taux d'Intérêt Annuel**: % (ex: 8.5)
   - **Durée en Mois**: Durée remboursement (ex: 24)
   - **Description**: Motif du prêt (ex: "Immobilier")
3. Cliquer **"Calculer Amortissements"**
4. Voir le tableau d'amortissement avec:
   - Mois 1 à N (mensualités, intérêts, capital, reste dû)
5. Cliquer **"Créer Prêt"**
6. Confirmation avec numéro de contrat

**Screenshots à capturer**:

**Screenshot 17 - Formulaire Création Prêt**:
```
[À AJOUTER: Capture de form_pret.jsp]
- Client: Dropdown avec noms
- Montant: 5000.00
- Taux: 8.5%
- Durée: 24 mois
- Description: "Immobilier"
- Bouton "Calculer Amortissements"
```

**Screenshot 18 - Table Amortissement Calculée**:
```
[À AJOUTER: Tableau d'amortissement généré]
- Colonnes: Mois, Mensualité, Intérêt, Capital, Reste Dû
- Exemple 24 lignes (une par mois)
- Dernier mois: Reste Dû = 0
- Bouton "Créer Prêt"
```

**Screenshot 19 - Confirmation Prêt Créé**:
```
[À AJOUTER: Confirmation de création]
- Message: "Prêt créé avec succès"
- Numéro contrat
- Date de démarrage
- Première échéance prévue
```

---

#### 🔄 Scénario 7 (Optionnel): Rembourser une Échéance de Prêt

**Accès via API** (Collections Bruno/Postman):
1. Dans `n-tiers api/collection.bru`
2. Section "Prêt" → "Payer Amortissement"
3. PUT `/api/amortissements/{idAmortissement}/payer`
4. Le système marque l'échéance comme payée
5. Historique des remboursements mis à jour

**Screenshots à capturer**:

**Screenshot 20 - Remboursement en Cours (via Bruno)**:
```
[À AJOUTER: Capture de Bruno/Postman]
- Endpoint: PUT /api/amortissements/1/payer
- Response: { "statut": "payee", "dateRemboursement": "..." }
```

**Screenshot 21 - Statut Après Remboursement**:
```
[À AJOUTER: État après remboursement]
- Amortissement 1: Statut "Payée"
- Reste à payer: Amortissements 2-24
- Solde du compte courant diminué
```

---

### 📸 Résumé des Screenshots Requis

| # | Scénario | Fichier JSP | Description |
|---|----------|-------------|------------|
| 1 | Login | `login.jsp` | Page de connexion |
| 2 | Home | `home.jsp` | Accueil après login |
| 3 | Dépôt - Liste | `depot_liste.jsp` | Historique dépôts |
| 4 | Dépôt - Formulaire | `depot_form.jsp` | Création dépôt |
| 5 | Dépôt - Confirmation | Message | Succès dépôt |
| 6 | Retrait - Liste | `retrait_liste.jsp` | Historique retraits |
| 7 | Retrait - Formulaire | `retrait_form.jsp` | Création retrait |
| 8 | Retrait - Confirmation | Message | Succès retrait |
| 9 | Virement - Liste | `virement_liste.jsp` | Historique virements |
| 10 | Virement - Formulaire | `virement_courant_form.jsp` | Création virement |
| 11 | Virement - Confirmation | Message | Succès virement |
| 12 | Transactions | `transaction_courant_liste.jsp` | Toutes transactions |
| 13 | Transaction - Détail | Message/Vue | Détails complets |
| 14 | Devises - Liste | `devise_liste.jsp` | Toutes devises |
| 15 | Devises - Formulaire | `devise_form.jsp` | Ajout devise |
| 16 | Devises - Confirmation | Message | Succès ajout |
| 17 | Prêt - Formulaire | `form_pret.jsp` | Création prêt |
| 18 | Prêt - Amortissement | Table HTML | Table amortissement |
| 19 | Prêt - Confirmation | Message | Succès création |
| 20 | Remboursement - API | Bruno/Postman | Requête remboursement |
| 21 | Remboursement - Confirmation | Response JSON | Confirmation paiement |

---

**Instructions pour ajouter les screenshots**:

1. Prendre chaque capture écran en haute définition
2. Redimensionner si nécessaire (max largeur 800px)
3. Sauvegarder en `docs/images/screenshot_XX.png`
4. Remplacer chaque bloc `[À AJOUTER: ...]` par: `![Description](docs/images/screenshot_XX.png)`


## 🛠️ Technologies et Stack

### Backend

| Couche | Technologie | Version | Rôle |
|--------|-------------|---------|------|
| **EJB** | Jakarta EE | 9.1 | Compte courant, devises |
| **.NET** | ASP.NET Core | 8.0 | Épargne, prêts |
| **ORM Java** | Jakarta Persistence | 3.1 | Mapping ORM EJB |
| **ORM .NET** | Entity Framework Core | 8.0 | Mapping ORM .NET |
| **Serveur App** | WildFly | 19.0.1 | Conteneur EJB |

### Données

| Base | Technologie | Port | Utilité |
|------|-------------|------|---------|
| **PostgreSQL** | 16 | 5434 | Compte courant (ejb_db) |
| **PostgreSQL** | 16 | 5432 | Épargne (epargne_db) |
| **PostgreSQL** | 16 | 5433 | Prêt (pret_db) |

### API

| Aspect | Technologie |
|--------|-------------|
| **Style** | REST / OpenAPI |
| **Sérialisation** | JSON |
| **Documentation** | Swagger (intégré) |

### Infrastructure

| Outil | Version | Usage |
|------|---------|-------|
| **Docker** | 20.10+ | Conteneurs |
| **Docker Compose** | 2.0+ | Orchestration |
| **Maven** | 3.8+ | Build Java |
| **.NET SDK** | 8.0+ | Build C# |
| **Git** | 2.30+ | VCS |

### Frameworks & Libraires

**Java**:
- `jakarta.ee:jakarta.ee-api:9.1.0`
- `org.hibernate:hibernate-core`
- `org.hibernate.orm:hibernate-jpa`
- Lombok (annotations)

**.NET**:
- `Microsoft.EntityFrameworkCore`
- `Npgsql.EntityFrameworkCore.PostgreSQL`
- `Swashbuckle.AspNetCore` (Swagger)
- `Microsoft.AspNetCore.OpenApi`

---

## 📚 Structure des Données

### Exemple de JSON Réponse: Compte Épargne

```json
{
  "idCompte": 1,
  "idClient": 1,
  "capitalEpargne": 1000.00,
  "libelle": "Vacances 2026",
  "dateOuverture": "2025-01-15",
  "retrait": 50.00,
  "tauxInteret": 3.50,
  "transactions": [
    {
      "idTransaction": 1,
      "idCompte": 1,
      "dateTransaction": "2025-01-15",
      "libelle": "Dépôt initial",
      "montant": 1000.00,
      "sens": "credit"
    },
    {
      "idTransaction": 2,
      "idCompte": 1,
      "dateTransaction": "2025-02-10",
      "libelle": "Retrait partiel",
      "montant": 200.00,
      "sens": "debit"
    }
  ]
}
```

### Exemple de JSON: Contrat Prêt

```json
{
  "idCompte": 1,
  "idClient": 1,
  "libelle": "Immobilier",
  "dateOuverture": "2026-01-01",
  "capitalEmprunte": 1000.00,
  "tauxInteret": 10.00,
  "dureeMois": 4,
  "dateEcheance": "2026-05-01",
  "statut": "actif",
  "amortissements": [
    {
      "idAmortissement": 1,
      "idCompte": 1,
      "mois": 1,
      "mensualite": 260.00,
      "interet": 8.33,
      "capital": 251.67,
      "resteDu": 748.33,
      "createdAt": "2026-01-01",
      "statut": "en_attente"
    }
  ]
}
```

---

## ⚙️ Configuration et Variables d'Environnement

### Variables Docker (docker-compose.yml)

```yaml
# PostgreSQL Épargne
POSTGRES_DB: epargne_db
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres

# PostgreSQL Prêt
POSTGRES_DB: pret_db
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres

# PostgreSQL EJB
POSTGRES_DB: ejb_db
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres

# Services .NET
ASPNETCORE_ENVIRONMENT: Development
ConnectionStrings__DefaultConnection: Host=postgres-epargne;Database=epargne_db;Username=postgres;Password=postgres
```

### Configuration Épargne (appsettings.json)

```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Host=localhost;Database=epargne_db;Username=postgres;Password=postgres"
  },
  "Logging": {
    "LogLevel": {
      "Default": "Information"
    }
  }
}
```

### Configuration Prêt (appsettings.json)

```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Host=localhost;Database=pret_db;Username=postgres;Password=postgres"
  },
  "Logging": {
    "LogLevel": {
      "Default": "Information"
    }
  }
}
```

---

## 🔍 Dépannage

### ❌ Problème: Conteneur WildFly ne démarre pas

```bash
# Vérifier les logs
docker logs change-server

# Solution: Reconstruire l'image
docker-compose down
docker-compose build --no-cache change-ejb
docker-compose up -d
```

### ❌ Problème: PostgreSQL ne se connecte pas

```bash
# Vérifier la connexion
docker exec -it postgres-epargne psql -U postgres -d epargne_db

# Vérifier les logs
docker logs postgres-epargne
```

### ❌ Problème: Service .NET refuse connexions à BD

```bash
# Vérifier la chaîne de connexion
docker exec -it epargne cat /src/appsettings.json

# Tester la connectivité
docker exec -it epargne ping postgres-epargne
```

### ❌ Problème: Port déjà utilisé

```bash
# Trouver le processus utilisant le port
lsof -i :6000
lsof -i :5000
lsof -i :9090

# Terminer le processus
kill -9 <PID>

# Ou changer les ports dans docker-compose.yml
```

---

## 📖 Documentation API

### Swagger Interactif

Une fois les services lancés, accédez à:

- **EJB**: [http://localhost:9090/swagger-ui.html](http://localhost:9090/swagger-ui.html)
- **Épargne**: [http://localhost:6000/swagger/index.html](http://localhost:6000/swagger/index.html)
- **Prêt**: [http://localhost:5000/swagger/index.html](http://localhost:5000/swagger/index.html)

---

## 📝 Notes Importantes

### ⚠️ Limitations Connues

1. **Pas d'authentification JWT**: À implémenter pour la production
2. **Transaction distribuée limitée**: Pas de saga distribuée complète
3. **Pas de cache Redis**: Performance non optimisée
4. **Données test limitées**: À étendre avec davantage de scénarios
5. **Pas de logging centralisé**: Chaque service a ses propres logs

### ✅ Bonnes Pratiques Implantées

- ✓ Séparation des responsabilités (3-tiers)
- ✓ Mappers DTO pour API
- ✓ Services métier
- ✓ Repositories pour accès données
- ✓ Validation des données
- ✓ Gestion des erreurs
- ✓ Modèles JPA/EF Core
- ✓ Historiques des transactions

---

## 🤝 Contribution

Pour ajouter des fonctionnalités:

1. Créer une branche: `git checkout -b feature/nom-feature`
2. Implémenter la fonctionnalité
3. Tester localement
4. Committer: `git commit -m "Ajout fonctionnalité X"`
5. Pusher: `git push origin feature/nom-feature`
6. Créer une Pull Request

---

## 📞 Support

Pour des questions ou problèmes:

1. Consulter la section [Dépannage](#-%EF%B8%8F-d%C3%A9pannage)
2. Vérifier les logs: `docker logs <service>`
3. Tester l'API avec Postman/Bruno
4. Consulter la documentation Swagger

---

## 📄 Licence

Projet académique - 2025

---

## 🙏 Remerciements

- **Framework Jakarta EE** pour EJB robuste
- **ASP.NET Core** pour .NET microservices
- **PostgreSQL** pour gestion données
- **Docker** pour containerisation
- **Team ITU** pour le projet pédagogique

---

## 📸 **Lieu pour Captures d'Écran (À remplir par l'utilisateur)**

> Tu peux ajouter ici tes captures d'écran des scénarios:
>
> ### Scénario 1: Création Compte Épargne
> [Insère screenshot ici]
>
> ### Scénario 2: Retrait Épargne
> [Insère screenshot ici]
>
> ### Scénario 3: Création Prêt
> [Insère screenshot ici]
>
> ### Scénario 4: Remboursement Prêt
> [Insère screenshot ici]
>
> ### Scénario 5: Virement Inter-Comptes
> [Insère screenshot ici]

---

**Version du Document**: 1.0  
**Dernière Mise à Jour**: 14 Avril 2026  
**Auteur**: Documentation Générée Automatiquement  
**Status**: ✅ Prêt pour Intégration Screenshots et Tests

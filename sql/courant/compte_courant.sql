-- Supprimer le schema existant
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- direction du clients ou utilisateurs,(general, courant, depot)
CREATE TABLE directions  (
    id_direction SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    niveau INTEGER NOT NULL
);


-- roles (consulation, validation, insertion ) 
CREATE TABLE roles  (
    id_role SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

-- Table clients
CREATE TABLE clients_courant (
    id_client SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenoms VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE ,
    mdp   VARCHAR(500) NOT NULL ,
    id_direction INT REFERENCES directions(id_direction) ON DELETE CASCADE ,
    date_naissance DATE
);
CREATE TABLE clients_role (
    id_client_role  SERIAL PRIMARY KEY,
    id_role INT REFERENCES roles(id_role) ON DELETE CASCADE ,
    id_client INT REFERENCES clients_courant(id_client) ON DELETE CASCADE 
);

-- role utilisateur
CREATE TABLE actions  (
    id_action SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);


-- le role de l'utilisateur selon la table et l'action
CREATE TABLE actions_roles  (
    id_action_role SERIAL PRIMARY KEY,
    nom_table VARCHAR (255) NOT NULL , 
	id_action  INT REFERENCES actions(id_action) ON DELETE CASCADE,
	id_role  INT REFERENCES roles(id_role) ON DELETE CASCADE
);

-- type compte
CREATE TABLE types_compte (
    id_type_compte SERIAL PRIMARY KEY , 
    libelle  VARCHAR (255) NOT NULL 
);

-- Table comptes
CREATE TABLE comptes_courant (
    id_compte SERIAL PRIMARY KEY,
    id_object VARCHAR(255) UNIQUE NOT NULL,  -- cmpt_
    nom VARCHAR(100),
    capital NUMERIC (15,2) NOT NULL , 
    id_client INT NOT NULL REFERENCES clients_courant(id_client) ON DELETE CASCADE,
    date_ouverture DATE NOT NULL DEFAULT CURRENT_DATE,
    id_type_compte INT NOT NULL REFERENCES types_compte(id_type_compte) ON DELETE CASCADE,
    plafond NUMERIC (15,2) NOT NULL DEFAULT 10000,   
    decouvert_autorise NUMERIC(15,2) DEFAULT 0.00
);



-- Table transactions
CREATE TABLE transactions_courant (
    id_transaction SERIAL PRIMARY KEY,
    source VARCHAR (255) NOT NULL,
    date_transaction DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    libelle VARCHAR(100) NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    devise VARCHAR(50) DEFAULT 'ar',
    sens VARCHAR(6) NOT NULL CHECK (sens IN ('debit','credit')),
    banque boolean NOT NULL DEFAULT true
);


-- Retraits
CREATE TABLE retraits (
    id_retrait SERIAL PRIMARY KEY,
    id_object VARCHAR(255) UNIQUE NOT NULL, -- ex: "retr_1"
    id_compte_deb INT REFERENCES comptes_courant(id_compte) ON DELETE CASCADE,
    devise VARCHAR(50) DEFAULT 'ar',
    taux NUMERIC (15,2) NOT NULL , 
    montant NUMERIC(15,2) NOT NULL
);


-- Dépôts
CREATE TABLE depots (
    id_depot SERIAL PRIMARY KEY,
    id_object VARCHAR(255) UNIQUE NOT NULL, -- ex: "dept_1"
    id_compte_cred INT REFERENCES comptes_courant(id_compte) ON DELETE CASCADE,
    devise VARCHAR(50) DEFAULT 'ar',
    taux NUMERIC (15,2) NOT NULL , 
    montant NUMERIC(15,2) NOT NULL
);

CREATE TABLE etats (
    id_etat  SERIAL PRIMARY KEY,
    libelle VARCHAR (255) NOT NULL 
);


CREATE TABLE frais (
    id_frais SERIAL PRIMARY KEY,
    id_type_compte INT NOT NULL REFERENCES types_compte(id_type_compte) ON DELETE CASCADE,
    montant_inf NUMERIC(15,2) NOT NULL DEFAULT 0,
    montant_sup NUMERIC(15,2)  NULL DEFAULT NULL, 
    fond_montant NUMERIC(15,2) DEFAULT 0, 
    fond_pourcentage NUMERIC(5,2) DEFAULT 0 ,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE virements (
    id_virement SERIAL PRIMARY KEY,
    id_object VARCHAR(255) UNIQUE NOT NULL,  -- ex: "vrmt_1"
    id_compte_deb INT REFERENCES comptes_courant(id_compte) ON DELETE CASCADE,
    id_compte_cred INT REFERENCES comptes_courant(id_compte) ON DELETE CASCADE,
    date_virement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    montant NUMERIC(15,2) NOT NULL,
    frais_montant NUMERIC(15,2) NOT NULL,
    taux NUMERIC (15,2) NOT NULL , 
    id_frais INT REFERENCES frais(id_frais) ON DELETE CASCADE,
    devise VARCHAR(50) DEFAULT 'MG'
);

CREATE TABLE historiques_virement (
    id_historique SERIAL PRIMARY KEY,
    id_virement INT REFERENCES virements(id_virement) ON DELETE CASCADE,
    id_compte_deb INT NOT NULL,
    id_compte_cred INT NOT NULL,
    date_virement TIMESTAMP NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    devise VARCHAR(50) DEFAULT 'MG',
    taux NUMERIC (15,2) NOT NULL ,
    id_etat INT REFERENCES etats(id_etat) ON DELETE SET NULL,
    id_utilisateur INT REFERENCES clients_courant(id_client) ON DELETE SET NULL,
    date_historique TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action VARCHAR(50) NOT NULL 
);


CREATE TABLE historiques_depot (
    id_historique SERIAL PRIMARY KEY,
    id_depot INT REFERENCES depots(id_depot) ON DELETE CASCADE,
    id_compte_cred INT NOT NULL,
    date_depot TIMESTAMP NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    devise VARCHAR(50) DEFAULT 'ar',
    taux NUMERIC(15,2) NOT NULL,
    id_etat INT REFERENCES etats(id_etat) ON DELETE SET NULL,
    id_utilisateur INT REFERENCES clients_courant(id_client) ON DELETE SET NULL,
    date_historique TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action VARCHAR(50) NOT NULL
);

CREATE TABLE historiques_retrait (
    id_historique SERIAL PRIMARY KEY,
    id_retrait INT REFERENCES retraits(id_retrait) ON DELETE CASCADE,
    id_compte_deb INT NOT NULL,
    date_retrait TIMESTAMP NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    devise VARCHAR(50) DEFAULT 'ar',
    taux NUMERIC(15,2) NOT NULL,
    id_etat INT REFERENCES etats(id_etat) ON DELETE SET NULL,
    id_utilisateur INT REFERENCES clients_courant(id_client) ON DELETE SET NULL,
    date_historique TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action VARCHAR(50) NOT NULL
);





CREATE TABLE validations_transaction (
    id_validation SERIAL PRIMARY KEY,
    id_transaction INT REFERENCES transactions_courant(id_transaction) ON DELETE CASCADE,
    id_etat INT REFERENCES etats(id_etat) ON DELETE CASCADE,
    date_validation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE validations_virement (
    id_validation SERIAL PRIMARY KEY,
    id_virement INT REFERENCES virements(id_virement) ON DELETE CASCADE,
    id_etat INT REFERENCES etats(id_etat) ON DELETE CASCADE,
    id_utilisateur INT REFERENCES clients_courant(id_client) ON DELETE CASCADE,
    date_validation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);



-- Validation dépôt
CREATE TABLE validations_depot (
    id_validation SERIAL PRIMARY KEY,
    id_etat INT REFERENCES etats(id_etat) ON DELETE CASCADE,
    id_depot INT REFERENCES depots(id_depot) ON DELETE CASCADE,
    date_validation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Validation retrait
CREATE TABLE validations_retrait (
    id_validation SERIAL PRIMARY KEY,
    id_etat INT REFERENCES etats(id_etat) ON DELETE CASCADE,
    id_retrait INT REFERENCES retraits(id_retrait) ON DELETE CASCADE,
    date_validation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

);



-- config frais
-- CREATE TABLE config_frais (
    
-- );


INSERT INTO etats (libelle) VALUES 
('en_attente'),
('valider'),
('annuler');

INSERT INTO types_compte (libelle) VALUES 
('courant');

INSERT INTO frais (id_type_compte, montant_inf, montant_sup, fond_montant, fond_pourcentage)
VALUES
(1, 0, 500000, 20000, 5),
(1, 500001, 2000000, 50000, 3),
(1, 2000001, NULL, 50000, 1);

INSERT INTO directions (libelle, niveau) VALUES
('general', 1),
('courant', 2),
('epargne', 2);

INSERT INTO roles (libelle) VALUES
('insertion'),
('validation'),
('consultation');

INSERT INTO actions (libelle) VALUES
('post'),
('put'),
('delete'),
('get');

-- Permissions par table
INSERT INTO actions_roles (nom_table, id_action, id_role) VALUES
('clients_courant', 1, 1), -- Insertion = Ajouter
('clients_courant', 2, 2), -- Validation = Modifier
('clients_courant', 4, 3), -- Consultation = Voir
('transactions_courant', 4, 3),
('transactions_courant', 2, 3); 



-- Insertion clients
INSERT INTO clients_courant (nom, prenoms, email , mdp , date_naissance, id_direction) VALUES
('Jean', 'Marie','a@gmail' , '1234' ,'1990-05-23', 2),
('Rakoto', 'Alice','b@gmail' , '1234','1992-08-12', 1),
('Banque', 'Gasy','bank@gmail' , '1234' ,'1990-05-23', 2);

-- Assigner rôles aux clients
INSERT INTO clients_role (id_client, id_role) VALUES
(1, 1), -- Jean = Insertion
(1, 3), -- Jean = Insertion
(2, 3); 






-- Insérer un seul compte pour ce client
INSERT INTO comptes_courant (id_object ,nom, capital  , id_client, id_type_compte , plafond, decouvert_autorise)
VALUES 
('cmpt_1',  'Compte principal' , 3000000 , 1,  1 , 2500000,500.00),
('cmpt_2',  'Compte principal' , 3000000 , 2,  1 , 2500000,500.00),
('cmpt_3',  'banque' , 0 , 3,  1 ,12123  ,100.00);


-- Insérer quelques transactions pour ce compte
-- INSERT INTO transactions_courant (id_compte, date_transaction, libelle, montant, validate ,sens) VALUES
-- (1, '2023-08-01 10:30:00', 'Salaire', 1500.00, false ,'credit'),
-- (1, '2023-08-03 15:20:00', 'Achat Supermarché', 200.00,false  ,'debit'),
-- (1, '2023-08-05 09:00:00', 'Remboursement ami', 100.00, false ,'debit');




SELECT 
    c.id_client,
    CONCAT(c.nom, ' ', c.prenoms) AS client,
    r.libelle AS role,
    ar.nom_table AS table_cible,
    a.libelle AS action
FROM clients_courant c
JOIN clients_role cr ON c.id_client = cr.id_client
JOIN roles r ON cr.id_role = r.id_role
LEFT JOIN actions_roles ar ON r.id_role = ar.id_role
LEFT JOIN actions a ON ar.id_action = a.id_action
ORDER BY c.id_client, r.libelle, ar.nom_table;


CREATE SEQUENCE seq_virement START 1;

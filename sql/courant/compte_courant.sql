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

-- Table comptes
CREATE TABLE comptes_courant (
    id_compte SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    capital NUMERIC (15,2) NOT NULL , 
    id_client INT NOT NULL REFERENCES clients_courant(id_client) ON DELETE CASCADE,
    date_ouverture DATE NOT NULL DEFAULT CURRENT_DATE,
    decouvert_autorise NUMERIC(15,2) DEFAULT 0.00
);

-- Table transactions
CREATE TABLE transactions_courant (
    id_transaction SERIAL PRIMARY KEY,
    id_compte INT NOT NULL REFERENCES comptes_courant(id_compte) ON DELETE CASCADE,
    date_transaction TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    libelle VARCHAR(100) NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    validate boolean DEFAULT false ,
    sens VARCHAR(6) NOT NULL CHECK (sens IN ('debit','credit'))
);


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
('Rakoto', 'Alice','b@gmail' , '1234','1992-08-12', 1);

-- Assigner rôles aux clients
INSERT INTO clients_role (id_client, id_role) VALUES
(1, 1), -- Jean = Insertion
(1, 3), -- Jean = Insertion
(2, 3); 






-- Insérer un seul compte pour ce client
INSERT INTO comptes_courant (nom, capital  , id_client, decouvert_autorise)
VALUES ('Compte principal', 100 , 1, 500.00);

-- Insérer quelques transactions pour ce compte
INSERT INTO transactions_courant (id_compte, date_transaction, libelle, montant, validate ,sens) VALUES
(1, '2023-08-01 10:30:00', 'Salaire', 1500.00, false ,'credit'),
(1, '2023-08-03 15:20:00', 'Achat Supermarché', 200.00,false  ,'debit'),
(1, '2023-08-05 09:00:00', 'Remboursement ami', 100.00, false ,'debit');




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

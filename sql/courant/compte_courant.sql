-- Supprimer le schema existant
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table clients
CREATE TABLE clients_courant (
    id_client SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenoms VARCHAR(100) NOT NULL,
    date_naissance DATE
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
    sens VARCHAR(6) NOT NULL CHECK (sens IN ('debit','credit'))
);

-- Insérer un seul client
INSERT INTO clients_courant (nom, prenoms, date_naissance)
VALUES 
('Jean', 'Marie', '1990-05-23'),
('Rakoto', 'Alice', '1990-05-23');

-- Insérer un seul compte pour ce client
INSERT INTO comptes_courant (nom, capital  , id_client, decouvert_autorise)
VALUES ('Compte principal', 100 , 1, 500.00);

-- Insérer quelques transactions pour ce compte
INSERT INTO transactions_courant (id_compte, date_transaction, libelle, montant, sens) VALUES
(1, '2023-08-01 10:30:00', 'Salaire', 1500.00, 'credit'),
(1, '2023-08-03 15:20:00', 'Achat Supermarché', 200.00, 'debit'),
(1, '2023-08-05 09:00:00', 'Remboursement ami', 100.00, 'debit');

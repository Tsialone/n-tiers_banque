-- Réinitialiser le schema
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table clients_epargne
CREATE TABLE clients_epargne (
    id_client SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE
);

-- Table comptes_epargne
CREATE TABLE comptes_epargne (
    id_compte SERIAL PRIMARY KEY,
    id_client INT NOT NULL REFERENCES clients_epargne(id_client) ON DELETE CASCADE,
    date_ouverture DATE NOT NULL DEFAULT CURRENT_DATE,
    taux_interet NUMERIC(5,2) NOT NULL
);

-- Table transactions_epargne
CREATE TABLE transactions_epargne (
    id_transaction SERIAL PRIMARY KEY,
    id_compte INT NOT NULL REFERENCES comptes_epargne(id_compte) ON DELETE CASCADE,
    date_transaction TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    libelle VARCHAR(100) NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    sens VARCHAR(6) NOT NULL CHECK (sens IN ('debit','credit'))
);

-- Données exemples clients
INSERT INTO clients_epargne (nom, prenom, date_naissance) VALUES
('Rakoto', 'Alice', '1990-05-23'),
('Rabe', 'Bob', '1985-11-10'),
('Andrian', 'Claire', '2000-02-15');

-- Données exemples comptes
INSERT INTO comptes_epargne (id_client, date_ouverture, taux_interet) VALUES
(1, '2023-01-01', 3.50),
(1, '2024-05-10', 2.75),
(2, '2022-08-15', 4.00),
(3, '2023-11-20', 3.25);

-- Données exemples transactions
INSERT INTO transactions_epargne (id_compte, date_transaction, libelle, montant, sens) VALUES
(1, '2025-01-10 10:00:00', 'Dépôt initial', 1000.00, 'credit'),
(1, '2025-02-15 15:30:00', 'Retrait', 200.00, 'debit'),
(2, '2025-05-12 09:45:00', 'Dépôt', 500.00, 'credit'),
(3, '2025-03-20 14:00:00', 'Dépôt initial', 1500.00, 'credit'),
(4, '2025-04-01 16:20:00', 'Retrait', 300.00, 'debit');

-- Supprimer le schema existant
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE clients_pret (
    id_client SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE
);

CREATE TABLE comptes_pret (
    id_compte SERIAL PRIMARY KEY,
    id_client INT NOT NULL REFERENCES clients_pret(id_client) ON DELETE CASCADE,
    libelle VARCHAR(100) NULL , 
    date_ouverture DATE NOT NULL DEFAULT CURRENT_DATE,
    capital_emprunte NUMERIC(15,2) NOT NULL, 
    taux_interet NUMERIC(5,2) NOT NULL,
    duree_mois INT NOT NULL CHECK (duree_mois > 0),
    date_echeance DATE NOT NULL,
    statut VARCHAR(20) DEFAULT 'actif'
);
CREATE TABLE amortissements (
    id_amortissement SERIAL PRIMARY KEY,
    id_compte INT NOT NULL REFERENCES comptes_pret(id_compte) ON DELETE CASCADE,
    mois INT NOT NULL,
    mensualite NUMERIC(15,2) NOT NULL,
    interet NUMERIC(15,2) NOT NULL,
    capital NUMERIC(15,2) NOT NULL,
    reste_du NUMERIC(15,2) NOT NULL,
    created_at DATE NOT NULL , 
    statut VARCHAR(20) DEFAULT 'en_attente'
);


CREATE TABLE transactions_pret (
    id_transaction SERIAL PRIMARY KEY,
    id_compte INT NOT NULL REFERENCES comptes_pret(id_compte) ON DELETE CASCADE,
    id_amortissement INT REFERENCES amortissements(id_amortissement), -- lien vers mois à payer
    date_transaction DATE NOT NULL DEFAULT CURRENT_DATE,
    libelle VARCHAR(100) NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    type_transaction VARCHAR(20) NOT NULL CHECK (type_transaction IN ('decaissement', 'remboursement'))
);


-- Client
INSERT INTO clients_pret (nom, prenom, date_naissance)
VALUES ('Dupont', 'Jean', '1990-05-10');

-- Compte prêt
INSERT INTO comptes_pret (id_client, date_ouverture, capital_emprunte, taux_interet, duree_mois, date_echeance)
VALUES (1, '2025-09-01', 1000.00, 10.00, 4, '2026-01-01');

-- Amortissements (simplifié, avec capital et intérêts arrondis)
INSERT INTO amortissements (id_compte, mois, mensualite, interet, capital, reste_du , created_at)
VALUES 
(1, 1, 260.00, 8.33, 251.67, 748.33 ,  '2025-01-01'),
(1, 2, 260.00, 6.23, 253.77, 494.56 , '2025-02-01'),
(1, 3, 260.00, 4.12, 255.88, 238.68 , '2025-03-01'),
(1, 4, 260.00, 1.99, 258.01, 0.00 , '2025-04-01');

-- Transaction de décaissement (la banque donne l’argent)
INSERT INTO transactions_pret (id_compte, libelle, montant, type_transaction)
VALUES (1, 'Décaissement du prêt', 1000.00, 'decaissement');

-- Paiement des mensualités (remboursements liés aux amortissements)
INSERT INTO transactions_pret (id_compte, id_amortissement, libelle, montant, type_transaction, date_transaction)
VALUES 
(1, 1, 'Remboursement mois 1', 260.00, 'remboursement', '2025-10-01'),
(1, 2, 'Remboursement mois 2', 260.00, 'remboursement', '2025-11-01'),
(1, 3, 'Remboursement mois 3', 260.00, 'remboursement', '2025-12-01'),
(1, 4, 'Remboursement mois 4', 260.00, 'remboursement', '2026-01-01');

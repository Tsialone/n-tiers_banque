CREATE TABLE clients_pret (
    id_client SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE
);

CREATE TABLE comptes_pret (
    id_compte SERIAL PRIMARY KEY,
    id_client INT NOT NULL REFERENCES clients_pret(id_client) ON DELETE CASCADE,
    date_ouverture DATE NOT NULL DEFAULT CURRENT_DATE,
    montant NUMERIC(15,2) NOT NULL,   -- montant du prêt
    taux_interet NUMERIC(5,2) NOT NULL,
    duree_mois INT NOT NULL,
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
    statut VARCHAR(20) DEFAULT 'en_attente'
);


CREATE TABLE transactions_pret (
    id_transaction SERIAL PRIMARY KEY,
    id_compte INT NOT NULL REFERENCES comptes_pret(id_compte) ON DELETE CASCADE,
    id_amortissement INT REFERENCES amortissements(id_amortissement), -- lien vers mois à payer
    date_transaction TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    libelle VARCHAR(100) NOT NULL,
    montant NUMERIC(15,2) NOT NULL,
    type_transaction VARCHAR(20) NOT NULL CHECK (type_transaction IN ('decaissement', 'remboursement', 'interet'))
);


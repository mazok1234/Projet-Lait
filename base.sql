CREATE DATABASE laitsgo;
\c laitsgo;

CREATE TABLE RoleUtilisateur(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE EmployeePostes(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE Permissions(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE PermissionsPostes(
    Id SERIAL PRIMARY KEY,
    PosteId INT NOT NULL,
    PermissionId INT NOT NULL,
    FOREIGN KEY(PosteId) REFERENCES EmployeePostes(Id),
    FOREIGN KEY(PermissionId) REFERENCES Permissions(Id)
);

CREATE TABLE Employes(
    Id SERIAL PRIMARY KEY,
    Nom VARCHAR(150) NOT NULL,
    Prenom VARCHAR(150) NOT NULL,
    Adresse VARCHAR(255),
    PosteId INT NOT NULL,
    DateEmbauche DATE NOT NULL,
    Telephone VARCHAR(50),
    IsActif BOOLEAN NOT NULL DEFAULT TRUE,
    DateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(PosteId) REFERENCES EmployeePostes(Id)
);

CREATE TABLE Utilisateurs(
    Id SERIAL PRIMARY KEY,
    Nom VARCHAR(150) NOT NULL,
    Email VARCHAR(250) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    RoleUtilisateur INT NOT NULL,
    EmployeId INT UNIQUE,
    DateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(RoleUtilisateur) REFERENCES RoleUtilisateur(Id),
    FOREIGN KEY(EmployeId) REFERENCES Employes(Id)
);


CREATE TABLE Races(
    Id SERIAL PRIMARY KEY,
    Nom VARCHAR(100) NOT NULL
);

CREATE TABLE EtatsSante(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE Bovins(
    Id SERIAL PRIMARY KEY,
    NumeroBoucle VARCHAR(50) NOT NULL UNIQUE,
    RaceId INT NOT NULL,
    EtatSanteId INT NOT NULL,
    DateNaissance DATE NOT NULL,
    Sexe VARCHAR(10) NOT NULL DEFAULT 'Femelle',
    IsActif BOOLEAN NOT NULL DEFAULT TRUE,
    DateAjout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(RaceId) REFERENCES Races(Id),
    FOREIGN KEY(EtatSanteId) REFERENCES EtatsSante(Id)
);

CREATE TABLE HistoriqueBovins(
    Id SERIAL PRIMARY KEY,
    BovinId INT NOT NULL,
    EtatSanteId INT NOT NULL,
    DateEvent TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Motif TEXT,
    IdUser INT,
    FOREIGN KEY(BovinId) REFERENCES Bovins(Id),
    FOREIGN KEY(EtatSanteId) REFERENCES EtatsSante(Id),
    FOREIGN KEY(IdUser) REFERENCES Utilisateurs(Id)
);

CREATE TABLE CycleEtatVache(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE CycleBovins(
    Id SERIAL PRIMARY KEY,
    BovinId INT NOT NULL,
    DateDebut DATE NOT NULL,
    DateFin DATE,
    EtatCycleId INT NOT NULL,
    RaisonFin VARCHAR(255),
    FOREIGN KEY(BovinId) REFERENCES Bovins(Id),
    FOREIGN KEY(EtatCycleId) REFERENCES CycleEtatVache(Id)
);

CREATE TABLE ProductionLait(
    Id SERIAL PRIMARY KEY,
    CycleBovinId INT NOT NULL,
    Quantite DECIMAL(10,2) NOT NULL,
    DateProduction TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IdUser INT,
    FOREIGN KEY(CycleBovinId) REFERENCES CycleBovins(Id),
    FOREIGN KEY(IdUser) REFERENCES Utilisateurs(Id)
);


CREATE TABLE CategoriesTransaction(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE TypesTransaction(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE Transactions(
    Id SERIAL PRIMARY KEY,
    SourceType VARCHAR(50),
    SourceId INT,
    TypeId INT NOT NULL,
    Montant DECIMAL(12,2) NOT NULL,
    Description VARCHAR(255),
    DateTrans TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IdUser INT,
    FOREIGN KEY(TypeId) REFERENCES TypesTransaction(Id),
    FOREIGN KEY(IdUser) REFERENCES Utilisateurs(Id)
);

CREATE VIEW BilanFinancier AS
SELECT
    SUM(CASE WHEN tt.Libelle = 'recette' THEN t.Montant ELSE 0 END) AS TotalRecettes,
    SUM(CASE WHEN tt.Libelle = 'depense' THEN t.Montant ELSE 0 END) AS TotalDepenses,
    SUM(CASE WHEN tt.Libelle = 'recette' THEN t.Montant WHEN tt.Libelle = 'depense' THEN -t.Montant ELSE 0 END) AS BeneficeNet
FROM Transactions t
JOIN TypesTransaction tt ON t.TypeId = tt.Id;

CREATE TABLE Fournisseurs(
    Id SERIAL PRIMARY KEY,
    Nom VARCHAR(200) NOT NULL,
    Telephone VARCHAR(50),
    Adresse TEXT
);

CREATE TABLE Aliments(
    Id SERIAL PRIMARY KEY,
    Nom VARCHAR(150) NOT NULL,
    Unite VARCHAR(20),
    SeuilStockMin DECIMAL(10,2) DEFAULT 0,
    PrixAchat DECIMAL(10,2)
);

CREATE TABLE TypeMouvement(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE MouvementsStock(
    Id SERIAL PRIMARY KEY,
    TypeId INT NOT NULL,
    DateMvt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FournisseurId INT,
    Motif VARCHAR(255),
    IdUser INT,
    FOREIGN KEY(FournisseurId) REFERENCES Fournisseurs(Id),
    FOREIGN KEY(IdUser) REFERENCES Utilisateurs(Id),
    FOREIGN KEY(TypeId) REFERENCES TypeMouvement(Id)
);

CREATE TABLE MouvementStockFille(
    Id SERIAL PRIMARY KEY,
    MouvementStockId INT NOT NULL,
    AlimentId INT NOT NULL,
    Quantite DECIMAL(10,2) NOT NULL,
    PrixUnitaire DECIMAL(12,2),
    BovinId INT,
    FOREIGN KEY(MouvementStockId) REFERENCES MouvementsStock(Id) ON DELETE CASCADE,
    FOREIGN KEY(AlimentId) REFERENCES Aliments(Id),
    FOREIGN KEY(BovinId) REFERENCES Bovins(Id)
);

CREATE VIEW StockActuel AS
SELECT
    a.Id,
    a.Nom,
    a.Unite,
    a.SeuilStockMin,
    COALESCE(SUM(
        CASE WHEN tm.Libelle = 'Approvisionnement' THEN mf.Quantite
             WHEN tm.Libelle = 'Consommation' THEN -mf.Quantite
             ELSE 0 END
    ), 0) AS Stock
FROM Aliments a
LEFT JOIN MouvementStockFille mf ON a.Id = mf.AlimentId
LEFT JOIN MouvementsStock m ON mf.MouvementStockId = m.Id
LEFT JOIN TypeMouvement tm ON m.TypeId = tm.Id
GROUP BY a.Id, a.Nom, a.Unite, a.SeuilStockMin;

CREATE TABLE Clients(
    Id SERIAL PRIMARY KEY,
    Nom VARCHAR(200) NOT NULL,
    Telephone VARCHAR(50),
    Adresse TEXT,
    Email VARCHAR(250),
    IsActif BOOLEAN NOT NULL DEFAULT TRUE,
    DateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Produits(
    Id SERIAL PRIMARY KEY,
    Nom VARCHAR(150) NOT NULL,
    Unite VARCHAR(20),
    PrixVente DECIMAL(10,2)
);

CREATE TABLE StatutCommande(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE Commandes(
    Id SERIAL PRIMARY KEY,
    ClientId INT NOT NULL,
    StatutId INT NOT NULL,
    DateCommande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DateLivraison DATE,
    IdUser INT,
    FOREIGN KEY(ClientId) REFERENCES Clients(Id),
    FOREIGN KEY(StatutId) REFERENCES StatutCommande(Id),
    FOREIGN KEY(IdUser) REFERENCES Utilisateurs(Id)
);

CREATE TABLE CommandeDetails(
    Id SERIAL PRIMARY KEY,
    CommandeId INT NOT NULL,
    ProduitId INT NOT NULL,
    Quantite FLOAT NOT NULL,
    PrixUnitaire DECIMAL(10,2) NOT NULL,
    FOREIGN KEY(CommandeId) REFERENCES Commandes(Id),
    FOREIGN KEY(ProduitId) REFERENCES Produits(Id)
);

CREATE TABLE CommandeStatus(
    Id SERIAL PRIMARY KEY,
    CommandeId INT NOT NULL,
    StatutId INT NOT NULL,
    DateStatus TIMESTAMP,
    FOREIGN KEY(CommandeId) REFERENCES Commandes(Id),
    FOREIGN KEY(StatutId) REFERENCES StatutCommande(Id)
);


CREATE TABLE HistoriqueSalaire(
    Id SERIAL PRIMARY KEY,
    EmployeId INT NOT NULL,
    SalaireBase DECIMAL(12,2) NOT NULL,
    DateDebut DATE NOT NULL,
    DateFin DATE,
    Motif VARCHAR(255),
    DateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(EmployeId) REFERENCES Employes(Id)
);

CREATE TABLE Pointage(
    Id SERIAL PRIMARY KEY,
    EmployeId INT NOT NULL,
    DatePointage DATE NOT NULL,
    HeureEntree TIME,
    HeureSortie TIME,
    DateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(EmployeId, DatePointage),
    FOREIGN KEY(EmployeId) REFERENCES Employes(Id)
);

CREATE TABLE TypesConge(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE StatutConge(
    Id SERIAL PRIMARY KEY,
    Libelle VARCHAR(100) NOT NULL
);

CREATE TABLE Conges(
    Id SERIAL PRIMARY KEY,
    EmployeId INT NOT NULL,
    DateDebut DATE NOT NULL,
    DateFin DATE NOT NULL,
    TypeId INT NOT NULL,
    StatutId INT NOT NULL,
    Motif VARCHAR(255),
    ValidePar INT,
    DateDemande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DateValidation TIMESTAMP,
    FOREIGN KEY(EmployeId) REFERENCES Employes(Id),
    FOREIGN KEY(TypeId) REFERENCES TypesConge(Id),
    FOREIGN KEY(StatutId) REFERENCES StatutConge(Id),
    FOREIGN KEY(ValidePar) REFERENCES Utilisateurs(Id)
);

CREATE TABLE FichesPaie(
    Id SERIAL PRIMARY KEY,
    EmployeId INT NOT NULL,
    Mois INT NOT NULL,
    Annee INT NOT NULL,
    MontantBrut DECIMAL(12,2) NOT NULL,
    MontantNet DECIMAL(12,2) NOT NULL,
    DatePaiement DATE,
    DateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(EmployeId, Mois, Annee),
    FOREIGN KEY(EmployeId) REFERENCES Employes(Id)
);


CREATE OR REPLACE FUNCTION InsertTransactionCommande()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO Transactions(SourceType, SourceId, TypeId, Montant, Description, DateTrans, IdUser)
    SELECT
        'Commande',
        NEW.Id,
        1,
        COALESCE(SUM(cd.Quantite * cd.PrixUnitaire), 0),
        CONCAT('Vente commande #', NEW.Id),
        NEW.DateCommande,
        NEW.IdUser
    FROM CommandeDetails cd
    WHERE cd.CommandeId = NEW.Id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER TriggerInsertTransactionCommande
AFTER INSERT ON Commandes
FOR EACH ROW
EXECUTE FUNCTION InsertTransactionCommande();

CREATE OR REPLACE FUNCTION InsertTransactionMouvementStock()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.TypeId = 1 THEN
        INSERT INTO Transactions(SourceType, SourceId, TypeId, Montant, Description, DateTrans, IdUser)
        SELECT
            'Approvisionnement',
            NEW.Id,
            2,
            COALESCE(SUM(msf.Quantite * msf.PrixUnitaire), 0),
            CONCAT('Approvisionnement #', NEW.Id),
            NEW.DateMvt,
            NEW.IdUser
        FROM MouvementStockFille msf
        WHERE msf.MouvementStockId = NEW.Id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER TriggerInsertTransactionMouvementStock
AFTER INSERT ON MouvementsStock
FOR EACH ROW
EXECUTE FUNCTION InsertTransactionMouvementStock();

CREATE OR REPLACE FUNCTION InsertTransactionFichesPaie()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO Transactions(SourceType, SourceId, TypeId, Montant, Description, DateTrans)
    VALUES(
        'Salaire',
        NEW.Id,
        2,
        NEW.MontantNet,
        CONCAT('Salaire employé #', NEW.EmployeId, ' ', NEW.Mois, '/', NEW.Annee),
        NEW.DatePaiement
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER TriggerInsertTransactionFichesPaie
AFTER INSERT ON FichesPaie
FOR EACH ROW
EXECUTE FUNCTION InsertTransactionFichesPaie();

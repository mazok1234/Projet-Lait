-- Rôles
INSERT INTO RoleUtilisateur(Libelle) VALUES ('RH'), ('Employe');

-- Postes
INSERT INTO EmployeePostes(Libelle) VALUES ('Vacher'), ('Comptable'), ('RH');

-- Employé
INSERT INTO Employes(Nom, Prenom, Adresse, PosteId, DateEmbauche, Telephone)
VALUES ('Rakoto', 'Jean', 'Antananarivo', 1, '2025-03-01', '0341234567');

-- Vérification (optionnelle)
SELECT * FROM Employes;

-- Utilisateur RH (compte de test, sans employé lié)
INSERT INTO Utilisateurs(Nom, Email, Password, RoleUtilisateur)
VALUES ('Test RH', 'rh@laitsgo.com', '1234', 1);

-- Utilisateur Employé lié à Jean Rakoto
INSERT INTO Utilisateurs(Nom, Email, Password, RoleUtilisateur, EmployeId)
VALUES ('Jean Rakoto', 'jean@laitsgo.com', '1234', 2, 1);

-- Salaire de base de Jean
INSERT INTO HistoriqueSalaire(EmployeId, SalaireBase, DateDebut) VALUES (1, 500000, '2026-01-01');

-- Référentiels congés
INSERT INTO TypesConge(Libelle) VALUES ('Congé payé'), ('Maladie');
INSERT INTO StatutConge(Libelle) VALUES ('En attente'), ('Validé'), ('Refusé');

-- Un congé de test pour Jean
INSERT INTO Conges(EmployeId, DateDebut, DateFin, TypeId, StatutId, Motif)
VALUES (1, '2026-07-01', '2026-07-05', 1, 2, 'Vacances');


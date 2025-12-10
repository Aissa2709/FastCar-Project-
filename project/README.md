# FastCar Location - Système de Gestion de Location de Véhicules 🚗

Plateforme complète de gestion de location de voitures développée en **Java Swing** avec base de données **MySQL**.

## 📚 Table des matières

1. [Vue d'ensemble](#vue-densemble)
2. [Architecture du projet](#architecture-du-projet)
3. [Structure détaillée](#structure-détaillée)
4. [Fonctionnalités](#fonctionnalités)
5. [Démarrage rapide](#démarrage-rapide)
6. [Configuration](#configuration)
7. [Workflows](#workflows)

---

## 🎯 Vue d'ensemble

FastCar est une application desktop pour gérer complètement une agence de location de véhicules :

- **🚙 Véhicules** : Catalogue complet avec suivi d'état
- **👥 Clients** : Gestion des informations clients
- **👨‍💼 Agents** : Gestion des commerciaux
- **📝 Contrats** : Création et suivi des locations
- **📄 Factures** : Génération en HTML/PDF professionnel

**Technologies utilisées :**
- Langage : Java 8+
- Interface : Swing (AWT)
- Base de données : MySQL 8.0+
- Dépendance unique : mysql-connector-j-9.5.0.jar

---

## 🏗️ Architecture du projet

FastCar suit une **architecture MVC (Model-View-Controller)** avec le pattern **DAO (Data Access Object)**.

```
┌────────────────────────────────────────────┐
│     COUCHE PRÉSENTATION (Swing)            │
│  MainFrame + 6 Panels (Voitures, etc.)    │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│     COUCHE MÉTIER (Model)                │
│  4 classes : Client, Voiture, etc.      │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│     COUCHE ACCÈS AUX DONNÉES (DAO)       │
│  5 DAOs + DBConnection                  │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│     BASE DE DONNÉES (MySQL)              │
│  fastcar_location avec 4 tables          │
└────────────────────────────────────────┘
```

**Avantages de cette architecture :**
- ✅ Séparation des responsabilités claire
- ✅ Code facilement testable
- ✅ Modifications BD sans toucher l'UI
- ✅ Extensibilité future simple

---

## 📁 Structure détaillée

### Racine du projet

```
project/
├── README.md                 # Ce fichier
├── QUICKSTART.md             # Guide rapide (3 étapes)
├── ARCHITECTURE.md           # Architecture technique détaillée
├── NETTOYAGE.md              # Résumé du nettoyage
├── run.bat                   # Script pour compiler et lancer
├── .gitignore                # Fichiers à ignorer pour Git
│
├── lib/                      # Dépendances externes
│   └── mysql-connector-j-9.5.0.jar
│
├── src/                      # Code source Java
│   └── com/fastcar/          # Package principal
│       ├── dao/              # Accès aux données
│       ├── model/            # Entités métier
│       ├── ui/               # Interface utilisateur
│       └── util/             # Utilitaires
│
├── bin/                      # Fichiers compilés (.class)
│                             # Généré automatiquement
│
└── invoices/                 # Factures générées
                              # Dossier pour runtime
```

---

## 🔍 Détail des fichiers source

### 1️⃣ Couche DAO (Accès aux données)

**Localisation :** `src/com/fastcar/dao/`

#### DBConnection.java
```
Rôle : Gestionnaire unique de la connexion MySQL
Éléments clés :
  - URL : jdbc:mysql://localhost:3306/fastcar_location
  - Utilisateur : root
  - Mot de passe : (vide)
  - Classe statique getConnection()
```

#### ClientDAO.java
```
Rôle : Gestion des clients
Méthodes :
  - getAllClients()           → Liste tous les clients
  - getClientByCin(cin)       → Récupère 1 client par CIN
  - addClient(client)         → Ajoute un nouveau client
  - updateClient(client)      → Modifie les infos client
  - deleteClient(cin)         → Supprime un client
```

#### VoitureDAO.java
```
Rôle : Gestion des véhicules
Méthodes :
  - getAllVoitures()          → Liste tous les véhicules
  - getVoituresDisponibles()  → Seulement les disponibles
  - getVoitureByMatricule()   → 1 voiture par matricule
  - updateVoitureEtat(mat, etat) → Change l'état (Disponible/Louée)
  - addVoiture(voiture)       → Ajoute une voiture
  - deleteVoiture(matricule)  → Supprime une voiture
```

#### ContratDAO.java
```
Rôle : Gestion des contrats de location
Méthodes :
  - getAllContrats()           → Liste tous les contrats
  - getContratsActifs()        → Seulement les actifs
  - getContratByNumero(num)    → 1 contrat par numéro
  - addContrat(contrat)        → Crée un contrat
  - updateContratStatut(id, statut) → Change le statut
  - deleteContrat(numero)      → Supprime un contrat
```

#### AgentDAO.java
```
Rôle : Gestion des agents commerciaux
Méthodes :
  - getAllAgents()            → Liste les agents actifs
  - getAgentByNumero(num)     → 1 agent par numéro
  - addAgent(agent)           → Ajoute un agent
  - deleteAgent(numero)       → Désactive un agent
```

---

### 2️⃣ Couche Model (Entités métier)

**Localisation :** `src/com/fastcar/model/`

#### Client.java
```
Attributs :
  - cin : String              # Clé primaire
  - nom, prenom : String
  - adresse : String
  - telephone : String
  - email : String

Utilisation : Représente un client qui loue des voitures
```

#### Voiture.java
```
Attributs :
  - matricule : String        # Clé primaire
  - marque, modele : String
  - prixJournalier : double
  - etat : String             # "Disponible" ou "Louée"

Utilisation : Représente un véhicule de la flotte
```

#### Contrat.java
```
Attributs :
  - id : String               # Clé primaire (LOC-xxxxx)
  - dateDebut, dateFin : Date
  - montant : double          # Montant total du contrat
  - modePaiement : String     # Espèces, Carte, etc.
  - statut : String           # "Actif" ou "Facturée"
  - kilometrageDebut : int
  - cinClient : String        # FK vers Client
  - matVoiture : String       # FK vers Voiture
  - numAgent : String         # FK vers Agent

Utilisation : Représente une location entre client et voiture
```

#### Agent.java
```
Attributs :
  - numAgent : String         # Clé primaire
  - nom, prenom : String
  - estActif : boolean

Utilisation : Représente un vendeur/agent commercial
```

---

### 3️⃣ Couche UI (Interface utilisateur)

**Localisation :** `src/com/fastcar/ui/`

#### MainFrame.java
```
Rôle : Fenêtre principale de l'application
Contient :
  - 7 onglets (avec CardLayout)
  - Barre de navigation avec boutons
  - Chaque bouton = un onglet
  
Onglets :
  1. Gérer Véhicules
  2. Gérer Clients
  3. Gérer Agents
  4. Gérer Contrats
  5. Nouveau Contrat
  6. Tableau de bord (optional)
  7. Paramètres (optional)
```

#### FormContrat.java
```
Rôle : Formulaire de création de contrat
Contient :
  - ComboBox Client (dropdown)
  - ComboBox Voiture (dropdown)
  - ComboBox Agent (dropdown)
  - TextField DateDebut (DD/MM/YYYY)
  - TextField DateFin (DD/MM/YYYY)
  - RadioButtons Mode paiement (Espèces, Carte, Virement, Chèque)
  - Label Montant Total (calcul auto)
  
Boutons :
  - Enregistrer Contrat → Sauve + marque voiture "Louée"
  - Générer Facture → Crée facture HTML + met à jour statut
  - Actualiser Listes → Recharge dropdowns
  
Fonctionnalités :
  - Calcul automatique du montant basé sur jours
  - Validation complète des données
  - Rafraîchissement auto lors accès au panel
```

#### PanelVoitures.java
```
Rôle : Gestion et affichage des véhicules
Contient :
  - JTable avec liste complète des véhicules
  - Recherche dynamique (filtre en temps réel)
  - Tri par colonnes
  
Boutons :
  - Ajouter (+) → Dialog pour ajouter véhicule
  - Supprimer (✕) → Supprime ligne sélectionnée
  - Actualiser → Recharge depuis BD
  
Colonnes du tableau :
  - Matricule, Marque, Modèle, Prix/jour, État
```

#### PanelClients.java
```
Rôle : Gestion et affichage des clients
Contient :
  - JTable avec liste complète des clients
  - Recherche dynamique (filtre en temps réel)
  
Boutons :
  - Ajouter (+) → Dialog pour ajouter client
  - Supprimer (✕) → Supprime ligne sélectionnée
  - Actualiser → Recharge depuis BD
  
Colonnes du tableau :
  - CIN, Nom, Prénom, Adresse, Téléphone, Email
  
Dialog d'ajout :
  - Validation CIN (obligatoire)
  - Validation email
  - Tous les champs obligatoires
```

#### PanelAgents.java
```
Rôle : Gestion et affichage des agents
Contient :
  - JTable avec liste des agents
  - Dialog pour ajouter agent
  
Boutons :
  - Ajouter (+) → Nouveau agent
  - Supprimer (✕) → Désactive agent
  
Colonnes du tableau :
  - Numéro Agent, Nom, Prénom, Actif (yes/no)
```

#### PanelContrats.java
```
Rôle : Affichage et gestion des contrats existants
Contient :
  - JTable avec tous les contrats
  - Recherche et tri dynamiques
  
Boutons :
  - 📄 Générer Facture → Crée facture HTML
  - Supprimer (✕) → Supprime contrat
  - Actualiser → Recharge depuis BD
  
Colonnes du tableau :
  - Numéro, Client (CIN), Voiture (Matricule)
  - Dates (début/fin), Montant, Mode Paiement, Statut
  
Fonctionnalité spéciale :
  - Sélectionner ligne + Clic "Générer Facture"
  - Facture HTML s'ouvre directement
  - Mise à jour automatique du statut
```

---

### 4️⃣ Couche Utilitaire

**Localisation :** `src/com/fastcar/util/`

#### PDFInvoiceGenerator.java
```
Rôle : Génération des factures en HTML
Entrée : Objet Contrat + Client + Voiture + Agent
Sortie : Fichier HTML dans invoices/

Contenu de la facture HTML :
  - En-tête : Logo et titre "FACTURE DE LOCATION"
  - Section Contrat : N°, dates, montant, mode paiement
  - Section Client : Tous les infos client
  - Section Voiture : Matricule, marque, modèle, prix
  - Section Agent : Numéro, nom, prénom
  - Montant Total en grand
  - Bouton "🖨️ Imprimer / Enregistrer en PDF"
  - Pied de page : Coordonnées FASTCAR MAROC

Style CSS :
  - Design professionnel et responsive
  - Couleurs : Bleu et gris
  - Optimisé pour impression
  - Le bouton disparaît à l'impression

Sauvegarde :
  - Dossier : invoices/
  - Nom : Facture_yyyyMMdd_HHmmss.html
  - Réutilisable : Peut ouvrir à nouveau
```

---

## 🎯 Fonctionnalités détaillées

### 🚙 Gestion des véhicules

| Feature | Description |
|---------|-------------|
| **Voir liste** | Tableau avec tri/filtre dynamique |
| **Ajouter** | Dialog avec matricule, marque, modèle, prix |
| **Modifier** | État changeable (Disponible ↔ Louée) |
| **Supprimer** | Avec confirmation |
| **Filtrer** | Recherche en temps réel |
| **Trier** | Clic sur header de colonne |

### 👥 Gestion des clients

| Feature | Description |
|---------|-------------|
| **Voir liste** | Tous les clients avec contact |
| **Ajouter** | Dialog avec validation CIN/Email |
| **Modifier** | Via dialog d'ajout |
| **Supprimer** | Avec confirmation |
| **Chercher** | Par CIN ou nom |
| **Valider** | Email et CIN requis |

### 👨‍💼 Gestion des agents

| Feature | Description |
|---------|-------------|
| **Voir liste** | Agents actifs seulement |
| **Ajouter** | Numéro, nom, prénom |
| **Supprimer** | Marque comme inactif |
| **Assigner** | Automatiquement à contrats |

### 📝 Gestion des contrats

| Feature | Description |
|---------|-------------|
| **Créer** | Sélection client/voiture/agent + dates |
| **Calcul auto** | Montant = Prix/jour × Nombre de jours |
| **Paiement** | 4 modes : Espèces, Carte, Virement, Chèque |
| **Statut** | Actif → Facturée |
| **Voiture** | Auto-marquée "Louée" |
| **Actualiser** | Listes se rafraîchissent auto |

### 📄 Factures

| Feature | Description |
|---------|-------------|
| **Générer** | Depuis FormContrat ou PanelContrats |
| **Format** | HTML avec CSS professionnel |
| **Print** | Bouton intégré pour imprimer |
| **PDF** | Enregistrer en PDF depuis navigateur |
| **Stockage** | invoices/Facture_timestamp.html |
| **Réutilisable** | Peut ouvrir à nouveau |

---

## 🚀 Démarrage rapide

### Prérequis

- ✅ Java 8+ (JDK ou JRE)
- ✅ MySQL 8.0+ avec base `fastcar_location`
- ✅ Windows (pour run.bat) ou terminal Linux/Mac

### 3 étapes pour démarrer

**Étape 1 : Vérifier MySQL**
```
La base de données fastcar_location doit exister
Utilisateur : root
Mot de passe : (vide, ou modifier DBConnection.java)
```

**Étape 2 : Compiler**
```bash
# Windows - Double-cliquez run.bat
# OU ligne de commande :
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d bin ^
  src\com\fastcar\model\*.java ^
  src\com\fastcar\dao\*.java ^
  src\com\fastcar\util\*.java ^
  src\com\fastcar\ui\*.java
```

**Étape 3 : Lancer**
```bash
java -cp "lib\mysql-connector-j-9.5.0.jar;bin" com.fastcar.ui.MainFrame
```

---

## ⚙️ Configuration

### Modifier la connexion MySQL

Éditer `src/com/fastcar/dao/DBConnection.java` :

```java
private static final String URL = "jdbc:mysql://localhost:3306/fastcar_location";
private static final String USER = "root";
private static final String PASSWORD = "";  // Votre mot de passe si besoin
```

---

## 🔄 Workflows complets

### Workflow 1 : Créer un contrat et générer facture

```
1. Clic onglet "Nouveau Contrat"
   ↓
2. Sélectionner :
   - Client (dropdown)
   - Voiture (dropdown)
   - Agent (dropdown)
   - Dates début/fin (DD/MM/YYYY)
   - Mode paiement (radio button)
   ↓
3. Le montant se calcule automatiquement
   (Prix/jour × Nombre de jours)
   ↓
4. Clic "Enregistrer Contrat"
   ├─ Contrat créé en BD (statut = "Actif")
   └─ Voiture marquée "Louée" automatiquement
   ↓
5. Clic "Générer Facture"
   ├─ Facture HTML créée et ouverte
   ├─ Contrat marqué "Facturée"
   └─ Voiture marquée "Louée" (confirmation)
   ↓
6. Dans le navigateur : Ctrl+P (Imprimer)
   ├─ Sélectionner "Enregistrer en PDF"
   ↓
7. ✅ Facture PDF créée et sauvegardée
```

### Workflow 2 : Générer facture depuis la liste

```
1. Clic onglet "Gérer Contrats"
   ↓
2. Sélectionner une ligne dans le tableau
   ↓
3. Clic bouton "📄 Générer Facture"
   ├─ Facture HTML s'ouvre
   ├─ Statut contrat → "Facturée"
   └─ Voiture → "Louée"
   ↓
4. Dans navigateur : Imprimer/PDF
   ↓
5. ✅ Facture PDF créée
```

### Workflow 3 : Ajouter un client et louer une voiture

```
1. Clic onglet "Gérer Clients"
   ↓
2. Clic bouton "+" (Ajouter)
   ├─ Dialog s'ouvre
   ├─ Entrer : CIN, Nom, Prénom, Adresse, Tél, Email
   └─ Validation automatique
   ↓
3. Clic "OK" → Client ajouté en BD
   ↓
4. Clic onglet "Nouveau Contrat"
   ├─ Client est maintenant visible dans dropdown
   ├─ Dropdown s'est rafraîchi automatiquement
   ↓
5. Créer contrat normalement (voir Workflow 1)
```

---

## 🗄️ Base de données

### Connexion

```
Type : MySQL
Hôte : localhost
Port : 3306
Base : fastcar_location
Utilisateur : root
Mot de passe : (vide)
```

### Tables

```sql
-- VOITURE
CREATE TABLE VOITURE (
  MATRICULE varchar(20) PRIMARY KEY,
  MARQUE varchar(50),
  MODELE varchar(50),
  PRIX_JOURNALIER decimal(10,2),
  ETAT varchar(20) DEFAULT 'Disponible'
);

-- CLIENT
CREATE TABLE CLIENT (
  CIN varchar(20) PRIMARY KEY,
  NOM varchar(50),
  PRENOM varchar(50),
  ADRESSE varchar(100),
  TELEPHONE varchar(20),
  EMAIL varchar(100)
);

-- AGENT
CREATE TABLE AGENT (
  NUMERO_AGENT varchar(20) PRIMARY KEY,
  NOM varchar(50),
  PRENOM varchar(50),
  EST_ACTIF boolean DEFAULT true
);

-- CONTRAT
CREATE TABLE CONTRAT (
  NUMERO_CONTRAT varchar(20) PRIMARY KEY,
  DATE_DEBUT date,
  DATE_FIN date,
  MONTANT_TOTAL decimal(10,2),
  MODE_PAIEMENT varchar(20),
  STATUT varchar(20) DEFAULT 'Actif',
  CIN_CLIENT varchar(20),
  MATRICULE_VOITURE varchar(20),
  NUMERO_AGENT varchar(20),
  FOREIGN KEY (CIN_CLIENT) REFERENCES CLIENT(CIN),
  FOREIGN KEY (MATRICULE_VOITURE) REFERENCES VOITURE(MATRICULE),
  FOREIGN KEY (NUMERO_AGENT) REFERENCES AGENT(NUMERO_AGENT)
);
```

---

## 📚 Fichiers de documentation

| Fichier | Contenu |
|---------|---------|
| **README.md** | Ce fichier - Guide complet |
| **QUICKSTART.md** | Guide rapide (3 étapes) |
| **ARCHITECTURE.md** | Architecture technique |
| **NETTOYAGE.md** | Résumé du nettoyage |

---

## 🔐 Points importants

✅ **Architecture bien organisée**
- Séparation Model/View/DAO claire
- Facile à maintenir et étendre

✅ **Code robuste**
- Validation complète des données
- Gestion d'erreurs appropriée
- Messages clairs à l'utilisateur

✅ **BD synchronisée**
- Chaque action BD se reflète immédiatement
- Dropdowns rafraîchis automatiquement
- Pas de données obsolètes

✅ **Factures professionnelles**
- Design moderne et imprimable
- Export PDF simple (depuis navigateur)
- Stockage des fichiers

✅ **Dépendances minimales**
- Seulement MySQL JDBC driver
- Pas de libraries inutiles
- Léger et performant

---

## 📞 Troubleshooting

**Erreur : "Connection refused"**
→ MySQL n'est pas en cours d'exécution
→ Démarrer le service MySQL

**Erreur : "Table not found"**
→ Base de données `fastcar_location` manquante
→ Créer la base et les tables

**Facture ne s'ouvre pas**
→ Navigateur par défaut non configuré
→ Ouvrir manuellement invoices/Facture_xxxxx.html

**Dropdown vide dans FormContrat**
→ Aucun client/voiture/agent en BD
→ Ajouter les données via les panneaux correspondants

---

## 🎓 Pour aller plus loin

Consultez **ARCHITECTURE.md** pour :
- Diagramme de flux détaillé
- Patterns de conception utilisés
- Points d'extensibilité
- Bonnes pratiques de code

---

**Version** : 1.0  
**Date** : 6 décembre 2025  
**Statut** : ✅ Production-ready



### Model (Métier)
- `Client.java` : Clients de l'agence (CIN, nom, prénom, adresse, téléphone, email)
- `Voiture.java` : Véhicules (matricule, marque, modèle, prix journalier, état)
- `Contrat.java` : Contrats de location (dates, montant, mode de paiement)
- `Agent.java` : Agents de l'agence (numéro, nom, prénom)

### DAO (Data Access Object)
- `ClientDAO.java` : CRUD pour les clients
- `VoitureDAO.java` : CRUD pour les voitures
- `ContratDAO.java` : CRUD pour les contrats
- `AgentDAO.java` : CRUD pour les agents
- `DBConnection.java` : Gestion de la connexion MySQL

### UI (Interface Utilisateur)
- `MainFrame.java` : Fenêtre principale avec navigation
- `PanelVoitures.java` : Gestion des voitures
- `PanelContrats.java` : Liste des contrats
- `FormContrat.java` : Formulaire de création de contrats

### Utilitaires
- `InvoiceGenerator.java` : Génération de factures PDF/TXT

## 📝 Fonctionnalités

✅ **Gestion des Voitures**
- Afficher liste des voitures
- Filtrer par état (Disponible, Louée, En maintenance)
- Ajouter/Modifier des voitures

✅ **Gestion des Contrats**
- Créer des contrats de location
- Lister les contrats
- Générer des factures

⏳ **Gestion des Clients**
- À implémenter dans `PanelClients.java`

## 🔗 Base de Données

### Tables Principales
- `CLIENT` : Informations des clients
- `AGENT` : Informations des agents
- `VOITURE` : Véhicules disponibles
- `CONTRAT` : Contrats de location
- `PAIEMENT` : Historique des paiements

### Vues Utiles
- `V_VOITURES_DISPONIBLES` : Voitures disponibles pour location
- `V_CONTRATS_ACTIFS` : Contrats actifs avec détails
- `V_REVENUS_MENSUELS` : Revenus mensuels

## ⚙️ Configuration

### Connexion MySQL
Fichier : `src/com/fastcar/dao/DBConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/fastcar_location";
private static final String USER = "root";
private static final String PASSWORD = "";
```

Pour modifier les identifiants, éditez ce fichier et recompilez.

## 🐛 Troubleshooting

### Erreur : "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- Vérifiez que `mysql-connector-j-9.5.0.jar` est dans le dossier `lib/`
- Recompilez et relancez

### Erreur : "Connection refused"
- Vérifiez que MySQL est en cours d'exécution
- Vérifiez les identifiants dans `DBConnection.java`
- Pour XAMPP, démarrez le service MySQL

### Erreur : "Unknown database 'fastcar_location'"
- Vérifiez que le script SQL a bien été exécuté
- La base de données doit s'appeler exactement `fastcar_location`

### L'application se lance mais est vide
- Vérifiez la connexion avec `test-connection.bat`
- Vérifiez que les données ont bien été insérées dans la BD

## 📦 Compilation Manuelle

Pour compiler manuellement :
```bash
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d bin ^
  src\com\fastcar\model\*.java ^
  src\com\fastcar\dao\*.java ^
  src\com\fastcar\util\*.java ^
  src\com\fastcar\ui\*.java
```

Pour exécuter :
```bash
java -cp "lib\mysql-connector-j-9.5.0.jar;bin" com.fastcar.ui.MainFrame
```

## 📞 Support

Pour tout problème :
1. Exécutez `test-connection.bat` pour diagnostiquer
2. Vérifiez que MySQL est bien en cours d'exécution
3. Consultez les logs de la console

## 📄 Licence

Projet FastCar Location - 2025

# 📱 ChatTime - Application de Chat en Temps Réel 

## 📝 Description

ChatTime est une application de messagerie instantanée en temps réel développée en Java avec Android Studio et Firebase. Elle permet aux utilisateurs de s’inscrire, de discuter en direct avec leurs amis, de personnaliser leur profil et de se déconnecter en toute sécurité.

## 🎯 Fonctionnalités

- Authentification Firebase : Inscription et connexion via email et mot de passe
- Liste d’amis : Affichage des contacts disponibles pour discuter
- Chat en temps réel : Envoi et réception de messages instantanés
- Personnalisation du profil : Modification du nom et de la photo de profil
- Déconnexion sécurisée

## 🛠️ Technologies Utilisées

- Java - Langage de développement Android

- Firebase Authentication - Gestion des utilisateurs

- Firebase Firestore - Base de données en temps réel

- Firebase Storage - Stockage des images de profil

- Glide - Chargement et affichage d'images

RecyclerView - Affichage dynamique des messages

## 🚀 Installation & Exécution

### 1️⃣ Cloner le dépôt GitHub

#### git clone https://github.com/ton-utilisateur/chat-app.git
#### cd chat-app

### 2️⃣ Ouvrir le projet dans Android Studio

- Lancer Android Studio

- Sélectionner "Open an existing project"

- Naviguer jusqu'au dossier cloné

### 3️⃣ Configurer Firebase

- Aller sur Firebase Console et créer un projet.

- Ajouter une application Android avec ton package (com.exemple.chatapp).

- Télécharger et ajouter le fichier google-services.json dans app/.

- Activer Authentication (Email/Mot de passe).

- Configurer Cloud Firestore et Storage.

### 4️⃣ Lancer l’application sur un émulateur ou un appareil physique

- Brancher un téléphone ou configurer un émulateur Android

- Exécuter la commande :

 ####  ./gradlew assembleDebug
 
- Ou directement "Run" dans Android Studio
  
## 🛠️ API Firebase : Structure des Données

### 1️⃣ Utilisateurs (users)

##### Stocke les informations des utilisateurs inscrits.

{
  ##### "uid": "123456",
  ##### "nom": "Abdellatif",
  ##### "email": "tayebi@gmail.com",
  ##### "photoURL": "https://firebase.storage.com/profil.jpg",
  ##### "statut": "En ligne"
}

### 2️⃣ Messages (messages)

##### Stocke les messages envoyés et reçus.

{
  ##### "expediteur": "123456",
  ##### "destinataire": "7891011",
  ##### "message": "Salut ! Comment ça va ?",
  ##### "timestamp": "2024-03-28T15:30:00Z"
}

## 💡 Améliorations Futures

###### 🚀 Ajout d’appels vocaux/vidéo
###### 🚀 Système de notifications push avec Firebase Cloud Messaging
###### 🚀 Statut "En ligne / Hors ligne" en temps réel
###### 🚀 Thème sombre / clair

## 📌 Contribution

### 👨‍💻 Tu veux contribuer ?

#### Fork le projet 🍴

###### Crée une branche feature/nom-fonctionnalité 🌿

###### Ajoute tes modifications et fais un commit

###### Envoie une Pull Request 📩

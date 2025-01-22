
# GRT Associatif

### Contexte du projet

Membre engagé dans la vie associative depuis maintenant 4 ans, cette aventure m'a amené à développer, pour l'événement des ***14, 15 et 16 février 2025***, un site web ainsi qu'une API permettant de suivre les statistiques de l'événement. Cela contribue également à offrir davantage de visibilité aux streamers participants, diffusant en direct sur la plateforme Twitch.

### Objectifs du projet  
- ✔️ Fournir un lien vers la page de dons.
- ✔️ Centraliser la liste des streamers de l'événement sur une seule et même plateforme accessible à toutes et à tous.  
- ✔️ Permettre un suivi en direct de la cagnotte des dons.  
- ✔️ Mettre en avant notre sponsor.

## Stack Technique

**Front end :**  
- **Framework :** Angular
- **Stack :** Typescript / HTML / CSS / tailwind

  ![image](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)  ![image](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![image](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)  ![image](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)


**Serveur :**  
- **Stack :** Java 17  
- **Framework :** Spring Boot  
- **Réactivité :** Java WebFlux (SSE)
![image](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)

## FrontEnd 
[Site](https://grt-associatif.fr/)

---

## API
Cette API répond à trois objectifs principaux :

1. Récupérer les données des streamers en ligne via l'API de Twitch. [api twitch](https://dev.twitch.tv/docs/api/reference/#get-streams)

2. Récupérer les donations et le montant de la cagnotte via l'API StreamLabs Charity. [charity](https://streamlabs-charity.readme.io/reference/getting-started-with-your-api)

3. Créer, analyser et redistribuer les informations entre le serveur et le client Angular grâce à une connexion SSE (Server-Sent Events).

---

## Variables d'environnement
Vous devez obtenir ces clés lors de l'enregistrement de votre application sur le site [Twitch Developer API](https://dev.twitch.tv/).

- `TWITCH_CLIENT_ID`
- `TWITCH_CLIENT_SECRET`

Ces variables doivent être enregistrées dans votre système en tant que variables d'environnement ou être passées dans la commande de lancement de votre conteneur Docker.

### Exemple de commande Docker :

```bash
docker run \
  --name apiApp \
  -e TWITCH_CLIENT_ID=your_client_id \
  -e TWITCH_CLIENT_SECRET=your_client_secret \
  --user 1000:1000 \
  --network grtApi \
  -p 8080:8080 \
  your_image
```

**Notes :**  
- `--user 1000:1000` : Permet de retirer les droits root du conteneur.  
- `--network grtApi` : Ajoute le conteneur à un réseau personnalisé que vous devez créer au préalable.  

---

## Documentation de l'API

### Connexion au service SSE

```http
GET /connect
```

| Contenue                     | Type     |  
|----------------------------|----------| 
| Tableau de streamers + donations | Flux SSE |

---

## Documentation Technique

### Système SSE

1. [**Controller SSE**](https://github.com/Szczapa/GrtAssociatif/blob/master/GrtApi/src/main/java/com/example/grt3api/controller/SSEController.java)  
   Endpoint principal pour la connexion au système SSE.

2. [**StreamerCache**](https://github.com/Szczapa/GrtAssociatif/blob/master/GrtApi/src/main/java/com/example/grt3api/utils/StreamCache.java)  
   Gestion des données avant transfert via le flux SSE.

---

### API Twitch

1. [**TwitchService**](https://github.com/Szczapa/GrtAssociatif/blob/master/GrtApi/src/main/java/com/example/grt3api/service/TwitchService.java)  
   - Gestion des tokens d'authentification.  
   - Requêtes pour récupérer les informations des streamers.

---

### API StreamLabs

1. [**StreamLabsService**](https://github.com/Szczapa/GrtAssociatif/blob/master/GrtApi/src/main/java/com/example/grt3api/service/StreamlabsService.java)  
   Récupération des informations relatives aux donations sur la page de l'événement.

---

### Gestion des Tâches

1. [**ScheduleTasks**](https://github.com/Szczapa/GrtAssociatif/blob/master/GrtApi/src/main/java/com/example/grt3api/utils/ScheduleTasks.java)  
   Automatisation des tâches pour la récupération des données toutes les 45 secondes.

---
## Auteur

- [@Szczapa](https://www.github.com/Szczapa)


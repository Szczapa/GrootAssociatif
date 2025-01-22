
# API GRT Associatif


Cette API répond à trois objectifs principaux :

1. Récupérer les données des streamers en ligne via l'API de Twitch.
2. Récupérer les donations et le montant de la cagnotte via l'API StreamLabs.
3. Créer, analyser et redistribuer les informations entre le serveur et le client Angular grâce à une connexion SSE (Server-Sent Events).

---

## Stack Technique

**Serveur :**  
- **Langage :** Java 17  
- **Framework :** Spring Boot  
- **Réactivité :** Java WebFlux  

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

| Retour                     | Type     |  
|----------------------------|----------| 
| Tableau de streamers + donations | Flux SSE |

---

## Documentation Technique

### Système SSE

1. [**Controller SSE**](https://github.com/Szczapa/Event_Api/blob/TestLogg/src/main/java/com/example/grt3api/controller/SSEController.java)  
   Endpoint principal pour la connexion au système SSE.

2. [**StreamerCache**](https://github.com/Szczapa/Event_Api/blob/TestLogg/src/main/java/com/example/grt3api/utils/StreamerCache.java)  
   Gestion des données avant transfert via le flux SSE.

---

### API Twitch

1. [**TwitchService**](https://github.com/Szczapa/Event_Api/blob/TestLogg/src/main/java/com/example/grt3api/service/TwitchService.java)  
   - Gestion des tokens d'authentification.  
   - Requêtes pour récupérer les informations des streamers.

---

### API StreamLabs

1. [**StreamLabsService**](https://github.com/Szczapa/Event_Api/blob/TestLogg/src/main/java/com/example/grt3api/service/StreamlabsService.java)  
   Récupération des informations relatives aux donations sur la page de l'événement.

---

### Gestion des Tâches

1. [**ScheduleTasks**](https://github.com/Szczapa/Event_Api/blob/TestLogg/src/main/java/com/example/grt3api/utils/ScheduleTasks.java)  
   Automatisation des tâches pour la récupération des données toutes les 45 secondes.

---
## Auteur

- [@Szczapa](https://www.github.com/Szczapa)


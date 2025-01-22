package com.example.grt3api.service;

import com.example.grt3api.DTO.StreamerDTO;
import com.example.grt3api.models.Streamer;
import com.example.grt3api.utils.DataSingleton;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class TwitchService {
    private static final Logger logger = LoggerFactory.getLogger(TwitchService.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private final AtomicReference<String> token = new AtomicReference<>(null);
    private final AtomicReference<Instant> tokenExpirationTime = new AtomicReference<>(Instant.now());

    @Value("${twitch.client.id}")
    private String twitchClientId;

    @Value("${twitch.client.secret}")
    private String twitchClientSecret;

    public TwitchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://id.twitch.tv").build();
        this.objectMapper = objectMapper;
    }

    private synchronized void refreshToken() {
        try {
            String response = webClient.post()
                    .uri("/oauth2/token")
                    .bodyValue(String.format(
                            "client_id=%s&client_secret=%s&grant_type=client_credentials",
                            twitchClientId,
                            twitchClientSecret))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            String accessToken = rootNode.get("access_token").asText();
            int expiresIn = rootNode.get("expires_in").asInt();

            token.set(accessToken);
            tokenExpirationTime.set(Instant.now().plusSeconds(expiresIn));

            System.out.println("✅ Token Twitch mis à jour avec succès.");

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du token Twitch : " + e.getMessage(), e);
        }
    }

    private String getValidToken() {
        if (token.get() == null || Instant.now().isAfter(tokenExpirationTime.get())) {
            refreshToken();
        }
        System.out.println(tokenExpirationTime.get().toString());
        return token.get();
    }

    public String getTwitchToken() throws Exception {

        String apiURl = "https://id.twitch.tv/oauth2/token";

        URI uri = new URI(apiURl);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = String.format(
                "client_id=%s&client_secret=%s&grant_type=client_credentials",
                twitchClientId,
                twitchClientSecret
        );

        try {
            OutputStream os = connection.getOutputStream();
            os.write(params.getBytes());
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Erreur lors de la récupération du token, code HTTP: " + responseCode);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        String jsonResponse = response.toString();
        return extractAccessTokenFromResponse(jsonResponse);

    }

    private String extractAccessTokenFromResponse(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        return rootNode.get("access_token").asText();
    }


    public List<StreamerDTO> getStreamerOnline() throws Exception {
        String accessToken = getValidToken();


        List<String> userLogins = DataSingleton.getInstance().loadUserLogins();
        if (userLogins == null || userLogins.isEmpty()) {
            logger.warn("Aucun utilisateur trouvé pour vérifier les streams en ligne.");
            return new ArrayList<>();
        }

        StringBuilder params = new StringBuilder();
        for (String login : userLogins) {
            if (params.length() > 0) {
                params.append("&");
            }
            params.append("user_login=").append(login);
        }

        String baseUrl = "https://api.twitch.tv/helix/streams?";
        String fullUrl = baseUrl + params;

        logger.info("Liste des utilisateurs : {}", userLogins);
        logger.info("URL appelée : {}", fullUrl);

        try {
            // Appel de l'API Twitch.
            String response = webClient.get()
                    .uri(fullUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Client-Id", twitchClientId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Analyse de la réponse pour extraire les données des streamers.
            return parseStreamersResponse(response, userLogins);

        } catch (WebClientResponseException.NotFound e) {
            logger.warn("Aucun streamer en ligne trouvé pour les utilisateurs : {}", userLogins);
            return new ArrayList<>(); // Retourne une liste vide.
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                refreshToken();
                return getStreamerOnline();
            } else {
                throw new RuntimeException("Erreur lors de la récupération des streams : " + e.getMessage(), e);
            }
        }
    }

    private List<StreamerDTO> parseStreamersResponse(String response, List<String> userLogins) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.get("data");

            List<Streamer> streamers = new ArrayList<>();

            for (JsonNode stream : dataNode) {
                String userLogin = stream.get("user_login").asText();
                String title = stream.get("title").asText();
                int viewerCount = stream.get("viewer_count").asInt();
                String thumbnailUrl = stream.get("thumbnail_url").asText();

                streamers.add(new Streamer(userLogin, title, viewerCount, thumbnailUrl));
            }

            List<StreamerDTO> streamersStatus = new ArrayList<>();
            for (String login : userLogins) {
                Optional<Streamer> matchedStreamer = streamers.stream()
                        .filter(streamer -> streamer.getuser_login().equals(login))
                        .findFirst();

                String thumbnailUrl = matchedStreamer.map(Streamer::getUrl).orElse("");
                boolean isOnline = matchedStreamer.isPresent();

                streamersStatus.add(new StreamerDTO(login, isOnline, thumbnailUrl));
            }

            return streamersStatus;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'analyse de la réponse Twitch : " + e.getMessage(), e);
        }
    }

}

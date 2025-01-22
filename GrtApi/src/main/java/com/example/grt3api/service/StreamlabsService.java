package com.example.grt3api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.text.DecimalFormat;

@Service
public class StreamlabsService {

    private static final String API_URL = "https://streamlabscharity.com/api/v1/teams/@grt/grt-reborn-3";

    public String getCharityAmount() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erreur HTTP: " + response.statusCode());
            }


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            int amountRaisedCents = jsonNode.path("amount_raised").asInt(0);

            String currency = jsonNode.path("campaign").path("currency").asText("USD");

            double amount = amountRaisedCents / 100.0;

            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(amount) + " " + (currency.equalsIgnoreCase("USD") ? "$" : "€");

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération du montant";
        }
    }
}

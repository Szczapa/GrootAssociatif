package com.example.grt3api.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class DataSingleton {
    private static DataSingleton instance;
    private List<String> userLogins;
    private final ObjectMapper objectMapper;

    private DataSingleton() {
        this.objectMapper = new ObjectMapper();
    }

    public static synchronized DataSingleton getInstance() {
        if (instance == null) {
            instance = new DataSingleton();
        }
        return instance;
    }

    public synchronized List<String> loadUserLogins() throws Exception {
        if (userLogins != null) {
            return userLogins;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/stream_data.json")))) {
            JsonNode rootNode = objectMapper.readTree(reader);

            JsonNode streamerNode = rootNode.get("Streamer");
            if (streamerNode == null || !streamerNode.isArray()) {
                throw new Exception("Invalid JSON structure: 'Streamer' key is missing or not an array.");
            }

            userLogins = new ArrayList<>();
            for (JsonNode userLogin : streamerNode) {
                userLogins.add(userLogin.asText());
            }
        }
        return userLogins;
    }
}

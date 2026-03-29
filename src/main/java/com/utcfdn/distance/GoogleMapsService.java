package com.utcfdn.distance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api-key:}")
    private String apiKey;

    @Value("${google.maps.base-url:https://maps.googleapis.com/maps/api}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> getDistanceMatrix(String origins, String destinations) {
        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.badRequest().body("{\"status\": \"REQUEST_DENIED\", \"error_message\": \"API Key not configured in backend.\"}");
        }

        String url = String.format(
            "%s/distancematrix/json?origins=%s&destinations=%s&key=%s",
            baseUrl, origins, destinations, apiKey
        );

        return restTemplate.getForEntity(url, String.class);
    }
}

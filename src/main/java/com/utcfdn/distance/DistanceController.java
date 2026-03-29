package com.utcfdn.distance;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/distance")
@RequiredArgsConstructor
public class DistanceController {

    private final GoogleMapsService googleMapsService;

    @GetMapping("/matrix")
    public ResponseEntity<String> getDistanceMatrix(
            @RequestParam String origins,
            @RequestParam String destinations) {
        
        return googleMapsService.getDistanceMatrix(origins, destinations);
    }
}

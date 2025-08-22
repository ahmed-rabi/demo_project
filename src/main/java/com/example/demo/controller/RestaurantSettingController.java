package com.example.demo.controller;

import com.example.demo.entity.RestaurantSetting;
import com.example.demo.service.RestaurantSettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurant-settings")
@CrossOrigin(origins = "*")
public class RestaurantSettingController {
    
    private final RestaurantSettingService restaurantSettingService;

    public RestaurantSettingController(RestaurantSettingService restaurantSettingService) {
        this.restaurantSettingService = restaurantSettingService;
    }

    // GET restaurant settings - Spring Security handles authentication
    @GetMapping
    public ResponseEntity<RestaurantSetting> getRestaurantSettings() {
        RestaurantSetting settings = restaurantSettingService.getSettings();
        if (settings != null) {
            return ResponseEntity.ok(settings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT update restaurant settings
    @PutMapping
    public ResponseEntity<RestaurantSetting> updateRestaurantSettings(@RequestBody RestaurantSetting settings) {
        try {
            RestaurantSetting updatedSettings = restaurantSettingService.updateSettings(settings);
            return ResponseEntity.ok(updatedSettings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST preview settings
    @PostMapping("/preview")
    public ResponseEntity<RestaurantSetting> previewRestaurantSettings(@RequestBody RestaurantSetting settings) {
        try {
            RestaurantSetting previewedSettings = restaurantSettingService.previewSettings(settings);
            return ResponseEntity.ok(previewedSettings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
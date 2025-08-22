

package com.example.demo.controller;

import com.example.demo.entity.RestaurantSetting;
import com.example.demo.service.RestaurantSettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant-settings")
public class RestaurantSettingController {

    private final RestaurantSettingService restaurantSettingService;

    public RestaurantSettingController(RestaurantSettingService restaurantSettingService) {
        this.restaurantSettingService = restaurantSettingService;
    }

    // GET restaurant settings - only ADMIN can access
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantSetting> getRestaurantSettings() {
        RestaurantSetting settings = restaurantSettingService.getSettings();
        if (settings != null) {
            return ResponseEntity.ok(settings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT update restaurant settings - only ADMIN can update
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantSetting> updateRestaurantSettings(@RequestBody RestaurantSetting settings) {
        try {
            RestaurantSetting updatedSettings = restaurantSettingService.updateSettings(settings);
            return ResponseEntity.ok(updatedSettings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST preview settings - only ADMIN can preview
    @PostMapping("/preview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantSetting> previewRestaurantSettings(@RequestBody RestaurantSetting settings) {
        try {
            RestaurantSetting previewedSettings = restaurantSettingService.previewSettings(settings);
            return ResponseEntity.ok(previewedSettings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

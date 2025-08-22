package com.example.demo.service;

import com.example.demo.entity.RestaurantSetting;
import com.example.demo.repository.RestaurantSettingRepository;
import org.springframework.stereotype.Service;

@Service
public class RestaurantSettingService {
    private final RestaurantSettingRepository repository;

    public RestaurantSettingService(RestaurantSettingRepository repository) {
        this.repository = repository;
    }

    // Get the single settings record
    public RestaurantSetting getSettings() {
        return repository.findAll().stream().findFirst().orElse(null);
    }

    // Update settings (Task T1-34)
    public RestaurantSetting updateSettings(RestaurantSetting newSettings) {
        // Get existing settings
        RestaurantSetting existingSettings = getSettings();
        
        if (existingSettings != null) {
            // Update existing record by setting the ID
            newSettings.setId(existingSettings.getId());
        }
        
        return repository.save(newSettings);
    }

    // Preview settings before save (Task T1-35)
    public RestaurantSetting previewSettings(RestaurantSetting settings) {
        // This method doesn't save to database
        // Just returns the settings for preview
        // You can add validation or formatting logic here
        return settings;
    }
}
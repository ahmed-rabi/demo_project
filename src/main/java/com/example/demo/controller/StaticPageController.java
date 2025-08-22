package com.example.demo.controller;

import com.example.demo.entity.StaticPage;
import com.example.demo.service.StaticPageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class StaticPageController {
    private final StaticPageService service;

    public StaticPageController(StaticPageService service) {
        this.service = service;
    }

    // ✅ Get all settings
    @GetMapping
    public List<StaticPage> getAll() {
        return service.getAll();
    }

    // ✅ Get terms & conditions
    @GetMapping("/terms")
    public String getTermsAndConditions() {
        StaticPage setting = service.getFirstSetting();
        return setting != null ? setting.getTermsAndConditions() : null;
    }

    // ✅ Get Facebook URL
    @GetMapping("/facebook")
    public String getFacebookUrl() {
        StaticPage setting = service.getFirstSetting();
        return setting != null ? setting.getFacebookUrl() : null;
    }

    // ✅ Get About Description
    @GetMapping("/about-description")
    public String getAboutDescription() {
        StaticPage setting = service.getFirstSetting();
        return setting != null ? setting.getAboutDescription() : null;
    }

    // ✅ Get WhatsApp Number
    @GetMapping("/whatsapp")
    public String getWhatsappNumber() {
        StaticPage setting = service.getFirstSetting();
        return setting != null ? setting.getWhatsappNumber() : null;
    }

    // ✅ Get phone numbers
    @GetMapping("/phones")
    public List<String> getPhoneNumbers() {
        StaticPage setting = service.getFirstSetting();
        return setting != null ?
                List.of(setting.getPhoneNumber(), setting.getSecondPhoneNumber())
                : List.of();
    }
}
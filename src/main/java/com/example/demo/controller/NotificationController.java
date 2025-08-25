package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Get unseen notifications
    @GetMapping("/{customerId}")
    public List<Notification> getNotifications(@PathVariable Long customerId) {
        return notificationService.getUnseenNotifications(customerId);
    }

    // Mark as seen
    @PutMapping("/{notifId}/seen")
    public String markAsSeen(@PathVariable Long notifId) {
        notificationService.markAsSeen(notifId);
        return "Notification marked as seen";
    }
}

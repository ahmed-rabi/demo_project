package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public List<Notification> getUnseenNotifications(Long customerId) {
        return repo.findByCustomerIdAndSeenFalse(customerId);
    }

    public void markAsSeen(Long notifId) {
        Notification notif = repo.findById(notifId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notif.setSeen(true);
        repo.save(notif);
    }
}

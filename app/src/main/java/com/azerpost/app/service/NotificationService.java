package com.azerpost.app.service;

import com.azerpost.app.model.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    void sendNotification(Long shipmentId);

    List<Notification> getNotificationHistory(Long shipmentId);
}

package com.azerpost.app.controller;

import com.azerpost.app.model.entity.Notification;
import com.azerpost.app.service.NotificationService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void sendNotification(@RequestParam @Positive Long shipmentId){
        notificationService.sendNotification(shipmentId);
    }

    @GetMapping("/{shipmentId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_OPERATOR')")
    public List<Notification> getNotificationHistory(@PathVariable @Positive Long shipmentId){
        return notificationService.getNotificationHistory(shipmentId);
    }
}

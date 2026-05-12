package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ShipmentNotFoundException;
import com.azerpost.app.model.entity.Notification;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.entity.User;
import com.azerpost.app.repository.ShipmentRepository;
import com.azerpost.app.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final ShipmentRepository shipmentRepository;

    @Value("${spring.mail.username}")
    private String mailFrom;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    @Override
    public void sendNotification(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found"));

        User sender = shipment.getSender();
        if (sender == null || sender.getEmail() == null || sender.getEmail().isBlank()) {
            throw new IllegalArgumentException("Shipment sender email is required");
        }

        Context context = new Context();
        context.setVariable("username", sender.getUsername());
        context.setVariable("receiverName", shipment.getReceiverName());
        context.setVariable("trackingNumber", shipment.getTrackingNumber());
        context.setVariable("status", shipment.getStatus() == null ? "Not available" : formatStatus(shipment.getStatus().name()));
        context.setVariable("shipmentType", shipment.getShipmentType());
        context.setVariable("description", shipment.getDescription());
        context.setVariable("deliveryDate", formatDateTime(shipment.getDeliveryDate()));
        context.setVariable("updatedAt", formatDateTime(LocalDateTime.now()));
        context.setVariable("destination", buildDestination(shipment));

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(sender.getEmail());
            helper.setFrom(mailFrom);
            helper.setSubject("Shipment update - Tracking #" + shipment.getTrackingNumber());

            String htmlContent = templateEngine.process("shipment-update", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

            Notification notification = new Notification();
            notification.setShipment(shipment);
            notification.setStatus(shipment.getStatus());
            notification.setTime(LocalDateTime.now());
            shipment.getNotifications().add(notification);
            shipmentRepository.save(shipment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send shipment update email", e);
        }

    }

    @Override
    public List<Notification> getNotificationHistory(Long shipmentId) {
        Shipment shipment = shipmentRepository.findWithNotificationById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found"));
        return shipment.getNotifications();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Not scheduled";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private String formatStatus(String status) {
        return status.replace("_", " ");
    }

    private String buildDestination(Shipment shipment) {
        if (shipment.getAddress() == null) {
            return "Not available";
        }

        return Stream.of(
                        shipment.getAddress().getCountry(),
                        shipment.getAddress().getRegion(),
                        shipment.getAddress().getCity(),
                        shipment.getAddress().getDistrict(),
                        shipment.getAddress().getStreet(),
                        shipment.getAddress().getPostalCode()
                )
                .filter(value -> value != null && !value.isBlank())
                .reduce((first, second) -> first + ", " + second)
                .orElse("Not available");
    }
}

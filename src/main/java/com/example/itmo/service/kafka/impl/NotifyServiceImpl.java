package com.example.itmo.service.kafka.impl;

import com.example.itmo.service.kafka.NotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final KafkaTemplate<String, String> template;

    @Value("${kafka.topics.notification}")
    private String notificationTopic;

    @Override
    public void sendNotification(String msg) {

        try {
            template.send(notificationTopic, String.valueOf(UUID.randomUUID()), msg)
                    .addCallback(result -> {
                        if (result != null) {
                            log.info("notification send successful");
                        }
                    }, ex -> log.error("notification send error"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

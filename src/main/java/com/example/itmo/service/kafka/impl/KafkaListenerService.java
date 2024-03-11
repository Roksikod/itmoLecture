package com.example.itmo.service.kafka.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListenerService {

    @KafkaListener(topics = "${kafka.topics.notification}")
    public void eventListener(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                              @Payload String message) {

//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Car car = mapper.readValue(message, Car.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        log.info(message);


    }


    @KafkaListener(topics = "topic")
    public void anotherEventListener(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                     @Payload String message) {


    }

}

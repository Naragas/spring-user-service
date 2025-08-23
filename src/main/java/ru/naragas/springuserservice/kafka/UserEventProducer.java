package ru.naragas.springuserservice.kafka;


import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.naragas.springuserservice.dto.UserEventDTO;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/18/2025
 */
@Service
@AllArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, UserEventDTO> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public void sendUserEvent(UserEventDTO eventDTO){
        kafkaTemplate.send(TOPIC, eventDTO);
    }

}

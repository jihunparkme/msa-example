package com.msa.member.framework.kafkaadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msa.member.domain.model.event.EventResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventProducer {

    @Value(value = "${producers.topic1.name}")
    private String TOPIC;
    private final KafkaTemplate<String, EventResult> kafkaTemplate;


    public void occurEvent(EventResult result) throws JsonProcessingException {
        ListenableFuture<SendResult<String, EventResult>> future = this.kafkaTemplate.send(TOPIC, result);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, EventResult> result) {
                EventResult g = result.getProducerRecord().value();
                log.info("Sent message=[{}] with offset=[{}]", g.getEventType(), result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable t) {
                // needed to do compensation transaction.
                log.error("Unable to send message=[{}] due to : {}", result.getEventType(), t.getMessage(), t);
            }
        });
    }
}
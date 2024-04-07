package com.msa.member.framework.kafkaadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.member.application.usecase.SavePointUseCase;
import com.msa.member.application.usecase.UsePointUseCase;
import com.msa.member.domain.model.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventConsumers {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SavePointUseCase savePointUsecase;
    private final UsePointUseCase usePointUsecase;
    private final MemberEventProducer eventProducer;

    @KafkaListener(topics = "${consumer.topic1.name}", groupId = "${consumer.groupid.name}")
    public void consumeRent(ConsumerRecord<String, String> record) throws IOException {
        log.info("rental_rent: {}", record.value());
        ItemRented itemRented = objectMapper.readValue(record.value(), ItemRented.class);
        savePointUsecase.savePoint(itemRented.getIdName(), itemRented.getPoint());
    }

    @KafkaListener(topics = "${consumer.topic2.name}", groupId = "${consumer.groupid.name}")
    public void consumeReturn(ConsumerRecord<String, String> record) throws IOException {
        log.info("rental_return: {}", record.value());
        ItemReturned itemReturned = objectMapper.readValue(record.value(), ItemReturned.class);
        savePointUsecase.savePoint(itemReturned.getIdName(), itemReturned.getPoint());
    }

    @KafkaListener(topics = "${consumer.topic3.name}", groupId = "${consumer.groupid.name}")
    public void consumeClear(ConsumerRecord<String, String> record) throws Exception {
        OverdueCleared overdueCleared = objectMapper.readValue(record.value(), OverdueCleared.class);

        EventResult eventResult = new EventResult();
        eventResult.setEventType(EventType.OVERDUE);
        eventResult.setIdName(overdueCleared.getIdName());
        eventResult.setPoint(overdueCleared.getPoint());

        System.out.printf(record.value());
        try {
            usePointUsecase.userPoint(overdueCleared.getIdName(), overdueCleared.getPoint());
            eventResult.setSuccess(true);
        } catch (Exception e) {
            eventResult.setSuccess(false);
        }
        eventProducer.occurEvent(eventResult);
    }

    @KafkaListener(topics = "${consumer.topic4.name}", groupId = "${consumer.groupid.name}")
    public void consumeUsePoint(ConsumerRecord<String, String> record) throws Exception {
        log.info(record.value());
        PointUseCommand pointUseCommand = objectMapper.readValue(record.value(), PointUseCommand.class);
        try {
            usePointUsecase.userPoint(pointUseCommand.getIdName(), pointUseCommand.getPoint());
        } catch (Exception e) {
            throw e;
        }

    }
}

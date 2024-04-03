package com.msa.rental.framework.kafkaadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msa.rental.application.outputport.EventOutputPort;
import com.msa.rental.domain.model.event.ItemRented;
import com.msa.rental.domain.model.event.ItemReturned;
import com.msa.rental.domain.model.event.OverdueCleared;
import com.msa.rental.domain.model.event.PointUseCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalKafkaProducer implements EventOutputPort {

    @Value(value = "${producers.topic1.name}")
    private String TOPIC_RENT;
    @Value(value = "${producers.topic2.name}")
    private String TOPIC_RETURN;
    @Value(value = "${producers.topic3.name}")
    private String TOPIC_CLEAR;
    @Value(value = "${producers.topic4.name}")
    private String TOPIC_POINT;

    private final KafkaTemplate<String, ItemRented> kafkaTemplate1;
    private final KafkaTemplate<String, ItemReturned> kafkaTemplate2;
    private final KafkaTemplate<String, OverdueCleared> kafkaTemplate3;
    private final KafkaTemplate<String, PointUseCommand> kafkaTemplate4;

    @Override
    public void occurRentalEvent(ItemRented itemRented) throws JsonProcessingException {
        ListenableFuture<SendResult<String, ItemRented>> future = kafkaTemplate1.send(TOPIC_RENT, itemRented);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, ItemRented> result) {
                ItemRented g = result.getProducerRecord().value();
                log.info("Sent message=[{}] with offset=[{}]", g.getItem().getNo(), result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[] due to : {}", itemRented.getItem().getNo(), ex.getMessage(), ex);
            }

        });
    }

    @Override
    public void occurReturnEvent(ItemReturned itemReturned) throws JsonProcessingException {
        ListenableFuture<SendResult<String, ItemReturned>> future = this.kafkaTemplate2.send(TOPIC_RETURN, itemReturned);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, ItemReturned> result) {
                ItemReturned g = result.getProducerRecord().value();
                log.info("Sent message=[{}] with offset=[{}]", g.getItem().getNo(), result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[] due to : {}", itemReturned.getItem().getNo(), ex.getMessage(), ex);
            }
        });
    }

    @Override
    public void occurOverdueClearedEvent(OverdueCleared overdueCleared) throws JsonProcessingException {
        ListenableFuture<SendResult<String, OverdueCleared>> future = this.kafkaTemplate3.send(TOPIC_CLEAR, overdueCleared);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, OverdueCleared> result) {
                OverdueCleared g = result.getProducerRecord().value();
                log.info("Sent message=[{}] with offset=[{}]", g.getIdName().getId(), result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[] due to : {}", overdueCleared.getIdName().getId(), ex.getMessage(), ex);
            }
        });
    }

    @Override
    public void occurPointUseCommand(PointUseCommand pointUseCommand) {
        ListenableFuture<SendResult<String, PointUseCommand>> future = this.kafkaTemplate4.send(TOPIC_POINT, pointUseCommand);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, PointUseCommand> result) {
                PointUseCommand g = result.getProducerRecord().value();
                log.info("Sent message=[{}] with offset=[{}]", g.getIdName().getId(), result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[] due to : {}", pointUseCommand.getIdName().getId(), ex.getMessage(), ex);
            }
        });

    }
}

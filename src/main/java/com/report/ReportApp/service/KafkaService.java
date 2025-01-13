package com.report.ReportApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.report.ReportApp.config.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.report.ReportApp.Constants;


import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {
    private KafkaTemplate<String, JsonNode>kafkaTemplate;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public KafkaService(KafkaTemplate<String, JsonNode> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public CompletableFuture<Void> raiseSendEmailEvent(JsonNode payloadForService){
      return CompletableFuture.runAsync(()->{
          try{
              this.kafkaTemplate.send(Constants.raiseEmailEvent,payloadForService);
              logger.info("Kafka Event Raised SuccessFully Topic Has Been Produced");

          }catch (Exception e){
              logger.error("FailTo Raiase Kakfa Event");
          }
      });
    }
}

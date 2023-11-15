package com.bridgingcode.springbootactivemqdemo.publisher.controller;

import com.bridgingcode.springbootactivemqdemo.model.SystemMessage;
//import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author AJ Catambay of Bridging Code
 *
 */
@RestController
public class PublishController {
Logger logger= LoggerFactory.getLogger(PublishController.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/publishMessage")
    public ResponseEntity<String> publishMessage(@RequestBody SystemMessage systemMessage) {
        try {
            jmsTemplate.setDeliveryDelay(1000);
            logger.info("Messsage recived");
//            for (int i = 0; i < 30; i++) {
//                systemMessage.setMessage(systemMessage.getMessage().concat(Integer.toString()));
            jmsTemplate.convertAndSend("bridgingcode-queue", systemMessage,postProcessor->{
                postProcessor.setLongProperty("AMQ_SCHEDULED_DELAY",100000);
//                postProcessor.setLongProperty("AMQ_SCHEDULED_PERIOD",100000);
                postProcessor.setLongProperty("AMQ_SCHEDULED_REPEAT",3);
                return postProcessor;
            });
//            jmsTemplate.convertAndSend("bridgingcode-queue",systemMessage);
//            }

            return new ResponseEntity<>("Sent.", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

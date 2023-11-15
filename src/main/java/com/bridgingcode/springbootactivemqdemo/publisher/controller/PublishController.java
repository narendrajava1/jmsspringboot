package com.bridgingcode.springbootactivemqdemo.publisher.controller;

import com.bridgingcode.springbootactivemqdemo.model.SystemMessage;
//import org.apache.activemq.ScheduledMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;

/**
 * @author AJ Catambay of Bridging Code
 */
@RestController
public class PublishController {
    Logger logger = LoggerFactory.getLogger(PublishController.class);
    @Autowired
    private JmsTemplate jmsTemplate;


    @PostMapping("/publishMessage")
    public ResponseEntity<String> publishMessage(@RequestBody SystemMessage systemMessage) throws JMSException, JsonProcessingException {
        Connection connection = this.jmsTemplate.getConnectionFactory().createConnection();
        Session session = connection.createSession();
        Queue queue = session.createQueue("bridgingcode-queue");
        MessageProducer producer = session.createProducer(queue);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMessage message = session.createObjectMessage(systemMessage);
        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1000);
        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1000);
        message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 9);
        producer.send(message);
        return new ResponseEntity<>("Sent.", HttpStatus.OK);
    }
}

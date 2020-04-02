package org.ludo.experiment.solace.controller;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.qpid.jms.JmsQueue;
import org.apache.qpid.jms.JmsTopic;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(JmsTestController.PATH)
@Validated
public class JmsTestController {
    public static final String PATH = "/jms";

    private static final String TEST_TOPIC = "T/TestTopic";
    private static final String TEST_QUEUE = "Q/TestQueue";

    private final JmsTemplate jmsTemplate;

    public JmsTestController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @GetMapping("/send/T/{topic}/{message}")
    public void sendTopic(@PathVariable String topic, @PathVariable String message) {
        doSend("T/" + topic, message);
    }

    @GetMapping("/send/T/{topic}/{subTopic}/{message}")
    public void sendTopic(@PathVariable String topic, @PathVariable String subTopic, @PathVariable String message) {
        doSend("T/" + topic + "/" + subTopic, message);
    }

    @GetMapping("/send/Q/{queue}/{message}")
    public void sendQueue(@PathVariable String queue, @PathVariable String message) {
        doSend("Q/" + queue, message);
    }

    private void doSend(String destination, String message) {
        Destination jmsDestination = null;
        if (destination.startsWith("Q/")) {
            jmsDestination = new JmsQueue(destination);
        } else if (destination.startsWith("T/")) {
            jmsDestination = new JmsTopic(destination);
        }
        jmsTemplate.convertAndSend(jmsDestination, message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setBooleanProperty("SOLACE_JMS_PROP_DEAD_MSG_QUEUE_ELIGIBLE", true);
                return message;
            }
        });
    }

    @JmsListener(destination = TEST_TOPIC, containerFactory = "topicListenerContainerFactory")
    public void receiveFromShortTopic(String message) {
        System.out.println("From Topic (short):" + message);
    }

    @JmsListener(destination = TEST_QUEUE, containerFactory = "queueListenerContainerFactory")
    public void receiveFromQueue(String message, Message messageObject) {
        System.out.println("From Queue:" + message);
        throw new IllegalArgumentException("failed");
    }
}

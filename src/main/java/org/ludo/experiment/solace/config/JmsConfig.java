package org.ludo.experiment.solace.config;

import javax.jms.ConnectionFactory;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
@ConditionalOnMissingBean(ConnectionFactory.class)
public class JmsConfig {
    @Bean
    public JmsConnectionFactory jmsConnectionFactory(JmsConfigurationProperties props) {
        JmsConnectionFactory factory = new JmsConnectionFactory();
        factory.setRemoteURI(props.getUri());
        factory.setUsername(props.getUsername());
        factory.setPassword(props.getPassword());
        return factory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(JmsConnectionFactory jmsConnectionFactory) {
        return new CachingConnectionFactory(jmsConnectionFactory);
    }

    @Bean
    public JmsTemplate jmsTemplate(CachingConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setPubSubDomain(true);
//        jmsTemplate.setExplicitQosEnabled(true);
//        jmsTemplate.setTimeToLive(3000);
        jmsTemplate.setDeliveryPersistent(true);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory topicListenerContainerFactory(JmsConnectionFactory jmsConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(jmsConnectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory queueListenerContainerFactory(JmsConnectionFactory jmsConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(jmsConnectionFactory);
        return factory;
    }
}

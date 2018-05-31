package com.microservice

import org.apache.activemq.camel.component.ActiveMQComponent
import org.apache.activemq.jms.pool.PooledConnectionFactory
import org.apache.activemq.spring.ActiveMQConnectionFactory
import org.apache.camel.CamelContext
import org.apache.camel.component.jms.JmsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ConfigBeans {
    
    /*
     * ActiveMQ broker config
     */
    
    @Primary
    @Bean
    public ActiveMQConnectionFactory getConnectionFactory() {
        String brokerURL = "tcp://172.30.167.19:61616"
        
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerURL);
        return connectionFactory;
    }
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public PooledConnectionFactory getPooledConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(10);
        pooledConnectionFactory.setConnectionFactory(getConnectionFactory());
        return pooledConnectionFactory;
    }
    
    @Bean
    public JmsConfiguration getJmsConfiguration() {
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setConnectionFactory(getPooledConnectionFactory());
        return jmsConfiguration;
    }
    
    @Bean
    public JmsConfiguration getJmsHighPriorityConfiguration() {
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setConnectionFactory(getPooledConnectionFactory());
        jmsConfiguration.setPriority(8);
        return jmsConfiguration;
    }

    //@Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConfiguration(getJmsConfiguration());
        camelContext.addComponent("activemq", activeMQComponent);

        ActiveMQComponent activeMQHighPriorityComponent = new ActiveMQComponent();
        activeMQHighPriorityComponent.setConfiguration(getJmsHighPriorityConfiguration());
        camelContext.addComponent("activemq-high-priority", activeMQHighPriorityComponent);
    }
    
    /*
     * Camel REST config
     */
    
    @Bean
    RestConfigBean restConfigBean(){
        RestConfigBean bean = new RestConfigBean()
        
        bean.with {
            host =      "${System.getenv('API_SERVER_ADDRESS')?.trim() ?: '0.0.0.0'}"?.toString()
            adminPort = "${System.getenv('ADMIN_SERVER_PORT')?.trim() ?: '9080'}"?.toInteger()
            apiPort =   "${System.getenv('API_SERVER_PORT')?.trim() ?: '8080'}"?.toInteger()
            protocol =  "${System.getenv('API_REST_SERVER_PROTOCOL')?.trim() ?: 'http'}"?.toString()
        }
        
        return bean
    }

}




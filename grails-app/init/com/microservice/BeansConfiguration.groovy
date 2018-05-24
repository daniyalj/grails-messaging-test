package com.microservice

import org.apache.camel.CamelContext
import org.apache.camel.ConsumerTemplate
import org.apache.camel.ProducerTemplate
import org.apache.camel.spring.CamelBeanPostProcessor
import org.apache.camel.spring.boot.CamelContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 */

@Configuration
class BeansConfiguration {
    
    @Autowired
    ApplicationContext appContext
    
    private static int PRODUCER_CACHE_SIZE = 100
    private static int CONSUMER_CACHE_SIZE = 100
    
    @Bean
    ProducerTemplate producerTemplate(CamelContext camelContext) {
        return camelContext.createProducerTemplate(PRODUCER_CACHE_SIZE)
    }
    
    @Bean
    ConsumerTemplate consumerTemplate(CamelContext camelContext) {
        return camelContext.createConsumerTemplate(CONSUMER_CACHE_SIZE)
    }
    
    @Bean
    CamelBeanPostProcessor camelBeanPostProcessor() {
        CamelBeanPostProcessor processor = new CamelBeanPostProcessor()
        processor.setApplicationContext(appContext)
        return processor
    }
    
    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            void beforeApplicationStart(CamelContext camelContext) {
                
                // prepare for graceful camel shutdown
                camelContext.getShutdownStrategy().setSuppressLoggingOnTimeout(false)
                camelContext.getShutdownStrategy().setLogInflightExchangesOnTimeout(true)
            }
            
            @Override
            void afterApplicationStart(CamelContext camelContext) {
                
            }
        }
    }
    
}



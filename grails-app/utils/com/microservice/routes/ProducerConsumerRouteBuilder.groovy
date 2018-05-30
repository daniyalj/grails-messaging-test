package com.microservice.routes

import com.microservice.ProducerConsumerService
import com.microservice.dtos.LogMessage
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.dataformat.JsonLibrary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProducerConsumerRouteBuilder extends RouteBuilder {
    
    static Integer MAX_PRODUCER_BATCH_SIZE = 50
    static Integer producerBatchSize = 10
    static Integer maxConcurrentConsumers = 20
    
    @Autowired
    ProducerTemplate producerTemplate
    
    @Autowired
    ProducerConsumerService producerConsumerService
    
    void configure() {
        
        from("timer:producer-timer?delay=1000&period=500&fixedRate=true")
            .routeId('producer-timer')
            //.to('log:producer-timer')
            .setHeader('batchRecipientList', constant(''))
            .process { Exchange exchange ->
                    String targetRoute = 'direct:producer-create-instance'
                    String batchRecipientList = ''
                    if (producerBatchSize == 1) {
                        batchRecipientList = targetRoute
                    } else if (producerBatchSize > 1) {
                        Integer tmpBatchSize = producerBatchSize < MAX_PRODUCER_BATCH_SIZE ? producerBatchSize : MAX_PRODUCER_BATCH_SIZE
                        batchRecipientList = ( [1..tmpBatchSize].collect { it.collect { targetRoute }.join(',') }[0] )
                        //println batchRecipientList
                    }
                    //exchange.in.setBody(batchRecipientList, String.class)
                    exchange.in.headers['batchRecipientList'] = batchRecipientList
                }
            .to("log:producer-timer?showHeaders=true")
            //.recipientList(simple('${body}')).parallelProcessing()
            .recipientList( header('batchRecipientList'), ',' ).parallelProcessing()

        from('direct:producer-create-instance')
            .routeId('producer-create-instance')
            .process { Exchange exchange ->
                    exchange.in.setBody(producerConsumerService.createInstance(), LogMessage.class)
                }
            .marshal().json(JsonLibrary.Jackson, LogMessage.class, true)
            .to("log:producer-create-instance")
            .to('direct:producer-queue')
        
        String producerQueueName = "producerconsumer.1.producer-queue"
        
        from('direct:producer-queue')
            .routeId('producer-queue')
            .marshal().json(JsonLibrary.Jackson, LogMessage.class, true)
            .to("activemq:queue:${producerQueueName}")
        
        from("activemq:queue:${producerQueueName}?maxConcurrentConsumers=${maxConcurrentConsumers}")
            .routeId('consumer-from-queue')
            .unmarshal().json(JsonLibrary.Jackson, LogMessage.class)
            .to("log:consumer-from-queue")
            .process { Exchange exchange ->
                    LogMessage logMessage = exchange.in.getBody(LogMessage)
                    exchange.in.headers['resolveInstanceResult'] = producerConsumerService.resolveInstance(logMessage?.logMessageId)
                }
            .delay(200)

    }
}

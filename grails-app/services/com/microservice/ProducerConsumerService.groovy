package com.microservice

import com.microservice.dtos.LogMessage
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class ProducerConsumerService {
    
    static final HashMap<String,Object> inflightInstances = [:]
    
    LogMessage createInstance() {
        LogMessage logMessage = new LogMessage()
        
        logMessage.logMessageId = UUID.randomUUID().toString()
        logMessage.logMessage = logMessage.logMessageId
        logMessage.dateCreated = new Date()
        logMessage.lastUpdated = logMessage.dateCreated
        
        pushInstance(logMessage.logMessageId, logMessage)
        
        return logMessage
    }
    
    Integer pushInstance(String key, Object object) {
        synchronized (inflightInstances) {
            inflightInstances[key] = object
            
            return inflightInstances.size()
        }
    }
    
    Integer resolveInstance(String key) {
        sleep(200)
        synchronized (inflightInstances) {
            
            inflightInstances.remove(key)
            
            return inflightInstances.size()
        }
    }
    
    
    
}



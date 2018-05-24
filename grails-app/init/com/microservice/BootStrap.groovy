package com.microservice

import org.apache.camel.CamelContext
import org.springframework.beans.factory.annotation.Autowired

class BootStrap {
    
    @Autowired
    CamelContext camelContext
    
    def init = { servletContext ->
        
        // enable graceful camel shutdown
        camelContext.getShutdownStrategy().setSuppressLoggingOnTimeout(false)
        camelContext.getShutdownStrategy().setLogInflightExchangesOnTimeout(true)
        
    }
    
    def destroy = {
        // graceful camel shutdown
        camelContext.stop()
    }
}

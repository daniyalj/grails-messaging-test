
## Environment Variables

```

export API_SERVER_ADDRESS=localhost
export API_SERVER_PORT=8080
export ADMIN_SERVER_PORT=9080
export API_REST_SERVER_PROTOCOL=http
export AMQ_HOST=localhost
export AMQ_PORT=61616
export AMQ_USER=mquser
export AMQ_PASS=mqpass

```


## Curl Request(s)





## In grails web console:

http://localhost:9080/console#new


*MAKE SURE TO CLEAR THE QUEUE*


kick off the load:
```

com.microservice.routes.ProducerConsumerRouteBuilder.producerBatchSize = 100

synchronized(com.microservice.ProducerConsumerService?.inflightInstances) {
println "${com.microservice.ProducerConsumerService?.inflightInstances?.toString()}"

com.microservice.ProducerConsumerService?.inflightInstances?.size()
}

```

monitor:
```

synchronized(com.microservice.ProducerConsumerService?.inflightInstances) {
com.microservice.ProducerConsumerService?.inflightInstances?.size()
}

```

ramp down:
```

com.microservice.routes.ProducerConsumerRouteBuilder.producerBatchSize = 0

synchronized(com.microservice.ProducerConsumerService?.inflightInstances) {
com.microservice.ProducerConsumerService?.inflightInstances?.size()
}



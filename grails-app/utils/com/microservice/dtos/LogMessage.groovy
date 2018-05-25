package com.microservice.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.ToString


@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class LogMessage {

    String logMessageId = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    Date dateCreated = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    Date lastUpdated = null
    
    String logMessage = null
    
}

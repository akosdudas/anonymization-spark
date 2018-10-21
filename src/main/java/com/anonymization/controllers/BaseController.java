package com.anonymization.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;


public class BaseController {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleControllerException(Exception ex) throws Exception {
        LOGGER.info(ex.getMessage());
        throw ex;
    }

    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }
}


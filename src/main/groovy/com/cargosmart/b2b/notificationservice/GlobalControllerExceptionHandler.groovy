package com.cargosmart.b2b.notificationservice

import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Global exception handler
 */
@Slf4j
@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ResponseObject> handleException(final Exception ex) {
        log.error(ex.message);
        return new ResponseEntity<ResponseObject>(new ResponseObject(systemMessage: ex.message), HttpStatus.BAD_REQUEST);
    }

}

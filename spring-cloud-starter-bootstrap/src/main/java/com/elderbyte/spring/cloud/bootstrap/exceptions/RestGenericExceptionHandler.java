package com.elderbyte.spring.cloud.bootstrap.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A controller advice which deals with unhandled exceptions and transforms them into
 * HTTP responses.
 */
@ControllerAdvice
public class RestGenericExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Map NotFoundException to NOT_FOUND HTTP CODE
     */
    @ExceptionHandler(value = NotFoundException.class)
    public void handleNotFound(
            HttpServletRequest req,
            HttpServletResponse reponse,
            Exception exception) throws IOException {

        String agrMessages = ExceptionUtil.aggregateMessages(exception);

        logger.warn("NotFoundException: " + agrMessages + " resource: " +  req.getRequestURL());

        reponse.sendError(HttpStatus.NOT_FOUND.value(), agrMessages);
    }


    /**
     * Generic Exception handler translates them to INTERNAL_SERVER_ERROR Http codes
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public void handleUnhandled(
            HttpServletRequest req,
            HttpServletResponse reponse,
            Exception exception) throws IOException {

        String agrMessages = ExceptionUtil.aggregateMessages(exception);

        logger.error("Unhandled Exception: " + agrMessages + " resource: " +  req.getRequestURL(), exception);

        reponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.value() + " - Server Error: " + agrMessages);
    }


}
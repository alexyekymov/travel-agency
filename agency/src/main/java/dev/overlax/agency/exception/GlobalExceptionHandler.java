package dev.overlax.agency.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(EntityNotFoundException ex, Model model) {
        log.debug("Resource not found: {}", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        return "error/404";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResource(NoResourceFoundException ex, Model model) {
        log.debug("Static resource not found: {}", ex.getResourcePath());
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        return "error/404";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadPathVariable(MethodArgumentTypeMismatchException ex, Model model) {
        log.debug("Invalid path variable: {}: {}", ex.getName(), ex.getValue());
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        return "error/4xx";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpected(Exception ex, Model model) {
        log.error("Unexpected error: {}: {}", ex.getClass().getName(), ex.getMessage());
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error/5xx";
    }
}

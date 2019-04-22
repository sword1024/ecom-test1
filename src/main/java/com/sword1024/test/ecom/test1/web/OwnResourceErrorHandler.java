package com.sword1024.test.ecom.test1.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.util.Optional;

@RestControllerAdvice(assignableTypes = OwnResource.class)
@Slf4j
public class OwnResourceErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error(e.getMessage(), e);

        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            String msg = "Неверный запрос" + Optional.ofNullable(ex.getBindingResult().getFieldError())
                    .map(fe -> ": " + fe.getField() + " " + fe.getDefaultMessage()).orElse("");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        } else if (e instanceof HttpMessageNotReadableException || e instanceof ValidationException) {
            return new ResponseEntity<>("Неверный запрос", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Внутренняя ошибка", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

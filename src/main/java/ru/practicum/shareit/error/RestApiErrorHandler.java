package ru.practicum.shareit.error;

import java.util.Map;
import javax.validation.ConstraintViolationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class RestApiErrorHandler {

    @ExceptionHandler({
        ConstraintViolationException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        IllegalArgumentException.class
    })
    public ResponseEntity<?> handleBadRequest(Exception ex, WebRequest request) {
        String error = ex.getMessage();
        if (ex.getCause() != null) {
            Throwable cause = ex.getCause();
            if (cause instanceof ConversionFailedException) {
                ConversionFailedException conversionEx = (ConversionFailedException) cause;
                if (conversionEx.getCause() != null) {
                    error = conversionEx.getCause().getMessage();
                }
            }
        }

        Map<String, String> msg = Map.of("error", error);
        return new ResponseEntity<>(msg, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}

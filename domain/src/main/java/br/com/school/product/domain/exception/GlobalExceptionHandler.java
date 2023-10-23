package br.com.school.product.domain.exception;

import br.com.school.product.domain.validation.Error;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException domainException) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(domainException));
    }

    record ApiError(String domain, List<Error> errors) {

        static ApiError from(final DomainException domainException) {
            return new ApiError(domainException.getMessage(), domainException.getErrors());
        }
    }
}

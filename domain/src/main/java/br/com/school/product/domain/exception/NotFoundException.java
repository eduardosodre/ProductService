package br.com.school.product.domain.exception;

import br.com.school.product.domain.validation.Error;
import java.util.ArrayList;
import java.util.List;

public class NotFoundException extends DomainException {

    private NotFoundException(String message,
                              List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException create(String message) {
        return new NotFoundException(message, new ArrayList<>());
    }
}

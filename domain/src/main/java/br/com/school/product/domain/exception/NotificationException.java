package br.com.school.product.domain.exception;

import br.com.school.product.domain.validation.NotificationValidation;

public class NotificationException extends DomainException {

    public NotificationException(final String message,
                                 final NotificationValidation notificationValidation) {
        super(message, notificationValidation.getErrors());
    }
}

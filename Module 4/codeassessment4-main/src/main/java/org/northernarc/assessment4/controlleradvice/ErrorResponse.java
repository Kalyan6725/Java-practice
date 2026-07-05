package org.northernarc.assessment4.controlleradvice;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String status,
        String message,
        LocalDateTime timestamp,
        List<String> errors
) {
}

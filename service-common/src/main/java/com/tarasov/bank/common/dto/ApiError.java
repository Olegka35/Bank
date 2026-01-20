package com.tarasov.bank.common.dto;

import java.time.LocalDateTime;

public record ApiError(
        String message,
        String service,
        LocalDateTime time
) {
}

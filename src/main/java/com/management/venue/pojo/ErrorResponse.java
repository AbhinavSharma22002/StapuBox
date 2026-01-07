package com.management.venue.pojo;

import java.time.LocalDateTime;

public record ErrorResponse(
    String message,
    String errorCode,
    LocalDateTime timestamp,
    String details
) {}
package com.vcs.finstream.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private final String message;
    private final int status;
    private final LocalDateTime timestamp;
}
package org.devin.yozma.qa.platform.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final List<String> messages;

    public ValidationError(int status, String error) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.messages = new ArrayList<>();
    }

    public void addMessage(String message) {
        messages.add(message);
    }
}
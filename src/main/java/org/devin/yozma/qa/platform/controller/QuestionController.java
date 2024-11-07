package org.devin.yozma.qa.platform.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devin.yozma.qa.platform.dto.QuestionRequest;
import org.devin.yozma.qa.platform.dto.QuestionResponse;
import org.devin.yozma.qa.platform.exception.QuestionNotFoundException;
import org.devin.yozma.qa.platform.exception.ValidationError;
import org.devin.yozma.qa.platform.model.Question;
import org.devin.yozma.qa.platform.model.VoteResult;
import org.devin.yozma.qa.platform.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionRequest request) {
        QuestionResponse response = questionService.createQuestion(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<VoteResult> vote(@PathVariable Long id, @RequestBody String answer) {
        return ResponseEntity.ok(questionService.vote(id, answer));
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<String> handleQuestionNotFound(QuestionNotFoundException e) {
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ValidationError errors = new ValidationError(400, "Validation Failed");

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            if (error instanceof FieldError) {
                String field = ((FieldError) error).getField();
                message = field + ": " + message;
            }
            errors.addMessage(message);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

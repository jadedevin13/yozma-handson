package org.devin.yozma.qa.platform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.devin.yozma.qa.platform.dto.QuestionRequest;

import java.util.HashSet;

public class AnswersValidator implements ConstraintValidator<AnswersConstraint, QuestionRequest> {

    @Override
    public boolean isValid(QuestionRequest request, ConstraintValidatorContext context) {
        if (request.getType() == null) {
            return true; // Let @NotNull handle this
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        switch (request.getType()) {
            case POLL -> {
                if (isAnswersInvalid(request)) {
                    context.buildConstraintViolationWithTemplate(
                                    "Poll questions must have between 2 and 10 answers")
                            .addConstraintViolation();
                    isValid = false;
                }
            }
            case TRIVIA -> {
                if (isAnswersInvalid(request)) {
                    context.buildConstraintViolationWithTemplate(
                                    "Trivia questions must have between 2 and 10 answers")
                            .addConstraintViolation();
                    isValid = false;
                }
                if (request.getCorrectAnswers() == null || request.getCorrectAnswers().isEmpty()) {
                    context.buildConstraintViolationWithTemplate(
                                    "Trivia questions must have at least one correct answer")
                            .addConstraintViolation();
                    isValid = false;
                } else if (!new HashSet<>(request.getAnswers()).containsAll(request.getCorrectAnswers())) {
                    context.buildConstraintViolationWithTemplate(
                                    "All correct answers must be from the provided answers list")
                            .addConstraintViolation();
                    isValid = false;
                } else if (request.getCorrectAnswers().size() > request.getAnswers().size()) {
                    context.buildConstraintViolationWithTemplate(
                                    "Number of correct answers cannot exceed total number of answers")
                            .addConstraintViolation();
                    isValid = false;
                }
            }
            default -> {
                // Future question types might not need answers
                isValid = true;
            }
        }

        return isValid;
    }

    private boolean isAnswersInvalid(QuestionRequest request) {
        return request.getAnswers() == null ||
                request.getAnswers().isEmpty() ||
                request.getAnswers().size() < 2 ||
                request.getAnswers().size() > 10;
    }
}
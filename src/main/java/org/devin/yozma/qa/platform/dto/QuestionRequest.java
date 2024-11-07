package org.devin.yozma.qa.platform.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.devin.yozma.qa.platform.model.QuestionType;
import org.devin.yozma.qa.platform.validation.AnswersConstraint;

import java.util.List;
import java.util.Set;

@Data
@AnswersConstraint
public class QuestionRequest {
    @NotNull(message = "Question type is required")
    private QuestionType type;

    @NotBlank(message = "Question text is required")
    @Size(min = 3, max = 500, message = "Question text must be between 3 and 500 characters")
    private String text;

    private List<@NotBlank(message = "Individual answer text cannot be blank") String> answers;

    private Set<String> correctAnswers;
}
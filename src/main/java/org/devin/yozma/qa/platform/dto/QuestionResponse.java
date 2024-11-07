package org.devin.yozma.qa.platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.devin.yozma.qa.platform.model.QuestionType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record QuestionResponse(
        Long id,
        String text,
        QuestionType type,
        List<String> answers,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Set<String> correctAnswers,
        Map<String, Integer> votes
) {
    // Compact constructor to ensure immutability
    public QuestionResponse {
        answers = List.copyOf(answers);
        votes = Map.copyOf(votes);
        if (correctAnswers != null) {
            correctAnswers = Set.copyOf(correctAnswers);
        }
    }
}
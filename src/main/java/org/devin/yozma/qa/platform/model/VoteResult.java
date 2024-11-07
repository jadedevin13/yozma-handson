package org.devin.yozma.qa.platform.model;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VoteResult(
        Map<String, Integer> votes,
        Boolean isCorrect,
        Set<String> correctAnswers
) {
    public VoteResult {
        votes = Map.copyOf(votes);
        if (correctAnswers != null) {
            correctAnswers = Set.copyOf(correctAnswers);
        }
    }
}
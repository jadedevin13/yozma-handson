package org.devin.yozma.qa.platform.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class TriviaQuestion extends Question {
    private final Set<String> correctAnswers;

    public TriviaQuestion(String text, List<String> answers, Set<String> correctAnswers) {
        super(text, answers, QuestionType.TRIVIA);
        if (!new HashSet<>(answers).containsAll(correctAnswers)) {
            throw new IllegalArgumentException("All correct answers must be in answers list");
        }
        this.correctAnswers = Set.copyOf(correctAnswers); // Immutable copy
    }

    @Override
    public VoteResult vote(String answer) {
        if (!getAnswers().contains(answer)) {
            throw new IllegalArgumentException("Invalid answer");
        }
        getVotes().merge(answer, 1, Integer::sum);
        return new VoteResult(getVotes(), correctAnswers.contains(answer), correctAnswers);
    }
}
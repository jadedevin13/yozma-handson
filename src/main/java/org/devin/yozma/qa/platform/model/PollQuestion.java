package org.devin.yozma.qa.platform.model;

import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public final class PollQuestion extends Question {
    public PollQuestion(String text, List<String> answers) {
        super(text, answers, QuestionType.POLL);
    }

    @Override
    public VoteResult vote(String answer) {
        if (!getAnswers().contains(answer)) {
            throw new IllegalArgumentException("Invalid answer");
        }
        getVotes().merge(answer, 1, Integer::sum);
        return new VoteResult(getVotes(), null, null);
    }
}

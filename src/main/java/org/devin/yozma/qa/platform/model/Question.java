package org.devin.yozma.qa.platform.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public sealed abstract class Question permits PollQuestion, TriviaQuestion {
    private Long id;
    private final String text;
    private final List<String> answers;
    private Map<String, Integer> votes;
    private final QuestionType type;

    protected Question(String text, List<String> answers, QuestionType type) {
        this.text = text;
        this.answers = List.copyOf(answers);
        this.votes = new ConcurrentHashMap<>();
        this.type = type;
        answers.forEach(answer -> votes.put(answer, 0));
    }

    public abstract VoteResult vote(String answer);
}

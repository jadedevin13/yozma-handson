package org.devin.yozma.qa.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.devin.yozma.qa.platform.model.QuestionType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerEntity> answers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CorrectAnswerEntity> correctAnswers = new HashSet<>();

    public void addAnswer(AnswerEntity answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }

    public void addCorrectAnswer(AnswerEntity answer) {
        CorrectAnswerEntity correctAnswer = new CorrectAnswerEntity();
        correctAnswer.setQuestion(this);
        correctAnswer.setAnswer(answer);
        correctAnswers.add(correctAnswer);
    }
}
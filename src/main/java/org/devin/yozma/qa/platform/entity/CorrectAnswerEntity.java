package org.devin.yozma.qa.platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Entity
@Table(name = "correct_answers")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"question", "answer"})
public class CorrectAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private AnswerEntity answer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CorrectAnswerEntity that)) return false;
        return Objects.equals(answer, that.answer) &&
                Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer, question);
    }
}

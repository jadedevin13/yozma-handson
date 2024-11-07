package org.devin.yozma.qa.platform.mapper;

import org.devin.yozma.qa.platform.dto.QuestionRequest;
import org.devin.yozma.qa.platform.entity.QuestionEntity;
import org.devin.yozma.qa.platform.mapper.QuestionMapper;
import org.devin.yozma.qa.platform.model.QuestionType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionMapperTest {
    private final QuestionMapper mapper = new QuestionMapper();

    @Test
    void mapPollRequestToEntity() {
        // Given
        QuestionRequest request = new QuestionRequest();
        request.setType(QuestionType.POLL);
        request.setText("Test poll");
        request.setAnswers(List.of("A", "B", "C"));

        // When
        QuestionEntity entity = mapper.toEntity(request);

        // Then
        assertThat(entity.getText()).isEqualTo("Test poll");
        assertThat(entity.getType()).isEqualTo(QuestionType.POLL);
        assertThat(entity.getAnswers()).hasSize(3);
        assertThat(entity.getCorrectAnswers()).isEmpty();
    }

    @Test
    void mapTriviaRequestToEntity() {
        // Given
        QuestionRequest request = new QuestionRequest();
        request.setType(QuestionType.TRIVIA);
        request.setText("Test trivia");
        request.setAnswers(List.of("A", "B", "C"));
        request.setCorrectAnswers(Set.of("A", "B"));

        // When
        QuestionEntity entity = mapper.toEntity(request);

        // Then
        assertThat(entity.getText()).isEqualTo("Test trivia");
        assertThat(entity.getType()).isEqualTo(QuestionType.TRIVIA);
        assertThat(entity.getAnswers()).hasSize(3);
        assertThat(entity.getCorrectAnswers()).hasSize(2);
    }
}
package org.devin.yozma.qa.platform.mapper;

import lombok.extern.slf4j.Slf4j;
import org.devin.yozma.qa.platform.dto.QuestionRequest;
import org.devin.yozma.qa.platform.dto.QuestionResponse;
import org.devin.yozma.qa.platform.entity.AnswerEntity;
import org.devin.yozma.qa.platform.entity.CorrectAnswerEntity;
import org.devin.yozma.qa.platform.entity.QuestionEntity;
import org.devin.yozma.qa.platform.model.PollQuestion;
import org.devin.yozma.qa.platform.model.Question;
import org.devin.yozma.qa.platform.model.QuestionType;
import org.devin.yozma.qa.platform.model.TriviaQuestion;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class QuestionMapper {


    public QuestionEntity toEntity(QuestionRequest request) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setText(request.getText());
        questionEntity.setType(request.getType());

        // Create map to store AnswerEntity references
        Map<String, AnswerEntity> answerMap = new HashMap<>();

        // Create and add answers
        request.getAnswers().forEach(answerText -> {
            AnswerEntity answerEntity = new AnswerEntity(answerText);
            questionEntity.addAnswer(answerEntity);
            answerMap.put(answerText, answerEntity);
        });

        // Handle correct answers for trivia questions
        if (request.getType() == QuestionType.TRIVIA &&
                request.getCorrectAnswers() != null &&
                !request.getCorrectAnswers().isEmpty()) {

            log.debug("Processing correct answers: {}", request.getCorrectAnswers());

            request.getCorrectAnswers().forEach(correctAnswerText -> {
                AnswerEntity answerEntity = answerMap.get(correctAnswerText);
                if (answerEntity != null) {
                    log.debug("Adding correct answer: {}", correctAnswerText);
                    questionEntity.addCorrectAnswer(answerEntity);
                } else {
                    log.warn("Answer not found for correct answer: {}", correctAnswerText);
                }
            });

            log.debug("Total correct answers added: {}", questionEntity.getCorrectAnswers().size());
        }

        return questionEntity;
    }

    public QuestionResponse toResponse(QuestionEntity entity) {
        List<String> answers = entity.getAnswers().stream()
                .map(AnswerEntity::getText)
                .collect(Collectors.toList());

        Set<String> correctAnswers = Collections.emptySet();
        if (entity.getType() == QuestionType.TRIVIA) {
            correctAnswers = entity.getCorrectAnswers().stream()
                    .map(ca -> ca.getAnswer().getText())
                    .collect(Collectors.toSet());
        }

        return new QuestionResponse(
                entity.getId(),
                entity.getText(),
                entity.getType(),
                answers,
                correctAnswers,
                getVotesMap(entity)
        );
    }

    public Question toModel(QuestionEntity entity) {
        List<String> answers = entity.getAnswers().stream()
                .map(AnswerEntity::getText)
                .collect(Collectors.toList());

        Map<String, Integer> votes = getVotesMap(entity);

        return switch (entity.getType()) {
            case POLL -> {
                PollQuestion question = new PollQuestion(
                        entity.getText(),
                        answers
                );
                question.setId(entity.getId());
                question.setVotes(votes);
                yield question;
            }
            case TRIVIA -> {
                Set<String> correctAnswers = entity.getCorrectAnswers().stream()
                        .map(ca -> ca.getAnswer().getText())
                        .collect(Collectors.toSet());

                TriviaQuestion question = new TriviaQuestion(
                        entity.getText(),
                        answers,
                        correctAnswers
                );
                question.setId(entity.getId());
                question.setVotes(votes);
                yield question;
            }
            default -> throw new IllegalArgumentException("Unsupported question type: " + entity.getType());
        };
    }

    private Map<String, Integer> getVotesMap(QuestionEntity entity) {
        return entity.getAnswers().stream()
                .collect(Collectors.toMap(
                        AnswerEntity::getText,
                        AnswerEntity::getVoteCount,
                        (v1, v2) -> v1, // In case of duplicates, take first value
                        LinkedHashMap::new // Preserve order
                ));
    }
}

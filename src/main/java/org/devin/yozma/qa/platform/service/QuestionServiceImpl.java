package org.devin.yozma.qa.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devin.yozma.qa.platform.dto.QuestionRequest;
import org.devin.yozma.qa.platform.dto.QuestionResponse;
import org.devin.yozma.qa.platform.entity.AnswerEntity;
import org.devin.yozma.qa.platform.entity.QuestionEntity;
import org.devin.yozma.qa.platform.exception.QuestionNotFoundException;
import org.devin.yozma.qa.platform.mapper.QuestionMapper;
import org.devin.yozma.qa.platform.model.Question;
import org.devin.yozma.qa.platform.model.QuestionType;
import org.devin.yozma.qa.platform.model.VoteResult;
import org.devin.yozma.qa.platform.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Transactional
    @Override
    public QuestionResponse createQuestion(QuestionRequest request) {

        QuestionEntity entity = questionMapper.toEntity(request);
        log.debug("Before save - correct answers count: {}", entity.getCorrectAnswers().size());

        entity = questionRepository.save(entity);

        // Force initialization of correct answers
        entity.getCorrectAnswers().size();
        log.debug("After save - correct answers count: {}", entity.getCorrectAnswers().size());

        // Verify correct answers are properly loaded
        Set<String> correctAnswerTexts = entity.getCorrectAnswers().stream()
                .map(ca -> ca.getAnswer().getText())
                .collect(Collectors.toSet());
        log.debug("Correct answer texts after save: {}", correctAnswerTexts);

        return questionMapper.toResponse(entity);
    }

    @Transactional
    @Override
    public Question getQuestion(Long id) {
        QuestionEntity entity = questionRepository.findByIdWithAnswers(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));
        return questionMapper.toModel(entity);
    }

    @Transactional
    public VoteResult vote(Long id, String answer) {
        QuestionEntity entity = questionRepository.findByIdWithAnswers(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        AnswerEntity answerEntity = entity.getAnswers().stream()
                .filter(a -> a.getText().equals(answer))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid answer"));

        answerEntity.incrementVoteCount();
        entity = questionRepository.save(entity);

        Map<String, Integer> votes = entity.getAnswers().stream()
                .collect(Collectors.toMap(
                        AnswerEntity::getText,
                        AnswerEntity::getVoteCount
                ));

        if (entity.getType() == QuestionType.TRIVIA) {
            Set<String> correctAnswers = entity.getCorrectAnswers().stream()
                    .map(ca -> ca.getAnswer().getText())
                    .collect(Collectors.toSet());

            boolean isCorrect = correctAnswers.contains(answer);
            return new VoteResult(votes, isCorrect, correctAnswers);
        }

        return new VoteResult(votes, null, null);
    }
}
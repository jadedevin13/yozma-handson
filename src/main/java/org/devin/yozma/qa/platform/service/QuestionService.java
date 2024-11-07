package org.devin.yozma.qa.platform.service;

import jakarta.transaction.Transactional;
import org.devin.yozma.qa.platform.dto.QuestionRequest;
import org.devin.yozma.qa.platform.dto.QuestionResponse;
import org.devin.yozma.qa.platform.model.Question;
import org.devin.yozma.qa.platform.model.VoteResult;

public interface QuestionService {
    @Transactional
    QuestionResponse createQuestion(QuestionRequest request);

    @Transactional
    Question getQuestion(Long id);

    @Transactional
    VoteResult vote(Long id, String answerText);
}

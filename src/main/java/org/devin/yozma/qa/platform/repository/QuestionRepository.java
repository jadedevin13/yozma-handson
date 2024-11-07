package org.devin.yozma.qa.platform.repository;

import org.devin.yozma.qa.platform.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    @Query("SELECT DISTINCT q FROM QuestionEntity q " +
            "LEFT JOIN FETCH q.answers " +
            "LEFT JOIN FETCH q.correctAnswers " +
            "WHERE q.id = :id")
    Optional<QuestionEntity> findByIdWithAnswers(@Param("id") Long id);
}
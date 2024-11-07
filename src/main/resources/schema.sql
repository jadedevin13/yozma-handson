DROP TABLE IF EXISTS correct_answers;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;

CREATE TABLE questions (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           text VARCHAR(255) NOT NULL,
                           type VARCHAR(50) NOT NULL
);

CREATE TABLE answers (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         question_id BIGINT,
                         text VARCHAR(255) NOT NULL,
                         vote_count INTEGER DEFAULT 0,
                         FOREIGN KEY (question_id) REFERENCES questions(id)
);

CREATE TABLE correct_answers (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 question_id BIGINT,
                                 answer_id BIGINT,
                                 FOREIGN KEY (question_id) REFERENCES questions(id),
                                 FOREIGN KEY (answer_id) REFERENCES answers(id)
);
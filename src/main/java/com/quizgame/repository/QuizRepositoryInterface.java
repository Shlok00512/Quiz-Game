package com.quizgame.repository;

import com.quizgame.model.Quiz;

public interface QuizRepositoryInterface {
    void save(Quiz quiz);
    Quiz findById(String id);
}


package com.quizgame.repository;

import com.quizgame.model.Quiz;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("prod")
public class InMemoryQuizRepository implements QuizRepositoryInterface {
    private final Map<String, Quiz> quizStore = new ConcurrentHashMap<>();

    public void save(Quiz quiz) {
        quizStore.put(quiz.getId(), quiz);
    }

    public Quiz findById(String id) {
        return quizStore.get(id);
    }

    public void deleteAll() {
        quizStore.clear();
    }

    public int count() {
        return quizStore.size();
    }
}



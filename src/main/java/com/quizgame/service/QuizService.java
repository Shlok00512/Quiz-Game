package com.quizgame.service;

import com.quizgame.model.*;
import com.quizgame.repository.QuizRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final SqsService sqsService;

    public QuizService(QuizRepository quizRepository, SqsService sqsService) {
        this.quizRepository = quizRepository;
        this.sqsService = sqsService;
    }

    public Quiz startQuiz(String playerId) {
        Quiz quiz = new Quiz();
        quiz.setId(UUID.randomUUID().toString());
        quiz.setPlayerId(playerId);
        quiz.setQuestions(createSampleQuestions());
        quiz.setCurrentQuestionIndex(0);
        quiz.setScore(0);
        quiz.setStartTime(System.currentTimeMillis());
        quiz.setStatus("ACTIVE");
        
        quizRepository.save(quiz);
        return quiz;
    }

    public Quiz getQuiz(String quizId) {
        return quizRepository.findById(quizId);
    }

    public Map<String, Object> submitAnswer(Answer answer) {
        Quiz quiz = quizRepository.findById(answer.getQuizId());
        if (quiz == null || !"ACTIVE".equals(quiz.getStatus())) {
            throw new RuntimeException("Quiz not found or not active");
        }

        Question currentQuestion = quiz.getQuestions().get(quiz.getCurrentQuestionIndex());
        
        // Check if time expired
        long elapsedTime = (System.currentTimeMillis() - quiz.getStartTime()) / 1000;
        long questionStartTime = quiz.getCurrentQuestionIndex() * currentQuestion.getTimeLimit();
        boolean timeExpired = elapsedTime > (questionStartTime + currentQuestion.getTimeLimit());
        
        boolean correct = !timeExpired && 
                         currentQuestion.getCorrectAnswer() == answer.getSelectedAnswer();
        
        if (correct) {
            quiz.setScore(quiz.getScore() + 10);
        }

        // Send real-time score update to SQS
        ScoreUpdate scoreUpdate = new ScoreUpdate();
        scoreUpdate.setQuizId(quiz.getId());
        scoreUpdate.setPlayerId(quiz.getPlayerId());
        scoreUpdate.setScore(quiz.getScore());
        scoreUpdate.setCorrect(correct);
        scoreUpdate.setTimestamp(System.currentTimeMillis());
        sqsService.sendScoreUpdate(scoreUpdate);

        // Move to next question
        quiz.setCurrentQuestionIndex(quiz.getCurrentQuestionIndex() + 1);
        if (quiz.getCurrentQuestionIndex() >= quiz.getQuestions().size()) {
            quiz.setStatus("COMPLETED");
        }

        quizRepository.save(quiz);

        Map<String, Object> result = new HashMap<>();
        result.put("correct", correct);
        result.put("currentScore", quiz.getScore());
        result.put("quizStatus", quiz.getStatus());
        result.put("timeExpired", timeExpired);
        return result;
    }

    private List<Question> createSampleQuestions() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q1");
        q1.setText("What is the capital of France?");
        q1.setOptions(Arrays.asList("London", "Berlin", "Paris", "Madrid"));
        q1.setCorrectAnswer(2);
        q1.setTimeLimit(10);
        questions.add(q1);

        Question q2 = new Question();
        q2.setId("q2");
        q2.setText("What is 2 + 2?");
        q2.setOptions(Arrays.asList("3", "4", "5", "6"));
        q2.setCorrectAnswer(1);
        q2.setTimeLimit(5);
        questions.add(q2);

        Question q3 = new Question();
        q3.setId("q3");
        q3.setText("Which programming language is used for Spring Boot?");
        q3.setOptions(Arrays.asList("Python", "Java", "JavaScript", "Ruby"));
        q3.setCorrectAnswer(1);
        q3.setTimeLimit(10);
        questions.add(q3);

        return questions;
    }
}

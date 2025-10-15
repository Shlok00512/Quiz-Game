package com.quizgame.service;

import com.quizgame.model.*;
import com.quizgame.repository.QuizRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class QuizService {
    private final QuizRepositoryInterface quizRepository;
    private final SqsService sqsService;

    public QuizService(QuizRepositoryInterface quizRepository, @Autowired(required = false) SqsService sqsService) {
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

        // Send real-time score update to SQS (if available)
        if (sqsService != null) {
            ScoreUpdate scoreUpdate = new ScoreUpdate();
            scoreUpdate.setQuizId(quiz.getId());
            scoreUpdate.setPlayerId(quiz.getPlayerId());
            scoreUpdate.setScore(quiz.getScore());
            scoreUpdate.setCorrect(correct);
            scoreUpdate.setTimestamp(System.currentTimeMillis());
            sqsService.sendScoreUpdate(scoreUpdate);
        }

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
        q1.setText("What year was Nike founded?");
        q1.setOptions(Arrays.asList("1964", "1971", "1980", "1985"));
        q1.setCorrectAnswer(0);
        q1.setTimeLimit(15);
        questions.add(q1);

        Question q2 = new Question();
        q2.setId("q2");
        q2.setText("What is Nike's famous slogan?");
        q2.setOptions(Arrays.asList("Impossible is Nothing", "Just Do It", "Think Different", "I'm Lovin' It"));
        q2.setCorrectAnswer(1);
        q2.setTimeLimit(10);
        questions.add(q2);

        Question q3 = new Question();
        q3.setId("q3");
        q3.setText("Who designed the iconic Nike Swoosh logo?");
        q3.setOptions(Arrays.asList("Phil Knight", "Carolyn Davidson", "Bill Bowerman", "Michael Jordan"));
        q3.setCorrectAnswer(1);
        q3.setTimeLimit(15);
        questions.add(q3);

        Question q4 = new Question();
        q4.setId("q4");
        q4.setText("What was Nike originally called before changing its name?");
        q4.setOptions(Arrays.asList("Blue Ribbon Sports", "Air Sport Company", "American Athletics", "Knight Footwear"));
        q4.setCorrectAnswer(0);
        q4.setTimeLimit(15);
        questions.add(q4);

        Question q5 = new Question();
        q5.setId("q5");
        q5.setText("Which basketball legend has a signature shoe line with Nike (Air Jordan)?");
        q5.setOptions(Arrays.asList("LeBron James", "Kobe Bryant", "Michael Jordan", "Magic Johnson"));
        q5.setCorrectAnswer(2);
        q5.setTimeLimit(10);
        questions.add(q5);

        return questions;
    }
}


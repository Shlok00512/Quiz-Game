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

        Question q6 = new Question();
        q6.setId("q6");
        q6.setText("What is the approximate market cap of Nike (as of 2024)?");
        q6.setOptions(Arrays.asList("$50 billion", "$160 billion", "$250 billion", "$320 billion"));
        q6.setCorrectAnswer(1);
        q6.setTimeLimit(20);
        questions.add(q6);

        Question q7 = new Question();
        q7.setId("q7");
        q7.setText("Who are the co-founders of Nike?");
        q7.setOptions(Arrays.asList("Phil Knight & Steve Jobs", "Phil Knight & Bill Bowerman", "Michael Jordan & Phil Knight", "Bill Gates & Bill Bowerman"));
        q7.setCorrectAnswer(1);
        q7.setTimeLimit(15);
        questions.add(q7);

        Question q8 = new Question();
        q8.setId("q8");
        q8.setText("In which year did Nike sign Michael Jordan?");
        q8.setOptions(Arrays.asList("1980", "1984", "1988", "1992"));
        q8.setCorrectAnswer(1);
        q8.setTimeLimit(15);
        questions.add(q8);

        Question q9 = new Question();
        q9.setId("q9");
        q9.setText("What is the name of Nike's innovation lab in Oregon?");
        q9.setOptions(Arrays.asList("Innovation Hub", "Nike Sport Research Lab", "Tech Center Portland", "Bowerman Building"));
        q9.setCorrectAnswer(1);
        q9.setTimeLimit(20);
        questions.add(q9);

        Question q10 = new Question();
        q10.setId("q10");
        q10.setText("Which famous soccer player had a lifetime deal with Nike?");
        q10.setOptions(Arrays.asList("Lionel Messi", "Cristiano Ronaldo", "Neymar Jr", "Kylian Mbappé"));
        q10.setCorrectAnswer(1);
        q10.setTimeLimit(15);
        questions.add(q10);

        Question q11 = new Question();
        q11.setId("q11");
        q11.setText("How much did Carolyn Davidson get paid for designing the Nike Swoosh?");
        q11.setOptions(Arrays.asList("$35", "$350", "$3,500", "$35,000"));
        q11.setCorrectAnswer(0);
        q11.setTimeLimit(20);
        questions.add(q11);

        Question q12 = new Question();
        q12.setId("q12");
        q12.setText("What is Nike's corporate headquarters called?");
        q12.setOptions(Arrays.asList("Nike Campus", "World Headquarters", "Nike World Campus", "Swoosh Center"));
        q12.setCorrectAnswer(2);
        q12.setTimeLimit(15);
        questions.add(q12);

        Question q13 = new Question();
        q13.setId("q13");
        q13.setText("Which Nike shoe line became a cultural icon in the 1980s?");
        q13.setOptions(Arrays.asList("Nike Cortez", "Air Max", "Air Jordan", "Nike Dunk"));
        q13.setCorrectAnswer(2);
        q13.setTimeLimit(15);
        questions.add(q13);

        Question q14 = new Question();
        q14.setId("q14");
        q14.setText("What does Nike's Air technology contain in the sole?");
        q14.setOptions(Arrays.asList("Oxygen", "Nitrogen", "Carbon Dioxide", "Pressurized Gas"));
        q14.setCorrectAnswer(3);
        q14.setTimeLimit(20);
        questions.add(q14);

        Question q15 = new Question();
        q15.setId("q15");
        q15.setText("Which tennis legend has been sponsored by Nike since 2003?");
        q15.setOptions(Arrays.asList("Serena Williams", "Roger Federer", "Rafael Nadal", "Novak Djokovic"));
        q15.setCorrectAnswer(2);
        q15.setTimeLimit(15);
        questions.add(q15);

        return questions;
    }
}


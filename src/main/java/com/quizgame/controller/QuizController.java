package com.quizgame.controller;

import com.quizgame.model.*;
import com.quizgame.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startQuiz(@RequestParam String playerId) {
        Quiz quiz = quizService.startQuiz(playerId);
        Map<String, Object> response = new HashMap<>();
        response.put("quizId", quiz.getId());
        response.put("question", getCurrentQuestionSafe(quiz));
        response.put("questionNumber", quiz.getCurrentQuestionIndex() + 1);
        response.put("totalQuestions", quiz.getQuestions().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuiz(@PathVariable String quizId) {
        Quiz quiz = quizService.getQuiz(quizId);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("quizId", quiz.getId());
        response.put("playerId", quiz.getPlayerId());
        response.put("score", quiz.getScore());
        response.put("status", quiz.getStatus());
        
        if ("ACTIVE".equals(quiz.getStatus())) {
            response.put("question", getCurrentQuestionSafe(quiz));
            response.put("questionNumber", quiz.getCurrentQuestionIndex() + 1);
            response.put("totalQuestions", quiz.getQuestions().size());
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/answer")
    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestBody Answer answer) {
        try {
            Map<String, Object> result = quizService.submitAnswer(answer);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }

    private Map<String, Object> getCurrentQuestionSafe(Quiz quiz) {
        Question q = quiz.getQuestions().get(quiz.getCurrentQuestionIndex());
        Map<String, Object> questionData = new HashMap<>();
        questionData.put("id", q.getId());
        questionData.put("text", q.getText());
        questionData.put("options", q.getOptions());
        questionData.put("timeLimit", q.getTimeLimit());
        return questionData;
    }
}


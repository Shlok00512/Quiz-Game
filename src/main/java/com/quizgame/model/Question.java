package com.quizgame.model;

import lombok.Data;
import java.util.List;

@Data
public class Question {
    private String id;
    private String text;
    private List<String> options;
    private int correctAnswer;
    private int timeLimit; // seconds
}

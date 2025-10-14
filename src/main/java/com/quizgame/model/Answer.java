package com.quizgame.model;

import lombok.Data;

@Data
public class Answer {
    private String quizId;
    private String questionId;
    private int selectedAnswer;
}

package com.quizgame.model;

import lombok.Data;
import java.util.List;

@Data
public class Quiz {
    private String id;
    private String playerId;
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private long startTime;
    private String status; // ACTIVE, COMPLETED
}



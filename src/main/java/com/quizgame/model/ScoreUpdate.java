package com.quizgame.model;

import lombok.Data;

@Data
public class ScoreUpdate {
    private String quizId;
    private String playerId;
    private int score;
    private boolean correct;
    private long timestamp;
}


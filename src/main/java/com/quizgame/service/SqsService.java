package com.quizgame.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizgame.model.ScoreUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsService {
    private final AmazonSQS amazonSQS;
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Value("${aws.sqs.queue.name:quiz-scores}")
    private String queueName;

    public SqsService(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    public void sendScoreUpdate(ScoreUpdate update) {
        try {
            String queueUrl = getOrCreateQueue();
            String message = mapper.writeValueAsString(update);
            amazonSQS.sendMessage(queueUrl, message);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    private String getOrCreateQueue() {
        try {
            GetQueueUrlResult result = amazonSQS.getQueueUrl(queueName);
            return result.getQueueUrl();
        } catch (QueueDoesNotExistException e) {
            CreateQueueResult createResult = amazonSQS.createQueue(queueName);
            return createResult.getQueueUrl();
        }
    }
}

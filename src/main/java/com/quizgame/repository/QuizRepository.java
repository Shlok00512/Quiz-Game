package com.quizgame.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizgame.model.Quiz;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
@Profile("!prod")
public class QuizRepository implements QuizRepositoryInterface {
    private final DynamoDB dynamoDB;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String TABLE_NAME = "Quizzes";

    public QuizRepository(AmazonDynamoDB amazonDynamoDB) {
        this.dynamoDB = new DynamoDB(amazonDynamoDB);
        createTableIfNotExists(amazonDynamoDB);
    }

    private void createTableIfNotExists(AmazonDynamoDB client) {
        try {
            client.describeTable(TABLE_NAME);
        } catch (ResourceNotFoundException e) {
            CreateTableRequest request = new CreateTableRequest()
                .withTableName(TABLE_NAME)
                .withKeySchema(new KeySchemaElement("id", KeyType.HASH))
                .withAttributeDefinitions(new AttributeDefinition("id", ScalarAttributeType.S))
                .withBillingMode(BillingMode.PAY_PER_REQUEST);
            client.createTable(request);
        }
    }

    public void save(Quiz quiz) {
        Table table = dynamoDB.getTable(TABLE_NAME);
        Item item = Item.fromJSON(toJson(quiz));
        table.putItem(item);
    }

    public Quiz findById(String id) {
        Table table = dynamoDB.getTable(TABLE_NAME);
        Item item = table.getItem("id", id);
        return item != null ? fromJson(item.toJSON(), Quiz.class) : null;
    }

    private String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


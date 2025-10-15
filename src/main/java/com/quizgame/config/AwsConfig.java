package com.quizgame.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class AwsConfig {

    @Value("${aws.region:us-east-1}")
    private String region;

    @Value("${aws.accessKey:test}")
    private String accessKey;

    @Value("${aws.secretKey:test}")
    private String secretKey;

    @Value("${aws.dynamodb.endpoint:}")
    private String dynamoDbEndpoint;

    @Value("${aws.sqs.endpoint:}")
    private String sqsEndpoint;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        
        if (!dynamoDbEndpoint.isEmpty()) {
            builder.withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(dynamoDbEndpoint, region));
            builder.withCredentials(new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(accessKey, secretKey)));
        } else {
            builder.withRegion(region);
        }
        
        return builder.build();
    }

    @Bean
    public AmazonSQS amazonSQS() {
        AmazonSQSClientBuilder builder = AmazonSQSClientBuilder.standard();
        
        if (!sqsEndpoint.isEmpty()) {
            builder.withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(sqsEndpoint, region));
            builder.withCredentials(new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(accessKey, secretKey)));
        } else {
            builder.withRegion(region);
        }
        
        return builder.build();
    }
}


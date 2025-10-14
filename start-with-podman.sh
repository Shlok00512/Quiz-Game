#!/bin/bash

echo "🚀 Starting Local AWS Services with Podman"
echo "==========================================="
echo ""

# Stop and remove existing containers
echo "🧹 Cleaning up old containers..."
podman stop quiz-game-dynamodb quiz-game-sqs 2>/dev/null
podman rm quiz-game-dynamodb quiz-game-sqs 2>/dev/null

# Start DynamoDB Local
echo "🎬 Starting DynamoDB Local..."
podman run -d \
  --name quiz-game-dynamodb \
  -p 8000:8000 \
  amazon/dynamodb-local:latest \
  -jar DynamoDBLocal.jar -sharedDb -inMemory

# Start SQS Local (ElasticMQ)
echo "🎬 Starting SQS Local (ElasticMQ)..."
podman run -d \
  --name quiz-game-sqs \
  -p 9324:9324 \
  -p 9325:9325 \
  -v ./elasticmq.conf:/opt/elasticmq.conf:Z \
  softwaremill/elasticmq:latest

# Wait for services
echo ""
echo "⏳ Waiting for services to start..."
sleep 5

# Check status
echo ""
if podman ps | grep -q "quiz-game-dynamodb"; then
    echo "✅ DynamoDB Local is running on http://localhost:8000"
else
    echo "❌ DynamoDB Local failed to start"
fi

if podman ps | grep -q "quiz-game-sqs"; then
    echo "✅ SQS Local (ElasticMQ) is running on http://localhost:9324"
else
    echo "❌ SQS Local failed to start"
fi

echo ""
echo "🎉 Local services are ready!"
echo ""
echo "Next steps:"
echo "  1. Run the application: ./mvnw spring-boot:run"
echo "  2. Test the API: curl -X POST 'http://localhost:8081/api/quiz/start?playerId=test'"
echo ""
echo "To stop services: podman stop quiz-game-dynamodb quiz-game-sqs"
echo "To view logs: podman logs -f quiz-game-dynamodb"




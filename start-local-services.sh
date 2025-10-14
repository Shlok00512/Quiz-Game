#!/bin/bash

echo "🚀 Starting Local AWS Services for Quiz Game"
echo "=============================================="
echo ""

# Check if Podman or Docker is available
CONTAINER_CMD=""
COMPOSE_CMD=""

if command -v podman &> /dev/null; then
    CONTAINER_CMD="podman"
    if command -v podman-compose &> /dev/null; then
        COMPOSE_CMD="podman-compose"
    else
        COMPOSE_CMD="podman compose"
    fi
    echo "✅ Using Podman"
elif command -v docker &> /dev/null; then
    CONTAINER_CMD="docker"
    COMPOSE_CMD="docker-compose"
    if ! docker info > /dev/null 2>&1; then
        echo "❌ Docker is not running. Please start Docker Desktop first."
        exit 1
    fi
    echo "✅ Using Docker"
else
    echo "❌ Neither Podman nor Docker found. Please install one of them:"
    echo ""
    echo "Podman: brew install podman"
    echo "Docker: https://www.docker.com/products/docker-desktop"
    exit 1
fi

echo ""

# Stop and remove existing containers
echo "🧹 Cleaning up old containers..."
$COMPOSE_CMD down 2>/dev/null

# Start services
echo ""
echo "🎬 Starting DynamoDB Local and SQS Local..."
$COMPOSE_CMD up -d

# Wait for services to be ready
echo ""
echo "⏳ Waiting for services to start..."
sleep 5

# Check if services are running
if $CONTAINER_CMD ps | grep -q "quiz-game-dynamodb"; then
    echo "✅ DynamoDB Local is running on http://localhost:8000"
else
    echo "❌ DynamoDB Local failed to start"
fi

if $CONTAINER_CMD ps | grep -q "quiz-game-sqs"; then
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
echo "To stop services: $COMPOSE_CMD down"
echo "To view logs: $COMPOSE_CMD logs -f"




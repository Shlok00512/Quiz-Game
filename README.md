# 🎯 Quiz Game

An interactive web-based quiz game built with Java Spring Boot, featuring real-time score tracking with DynamoDB and SQS.

![Quiz Game](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-green) ![License](https://img.shields.io/badge/license-MIT-blue)

## ✨ Features

- 🎨 **Beautiful Interactive Web UI** - Modern, responsive design
- ⏱️ **Timed Questions** - Answer within the time limit (5-10 seconds)
- 📊 **Real-time Score Tracking** - Instant feedback on answers
- 🗄️ **DynamoDB Storage** - Persistent quiz data
- 📬 **SQS Integration** - Real-time score updates via message queue
- 🐳 **Local Development** - Run everything locally with Podman/Docker

## 🚀 Quick Start

### Prerequisites

1. **Java 17** (required)
2. **Podman Desktop** or **Docker** (for local DynamoDB & SQS)

### Installation

**Step 1: Install Java**
```bash
cd ~/Desktop/Quiz-Game
./install-java.sh
```

**Step 2: Start Local Services**

Make sure Podman Desktop or Docker is running, then:
```bash
./start-with-podman.sh
# OR
./start-local-services.sh
```

**Step 3: Run the Application**
```bash
./mvnw spring-boot:run
```

**Step 4: Play the Quiz!**

Open your browser and go to:
```
http://localhost:8082
```

That's it! 🎉

## 🎮 How to Play

1. **Enter your name** on the welcome screen
2. **Click "Start Quiz"** to begin
3. **Answer questions** within the time limit
4. **See your score** after each question
5. **Get your final results** at the end!

### Quiz Questions

1. **Geography**: What is the capital of France? *(10 seconds)*
2. **Math**: What is 2 + 2? *(5 seconds)*
3. **Programming**: Which programming language is used for Spring Boot? *(10 seconds)*

**Scoring**: 10 points per correct answer | Maximum score: 30 points

## 🏗️ Architecture

```
┌──────────────┐
│   Browser    │
│   (Web UI)   │
└──────┬───────┘
       │ HTTP
       ▼
┌──────────────┐      ┌──────────────┐
│ Spring Boot  │─────►│  DynamoDB    │
│   REST API   │      │ (Quiz State) │
└──────┬───────┘      └──────────────┘
       │
       │ Publish
       ▼
   ┌────────────┐
   │    SQS     │ (Score Updates)
   └────────────┘
```

## 📡 API Endpoints

If you want to interact with the API directly:

### Start a Quiz
```bash
curl -X POST "http://localhost:8082/api/quiz/start?playerId=player123"
```

### Get Quiz Status
```bash
curl http://localhost:8082/api/quiz/{quizId}
```

### Submit Answer
```bash
curl -X POST http://localhost:8082/api/quiz/answer \
  -H "Content-Type: application/json" \
  -d '{"quizId":"abc-123","questionId":"q1","selectedAnswer":2}'
```

## 📂 Project Structure

```
Quiz-Game/
├── src/main/
│   ├── java/com/quizgame/
│   │   ├── QuizGameApplication.java    # Main application
│   │   ├── controller/
│   │   │   └── QuizController.java     # REST API endpoints
│   │   ├── service/
│   │   │   ├── QuizService.java        # Business logic
│   │   │   └── SqsService.java         # Message queue
│   │   ├── repository/
│   │   │   └── QuizRepository.java     # Database operations
│   │   ├── model/                       # Data models
│   │   └── config/
│   │       └── AwsConfig.java          # AWS configuration
│   └── resources/
│       ├── application.properties       # Configuration
│       └── static/
│           └── index.html              # Web UI
├── pom.xml                             # Dependencies
├── mvnw                                # Maven wrapper
├── docker-compose.yml                  # Local services
├── start-with-podman.sh                # Start with Podman
└── start-local-services.sh             # Start with Docker
```

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server Port
server.port=8082

# AWS Local Endpoints (for development)
aws.dynamodb.endpoint=http://localhost:8000
aws.sqs.endpoint=http://localhost:9324

# Test Credentials (local only)
aws.accessKey=test
aws.secretKey=test
```

## 🛑 Stopping the Application

**Stop Spring Boot:**
Press `Ctrl+C` in the terminal running the application

**Stop Local Services:**
```bash
podman stop quiz-game-dynamodb quiz-game-sqs
# OR
docker-compose down
```

## 🐛 Troubleshooting

**Port already in use?**
```bash
# Change port in application.properties
server.port=8083
```

**Podman not working?**
```bash
# Make sure Podman Desktop is running
podman ps

# Restart the machine
podman machine restart
```

**Java not found?**
```bash
# Run the installer
./install-java.sh

# Verify installation
java --version
```

## 🎨 Customization

### Add More Questions

Edit `src/main/java/com/quizgame/service/QuizService.java` in the `createSampleQuestions()` method:

```java
Question q4 = new Question();
q4.setId("q4");
q4.setText("Your question here?");
q4.setOptions(Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4"));
q4.setCorrectAnswer(0); // Index of correct answer
q4.setTimeLimit(10); // Seconds
questions.add(q4);
```

### Customize UI

Edit `src/main/resources/static/index.html` to change colors, styles, or layout.

## 🚢 Production Deployment

For AWS deployment:

1. Remove local endpoints from `application.properties`
2. Configure AWS credentials
3. Ensure DynamoDB table and SQS queue exist
4. Deploy with your preferred method (EC2, ECS, etc.)

## 📄 License

MIT License

---

**Built by Shlok Shrivastava!** 🎯

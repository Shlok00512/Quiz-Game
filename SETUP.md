# 🔧 Setup Guide

Complete setup instructions for the Quiz Game.

## 📋 Prerequisites

- **Java 17+** (Required)
- **Podman Desktop** or **Docker Desktop** (Required for local services)
- **macOS, Linux, or Windows**

---

## ☕ Step 1: Install Java 17

### Automated Installation (macOS/Linux)
```bash
cd ~/Desktop/Quiz-Game
./install-java.sh
```

### Manual Installation

**Option A: Using Homebrew (macOS)**
```bash
brew install openjdk@17
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

**Option B: Download from Adoptium**
1. Visit: https://adoptium.net/temurin/releases/
2. Download Java 17 (LTS) for your OS
3. Install the package

### Verify Installation
```bash
java --version
# Should show: openjdk 17.x.x
```

---

## 🐳 Step 2: Install Container Runtime

Choose either Podman or Docker:

### Option A: Podman Desktop (Recommended)
1. Download: https://podman-desktop.io/
2. Install and launch Podman Desktop
3. Make sure the Podman machine is running (green indicator)

### Option B: Docker Desktop
1. Download: https://www.docker.com/products/docker-desktop
2. Install and launch Docker Desktop
3. Ensure Docker is running

---

## 🚀 Step 3: Start Local Services

### Using Podman
```bash
cd ~/Desktop/Quiz-Game
./start-with-podman.sh
```

### Using Docker
```bash
cd ~/Desktop/Quiz-Game
./start-local-services.sh
```

This will start:
- **DynamoDB Local** on `http://localhost:8000`
- **SQS Local (ElasticMQ)** on `http://localhost:9324`

---

## 🎮 Step 4: Run the Quiz Game

```bash
./mvnw spring-boot:run
```

Wait for the application to start (you'll see "Started QuizGameApplication")

---

## 🌐 Step 5: Open the Web Interface

Open your browser and go to:
```
http://localhost:8082
```

Enter your name and start playing! 🎯

---

## 🛑 Stopping Everything

### Stop the Application
Press `Ctrl+C` in the terminal running Spring Boot

### Stop Local Services

**Podman:**
```bash
podman stop quiz-game-dynamodb quiz-game-sqs
```

**Docker:**
```bash
docker-compose down
```

---

## 🔍 Verification Commands

### Check if Services are Running

**Podman:**
```bash
podman ps
```

**Docker:**
```bash
docker ps
```

You should see:
- `quiz-game-dynamodb` on port 8000
- `quiz-game-sqs` on ports 9324-9325

### Check if Application is Running
```bash
curl http://localhost:8082/
```

Should return the HTML for the quiz game.

### Test API Directly
```bash
curl -X POST "http://localhost:8082/api/quiz/start?playerId=test"
```

---

## ⚙️ Configuration

The application is pre-configured for local development.

**File:** `src/main/resources/application.properties`

```properties
# Server Configuration
server.port=8082

# DynamoDB Local
aws.dynamodb.endpoint=http://localhost:8000

# SQS Local
aws.sqs.endpoint=http://localhost:9324

# Test Credentials (local only)
aws.accessKey=test
aws.secretKey=test
```

### Change Port

If port 8082 is in use:

1. Edit `application.properties`
2. Change `server.port=8082` to another port
3. Restart the application

---

## 🐛 Troubleshooting

### "Port already in use"

**Find what's using the port:**
```bash
lsof -i :8082
```

**Kill the process:**
```bash
lsof -ti:8082 | xargs kill
```

### "Java not found"

**Check Java installation:**
```bash
java --version
```

**Check JAVA_HOME:**
```bash
echo $JAVA_HOME
```

**Set JAVA_HOME (if needed):**
```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
```

### Podman/Docker Not Starting

**Podman:**
```bash
# Check machine status
podman machine list

# Start the machine
podman machine start

# Restart if needed
podman machine restart
```

**Docker:**
- Make sure Docker Desktop is running
- Check Docker icon in system tray
- Try restarting Docker Desktop

### Services Not Connecting

**Check if local services are running:**
```bash
# Podman
podman ps

# Docker
docker ps
```

**View logs:**
```bash
# Podman
podman logs quiz-game-dynamodb
podman logs quiz-game-sqs

# Docker
docker-compose logs dynamodb-local
docker-compose logs sqs-local
```

### Web Page Not Loading

1. **Check if application is running**
   ```bash
   curl http://localhost:8082/
   ```

2. **Check application logs** in the terminal where you ran `./mvnw spring-boot:run`

3. **Clear browser cache** and refresh

4. **Try another browser**

---

## 🔄 Restart Everything

If things aren't working, do a clean restart:

```bash
# 1. Stop the application (Ctrl+C)

# 2. Stop containers
podman stop quiz-game-dynamodb quiz-game-sqs
podman rm quiz-game-dynamodb quiz-game-sqs
# OR
docker-compose down

# 3. Start services again
./start-with-podman.sh
# OR
./start-local-services.sh

# 4. Start application
./mvnw spring-boot:run
```

---

## 📚 Additional Resources

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **DynamoDB Local**: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html
- **ElasticMQ (SQS)**: https://github.com/softwaremill/elasticmq
- **Podman**: https://podman.io/
- **Docker**: https://www.docker.com/

---

## 🎯 Next Steps

Once everything is running:

1. Play the quiz game at `http://localhost:8082`
2. Add your own questions (see README.md)
3. Customize the UI
4. Deploy to production (AWS or other cloud)

---

**Built by Shlok Shrivastava!** 🚀

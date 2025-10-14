# ⚡ Quick Start Guide

## 🚀 Start Everything (3 Steps)

### 1. Start Local Services
```bash
./start-with-podman.sh
```

### 2. Run the Application
```bash
./mvnw spring-boot:run
```

### 3. Open in Browser
```
http://localhost:8082
```

---

## 🎯 What You Get

- **Interactive Quiz Game** with beautiful UI
- **3 Questions** across different topics
- **Real-time scoring** and feedback
- **Time limits** for each question (5-10 seconds)
- **Maximum Score**: 30 points

---

## 🛑 Stop Everything

**Stop Application:** Press `Ctrl+C`

**Stop Containers:**
```bash
podman stop quiz-game-dynamodb quiz-game-sqs
```

---

## 📝 Quiz Answers (Cheat Sheet)

1. **What is the capital of France?** → Paris (option 2)
2. **What is 2 + 2?** → 4 (option 1)
3. **Which programming language is used for Spring Boot?** → Java (option 1)

---

## 🔧 Ports Used

- **8082** - Web Application
- **8000** - DynamoDB Local
- **9324** - SQS Local

---

## 📚 Documentation

- Full Setup: `SETUP.md`
- Project Info: `README.md`
- This Guide: `QUICKSTART.md`

---

**Built by Shlok Shrivastava!** 🎯



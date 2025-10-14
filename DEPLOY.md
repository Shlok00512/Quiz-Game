# Railway Deployment Guide

## 🚀 Deploy Nike Quiz Game to Railway

### Prerequisites
- GitHub account
- Railway account (sign up at https://railway.app with GitHub)

---

## Step 1: Push Code to GitHub

1. **Open GitHub Desktop** (you already have it installed)

2. **Publish the repository:**
   - You should see "Quiz-Game" in GitHub Desktop
   - Click "Publish repository" button
   - **Uncheck** "Keep this code private" (or keep it private if you prefer)
   - Click "Publish Repository"

3. **Verify on GitHub:**
   - Go to https://github.com/YOUR_USERNAME/Quiz-Game
   - Make sure all files are there

---

## Step 2: Deploy to Railway

### Method 1: Railway Web Dashboard (Easiest)

1. **Go to Railway:** https://railway.app

2. **Sign in with GitHub**

3. **Create New Project:**
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Choose your "Quiz-Game" repository

4. **Railway will auto-detect the Dockerfile and deploy!**
   - It will automatically:
     - Build using your Dockerfile
     - Use the `railway.json` configuration
     - Set up the environment

5. **Configure (if needed):**
   - Railway will assign a random port
   - Your app is configured to use `PORT` environment variable (already set in `application-prod.properties`)

6. **Get your URL:**
   - Once deployed, Railway will give you a public URL like: `https://your-app.up.railway.app`
   - Click "Generate Domain" if it doesn't auto-generate

7. **That's it!** Your quiz game is live! 🎉

---

### Method 2: Railway CLI (Alternative)

```bash
# Install Railway CLI
brew install railway

# Login
railway login

# Initialize in your project directory
cd /Users/sshr32/Desktop/Quiz-Game
railway init

# Deploy
railway up
```

---

## 📝 Important Notes

### How It Works in Production:
- **No DynamoDB/SQS needed** - Uses in-memory storage
- **Quiz state resets on restart** - This is normal for the demo
- **Port 8080** - Railway sets `PORT` environment variable automatically
- **Production profile active** - Uses `application-prod.properties`

### Updating Your Deployed App:
1. Make changes locally
2. Commit and push to GitHub
3. Railway auto-deploys! (if auto-deploy is enabled)

### Environment Variables (if needed):
Railway dashboard → Your Project → Variables
- `SPRING_PROFILES_ACTIVE=prod` (already set in Dockerfile)
- `PORT` (Railway sets this automatically)

---

## 🎨 Your Live App Features:
- ✅ Nike Quiz with 5 questions
- ✅ Orange/dark premium UI
- ✅ Timer functionality
- ✅ Score tracking
- ✅ Responsive design
- ✅ Footer: "Built by Shlok Shrivastava!"

---

## Troubleshooting

**If deployment fails:**
1. Check Railway logs in the dashboard
2. Verify Dockerfile is in the root directory
3. Ensure Java 17 compatibility

**If the quiz doesn't load:**
1. Check that the domain is generated
2. Visit the root URL (not /api/quiz)
3. Check browser console for errors

---

## Alternative Platforms (if Railway doesn't work):

### Render.com:
1. Sign up at https://render.com
2. New → Web Service → Connect GitHub repo
3. Select "Quiz-Game"
4. Environment: Docker
5. Deploy!

### Fly.io:
```bash
brew install flyctl
flyctl auth login
cd /Users/sshr32/Desktop/Quiz-Game
flyctl launch
flyctl deploy
```

---

## 🎯 Quick Start Checklist:

- [ ] Publish repository to GitHub via GitHub Desktop
- [ ] Sign up/login to Railway with GitHub
- [ ] Create new project from GitHub repo
- [ ] Wait for deployment (~3-5 minutes)
- [ ] Generate domain
- [ ] Visit your live quiz game!
- [ ] Share the URL! 🎉


## First Aid Learning Mobile Application: Interactive Training and Progress Tracking Tool

The First Aid Learning Mobile Application is an Android-based educational application designed to provide users with accessible, interactive, and self-paced first aid training. The application combines multimedia learning materials, assessments, and progress tracking to help users learn essential first aid skills such as Cardiopulmonary Resuscitation (CPR) and Bleeding Management. It also recommends relevant YouTube videos to reinforce learning and encourage continuous improvement.

## 🔐 Key Features

- **Interactive Learning Modules** — Structured courses on CPR and Bleeding Management
- **Multimedia Learning** — Text, images, and video content integrated into lessons
- **Multiple Choice Quizzes** — Knowledge assessment with instant feedback
- **Scenario-Based Assessments** — Interactive drag-and-drop scenario challenges
- **Progress Tracking** — Monitor module completion, assessment scores, and learning summaries
- **Module Progress Dashboard** — Visual overview of learning achievements
- **Rule-based YouTube Video Recommendations** — Smart video suggestions based on user activity and module progress
- **Firebase Analytics** — Track user interactions and app performance

## Stack

- **Language:** Java
- **IDE:** Android Studio
- **Framework / Runtime:** Android SDK (API 26-35), Android Jetpack
- **Database:** SQLite (with SQLiteOpenHelper)
- **Analytics:** Firebase Analytics
- **Notable libraries:**
  - Firebase (Authentication, Firestore, Crashlytics, Analytics)
  - YouTube API (video integration)
  - Glide (image loading)
  - Picasso (image processing)
  - MPAndroidChart (data visualization)
  - RecyclerView & CardView (UI components)
  - Material Components (Material Design)
- **Development Methodology:** Waterfall Model
- **Platform:** Native Android Application

## Target Users & Use Cases

**Target Users:**
- Volunteer Aid Detachment (VAD) members
- General public
- Anyone interested in learning basic first aid skills

**Use Cases:**
- Learn first aid procedures through structured modules
- Practice knowledge using quizzes and scenario-based assessments
- Track learning progress and assessment performance
- Review completed modules
- Receive personalized video recommendations for further learning
- Study offline without continuous internet access (except YouTube videos and updates)

## How it's organized

```
app/
├── activities/                 Screen activities organized by feature
│   ├── Authentication         (Login, SignUp, Splash)
│   ├── Home                   (HomeActivity with smart feed)
│   ├── Modules                (ModuleActivity, SubTopicActivity)
│   ├── Quiz                   (QuizActivity, QuizMcqActivity, QuizScenarioActivity)
│   ├── Assessment             (AssessmentActivity, ReviewAnswersActivity)
│   ├── Progress               (ProgressTrackingActivity, ProgressSummaryActivity)
│   ├── Profile                (UserProfileActivity, EditProfileActivity)
│   └── Recommendations        (RecommendationActivity for video feed)
│
├── database/                  Data access objects (DAOs) and database helper
│   ├── FirstAidDatabaseHelper (SQLite database initialization & schema)
│   ├── UserDAO                (User CRUD operations)
│   ├── ModuleDAO              (Module and course data)
│   ├── ContentDAO             (Learning content management)
│   ├── QuestionDAO            (Quiz questions)
│   ├── AssessmentResultDAO    (Assessment tracking)
│   ├── ModuleProgressDAO      (User progress per module)
│   ├── VideoRecommendationDAO (Video recommendation logic)
│   └── VideoClickLogDAO       (Video interaction tracking)
│
├── models/                    Data models
│   ├── User.java
│   ├── Module.java
│   ├── Content.java
│   ├── Question.java
│   ├── AssessmentResult.java
│   ├── ModuleProgressStatus.java
│   ├── UserAnswer.java
│   ├── VideoRecommendation.java
│   └── VideoClickLog.java

```

**How it fits together:** The app starts with a splash screen, leading to login/signup via Firebase Authentication. Once logged in, users land on `HomeActivity`, which displays banner sliders, fun facts, available modules, and a smart video feed. Users navigate through five main sections via bottom navigation: Home, Modules, Assessments, Progress, and Profile. Each module contains subtopics with educational content, quizzes (MCQ and scenario-based), and assessments. The app tracks user progress, answers, and video interactions in a local SQLite database, while Firebase handles user authentication, crash reporting, and analytics.

## 📚 Database Schema

The application uses SQLite with the following tables:

- **USER** — User account information and credentials
- **MODULE** — Learning modules (CPR, Bleeding Management, etc.)
- **CONTENT** — Educational content (text, images, descriptions)
- **USER_CONTENT_VIEW** — Tracks which content users have viewed
- **QUESTION** — Quiz and assessment questions
- **ASSESSMENT_RESULT** — User assessment scores and results
- **USER_ANSWER** — Individual user answers to quiz questions
- **VIDEO_RECOMMENDATIONS** — YouTube video metadata and recommendations
- **VIDEO_CLICK_LOG** — User video interaction history

## 🎯 Progress Tracking System

The app combines two approaches for comprehensive progress tracking:

1. **Module-Based Progress Tracking** — Tracks completion percentage per learning module
2. **Assessment-Based Progress Tracking** — Monitors quiz scores and assessment performance

This dual approach provides users with personalized learning feedback and helps identify areas for improvement.

## 🎥 Recommendation System

A rule-based recommendation engine suggests relevant YouTube videos based on:
- Learning modules completed or recently accessed
- User viewing history and watch patterns
- Key learning tags (e.g., "cpr_basics", "bleeding_control")

Videos are ranked by relevance and presented in a personalized smart feed on the home screen.

## 📱 Offline Support

Most educational content is available offline, ensuring accessibility in areas with limited internet connectivity. Internet is required only for:
- YouTube video recommendations
- Firebase Analytics tracking

## 🚀 How to run it

### Prerequisites
- Android Studio (Hedgehog or newer recommended)
- Java Development Kit (JDK 8+)
- Android SDK (API 26 or higher)
- Android Emulator or Physical Android Device
- Firebase project configured (google-services.json needed)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Krnkayy17/FirstAidApp.git
   cd FirstAidApp
   ```

2. **Add Firebase configuration:**
   - Create a Firebase project at https://firebase.google.com/
   - Download your `google-services.json` file
   - Place it in the `app/` directory
   - This file is required for Firebase services to work

3. **Open in Android Studio:**
   - Open the project folder in Android Studio
   - Allow Gradle to sync and download dependencies

4. **Connect a device or emulator:**
   - Connect an Android device via USB (with USB debugging enabled)
   - OR launch an emulator from Android Studio

5. **Build and run:**
   ```bash
   ./gradlew build          # Build the project
   ./gradlew installDebug   # Install on device/emulator
   ```
   
   Or click **Run ▶️** in Android Studio

6. **Database initialization:**
   - The app automatically creates the SQLite database on first launch
   - Sample questions are inserted during initialization

### Main features to explore
- **Authentication:** Sign up or log in to access personalized content
- **Modules:** Browse structured first aid topics (CPR, Bleeding Management) with text, images, and multimedia
- **Quizzes:** Test knowledge with multiple-choice questions and instant feedback
- **Scenario-Based Assessments:** Complete interactive drag-and-drop challenges
- **Progress Dashboard:** View detailed progress per module and overall learning achievements
- **Smart Video Feed:** Personalized YouTube recommendations based on your learning history
- **Offline Access:** Learn on-the-go without constant internet connection

## 📌 Learning Topics

Currently available:
- **Cardiopulmonary Resuscitation (CPR)** — Techniques and best practices
- **Bleeding Management** — Stop bleeding and wound care

## 🔮 Future Improvements
- More first aid modules (burns, fractures, shock, etc.)
- iOS support
- AI-powered personalized recommendations
- Additional interactive assessments
- Emergency service integration (future scope)
- Offline video caching
- Multi-language support

## 📝 Notes
- Internet connection is required for YouTube video streaming and Firebase Analytics
- All quiz data and progress is saved locally on device for offline access
  

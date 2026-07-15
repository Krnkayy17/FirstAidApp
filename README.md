# First Aid Learning Mobile Application: Interactive Training and Progress Tracking Tool

The First Aid Learning Mobile Application is an Android-based educational application designed to provide users with accessible, interactive, and self-paced first aid training. The application combines multimedia learning materials, assessments, and progress tracking to help users learn essential first aid skills such as Cardiopulmonary Resuscitation (CPR) and Bleeding Management. It also recommends relevant YouTube videos to reinforce learning and encourage continuous improvement.

## Stack

- **Language:** Java
- **Framework / Runtime:** Android SDK (API 26-35), Android Jetpack
- **Notable libraries:**
  - Firebase (Authentication, Firestore, Crashlytics, Analytics)
  - Glide (image loading)
  - Picasso (image processing)
  - MPAndroidChart (data visualization)
  - RecyclerView & ViewPager2 (UI components)
 
## Target Users & Use Cases
Target Users:
- Volunteer Aid Detachment (VAD) members
- General public
- Anyone interested in learning basic first aid skills

Use Cases:
- Learn first aid procedures through structured modules
- Practice knowledge using quizzes and scenario-based assessments
- Track learning progress and assessment performance
- Review completed modules
- Receive personalized video recommendations for further learning
- Study offline without continuous internet access (except YouTube videos and updates)

## How it's organized

```
app/src/main/java/com/example/firstaidapp/
  ├── Activities/              15+ screen activities (Login, Home, Quiz, Module, Progress, etc.)
  ├── models/                  Data models (User, Question, Module, Assessment, Video, etc.)
  ├── database/                DAOs and database helper (SQLite with FirstAidDatabaseHelper)
  ├── adapters/                RecyclerView adapters for lists and carousels
  ├── analytics/               Firebase Analytics tracking
  ├── helpers/                 Utility helpers and business logic
  ├── utils/                   SessionManager and utility functions
  └── resources/               Layouts, drawables, values, animations

app/src/main/
  └── AndroidManifest.xml      App configuration with all activities registered
```

**How it fits together:** The app starts with a splash screen, leading to login/signup via Firebase Authentication. Once logged in, users land on `HomeActivity`, which displays banner sliders, fun facts, available modules, and a smart video feed. Users navigate through five main sections via bottom navigation: Home, Modules, Assessments, Progress, and Profile. Each module contains subtopics with educational content, quizzes (MCQ and scenario-based), and assessments. The app tracks user progress, answers, and video interactions in a local SQLite database, while Firebase handles user authentication and crash reporting.

## How to run it

### Prerequisites
- Android Studio (Arctic Fox or later)
- Android SDK API 26+ (minimum)
- Firebase project configured (google-services.json needed)

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Krnkayy17/FirstAidApp.git
   cd FirstAidApp
   ```

2. **Add Firebase configuration:**
   - Place your `google-services.json` in the `app/` directory
   - This file is required for Firebase services to work

3. **Build and run:**
   ```bash
   ./gradlew build          # Build the project
   ./gradlew installDebug   # Install on connected device/emulator
   ```

   Or use Android Studio:
   - Open the project and click **Run** (Shift + F10)

4. **Database initialization:**
   - The app automatically creates the SQLite database on first launch
   - Sample questions are inserted on initial startup

### Main features to explore
- **Authentication:** Sign up or log in to access personalized content
- **Modules:** Browse structured first aid topics with content and visuals
- **Quizzes:** Test knowledge with multiple-choice and scenario-based questions
- **Assessments:** Complete full assessments to track competency
- **Progress Tracking:** View detailed progress per module and overall performance
- **Smart Video Feed:** Personalized YouTube video recommendations based on learning history (I try to include this since my project does not involve any AI. That's why I want this feature imitates like AI).

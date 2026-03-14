# 🚗 Road Safety Mobile Application

> An Android application that tracks the user's real-time location and delivers proactive alerts for nearby accidents, traffic incidents, and road hazards within a 500m radius.

---

## 🎯 Problem

Drivers and commuters are often unaware of nearby road hazards until it's too late. This application uses real-time geospatial matching against reported incident zones to proactively notify users of accidents, potholes, and traffic conditions — enabling safer route decisions before entering a danger zone.

---

## ✨ Features

- **Secure Authentication** — user registration and login via Firebase Authentication
- **Real-Time Location Tracking** — continuous GPS tracking via FusedLocationProviderClient
- **Geospatial Hazard Matching** — GeoFire queries match user location against dangerous zones in real time
- **Proximity Alerts** — triggers in-app and system notifications when within 500m of a reported hazard
- **Interactive Map View** — displays danger zones as circles and user location as a live marker on Google Maps

---

## 🏗️ Architecture

```
LoginActivity / RegisterActivity
        │ Firebase Auth
        ▼
  MainActivity
        │ Intent
        ▼
  MapsActivity
   ├── FusedLocationProviderClient  (real-time GPS)
   ├── GeoFire + Firebase RTDB      (location storage + geo queries)
   ├── Google Maps SDK              (map rendering + markers)
   └── NotificationManager          (proximity alerts)
```

---

## 🛠️ Tech Stack

| Category | Tools |
|----------|-------|
| Language | Java |
| Platform | Android (minSdk 21, targetSdk 33) |
| Maps & Location | Google Maps SDK, FusedLocationProviderClient |
| Geospatial Queries | GeoFire for Android |
| Backend & Auth | Firebase Authentication, Firebase Realtime Database |
| Permissions | Dexter |

---

## 📁 Repository Structure

```
├── app/
│   ├── src/main/
│   │   ├── java/com/example/roadtosafety/
│   │   │   ├── LoginActivity.java       # Firebase auth login
│   │   │   ├── RegisterActivity.java    # New user registration
│   │   │   ├── MainActivity.java        # Home screen + session management
│   │   │   └── MapsActivity.java        # Maps, location tracking, GeoFire alerts
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_login.xml
│   │   │   │   ├── activity_register.xml
│   │   │   │   ├── activity_main.xml
│   │   │   │   └── activity_maps.xml
│   │   │   └── values/
│   │   │       └── strings.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle                     # App dependencies
├── build.gradle                         # Project-level config
├── settings.gradle
└── README.md
```

---

## 🚀 How to Run

### Prerequisites
- Android Studio Flamingo or later
- A Google Maps API key (enable Maps SDK for Android in Google Cloud Console)
- A Firebase project with Authentication and Realtime Database enabled

### Steps

1. Clone the repository
```bash
git clone https://github.com/keertanakappuram/Road-Safety-Mobile-Application.git
```

2. Open in Android Studio via **File → Open**

3. Add your `google-services.json` from Firebase Console into `app/`

4. Add your Maps API key to `local.properties`:
```
MAPS_API_KEY=your_api_key_here
```

5. Sync Gradle and run on an emulator or physical device

---

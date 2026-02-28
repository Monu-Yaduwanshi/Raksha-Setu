# 🛡️ Raksha Setu - Smart Society Visitor Management System

<div align="center">

<img src="https://img.shields.io/badge/Platform-Android-brightgreen?logo=android" />
<img src="https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin" />
<img src="https://img.shields.io/badge/UI-Jetpack%20Compose-blue?logo=jetpackcompose" />
<img src="https://img.shields.io/badge/Backend-Firebase-orange?logo=firebase" />
<img src="https://img.shields.io/badge/Images-Cloudinary-blue?logo=cloudinary" />
<img src="https://img.shields.io/badge/License-MIT-yellow" />

<br><br>

<b>रक्षा सेतु</b>  
<i>Building a Bridge of Security Between Residents and Society Management</i>

<br><br>

Raksha Setu - Building Safer Communities, One Visitor at a Time  

⭐ Star this repository if you like this project

</div>

---
<p align="center">
  <img width="500" height="500" alt="logoStatic" src="https://github.com/user-attachments/assets/497cd3fb-1184-4ba2-9dfe-9587c6279220" alt="Jeevan Rakshak Logo"/>
</p>



# 📋 Table of Contents

- Overview
- Problem Statement
- Proposed Solution
- Key Features
- User Roles
- Technology Stack
- System Architecture
- Modules
- Visitor Workflow
- Database Structure
- Installation
- Project Structure
- Screenshots
- Roadmap
- Contributing
- License
- Acknowledgments

---

# 🌟 Overview

**Raksha Setu (रक्षा सेतु)** is a modern Android application designed to digitize and secure visitor management in residential societies, gated communities, and apartments.

The name **Raksha Setu** means **"Bridge of Protection"**, representing a secure digital connection between:

- Residents 🏠  
- Watchmen 🛡️  
- Society Management 👑  

This app replaces traditional manual registers with a **secure, real-time, digital system** that provides visitor verification, instant notifications, and complete transparency.

---

# ❗ Problem Statement

Traditional society visitor management faces serious security and operational issues:

| Problem | Impact |
|--------|--------|
| Manual Registers | Data loss, illegible entries |
| No Real-time Alerts | Residents unaware of visitors |
| No Photo Records | No identity verification |
| Unauthorized Entry | Major security risk |
| No Central Database | No visitor history tracking |
| No Emergency Alerts | Slow emergency response |
| Communication Gap | Poor coordination |

---

# 💡 Proposed Solution

Raksha Setu provides a **complete digital visitor security system**.

### Workflow:

1. Watchman registers visitor with photo
2. Resident receives instant notification
3. Resident can Allow / Deny / Leave at Gate
4. Watchman receives decision instantly
5. Entry is securely controlled and logged

---

# ✨ Key Features

## 🛡️ Security Features

- Visitor photo capture
- Resident approval required
- Real-time notifications
- Digital visitor logs
- Visitor blacklist support

---

## 🏢 Management Features

- Society creation and management
- Add flats, residents, watchmen
- Society announcements
- Visitor analytics dashboard

---

## 🆘 Emergency Features

- SOS alert system
- Instant emergency notifications
- Emergency contact access

---

## ⭐ Advanced Features

- QR Code visitor entry
- Pre-approved visitors
- Entry and exit tracking
- Offline support
- Multi-society support

---

# 👥 User Roles

| Role | Responsibilities | Access |
|-----|------------------|--------|
| 👑 Secretary | Society management, user creation | Full Access |
| 🛡️ Watchman | Register visitors, send requests | Visitor Access |
| 🏠 Resident | Approve/deny visitors | Personal Access |

---

# 🛠️ Technology Stack

## 📱 Frontend

- Kotlin
- Jetpack Compose
- MVVM Architecture
- Navigation Compose
- StateFlow

---

## ☁️ Backend

- Firebase Authentication
- Firebase Firestore
- Firebase Cloud Messaging (FCM)
- Firebase Analytics

---

## 🖼️ Image Storage

- Cloudinary

---

## 🧰 Tools

- Android Studio
- Git & GitHub
- Gradle

---

# 🏗️ System Architecture

```
Android App (Jetpack Compose)
        │
        ▼
ViewModel Layer (Business Logic)
        │
        ▼
Repository Layer (Data Handling)
        │
        ▼
Firebase Services
 ├ Authentication
 ├ Firestore Database
 ├ Cloud Messaging
 └ Cloudinary Image Storage
```

---

# 📦 Modules

## 🔐 Authentication Module

- Login system
- Role-based access
- Session management
- Secretary creates accounts

---

## 🏢 Society Management Module

Secretary can:

- Create society
- Add residents
- Add watchmen
- Manage flats

---

## 🚪 Visitor Management Module (Core)

Watchman can:

- Select flat
- Capture visitor photo
- Enter visitor details
- Send approval request

Resident can:

- View visitor info
- Allow entry
- Deny entry
- Leave at gate

---

## 📜 Visitor History Module

- View visitor records
- Search visitors
- Filter by date

---

## 📢 Announcement Module

Secretary can:

- Post announcements
- Send emergency notices

---

## 🆘 Emergency SOS Module

- Send emergency alerts
- Notify entire society

---

## 📇 Directory Module

- View residents
- View watchmen
- Contact list

---

## 🔔 Notification Module

- Real-time notifications
- Visitor approval alerts
- Announcement alerts

---

## 👤 Profile Module

- View profile
- Edit profile
- Logout

---

# 🔄 Visitor Workflow

```
Visitor Arrives
     │
     ▼
Watchman Registers Visitor
     │
     ▼
Photo Uploaded to Cloudinary
     │
     ▼
Request Sent to Resident
     │
     ▼
Resident Approves / Denies
     │
     ▼
Watchman Receives Response
     │
     ▼
Entry Allowed or Denied
```

---

# 📊 Database Structure

## users

```json
{
 "userId": "",
 "name": "",
 "phone": "",
 "role": "resident/watchman/secretary",
 "societyId": "",
 "flatId": "",
 "profilePic": ""
}
```

## visitors

```json
{
 "visitorId": "",
 "name": "",
 "phone": "",
 "photo": "",
 "flatId": "",
 "purpose": "",
 "status": "pending/approved/denied",
 "entryTime": ""
}
```

## societies

```json
{
 "societyId": "",
 "name": "",
 "address": ""
}
```

---

# 📲 Installation

## Clone Repository

```
git clone https://github.com/yourusername/raksha-setu.git
cd raksha-setu
```

---

## Firebase Setup

- Create Firebase project
- Enable Authentication
- Enable Firestore
- Download google-services.json
- Place inside app folder

---

## Cloudinary Setup

Add in local.properties:

```
cloudinary.cloud_name=your_cloud_name
cloudinary.api_key=your_api_key
cloudinary.api_secret=your_api_secret
```

---

## Run App

```
./gradlew build
./gradlew installDebug
```

---

# 📁 Project Structure

```
app/
 ├ data/
 │  ├ models/
 │  ├ repository/
 │  └ remote/
 │
 ├ ui/
 │  ├ auth/
 │  ├ home/
 │  ├ visitor/
 │  ├ announcements/
 │  ├ emergency/
 │  ├ directory/
 │  └ profile/
 │
 ├ viewmodel/
 ├ utils/
```

---

# 📸 Screenshots

```
Screenshots coming soon
```

---

# 🗺️ Roadmap

## Phase 1

- Authentication
- Firebase integration
- UI setup

## Phase 2

- Visitor module
- Approval system

## Phase 3

- Notifications
- History

## Phase 4

- SOS system
- Announcements

## Phase 5

- QR entry
- Analytics
- Play Store release

---

# 🤝 Contributing

Steps:

```
Fork repository
Create branch
Commit changes
Push branch
Create Pull Request
```

---

# 📄 License

```
MIT License

Copyright (c) 2024 Raksha Setu

Permission is hereby granted...
```

---

# 🙏 Acknowledgments

- Android Jetpack Team
- Firebase Team
- Cloudinary
- Open source contributors

---

<div align="center">

Made with ❤️ in India 🇮🇳  

Raksha Setu - Secure Society, Smart Society  

</div>

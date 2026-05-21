## 📚 Smart Study Planner

A desktop application built with Java Swing that helps students 
plan and track their study sessions efficiently. Add study sessions 
by subject, duration, and difficulty — and get instant feedback, 
visual stats, and motivational quotes to keep you going.

## ✨ Features
- ➕ Add study sessions with subject, duration & difficulty level
- 📊 Real-time progress bars for Easy / Medium / Hard sessions
- 🥧 Custom pie chart showing difficulty distribution
- 💬 Smart feedback based on total study time & session count
- 🌟 Random motivational quotes on every session update
- 🕐 Total study time tracker (up to 5-hour goal)

## 🛠️ Tech Stack
- Java (JDK 8+)
- Java Swing — GUI framework
- AWT Graphics — Custom pie chart rendering
- OOP Design — Model/View separation (StudySession, StudyPlanner)

## 📂 Project Structure
├── SmartStudyPlannerGUI.java   # Main GUI + Entry point
├── StudySession.java           # Session model
├── StudyPlanner.java           # Business logic & feedback
├── StudyChartPanel.java        # Custom pie chart component
└── MotivationalQuote.java      # Quote generator

## 🚀 How to Run
# Compile
javac SmartStudyPlannerGUI.java

# Run
java SmartStudyPlannerGUI

## 📄 License
MIT

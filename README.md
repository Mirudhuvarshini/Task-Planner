<<<<<<< HEAD
# Full-Stack Personalized Task Planner

A comprehensive Task Planner application built with **Java Spring Boot**, **MySQL**, and **Vanilla JavaScript**.

## Features
- **CRUD Operations**: Create, Read, Update, Delete tasks.
- **Task Attributes**: Title, Description, Deadline, Priority (High/Medium/Low), Status (Pending/Completed).
- **Responsive UI**: Clean and modern interface styled with CSS.
- **REST API**: Backend exposes JSON APIs for frontend consumption.
- **Data Persistence**: Tasks are stored in a MySQL database.

## Prerequisites
1. **Java JDK 17+** installed.
2. **Maven** installed (or use your IDE's Maven).
3. **MySQL Server** installed and running.

## Setup Instructions

### 1. Database Setup
Log in to your MySQL server and create the database:
```sql
CREATE DATABASE taskplanner_db;
```
*Note: The application is configured to use `root` user with password `root`. If your credentials differ, update `backend/src/main/resources/application.properties`.*

### 2. Backend Setup
1. Navigate to the `backend` folder.
2. Build the project using Maven:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
   *The backend will start on `http://localhost:8080`.*

### 3. Frontend Setup
1. Navigate to the `frontend` folder.
2. Open `index.html` in your web browser. 
   *For the best experience, use a local server (e.g., Live Server in VS Code) to avoid CORS issues with local files, although the backend is configured to allow `*` origins.*

## Usage
- Click **"New Task"** to add a task.
- Click the **pencil icon** to edit a task's details or status.
- Click the **trash icon** to delete a task.
- Use the **Dashboard** at the top to see task progress stats.

## Project Structure
```
TaskPlanner/
├── backend/                # Spring Boot Application
│   ├── src/main/java/      # Java Source Code
│   ├── src/main/resources/ # Configuration (application.properties)
│   └── pom.xml             # Maven Dependencies
└── frontend/               # Static Web Assets
    ├── index.html          # Main UI
    ├── styles.css          # Styling
    └── app.js              # Frontend Logic (Fetch API)
```
=======
# Personalised Task Planner

Personalised Task Planner is a Java Spring Boot-based CRUD application that allows users to efficiently manage and organize their daily tasks. The system enables users to create, update, delete, and track tasks based on deadlines and priority levels. </br> The objective of this project is to demonstrate backend development using Spring Boot while implementing task management logic with a structured database design.

---

## Key Features
- Create new tasks with title, description, deadline, and priority
- View all tasks
- Update task details
- Delete completed or unnecessary tasks
- Filter tasks by priority or deadline
- Track task status (Pending / Completed)

---

## Tech Stack
- Java
- Spring Boot
- MySQL 
- REST APIs


---
>>>>>>> e0441c465035e73235e0d8052b864def3ed16250

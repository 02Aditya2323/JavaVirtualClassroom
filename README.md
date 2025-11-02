# Virtual Classroom 🎓

A lightweight desktop application for managing virtual classrooms, built with Java Swing and SQLite.

## Features

### For Teachers
- **Lecture Management**: Create and view scheduled lectures
- **Attendance Tracking**: Mark and view attendance reports
- **Assignment Management**: Create assignments and view student submissions

### For Students
- **Lecture Schedule**: View upcoming lectures
- **Attendance**: Check personal attendance records
- **Assignments**: View and submit assignments

## Tech Stack

- **Java Swing**: Desktop UI framework
- **SQLite**: Local database storage
- **JDBC**: Database connectivity

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- SQLite JDBC driver (included in `lib/` directory)

## Setup & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd VirtualLund2
   ```

2. **Compile the project**
   ```bash
   javac -cp ".:lib/*" -d out src/com/virtualclassroom/**/*.java
   ```

3. **Run the application**
   ```bash
   java -cp "out:lib/*" com.virtualclassroom.Main
   ```

## Usage

1. **First Time**: Register a new account (choose role: Teacher or Student)
2. **Login**: Use your credentials to access the dashboard
3. **Navigate**: Use the role-specific dashboard to manage classroom activities

## Project Structure

```
src/
├── com/virtualclassroom/
│   ├── Main.java              # Application entry point
│   ├── database/
│   │   └── DatabaseManager.java
│   ├── models/                # Data models
│   ├── ui/                    # Login & Registration
│   │   ├── student/          # Student-specific panels
│   │   └── teacher/          # Teacher-specific panels
│   └── utils/                # UI constants & utilities
```

## Database

The application automatically creates `classroom.db` on first run with the following tables:
- `users` - User accounts (teachers & students)
- `lectures` - Lecture schedules
- `attendance` - Attendance records
- `assignments` - Assignment details
- `submissions` - Student submissions

## License

This is a basic educational project for learning Java Swing and database integration.

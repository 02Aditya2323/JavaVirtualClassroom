package com.virtualclassroom.database;

import com.virtualclassroom.models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:classroom.db";
    private static Connection connection;

    // Initialize database connection and create tables
    public static void initialize() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("✅ Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create all necessary tables
    private static void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        // Users table
        stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL)");

        // Lectures table
        stmt.execute("CREATE TABLE IF NOT EXISTS lectures (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "lecture_date TEXT NOT NULL, " +
                "lecture_time TEXT NOT NULL, " +
                "teacher_id INTEGER, " +
                "FOREIGN KEY (teacher_id) REFERENCES users(id))");

        // Attendance table
        stmt.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lecture_id INTEGER, " +
                "student_id INTEGER, " +
                "status TEXT NOT NULL, " +
                "marked_at TEXT, " +
                "FOREIGN KEY (lecture_id) REFERENCES lectures(id), " +
                "FOREIGN KEY (student_id) REFERENCES users(id))");

        // Assignments table
        stmt.execute("CREATE TABLE IF NOT EXISTS assignments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "due_date TEXT NOT NULL, " +
                "teacher_id INTEGER, " +
                "FOREIGN KEY (teacher_id) REFERENCES users(id))");

        // Submissions table
        stmt.execute("CREATE TABLE IF NOT EXISTS submissions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "assignment_id INTEGER, " +
                "student_id INTEGER, " +
                "content TEXT NOT NULL, " +
                "submitted_at TEXT, " +
                "FOREIGN KEY (assignment_id) REFERENCES assignments(id), " +
                "FOREIGN KEY (student_id) REFERENCES users(id))");

        stmt.close();
    }

    // ========== USER OPERATIONS ==========

    // Register new user
    public static boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    // Authenticate user (login)
    public static User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        }
        return null;
    }

    // Check if username exists
    public static boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Username check failed: " + e.getMessage());
            return false;
        }
    }

    // Get all students
    public static List<User> getAllStudents() {
        List<User> students = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'Student' ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch students: " + e.getMessage());
        }
        return students;
    }

    // ========== LECTURE OPERATIONS ==========

    // Create new lecture
    public static boolean createLecture(Lecture lecture) {
        String sql = "INSERT INTO lectures (title, description, lecture_date, lecture_time, teacher_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, lecture.getTitle());
            pstmt.setString(2, lecture.getDescription());
            pstmt.setString(3, lecture.getDate());
            pstmt.setString(4, lecture.getTime());
            pstmt.setInt(5, lecture.getTeacherId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to create lecture: " + e.getMessage());
            return false;
        }
    }

    // Get all lectures for a teacher
    public static List<Lecture> getLecturesByTeacher(int teacherId) {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT l.*, u.name as teacher_name FROM lectures l " +
                "JOIN users u ON l.teacher_id = u.id " +
                "WHERE l.teacher_id = ? ORDER BY l.lecture_date DESC, l.lecture_time DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lectures.add(new Lecture(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("lecture_date"),
                        rs.getString("lecture_time"),
                        rs.getInt("teacher_id"),
                        rs.getString("teacher_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch lectures: " + e.getMessage());
        }
        return lectures;
    }

    // Get all lectures (for students)
    public static List<Lecture> getAllLectures() {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT l.*, u.name as teacher_name FROM lectures l " +
                "JOIN users u ON l.teacher_id = u.id " +
                "ORDER BY l.lecture_date DESC, l.lecture_time DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lectures.add(new Lecture(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("lecture_date"),
                        rs.getString("lecture_time"),
                        rs.getInt("teacher_id"),
                        rs.getString("teacher_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch lectures: " + e.getMessage());
        }
        return lectures;
    }

    // Delete lecture
    public static boolean deleteLecture(int lectureId) {
        // First delete related attendance records
        String deleteAttendance = "DELETE FROM attendance WHERE lecture_id = ?";
        String deleteLecture = "DELETE FROM lectures WHERE id = ?";

        try (PreparedStatement pstmt1 = connection.prepareStatement(deleteAttendance);
             PreparedStatement pstmt2 = connection.prepareStatement(deleteLecture)) {

            pstmt1.setInt(1, lectureId);
            pstmt1.executeUpdate();

            pstmt2.setInt(1, lectureId);
            pstmt2.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to delete lecture: " + e.getMessage());
            return false;
        }
    }

    // ========== ATTENDANCE OPERATIONS ==========

    // Mark attendance for multiple students
    public static boolean markAttendance(int lectureId, List<Integer> presentStudentIds) {
        try {
            // First, delete existing attendance for this lecture
            String deleteSql = "DELETE FROM attendance WHERE lecture_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
                pstmt.setInt(1, lectureId);
                pstmt.executeUpdate();
            }

            // Get all students
            List<User> allStudents = getAllStudents();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            // Insert attendance for all students
            String insertSql = "INSERT INTO attendance (lecture_id, student_id, status, marked_at) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                for (User student : allStudents) {
                    pstmt.setInt(1, lectureId);
                    pstmt.setInt(2, student.getId());
                    pstmt.setString(3, presentStudentIds.contains(student.getId()) ? "Present" : "Absent");
                    pstmt.setString(4, timestamp);
                    pstmt.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to mark attendance: " + e.getMessage());
            return false;
        }
    }

    // Get attendance for a specific lecture
    public static List<Attendance> getAttendanceByLecture(int lectureId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, u.name as student_name, l.title as lecture_title " +
                "FROM attendance a " +
                "JOIN users u ON a.student_id = u.id " +
                "JOIN lectures l ON a.lecture_id = l.id " +
                "WHERE a.lecture_id = ? ORDER BY u.name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lectureId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attendance attendance = new Attendance(
                        rs.getInt("id"),
                        rs.getInt("lecture_id"),
                        rs.getInt("student_id"),
                        rs.getString("status"),
                        rs.getString("marked_at")
                );
                attendance.setStudentName(rs.getString("student_name"));
                attendance.setLectureTitle(rs.getString("lecture_title"));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch attendance: " + e.getMessage());
        }
        return attendanceList;
    }

    // Get attendance for a specific student
    public static List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, l.title as lecture_title, l.lecture_date, l.lecture_time " +
                "FROM attendance a " +
                "JOIN lectures l ON a.lecture_id = l.id " +
                "WHERE a.student_id = ? ORDER BY l.lecture_date DESC, l.lecture_time DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Attendance attendance = new Attendance(
                        rs.getInt("id"),
                        rs.getInt("lecture_id"),
                        rs.getInt("student_id"),
                        rs.getString("status"),
                        rs.getString("marked_at")
                );
                attendance.setLectureTitle(rs.getString("lecture_title") +
                        " (" + rs.getString("lecture_date") + " " + rs.getString("lecture_time") + ")");
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch student attendance: " + e.getMessage());
        }
        return attendanceList;
    }

    // Calculate attendance percentage for a student
    public static double getAttendancePercentage(int studentId) {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM attendance WHERE student_id = ? AND status = 'Present') * 100.0 / " +
                "(SELECT COUNT(*) FROM attendance WHERE student_id = ?) as percentage";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("percentage");
            }
        } catch (SQLException e) {
            System.err.println("Failed to calculate attendance: " + e.getMessage());
        }
        return 0.0;
    }

    // ========== ASSIGNMENT OPERATIONS ==========

    // Create new assignment
    public static boolean createAssignment(Assignment assignment) {
        String sql = "INSERT INTO assignments (title, description, due_date, teacher_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, assignment.getTitle());
            pstmt.setString(2, assignment.getDescription());
            pstmt.setString(3, assignment.getDueDate());
            pstmt.setInt(4, assignment.getTeacherId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to create assignment: " + e.getMessage());
            return false;
        }
    }

    // Get all assignments for a teacher
    public static List<Assignment> getAssignmentsByTeacher(int teacherId) {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT a.*, u.name as teacher_name FROM assignments a " +
                "JOIN users u ON a.teacher_id = u.id " +
                "WHERE a.teacher_id = ? ORDER BY a.due_date";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                assignments.add(new Assignment(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("due_date"),
                        rs.getInt("teacher_id"),
                        rs.getString("teacher_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch assignments: " + e.getMessage());
        }
        return assignments;
    }

    // Get all assignments (for students)
    public static List<Assignment> getAllAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT a.*, u.name as teacher_name FROM assignments a " +
                "JOIN users u ON a.teacher_id = u.id " +
                "ORDER BY a.due_date";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                assignments.add(new Assignment(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("due_date"),
                        rs.getInt("teacher_id"),
                        rs.getString("teacher_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch assignments: " + e.getMessage());
        }
        return assignments;
    }

    // Delete assignment
    public static boolean deleteAssignment(int assignmentId) {
        // First delete related submissions
        String deleteSubmissions = "DELETE FROM submissions WHERE assignment_id = ?";
        String deleteAssignment = "DELETE FROM assignments WHERE id = ?";

        try (PreparedStatement pstmt1 = connection.prepareStatement(deleteSubmissions);
             PreparedStatement pstmt2 = connection.prepareStatement(deleteAssignment)) {

            pstmt1.setInt(1, assignmentId);
            pstmt1.executeUpdate();

            pstmt2.setInt(1, assignmentId);
            pstmt2.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to delete assignment: " + e.getMessage());
            return false;
        }
    }

    // ========== SUBMISSION OPERATIONS ==========

    // Submit assignment
    public static boolean submitAssignment(Submission submission) {
        // Check if already submitted
        String checkSql = "SELECT COUNT(*) FROM submissions WHERE assignment_id = ? AND student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(checkSql)) {
            pstmt.setInt(1, submission.getAssignmentId());
            pstmt.setInt(2, submission.getStudentId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Update existing submission
                String updateSql = "UPDATE submissions SET content = ?, submitted_at = ? WHERE assignment_id = ? AND student_id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, submission.getContent());
                    updateStmt.setString(2, submission.getSubmittedAt());
                    updateStmt.setInt(3, submission.getAssignmentId());
                    updateStmt.setInt(4, submission.getStudentId());
                    updateStmt.executeUpdate();
                    return true;
                }
            } else {
                // Insert new submission
                String insertSql = "INSERT INTO submissions (assignment_id, student_id, content, submitted_at) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, submission.getAssignmentId());
                    insertStmt.setInt(2, submission.getStudentId());
                    insertStmt.setString(3, submission.getContent());
                    insertStmt.setString(4, submission.getSubmittedAt());
                    insertStmt.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to submit assignment: " + e.getMessage());
            return false;
        }
    }

    // Get submissions for an assignment
    public static List<Submission> getSubmissionsByAssignment(int assignmentId) {
        List<Submission> submissions = new ArrayList<>();
        String sql = "SELECT s.*, u.name as student_name, a.title as assignment_title " +
                "FROM submissions s " +
                "JOIN users u ON s.student_id = u.id " +
                "JOIN assignments a ON s.assignment_id = a.id " +
                "WHERE s.assignment_id = ? ORDER BY s.submitted_at DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, assignmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Submission submission = new Submission(
                        rs.getInt("id"),
                        rs.getInt("assignment_id"),
                        rs.getInt("student_id"),
                        rs.getString("content"),
                        rs.getString("submitted_at")
                );
                submission.setStudentName(rs.getString("student_name"));
                submission.setAssignmentTitle(rs.getString("assignment_title"));
                submissions.add(submission);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch submissions: " + e.getMessage());
        }
        return submissions;
    }

    // Check if student has submitted an assignment
    public static boolean hasSubmitted(int assignmentId, int studentId) {
        String sql = "SELECT COUNT(*) FROM submissions WHERE assignment_id = ? AND student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, assignmentId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Failed to check submission: " + e.getMessage());
            return false;
        }
    }

    // Close database connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }
}
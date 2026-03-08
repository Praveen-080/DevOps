import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseService {

    private static String lastError = "";

    private static final String DB_URL = System.getenv().getOrDefault(
            "HMS_DB_URL",
            "jdbc:mysql://localhost:3306/hospital_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
    );
        private static final String DB_NAME = System.getenv().getOrDefault("HMS_DB_NAME", "hospital_management");
        private static final String DB_ADMIN_URL = System.getenv().getOrDefault(
            "HMS_DB_ADMIN_URL",
            "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );
    private static final String DB_USER = System.getenv().getOrDefault("HMS_DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("HMS_DB_PASSWORD", "1234");

    public static final String CREATE_CONTACTS_TABLE = """
            CREATE TABLE IF NOT EXISTS contacts (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(120) NOT NULL,
                email VARCHAR(160) NOT NULL,
                phone VARCHAR(20) NOT NULL,
                message TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

    public static final String CREATE_APPOINTMENTS_TABLE = """
            CREATE TABLE IF NOT EXISTS appointments (
                id INT AUTO_INCREMENT PRIMARY KEY,
                patient_name VARCHAR(120) NOT NULL,
                age INT NOT NULL,
                disease VARCHAR(160) NOT NULL,
                appointment_date VARCHAR(10) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

    public static final String CREATE_RECEPTION_PATIENTS_TABLE = """
            CREATE TABLE IF NOT EXISTS reception_patients (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(120) NOT NULL,
                age INT NOT NULL,
                disease VARCHAR(160) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

    public static final String CREATE_USER_LOGINS_TABLE = """
            CREATE TABLE IF NOT EXISTS user_logins (
                id INT AUTO_INCREMENT PRIMARY KEY,
                role VARCHAR(20) NOT NULL,
                username VARCHAR(120) NOT NULL,
                password VARCHAR(120) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE KEY uq_role_username (role, username)
            )
            """;

    public static final String INSERT_CONTACT = """
            INSERT INTO contacts (name, email, phone, message)
            VALUES (?, ?, ?, ?)
            """;

    public static final String INSERT_APPOINTMENT = """
            INSERT INTO appointments (patient_name, age, disease, appointment_date)
            VALUES (?, ?, ?, ?)
            """;

    public static final String INSERT_RECEPTION_PATIENT = """
            INSERT INTO reception_patients (name, age, disease)
            VALUES (?, ?, ?)
            """;

        public static final String INSERT_USER_LOGIN = """
            INSERT INTO user_logins (role, username, password)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE password = VALUES(password)
            """;

            public static final String SELECT_USER_LOGIN = """
                SELECT id
                FROM user_logins
                WHERE role = ? AND username = ? AND password = ?
                LIMIT 1
                """;

    public static final String SELECT_RECEPTION_PATIENTS = """
            SELECT name, age, disease
            FROM reception_patients
            ORDER BY id DESC
            """;

    public static final String DELETE_RECEPTION_PATIENT = """
            DELETE FROM reception_patients
            WHERE name = ? AND age = ? AND disease = ?
            LIMIT 1
            """;

    public static final String SELECT_APPOINTMENTS = """
            SELECT id, patient_name, age, disease, appointment_date
            FROM appointments
            ORDER BY id DESC
            """;

        public static final String DELETE_APPOINTMENT_BY_ID = """
            DELETE FROM appointments
            WHERE id = ?
            LIMIT 1
            """;

    private DatabaseService() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            lastError = "MySQL JDBC driver not found in classpath";
            throw new SQLException("MySQL JDBC driver not found in classpath", ex);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static boolean initializeDatabase() {
        try {
            ensureDatabaseExists();
        } catch (SQLException ex) {
            lastError = ex.getMessage();
            System.err.println("[DB] Database create check failed: " + ex.getMessage());
            return false;
        }

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_CONTACTS_TABLE);
            statement.execute(CREATE_APPOINTMENTS_TABLE);
            statement.execute(CREATE_RECEPTION_PATIENTS_TABLE);
            statement.execute(CREATE_USER_LOGINS_TABLE);
            seedDefaultLogins(connection);
            lastError = "";
            return true;
        } catch (SQLException ex) {
            lastError = ex.getMessage();
            System.err.println("[DB] Initialization skipped: " + ex.getMessage());
            return false;
        }
    }

    public static String getLastError() {
        return lastError;
    }

    public static boolean insertContact(String name, String email, String phone, String message) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_CONTACT)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, message);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[DB] insertContact failed: " + ex.getMessage());
            return false;
        }
    }

    public static boolean insertAppointment(String name, int age, String disease, String date) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_APPOINTMENT)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, disease);
            ps.setString(4, date);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[DB] insertAppointment failed: " + ex.getMessage());
            return false;
        }
    }

    public static boolean insertReceptionPatient(String name, int age, String disease) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_RECEPTION_PATIENT)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, disease);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[DB] insertReceptionPatient failed: " + ex.getMessage());
            return false;
        }
    }

    public static List<String[]> fetchReceptionPatients() {
        List<String[]> rows = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_RECEPTION_PATIENTS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new String[]{
                        rs.getString("name"),
                        String.valueOf(rs.getInt("age")),
                        rs.getString("disease")
                });
            }
        } catch (SQLException ex) {
            System.err.println("[DB] fetchReceptionPatients failed: " + ex.getMessage());
        }
        return rows;
    }

    public static boolean deleteReceptionPatient(String name, int age, String disease) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_RECEPTION_PATIENT)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, disease);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[DB] deleteReceptionPatient failed: " + ex.getMessage());
            return false;
        }
    }

    public static List<String[]> fetchAppointments() {
        List<String[]> rows = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_APPOINTMENTS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("patient_name"),
                        String.valueOf(rs.getInt("age")),
                        rs.getString("disease"),
                        rs.getString("appointment_date")
                });
            }
        } catch (SQLException ex) {
            System.err.println("[DB] fetchAppointments failed: " + ex.getMessage());
        }
        return rows;
    }

    public static boolean deleteAppointmentById(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_APPOINTMENT_BY_ID)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[DB] deleteAppointmentById failed: " + ex.getMessage());
            return false;
        }
    }

    public static boolean insertPatientLogin(String username, String password) {
        return insertRoleLogin("patient", username, password);
    }

    public static boolean insertDoctorLogin(String username, String password) {
        return insertRoleLogin("doctor", username, password);
    }

    public static boolean insertReceptionistLogin(String username, String password) {
        return insertRoleLogin("receptionist", username, password);
    }

    private static boolean insertRoleLogin(String role, String username, String password) {
        if (role == null || username == null || password == null) {
            return false;
        }

        String cleanRole = role.trim().toLowerCase();
        String cleanUsername = username.trim();
        String cleanPassword = password.trim();

        if (cleanRole.isEmpty() || cleanUsername.isEmpty() || cleanPassword.isEmpty()) {
            return false;
        }

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_USER_LOGIN)) {
            ps.setString(1, cleanRole);
            ps.setString(2, cleanUsername);
            ps.setString(3, cleanPassword);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[DB] insertRoleLogin failed: " + ex.getMessage());
            return false;
        }
    }

    public static boolean validatePatientLogin(String username, String password) {
        return validateRoleLogin("patient", username, password);
    }

    public static boolean validateDoctorLogin(String username, String password) {
        return validateRoleLogin("doctor", username, password);
    }

    public static boolean validateReceptionistLogin(String username, String password) {
        return validateRoleLogin("receptionist", username, password);
    }

    private static boolean validateRoleLogin(String role, String username, String password) {
        if (role == null || username == null || password == null) {
            return false;
        }

        String cleanRole = role.trim().toLowerCase();
        String cleanUsername = username.trim();
        String cleanPassword = password.trim();

        if (cleanRole.isEmpty() || cleanUsername.isEmpty() || cleanPassword.isEmpty()) {
            return false;
        }

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_LOGIN)) {
            ps.setString(1, cleanRole);
            ps.setString(2, cleanUsername);
            ps.setString(3, cleanPassword);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.err.println("[DB] validateRoleLogin failed: " + ex.getMessage());
            return false;
        }
    }

    private static void seedDefaultLogins(Connection connection) throws SQLException {
        ensureRoleLogin(connection, "doctor", "doctor", "1234");
        ensureRoleLogin(connection, "patient", "patient", "1234");
        ensureRoleLogin(connection, "receptionist", "reception", "1234");
    }

    private static void ensureDatabaseExists() throws SQLException {
        try (Connection adminConnection = DriverManager.getConnection(DB_ADMIN_URL, DB_USER, DB_PASSWORD);
             Statement statement = adminConnection.createStatement()) {
            statement.execute("CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "`");
        }
    }

    private static void ensureRoleLogin(Connection connection, String role, String username, String password) throws SQLException {
        try (PreparedStatement countPs = connection.prepareStatement("SELECT COUNT(*) FROM user_logins WHERE role = ?")) {
            countPs.setString(1, role);
            try (ResultSet rs = countPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return;
                }
            }
        }

        try (PreparedStatement insertPs = connection.prepareStatement(INSERT_USER_LOGIN)) {
            insertPs.setString(1, role);
            insertPs.setString(2, username);
            insertPs.setString(3, password);
            insertPs.executeUpdate();
        }
    }
}

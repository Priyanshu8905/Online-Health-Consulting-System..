@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        // Hash password for security
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Users (username, password, email, role) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, role);
            int result = pstmt.executeUpdate();

            if (result > 0) {
                response.sendRedirect("login.jsp");  // Redirect to login page after successful registration
            } else {
                response.sendRedirect("register.jsp?error=Registration failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=Database error.");
        }
    }
}
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && BCrypt.checkpw(password, rs.getString("password"))) {
                HttpSession session = request.getSession();
                session.setAttribute("user", rs.getString("username"));
                session.setAttribute("role", rs.getString("role"));
                response.sendRedirect("dashboard.jsp");  // Redirect to the user's dashboard
            } else {
                response.sendRedirect("login.jsp?error=Invalid credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Database error.");
        }
    }
}
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("user");

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Profiles WHERE user_id = (SELECT user_id FROM Users WHERE username = ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                request.setAttribute("profile", rs);
                RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=Error loading profile.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("full_name");
        String dateOfBirth = request.getParameter("dob");
        String healthConditions = request.getParameter("health_conditions");
        String medicalHistory = request.getParameter("medical_history");

        String username = (String) request.getSession().getAttribute("user");

        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE Profiles SET full_name = ?, date_of_birth = ?, health_conditions = ?, medical_history = ? WHERE user_id = (SELECT user_id FROM Users WHERE username = ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fullName);
            pstmt.setString(2, dateOfBirth);
            pstmt.setString(3, healthConditions);
            pstmt.setString(4, medicalHistory);
            pstmt.setString(5, username);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                response.sendRedirect("profile.jsp?message=Profile updated successfully.");
            } else {
                response.sendRedirect("profile.jsp?error=Error updating profile.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("profile.jsp?error=Database error.");
        }
    }
}
@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("user");

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");

                // Display available consultants and appointment slots
                String availableConsultantsSQL = "SELECT * FROM Users WHERE role = 'consultant' AND user_id != ?";
                PreparedStatement pstmtConsultants = conn.prepareStatement(availableConsultantsSQL);
                pstmtConsultants.setInt(1, userId);
                ResultSet rsConsultants = pstmtConsultants.executeQuery();

                request.setAttribute("consultants", rsConsultants);
                RequestDispatcher dispatcher = request.getRequestDispatcher("appointments.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=Error loading appointments.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int consultantId = Integer.parseInt(request.getParameter("consultant_id"));
        String appointmentDate = request.getParameter("appointment_date");

        String username = (String) request.getSession().getAttribute("user");

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT user_id FROM Users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int clientId = rs.getInt("user_id");

                String insertAppointmentSQL = "INSERT INTO Appointments (client_id, consultant_id, appointment_date) VALUES (?, ?, ?)";
                PreparedStatement pstmtInsert = conn.prepareStatement(insertAppointmentSQL);
                pstmtInsert.setInt(1, clientId);
                pstmtInsert.setInt(2, consultantId);
                pstmtInsert.setString(3, appointmentDate);

                int result = pstmtInsert.executeUpdate();
                if (result > 0) {
                    response.sendRedirect("appointments.jsp?message=Appointment booked successfully.");
                } else {
                    response.sendRedirect("appointments.jsp?error=Error booking appointment.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("appointments.jsp?error=Error handling appointment.");
        }
    }
}

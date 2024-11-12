# Online Health Consulting System

The **Online Health Consulting System** is a web-based platform built using **Java Servlets**, **JSP**, and **MySQL**. The system connects **clients** with **health consultants** for consultations, appointment scheduling, and accessing health resources such as articles and videos. The platform provides secure communication between clients and consultants and allows admins to manage users and appointments.

---

## Features

### **1. User Registration and Login:**
- Clients and Consultants can create accounts and log in to the system.
- Role-based access control (Client, Consultant, Admin).
- Secure login and session management.

### **2. Profile Management:**
- Clients can manage personal health information.
- Consultants can manage their profiles, including specialties, availability, and more.

### **3. Appointment Scheduling:**
- Clients can view consultant availability and book appointments.
- Appointment status updates (e.g., Scheduled, Completed, Cancelled).
- Automated email notifications and reminders for upcoming appointments.

### **4. Health Resources:**
- A repository of health-related resources such as articles, videos, and guides.
- Clients and Consultants can access and view these resources.

### **5. Admin Dashboard:**
- Admin users can manage all users (Clients, Consultants) and appointments.
- Admins can view all appointments, users' profiles, and more.

---

## Prerequisites

Before setting up the project, ensure you have the following:

- **Java 11** (or higher) installed.
- **Apache Maven** for building and managing the project.
- **MySQL** for the database.

---

## Setup Instructions

### **1. Clone the Repository**

```bash
git clone https://github.com/Priyanshu8905/Online-Health-Consulting-System.
cd Online-Health-Consulting-System
```

### **2. Set Up MySQL Database**

1. **Create a new MySQL database:**

   ```sql
   CREATE DATABASE health_consulting;
   ```

2. **Create tables:**

   Run the following SQL script to create the necessary tables for the system:

   ```sql
   -- Create Users Table (Client, Consultant, Admin)
   CREATE TABLE users (
       user_id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       role ENUM('client', 'consultant', 'admin') NOT NULL,
       name VARCHAR(100),
       email VARCHAR(100) UNIQUE,
       phone VARCHAR(15)
   );

   -- Create Appointments Table
   CREATE TABLE appointments (
       appointment_id INT AUTO_INCREMENT PRIMARY KEY,
       client_id INT NOT NULL,
       consultant_id INT NOT NULL,
       appointment_date DATETIME NOT NULL,
       status ENUM('scheduled', 'completed', 'cancelled') DEFAULT 'scheduled',
       FOREIGN KEY (client_id) REFERENCES users(user_id),
       FOREIGN KEY (consultant_id) REFERENCES users(user_id)
   );

   -- Create Health Resources Table
   CREATE TABLE resources (
       resource_id INT AUTO_INCREMENT PRIMARY KEY,
       title VARCHAR(255) NOT NULL,
       content TEXT NOT NULL,
       resource_type ENUM('article', 'video', 'image') NOT NULL,
       url VARCHAR(255) NULL
   );
   ```

3. **Update Database Connection:**
   In the `src/main/java/com/healthconsulting/DbUtil.java` file, update the database connection details:

   ```java
   public static final String DB_URL = "jdbc:mysql://localhost:3306/health_consulting";
   public static final String DB_USERNAME = "root";
   public static final String DB_PASSWORD = "yourpassword";
   ```

### **3. Build the Project**

Use **Maven** to clean and install the project dependencies.

```bash
mvn clean install
```

This will generate a `.war` file in the `target` directory (e.g., `online-health-consulting.war`).

### **4. Deploy the WAR File**

1. **Deploy the WAR file** to your servlet container (e.g., **Apache Tomcat**).

2. **Start the servlet container** and access the application in your web browser.

   ```bash
   http://localhost:8080/online-health-consulting
   ```

---

## Project Structure

### **1. Servlets**

- **LoginServlet**: Handles user login functionality.
- **RegisterServlet**: Handles user registration for Clients and Consultants.
- **ProfileServlet**: Manages user profile (Clients and Consultants).
- **AppointmentServlet**: Manages appointment booking and scheduling.
- **ResourceServlet**: Manages health-related resources (articles, videos).
- **AdminServlet**: Admin functionality to manage users and appointments.

### **2. JSP Pages**

- **login.jsp**: Login page for Clients and Consultants.
- **register.jsp**: Registration page for new users (Clients/Consultants).
- **client_dashboard.jsp**: Dashboard for Clients to view appointments and resources.
- **consultant_dashboard.jsp**: Dashboard for Consultants to manage their appointments.
- **admin_dashboard.jsp**: Dashboard for Admins to manage users and appointments.
- **resources.jsp**: Page to display health-related articles, videos, etc.
- **profile.jsp**: Page for profile management (Clients and Consultants).
- **error.jsp**: Page for error handling.

### **3. Configuration**

- **web.xml**: Servlet and filter configuration.

---

## Running the Application

1. **Start your servlet container (e.g., Apache Tomcat).**
2. **Open a web browser** and navigate to the following URL:

   ```bash
   http://localhost:8080/online-health-consulting
   ```

3. **Login** with a registered user (Client or Consultant), or use the admin credentials to manage the platform.

---

## Dependencies

- **JUnit**: For unit testing.
- **MySQL Connector**: For database connectivity.
- **Java Servlet API**: For servlet support.
- **JSP API**: For JSP page rendering.
- **SLF4J and Logback**: For logging purposes.

---

## Contributing

Feel free to fork the repository, create an issue, and submit a pull request if you'd like to contribute. 

1. Fork the repository.
2. Create a new branch.
3. Make your changes.
4. Create a pull request.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Acknowledgements

- [Java Servlet API](https://docs.oracle.com/javaee/7/api/)
- [JSP](https://www.oracle.com/java/technologies/java-server-pages.html)
- [MySQL](https://www.mysql.com/)
- [Apache Tomcat](https://tomcat.apache.org/)

# RESQURE-OOP-PROJECT-2-1-



## Overview
The **Disaster Management System** is an  emergency response platform designed to assist citizens, administrators, and responders during critical situations. The system focuses on efficient communication,  disaster reporting, and streamlined coordination to ensure rapid response and effective management of emergency events.

---


## Workflow

![Image](https://github.com/user-attachments/assets/b69a0a97-3dc8-47d6-84e4-33a665e115c5)


---

## Key Features

### 🔹 For Citizens
- **Report Disasters**: Instantly report disasters with location, severity, and details.
- **Survival Guidance**: Receive crucial safety instructions and survival tips.
- **Emergency Contacts**: Quick access to helplines and emergency services.
- **Real-Time Messaging**: Chat with admins to report issues, ask for guidance, and receive urgent updates.
  


### 🔹 For Administrators
- **Manage Reports**: Review, verify, and prioritize disaster reports.
- **Task Assignment**: Allocate tasks to responders and volunteers.

- **Real-Time Messaging**: Communicate directly with citizens for updates, instructions, and task management.
  
  


### 🔹 For Volunteers & Responders
- **Task Tracking**: View assigned tasks and update their status.
- **Performance Monitoring**: Log completed tasks and measure impact.
  
  

---

## Technology Used

- **IDE**: IntelliJ IDEA
- **Language**: Java
-  **Framework**: JavaFX (for a dynamic and user-friendly UI)
-  **Database**: MySQL (for structured data storage and management)
-  **Testing**: Manual Testing (to ensure smooth system functionality)

---


## Authors

- **Sumaiya Rahman Soma**
- **Chowdhury Shafahid Rahman**

---

## Download Instructions

1. **Download the ZIP File**:  
   Download the project ZIP file from the repository .

2. **Extract the Files**:  
   Extract the contents of the ZIP file to your preferred directory.

3. **Database Setup**:  
   - Import the provided **SQL schema** or execute the SQL script from the **Database Schema** folder into your MySQL database to set it
   - Then, execute the provided SQL script to create the required tables and structure for your application.
     
4. **Modify Database Credentials**:  
   - Open the `Database.java` file located in your project folder.
   - Locate and update the following lines with your **MySQL username** and **password**:
     ```java
     String jdbcUsername = "root";  // MySQL username
     String jdbcPassword = "root";  // MySQL password
     String jdbcURL = "jdbc:mysql://localhost:3306/disaster";  // Database URL
     ```
     Replace `"root"` with your actual MySQL username and password, if different from the default.
     Example:
     ```java
     String jdbcUsername = "your_username";  // MySQL username
     String jdbcPassword = "your_password";  // MySQL password
     String jdbcURL = "jdbc:mysql://localhost:3306/disaster";  // Database URL
     ```
   - Save the file.

5. **Run the Application**:  
   - After setting up the database and modifying the credentials, run the application.
   - For running the chat application, first run the chat server.

  

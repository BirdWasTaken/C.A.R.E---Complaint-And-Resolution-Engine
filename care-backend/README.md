# C.A.R.E Backend – Spring Boot
### Complaint And Resolution Engine

---

## Project Structure

```
care-backend/
├── pom.xml
└── src/
    └── main/
        ├── java/com/care/
        │   ├── CareApplication.java          ← Main entry point
        │   │
        │   ├── config/
        │   │   └── CorsConfig.java           ← CORS settings
        │   │
        │   ├── controller/
        │   │   ├── AuthController.java       ← /api/auth/*
        │   │   └── ComplaintController.java  ← /api/complaints/*
        │   │
        │   ├── service/
        │   │   ├── UserService.java          ← Signup / Login logic
        │   │   └── ComplaintService.java     ← Complaint logic
        │   │
        │   ├── repository/
        │   │   ├── UserRepository.java       ← DB queries for User
        │   │   └── ComplaintRepository.java  ← DB queries for Complaint
        │   │
        │   ├── entity/
        │   │   ├── User.java                 ← User table model
        │   │   └── Complaint.java            ← Complaint table model
        │   │
        │   └── dto/
        │       ├── SignupRequest.java
        │       ├── LoginRequest.java
        │       ├── LoginResponse.java
        │       ├── ComplaintRequest.java
        │       ├── ComplaintResponse.java
        │       ├── StatusUpdateRequest.java
        │       ├── DashboardStats.java
        │       └── ApiResponse.java          ← Generic wrapper
        │
        └── resources/
            ├── application.properties        ← DB config
            └── schema.sql                    ← Reference SQL schema
```

---

## Prerequisites

| Tool           | Version     |
|----------------|-------------|
| Java (JDK)     | 17 or above |
| Maven          | 3.8+        |
| MySQL          | 8.0+        |
| IDE (optional) | IntelliJ IDEA / VS Code |

---

## Steps to Run the Backend

### Step 1 – Set Up MySQL Database

Open MySQL Workbench or terminal and run:
```sql
CREATE DATABASE care_db;
```
That's it — Hibernate will auto-create the tables on first run.

---

### Step 2 – Configure application.properties

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/care_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
```

---

### Step 3 – Create the Admin Account

After the app starts once (tables are created), run this SQL to create the default admin:

```sql
USE care_db;

INSERT INTO users (full_name, student_id, email, phone, password, role, created_at)
VALUES (
  'Administrator',
  'ADMIN001',
  'admin@care.com',
  '0000000000',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'ADMIN',
  NOW()
);
```
> Admin credentials → Email: `admin@care.com` | Password: `admin123`

---

### Step 4 – Run the Backend

**Option A – Using Maven (terminal):**
```bash
cd care-backend
mvn spring-boot:run
```

**Option B – Using IntelliJ IDEA:**
1. Open the `care-backend` folder as a project
2. Right-click `CareApplication.java` → `Run`

**Option C – Build a JAR and run:**
```bash
mvn clean package
java -jar target/care-backend-1.0.0.jar
```

---

### Step 5 – Verify It's Running

Open your browser and go to:
```
http://localhost:8080
```
You should see a Whitelabel Error Page — this means the server is running!

Test with Postman or curl (see API section below).

---

## API Endpoint List

### Base URL: `http://localhost:8080`

---

### AUTH APIs

| Method | Endpoint           | Description          | Who    |
|--------|--------------------|----------------------|--------|
| POST   | /api/auth/signup   | Register new user    | Public |
| POST   | /api/auth/login    | Login (user + admin) | Public |

---

### COMPLAINT APIs (User)

| Method | Endpoint                         | Description               |
|--------|----------------------------------|---------------------------|
| POST   | /api/complaints                  | Submit new complaint       |
| GET    | /api/complaints/user/{userId}    | Get my complaints          |
| GET    | /api/complaints/{id}             | Get complaint details      |

---

### COMPLAINT APIs (Admin)

| Method | Endpoint                         | Description               |
|--------|----------------------------------|---------------------------|
| GET    | /api/complaints/admin/all        | View all complaints        |
| PUT    | /api/complaints/admin/{id}/status| Update status + response   |
| GET    | /api/complaints/admin/stats      | Dashboard stats            |

---

## Example Requests & Responses

### 1. SIGNUP
```
POST http://localhost:8080/api/auth/signup
Content-Type: application/json

{
  "fullName":  "John Doe",
  "studentId": "STU001",
  "email":     "john@example.com",
  "phone":     "9876543210",
  "password":  "secret123"
}
```
**Response (201 Created):**
```json
{
  "success": true,
  "message": "Account created successfully! Please login.",
  "data": "john@example.com"
}
```

---

### 2. LOGIN
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email":    "john@example.com",
  "password": "secret123"
}
```
**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "userId":    1,
    "fullName":  "John Doe",
    "email":     "john@example.com",
    "studentId": "STU001",
    "role":      "USER",
    "message":   "Login successful"
  }
}
```
> Save `userId` and `role` in browser localStorage for subsequent requests.

---

### 3. SUBMIT COMPLAINT
```
POST http://localhost:8080/api/complaints
Content-Type: application/json

{
  "userId":      1,
  "title":       "Library AC Not Working",
  "category":    "infrastructure",
  "location":    "Main Library, 2nd Floor",
  "priority":    "high",
  "description": "The AC has been broken for a week. Very uncomfortable."
}
```
**Response (201 Created):**
```json
{
  "success": true,
  "message": "Complaint submitted successfully.",
  "data": {
    "id":            1,
    "title":         "Library AC Not Working",
    "category":      "INFRASTRUCTURE",
    "location":      "Main Library, 2nd Floor",
    "priority":      "HIGH",
    "description":   "The AC has been broken for a week. Very uncomfortable.",
    "status":        "OPEN",
    "adminResponse": null,
    "createdAt":     "2026-03-19T10:30:00",
    "userId":        1,
    "userName":      "John Doe",
    "studentId":     "STU001"
  }
}
```

---

### 4. GET MY COMPLAINTS
```
GET http://localhost:8080/api/complaints/user/1
```
**Response (200 OK):**
```json
{
  "success": true,
  "message": "Complaints fetched successfully.",
  "data": [ { ...complaint1 }, { ...complaint2 } ]
}
```

---

### 5. ADMIN – UPDATE STATUS
```
PUT http://localhost:8080/api/complaints/admin/1/status
Content-Type: application/json

{
  "status":        "IN_PROGRESS",
  "adminResponse": "We have assigned a technician to fix the AC."
}
```
**Response (200 OK):**
```json
{
  "success": true,
  "message": "Complaint status updated.",
  "data": {
    "id":            1,
    "status":        "IN_PROGRESS",
    "adminResponse": "We have assigned a technician to fix the AC.",
    ...
  }
}
```

---

### 6. ADMIN – DASHBOARD STATS
```
GET http://localhost:8080/api/complaints/admin/stats
```
**Response (200 OK):**
```json
{
  "success": true,
  "message": "Dashboard stats fetched.",
  "data": {
    "totalComplaints":      45,
    "openComplaints":       12,
    "inProgressComplaints": 18,
    "resolvedComplaints":   15
  }
}
```

---

## Valid Values for Enums

### Category (case-insensitive in request)
- `infrastructure`
- `facilities`
- `academic`
- `administration`
- `other`

### Priority (case-insensitive in request)
- `low`
- `medium`
- `high`
- `urgent`

### Status (for admin update — case-insensitive)
- `OPEN`
- `IN_PROGRESS`
- `RESOLVED`

---

## Connecting Frontend to Backend

In your frontend JavaScript, replace static data with API calls:

```javascript
const BASE_URL = "http://localhost:8080/api";

// Example: Login
async function loginUser(email, password) {
  const response = await fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  });
  const result = await response.json();
  if (result.success) {
    // Save userId and role to localStorage
    localStorage.setItem("userId", result.data.userId);
    localStorage.setItem("role", result.data.role);
    localStorage.setItem("userName", result.data.fullName);
  }
  return result;
}

// Example: Submit Complaint
async function submitComplaint(formData) {
  const userId = localStorage.getItem("userId");
  const response = await fetch(`${BASE_URL}/complaints`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ ...formData, userId: parseInt(userId) })
  });
  return await response.json();
}

// Example: Get My Complaints
async function getMyComplaints() {
  const userId = localStorage.getItem("userId");
  const response = await fetch(`${BASE_URL}/complaints/user/${userId}`);
  return await response.json();
}
```

---

## Future Features (Pre-planned)

The backend is already structured to support these additions easily:

| Feature              | Where to Add                                      |
|----------------------|---------------------------------------------------|
| File attachments     | Add `attachmentPath` field to `Complaint` entity  |
| Search/Filter        | Use `ComplaintRepository.searchByKeyword()`       |
| Pagination           | Change `List<>` to `Page<>` in repository/service |
| JWT Authentication   | Add `spring-boot-starter-security` + JWT library  |
| Email notifications  | Add `spring-boot-starter-mail` + Email service    |
| Admin stats charts   | `DashboardStats` DTO already ready                |

---

## Common Errors & Fixes

| Error                               | Fix                                              |
|-------------------------------------|--------------------------------------------------|
| `Access denied for user 'root'`     | Wrong MySQL password in application.properties  |
| `Unknown database 'care_db'`        | Create DB manually or add `createDatabaseIfNotExist=true` to URL |
| `Port 8080 already in use`          | Change `server.port=8081` in properties         |
| `CORS error` in browser             | Already handled — make sure backend is running  |
| `Table 'users' doesn't exist`       | Start the app once with `ddl-auto=update`       |

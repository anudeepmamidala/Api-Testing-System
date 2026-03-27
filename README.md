# 🚀 API Testing System (Spring Boot + React)

## 📌 Overview

A full-stack API testing platform that allows users to send HTTP requests (GET, POST, PUT, DELETE) and view responses in real time. Designed to simplify API testing with an interactive UI and dynamic request handling.

---

## ⚙️ Tech Stack

* **Backend:** Spring Boot (Java)
* **Frontend:** React.js
* **Build Tool:** Maven
* **Communication:** REST APIs

---

## ✨ Features

* Send HTTP requests (GET, POST, PUT, DELETE)
* Add custom headers dynamically
* Support for request body and token-based authentication
* View API responses in real time
* Request history tracking for reuse

---

## 🧠 How It Works

1. User enters API URL, method, headers, and body in the UI
2. Frontend sends request data to backend (Spring Boot)
3. Backend processes request using service layer
4. External API is called dynamically
5. Response is returned and displayed in UI

---

## 🏗️ Backend Architecture

* **Controller Layer:** Handles incoming requests from frontend
* **Service Layer:** Contains logic for executing API calls
* **DTO Layer:** Transfers request/response data between layers

---

## 🎨 Frontend Structure

* **RequestBuilder:** Handles user input (URL, method, headers, body)
* **ResponseViewer:** Displays API response
* **History:** Stores and shows previous requests

---

## ⚠️ Limitations

* Request history is stored in memory (not persistent)
* No authentication or user management
* Basic UI (focus on functionality over design)

---

## 🚀 Future Improvements

* Add database for persistent history
* Implement authentication (JWT)
* Improve UI/UX and response formatting
* Add response metadata (status code, response time)

---

## 🛠️ Setup Instructions

### Backend

```bash
cd ApiDashboard
mvn install
mvn spring-boot:run
```

### Frontend

```bash
cd Frontend
npm install
npm start
```

---

## 📌 Key Learning Outcomes

* Built a full-stack application using Spring Boot and React
* Implemented dynamic API request handling
* Understood layered backend architecture (Controller-Service-DTO)
* Gained experience in frontend-backend integration

---

## 📷 Screenshots

(Add screenshots here if needed)

---

## 👨‍💻 Author

Anudeep Kumar

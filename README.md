# FinStream – Stream‑Powered Financial Analytics Engine

## 🚀 Live Demo

**Production URL:** [https://finstream-app.onrender.com](https://YOUR-RENDER-URL-HERE)

---

## 📌 Overview

FinStream is a high‑performance financial analytics web application built using **Spring Boot (Java 17)** and **Functional Programming with Java Streams**.

The system allows users to upload a CSV file containing transaction data and instantly receive structured financial insights — processed entirely **in-memory** without any database.

The application is fully containerized using **Docker (multi-stage build)** and deployed publicly via **Render**.

---

## 🎯 Problem Statement

Modern professionals often maintain transaction exports from banking or expense systems but lack lightweight tools to quickly analyze them without installing heavy software.

FinStream solves this by:

* Accepting raw transaction datasets
* Processing them using Java Streams
* Generating financial intelligence in seconds
* Presenting results in a clean dashboard UI

---

## 🧠 Key Features

### Financial Overview

* Total Income
* Total Expense
* Net Balance
* Average Transaction Value

### Spending Insights

* Category-wise Expense Breakdown
* Top Spending Category
* Highest Transaction
* Monthly Expense Trend

### Engineering Features

* Pure in-memory processing (no database)
* Functional programming with Streams API
* BigDecimal precision handling
* Input validation and error handling
* Docker multi-stage build
* Non-root container runtime
* Environment-aware port binding
* Auto-deploy via GitHub → Render

---

## 🏗️ Architecture

```
Client (HTML/CSS/JS)
        ↓
Spring Boot REST Controller
        ↓
CSV Parsing Service
        ↓
Stream-Based Analytics Engine
        ↓
Structured JSON Response
        ↓
Dashboard Rendering
```

The application follows a layered architecture:

* Controller Layer
* Service Layer
* Domain Model Layer

No persistence layer by design (stateless processing).

---

## 📂 Project Structure

```
src/
 ├── main/
 │   ├── java/com/vcs/finstream/
 │   │   ├── controller/
 │   │   ├── service/
 │   │   ├── model/
 │   │   └── FinstreamApplication.java
 │   └── resources/
 │       ├── static/
 │       └── application.properties
Dockerfile
.dockerignore
pom.xml
```

---

## 🧾 Expected CSV Format

| transactionId | amount  | type  | category | timestamp           |
| ------------- | ------- | ----- | -------- | ------------------- |
| TX1           | 2500.00 | DEBIT | FOOD     | 2026-01-10T10:15:30 |

* Allowed file type: `.csv`
* Timestamp format: ISO‑8601
* Type: CREDIT or DEBIT
* Category: FOOD, RENT, TRAVEL, SHOPPING, ENTERTAINMENT, SALARY, UTILITIES, OTHER

---

## ⚙️ Tech Stack

| Layer      | Technology            |
| ---------- | --------------------- |
| Backend    | Spring Boot 3.x       |
| Language   | Java 17               |
| Build Tool | Maven                 |
| Container  | Docker (Multi-stage)  |
| Deployment | Render                |
| Frontend   | HTML, CSS, JavaScript |

---

## 🐳 Dockerization

Multi-stage Docker build:

1. Maven builder stage
2. Lightweight JRE runtime stage
3. Non-root execution
4. Dynamic port configuration

Run locally:

```
docker build -t finstream-app .
docker run -p 8080:8080 finstream-app
```

---

## ☁️ Deployment Strategy

* GitHub repository connected to Render
* Automatic builds on push to `main`
* Free tier web service
* HTTPS enabled by default
* Stateless design for scalability

---

## 🔐 Production Considerations

* No database (stateless)
* Controlled CSV parsing
* Precision-safe monetary calculations
* No blocking operations
* Optimized for small-to-medium datasets (1K–50K records comfortably)

---

## 📊 Performance Notes

* In-memory Stream processing
* O(n) single-pass aggregations
* Minimal object overhead
* Instant response for ~1,000 transactions

---

## 📸 Screenshots

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/5a0996d0-c3f3-42a8-ab68-2aa18f4f9562" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/cfeeb424-7ec1-4f18-8a29-acc3255f4173" />

---

## 👨‍💻 Developer

**Chandra Sekhar Vipparla**
LinkedIn: [https://www.linkedin.com/in/chandra-sekhar-vipparla/](https://www.linkedin.com/in/chandra-sekhar-vipparla/)

GitHub: [https://github.com/codeWithVCS](https://github.com/codeWithVCS)

---

## 📜 License

This project is built for portfolio demonstration purposes.

---

## 🏁 Future Enhancements

* JWT authentication
* Multi-user support
* Historical analytics storage
* Redis caching layer
* Export insights as PDF
* Advanced visualization charts

---

⭐ If you found this project interesting, feel free to star the repository.

# 🏦 BankSecurity — Secure Banking Management System

A professional Spring Boot 3 banking application with Spring Security, JPA, and a polished dark navy + gold UI.

---

## 🏗️ Project Structure

```
BankSecurity/
├── src/main/java/com/Bank/BankSecurity/
│   ├── BankSecurityApplication.java        ← App entry point
│   ├── config/
│   │   └── SecurityConfig.java             ← Spring Security (BCrypt, form login)
│   ├── controller/
│   │   ├── AuthController.java             ← Login / Register
│   │   ├── DashboardController.java        ← Dashboard overview
│   │   ├── AccountController.java          ← Create bank accounts
│   │   └── TransactionController.java      ← Deposit / Withdraw / Transfer / History
│   ├── model/
│   │   ├── User.java                       ← User entity (name, email, password, role)
│   │   ├── Account.java                    ← Bank account (number, type, balance)
│   │   └── Transaction.java                ← Transaction record (type, amount, date)
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── AccountRepository.java
│   │   └── TransactionRepository.java
│   └── service/
│       ├── UserService.java                ← Registration + BCrypt encoding
│       ├── AccountService.java             ← Account creation
│       ├── TransactionService.java         ← Business logic: deposit/withdraw/transfer
│       └── CustomUserDetailsService.java   ← Spring Security UserDetailsService
│
├── src/main/resources/
│   ├── application.properties              ← Common config (active profile: dev)
│   ├── application-dev.properties          ← H2 in-memory DB (no MySQL needed)
│   ├── application-prod.properties         ← MySQL config
│   ├── static/css/style.css                ← Full professional UI stylesheet
│   └── templates/                          ← Thymeleaf HTML pages
│       ├── login.html
│       ├── register.html
│       ├── dashboard.html
│       ├── deposit.html
│       ├── withdraw.html
│       ├── transfer.html
│       ├── transactions.html
│       ├── account-new.html
│       └── error.html
│
└── pom.xml
```

---

## 🚀 Quick Start (Dev Mode — H2, no MySQL needed)

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the application

```bash
cd BankSecurity
mvn spring-boot:run
```

App starts at: **http://localhost:8081**

> The dev profile uses an **H2 in-memory database** — no MySQL setup required.
> H2 console available at: http://localhost:8081/h2-console (JDBC URL: `jdbc:h2:mem:banking_db`)

---

## 🗄️ MySQL Setup (Production)

1. Create the database:
```sql
CREATE DATABASE banking_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Update `application-prod.properties` with your credentials.

3. Switch active profile in `application.properties`:
```properties
spring.profiles.active=prod
```

4. Run: `mvn spring-boot:run`

---

## 🔐 Features

| Feature | Details |
|---|---|
| **Authentication** | Form-based login with Spring Security |
| **Password Security** | BCrypt hashing (strength 12) |
| **Registration** | Auto-creates a Savings account on sign-up |
| **Deposit** | Credit funds to any account |
| **Withdraw** | Debit funds with balance validation |
| **Transfer** | Atomic transfer between two accounts |
| **Transaction History** | Full log with type, amount, balance-after, date |
| **Multiple Accounts** | Users can open Savings / Current / Fixed Deposit |
| **CSRF Protection** | Enabled on all forms |
| **Profiles** | `dev` (H2) and `prod` (MySQL) |

---

## 🌐 URL Reference

| URL | Method | Description |
|---|---|---|
| `/login` | GET/POST | Login page |
| `/auth/register` | GET/POST | Register new user |
| `/dashboard` | GET | Main dashboard |
| `/deposit` | GET | Deposit form |
| `/transaction/deposit` | POST | Process deposit |
| `/withdraw` | GET | Withdraw form |
| `/transaction/withdraw` | POST | Process withdrawal |
| `/transfer` | GET | Transfer form |
| `/transaction/transfer` | POST | Process transfer |
| `/transactions` | GET | Transaction history (filter by account) |
| `/accounts/new` | GET/POST | Open a new bank account |
| `/logout` | GET | Sign out |

---

## 🎨 UI Theme

- **Font**: Playfair Display (headings) + DM Sans (body)
- **Palette**: Dark Navy (#06090f → #1e2f57) + Gold accents (#c9a84c)
- **Layout**: Fixed sidebar navigation + responsive main content area
- **Components**: Stat cards, account cards, transaction tables, alert banners

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.2 |
| Security | Spring Security 6 |
| ORM | Spring Data JPA + Hibernate |
| Database (dev) | H2 in-memory |
| Database (prod) | MySQL 8 |
| Templating | Thymeleaf |
| Build | Maven |
| Java | 17+ |
| Utilities | Lombok |

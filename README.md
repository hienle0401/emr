# EMR CLI — Electronic Medical Records System

A command-line Java application for managing Electronic Medical Records for a **dermatology practice**.  
Built with Java 21, MySQL 8.0.46, and JDBC — no external frameworks.

---

## Overview

This application provides full **Create, Read, Update, Delete (CRUD)** operations across three database tables through a hierarchical text-based menu. It was developed as a Software Engineering final project to simulate a real-world EMR system for a dermatology clinic.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (Eclipse Adoptium Temurin 21 JDK) |
| Database | MySQL 8.0.46 |
| Database Driver | MySQL Connector/J 9.6.0 |
| Build Tool | `javac` (no Maven/Gradle) |
| Frameworks | None |

---

## Project Structure

```
emr/
├── src/
│   └── emr/
│       ├── Main.java                   # Entry point — main menu
│       ├── db/
│       │   └── DBConnection.java       # JDBC connection (edit credentials here)
│       ├── model/
│       │   ├── Patient.java
│       │   ├── Procedure.java
│       │   └── PatientHistory.java
│       ├── dao/
│       │   ├── PatientDAO.java
│       │   ├── ProcedureDAO.java
│       │   └── PatientHistoryDAO.java
│       └── menu/
│           ├── BaseMenu.java           # Shared input helpers
│           ├── PatientMenu.java
│           ├── ProcedureMenu.java
│           └── PatientHistoryMenu.java
├── lib/
│   └── mysql-connector-j-9.6.0.jar
├── CSV files/
│   ├── patients.csv                    # 100 sample patient records
│   ├── procedures.csv                  # 35 dermatology procedures
│   └── patienthistory.csv              # 100 patient history records
├── out/                                # Compiled .class files (git-ignored)
├── SETUP.md                            # Full teammate setup guide
└── README.md
```

---

## Architecture

The application follows a three-layer architecture:

```
Menu Layer       →   handles all user input / output
DAO Layer        →   executes all SQL queries via PreparedStatement
Model Layer      →   plain Java objects (POJOs), no logic
```

Each table has its own Model class, DAO class, and Menu class. All JDBC resources use `try-with-resources` to ensure connections are always closed. All queries use `PreparedStatement` with `?` placeholders — no string-concatenated SQL.

---

## Database Schema

**Database:** `emr_db`

### patients
| Column | Type | Notes |
|---|---|---|
| pid | INT | Primary key, auto-increment |
| name | VARCHAR(100) | Required |
| dob | DATE | Required |
| gender | VARCHAR(10) | Required |
| medical_condition | TEXT | Optional |
| insurance | VARCHAR(50) | Optional |

### procedures
| Column | Type | Notes |
|---|---|---|
| id | INT | Primary key, auto-increment |
| category | VARCHAR(50) | Required |
| primary_indication | TEXT | Optional |
| typical_tool_device | VARCHAR(100) | Optional |
| recovery_time | VARCHAR(50) | Optional |

### patienthistory
| Column | Type | Notes |
|---|---|---|
| pid | INT | Primary key, auto-increment |
| condition | TEXT | Required (backtick-escaped — reserved keyword) |
| symptom_duration | VARCHAR(50) | Optional |
| severity | INT | Optional, value 1–10 |
| primary_treatment | TEXT | Optional |
| procedure_id | INT | Optional, FK → procedures(id) |
| last_visit_date | DATE | Optional |
| followup_required | BOOLEAN | Default FALSE |

---

## Features

### Main Menu
```
=== EMR System ===
1. Patients
2. Procedures
3. Patient History
0. Exit
```

### Each submenu supports
- **Add** — enter a new record
- **View All** — display all records
- **Update** — edit an existing record by ID
- **Delete** — remove a record by ID
- **Back** — return to the main menu

---

## Prerequisites

- Java 21 JDK (Eclipse Adoptium Temurin 21)
- MySQL Server 8.0+
- MySQL Workbench (for database setup)
- Git (to clone the repository)

> For full installation and database setup instructions, see **[SETUP.md](SETUP.md)**

---

## Quick Start

### 1. Clone the repository
```bash
git clone https://github.com/hienle0401/emr.git
cd emr
```

### 2. Set your database credentials
Open `src/emr/db/DBConnection.java` and update:
```java
private static final String USER     = "root";      // your MySQL username
private static final String PASSWORD = "password";  // your MySQL password
```

### 3. Set up the database
- Create the `emr_db` database and all three tables
- Import sample data (patients, procedures, patienthistory)

> See **[SETUP.md](SETUP.md)** — Parts 3 and 4 — for the exact SQL and import steps.

### 4. Compile
```bash
javac -cp ".;lib/*" -d out src/emr/db/DBConnection.java src/emr/model/*.java src/emr/dao/*.java src/emr/menu/*.java src/emr/Main.java
```

### 5. Run
```bash
java -cp "out;lib/*" emr.Main
```

> **Note:** The classpath separator `;` is Windows-specific. On macOS/Linux, replace `;` with `:` in both commands.

---

## Troubleshooting

| Error | Fix |
|---|---|
| `Communications link failure` | MySQL is not running — open MySQL Workbench and check the connection |
| `Access denied for user 'root'` | Wrong password in `DBConnection.java` |
| `Unknown database 'emr_db'` | Re-run the CREATE DATABASE SQL from SETUP.md Part 3 |
| `javac: command not found` | Java not in PATH — reinstall JDK with PATH option enabled |
| `Could not find or load main class` | Run the compile command before running |

---

## Known Limitations

- Database credentials are hardcoded in `DBConnection.java` — each user must edit this file manually
- `patienthistory` is not linked to the `patients` table by a foreign key — records are independent
- The classpath separator in compile/run commands is Windows-only (`;` vs `:`)
- No input validation on `procedure_id` — entering a non-existent ID will throw a raw SQL error

---

## Sample Data

The `CSV files/` folder contains pre-populated dermatology records:
- **100 patients** with real dermatology conditions (e.g., Plaque Psoriasis, Basal Cell Carcinoma, Rosacea)
- **35 procedures** covering common dermatology treatments (e.g., Mohs Surgery, Cryotherapy, Phototherapy)
- **100 patient history** entries linking conditions, severity scores, treatments, and follow-up status

---

## Authors

Developed as a Software Engineering final project.  
GitHub: [https://github.com/hienle0401/emr](https://github.com/hienle0401/emr)

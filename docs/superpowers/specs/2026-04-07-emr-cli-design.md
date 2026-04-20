# EMR CLI CRUD Application — Design Spec
**Date:** 2026-04-07
**Status:** Approved

---

## Overview

A command-line Java 21 application providing full CRUD operations on an Electronic Medical Records (EMR) MySQL database. Menu-driven, hierarchical navigation. JDBC only — no frameworks.

---

## Technical Constraints

- Java 21 (Eclipse Adoptium Temurin 21 JDK)
- MySQL database: `emr_db`
- MySQL Connector/J (JDBC only)
- CLI input via `Scanner`
- `PreparedStatement` for all queries — no string concatenation
- Try-with-resources for all `Connection` / `PreparedStatement` / `ResultSet`

---

## Database Schema

```sql
-- patients
CREATE TABLE patients (
  pid               INT PRIMARY KEY AUTO_INCREMENT,
  name              VARCHAR(100) NOT NULL,
  dob               DATE NOT NULL,
  gender            VARCHAR(10) NOT NULL,
  medical_condition TEXT,
  insurance         VARCHAR(50)
);

-- procedures
CREATE TABLE procedures (
  id                   INT PRIMARY KEY AUTO_INCREMENT,
  category             VARCHAR(50) NOT NULL,
  primary_indication   TEXT,
  typical_tool_device  VARCHAR(100),
  recovery_time        VARCHAR(50)
);

-- patienthistory (patient_id FK added per requirement)
CREATE TABLE patienthistory (
  id                INT PRIMARY KEY AUTO_INCREMENT,
  patient_id        INT NOT NULL,
  condition         TEXT NOT NULL,
  symptom_duration  VARCHAR(50),
  severity          INT CHECK (severity BETWEEN 1 AND 10),
  primary_treatment TEXT,
  procedure_id      INT,
  last_visit_date   DATE,
  followup_required BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (patient_id) REFERENCES patients(pid),
  FOREIGN KEY (procedure_id) REFERENCES procedures(id)
);
```

---

## Architecture: Layered (Model + DAO + Menu)

### File Structure

```
src/emr/
├── Main.java
├── db/
│   └── DBConnection.java
├── model/
│   ├── Patient.java
│   ├── Procedure.java
│   └── PatientHistory.java
├── dao/
│   ├── PatientDAO.java
│   ├── ProcedureDAO.java
│   └── PatientHistoryDAO.java
└── menu/
    ├── PatientMenu.java
    ├── ProcedureMenu.java
    └── PatientHistoryMenu.java
```

### Data Flow

```
Main → [PatientMenu | ProcedureMenu | PatientHistoryMenu]
              ↓
       [PatientDAO | ProcedureDAO | PatientHistoryDAO]
              ↓
         DBConnection.getConnection()
              ↓
            MySQL (emr_db)
```

- `Scanner` created once in `Main`, passed to all menu constructors
- Each DAO method opens its own `Connection` via try-with-resources
- DAOs throw `SQLException`; menus catch and display user-friendly messages

---

## Menu Flow

### Top-level (Main.java)
```
=== EMR System ===
1. Patients
2. Procedures
3. Patient History
0. Exit
```

### Sub-menus (same pattern for Patients and Procedures)
```
=== Patients ===
1. Add
2. View All
3. Update
4. Delete
0. Back
```

### Patient History sub-menu (extra option)
```
=== Patient History ===
1. Add
2. View All
3. View by Patient ID
4. Update
5. Delete
0. Back
```

---

## Models

Plain POJOs — all-args constructor + getters only.

```java
Patient(int pid, String name, LocalDate dob, String gender,
        String medicalCondition, String insurance)

Procedure(int id, String category, String primaryIndication,
          String typicalToolDevice, String recoveryTime)

PatientHistory(int id, int patientId, String condition, String symptomDuration,
               int severity, String primaryTreatment, Integer procedureId,
               LocalDate lastVisitDate, boolean followupRequired)
```

---

## DAO Contracts

```java
// PatientDAO
void              create(Patient p)           throws SQLException
List<Patient>     readAll()                   throws SQLException
void              update(Patient p)           throws SQLException
void              delete(int pid)             throws SQLException

// ProcedureDAO
void              create(Procedure p)         throws SQLException
List<Procedure>   readAll()                   throws SQLException
void              update(Procedure p)         throws SQLException
void              delete(int id)              throws SQLException

// PatientHistoryDAO
void                    create(PatientHistory h)             throws SQLException
List<PatientHistory>    readAll()                            throws SQLException
List<PatientHistory>    readByPatient(int patientId)         throws SQLException
void                    update(PatientHistory h)             throws SQLException
void                    delete(int id)                       throws SQLException
```

---

## DBConnection

```java
// emr.db.DBConnection
static Connection getConnection() throws SQLException
// URL      = "jdbc:mysql://localhost:3306/emr_db"
// USER     = "root"
// PASSWORD = "password"
```

Credentials are `private static final` constants — grader edits one file.

---

## Input Validation

| Field                          | Rule                                                        |
|-------------------------------|-------------------------------------------------------------|
| `name`, `category`, `condition` | Non-empty after trim; re-prompt on blank                   |
| `dob`, `last_visit_date`       | `LocalDate.parse(input)`; re-prompt on `DateTimeParseException` |
| `gender`                       | Non-empty after trim                                        |
| `severity`                     | Optional; if provided must be integer 1–10, blank → `null`  |
| `followup_required`            | `y/n` input → boolean                                       |
| `patient_id`, `procedure_id`   | Valid integer; FK violations reported from `SQLException`   |
| Optional fields                | Blank input stored as `null`                                |

---

## Error Handling

- DAOs declare `throws SQLException` — no swallowing
- Menu methods wrap DAO calls in try-catch, print a one-line error, return to sub-menu
- App never terminates on bad DB state

---

## Compile & Run

```bash
# Compile (Windows)
javac -cp ".;lib/mysql-connector-j-*.jar" -d out $(find src -name "*.java")

# Run
java -cp "out;lib/mysql-connector-j-*.jar" emr.Main
```

JAR file (`mysql-connector-j-*.jar`) placed in `lib/` directory at project root.

---

## Out of Scope

- Connection pooling
- Authentication / login
- Reporting / search beyond "view by patient ID"
- Unit tests

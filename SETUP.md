# EMR CLI — Setup Guide for Teammates

Follow every step in order. Do not skip steps.

---

## Part 1 — Install Required Software

### 1.1 Java 21 (Eclipse Adoptium Temurin 21 JDK)

1. Go to: https://adoptium.net/temurin/releases/
2. Select: **Version 21**, OS: **Windows**, Architecture: **x64**, Package: **JDK**
3. Download and run the `.msi` installer
4. On the installer screen, make sure **"Add to PATH"** and **"Set JAVA_HOME"** are both checked
5. Finish installation

Verify:
```bash
java -version
```
Expected output: `openjdk version "21..."`

---

### 1.2 MySQL Server + MySQL Workbench

1. Go to: https://dev.mysql.com/downloads/installer/
2. Download **MySQL Installer for Windows** (the larger ~450MB version)
3. Run the installer, choose **"Developer Default"** setup type
4. Follow the wizard — when prompted to set a root password, **write it down**, you will need it later
5. Complete the installation

Verify MySQL is running:
- Open **MySQL Workbench**
- Click the **Local instance MySQL80** connection box
- If it connects successfully, MySQL is running

---

## Part 2 — Get the Project Files

### 2.1 Clone the repository

Open a terminal (Git Bash or Command Prompt) and run:

```bash
git clone https://github.com/hienle0401/emr.git
cd emr
```

Or download as ZIP from GitHub: **Code → Download ZIP**, then extract.

---

## Part 3 — Set Up the Database

### 3.1 Create the database and tables

1. Open **MySQL Workbench** and connect to Local instance
2. Click the **"+"** icon to open a new query tab
3. Paste and run the following SQL (**Ctrl+Shift+Enter**):

```sql
CREATE DATABASE IF NOT EXISTS emr_db;
USE emr_db;

CREATE TABLE patients (
  pid INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  dob DATE NOT NULL,
  gender VARCHAR(10) NOT NULL,
  medical_condition TEXT,
  insurance VARCHAR(50)
);

CREATE TABLE procedures (
  id INT PRIMARY KEY AUTO_INCREMENT,
  category VARCHAR(50) NOT NULL,
  primary_indication TEXT,
  typical_tool_device VARCHAR(100),
  recovery_time VARCHAR(50)
);

CREATE TABLE patienthistory (
  pid INT PRIMARY KEY AUTO_INCREMENT,
  `condition` TEXT NOT NULL,
  symptom_duration VARCHAR(50),
  severity INT CHECK (severity BETWEEN 1 AND 10),
  primary_treatment TEXT,
  procedure_id INT,
  last_visit_date DATE,
  followup_required BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (procedure_id) REFERENCES procedures(id)
);
```

All 5 statements should show green checkmarks in the Action Output panel.

---

## Part 4 — Import Data

### 4.1 Import patients

1. In the left sidebar, expand **emr_db → Tables**
2. Right-click **patients** → **Table Data Import Wizard**
3. Click **Browse** → select `CSV files/patients.csv` from the cloned repo
4. Select **"Use existing table"** → `emr_db.patients` → **Next**
5. Verify column mapping looks correct → **Next** → **Next** → **Finish**

### 4.2 Import procedures

1. Right-click **procedures** → **Table Data Import Wizard**
2. Click **Browse** → select `CSV files/procedures.csv`
3. Select **"Use existing table"** → `emr_db.procedures` → **Next**
4. Verify column mapping → **Next** → **Next** → **Finish**

### 4.3 Import patienthistory

> **Because I did make some change to the database, please follow this instruction for a successful execuation**
 
1. Navigate to the **PatientHistory_directlyImportedThroughMySQLQuery.txt** file I have in GitHub, copy all the data here. 
2. With these cells copied, go to your MySQL Local instance -> create a new query -> paste them here with
 **USE emr_db**

---

## Part 5 — Configure Database Credentials

1. Open the file `src/emr/db/DBConnection.java` in any text editor
2. Find these two lines and update them with your MySQL credentials:

```java
private static final String USER     = "root";      // ← your MySQL username
private static final String PASSWORD = "root";  // ← your MySQL password
```

3. Save the file

---

## Part 6 — Compile

Open a terminal at the project root folder and run:

```bash
javac -cp ".;lib/*" -d out src/emr/db/DBConnection.java src/emr/model/*.java src/emr/dao/*.java src/emr/menu/*.java src/emr/Main.java
```

Expected: **no output** = successful compile.

If you see `javac: command not found` — Java was not added to PATH. Re-run the Java installer and ensure the PATH option is checked.

---

## Part 7 — Run the Application

```bash
java -cp "out;lib/*" emr.Main
```

You should see:

```
=== EMR System ===
1. Patients
2. Procedures
3. Patient History
0. Exit
Choice:
```

The application is ready to use.

---

## Troubleshooting

| Error | Fix |
|---|---|
| `Communications link failure` | MySQL is not running — open MySQL Workbench and check the connection |
| `Access denied for user 'root'` | Wrong password in `DBConnection.java` |
| `Unknown database 'emr_db'` | Re-run the CREATE DATABASE SQL from Part 3 |
| `javac: command not found` | Java not in PATH — reinstall JDK with PATH option enabled |
| `Could not find or load main class` | Run the compile command first before running |

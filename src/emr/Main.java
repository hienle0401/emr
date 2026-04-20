/*
 * EMR CLI — Electronic Medical Records System
 * ============================================
 * Prerequisites:
 *   - Java 21 (Eclipse Adoptium Temurin 21 JDK)
 *   - MySQL running with database 'emr_db' and tables created (see plan Task 1 schema)
 *   - mysql-connector-j-*.jar placed in lib/
 *
 * Edit src/emr/db/DBConnection.java to set your MySQL USER and PASSWORD before compiling.
 *
 * Compile (run from project root):
 *   mkdir -p out
 *   javac -cp ".;lib/*" -d out src/emr/db/DBConnection.java src/emr/model/*.java src/emr/dao/*.java src/emr/menu/*.java src/emr/Main.java
 *
 * Run:
 *   java -cp "out;lib/*" emr.Main
 *
 * Sample data (run in MySQL before first launch):
 *   INSERT INTO patients (name, dob, gender, medical_condition, insurance)
 *     VALUES ('Alice Smith', '1990-05-14', 'Female', 'Hypertension', 'BlueCross');
 *   INSERT INTO procedures (category, primary_indication, typical_tool_device, recovery_time)
 *     VALUES ('Surgery', 'Appendicitis', 'Scalpel', '2 weeks');
 *   INSERT INTO patienthistory (patient_id, condition, symptom_duration, severity,
 *     primary_treatment, procedure_id, last_visit_date, followup_required)
 *     VALUES (1, 'Abdominal pain', '3 days', 7, 'Appendectomy', 1, '2024-03-01', true);
 */
package emr;

import emr.menu.PatientHistoryMenu;
import emr.menu.PatientMenu;
import emr.menu.ProcedureMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        var patientMenu   = new PatientMenu(sc);
        var procedureMenu = new ProcedureMenu(sc);
        var historyMenu   = new PatientHistoryMenu(sc);

        while (true) {
            System.out.println("\n=== EMR System ===");
            System.out.println("1. Patients");
            System.out.println("2. Procedures");
            System.out.println("3. Patient History");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> patientMenu.run();
                case "2" -> procedureMenu.run();
                case "3" -> historyMenu.run();
                case "0" -> { System.out.println("Goodbye."); return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }
}

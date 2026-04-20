package emr.menu;

import emr.dao.PatientHistoryDAO;
import emr.model.PatientHistory;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PatientHistoryMenu extends BaseMenu {
    private final PatientHistoryDAO dao = new PatientHistoryDAO();

    public PatientHistoryMenu(Scanner sc) { super(sc); }

    public void run() {
        while (true) {
            System.out.println("\n=== Patient History ===");
            System.out.println("1. Add");
            System.out.println("2. View All");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            switch (readMenuChoice()) {
                case 1 -> add();
                case 2 -> viewAll();
                case 3 -> update();
                case 4 -> delete();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void add() {
        String  condition   = readRequired("Condition: ");
        String  duration    = readOptional("Symptom duration (optional): ");
        Integer severity    = readSeverity();
        String  treatment   = readOptional("Primary treatment (optional): ");
        Integer procedureId = readOptionalInt("Procedure ID (optional, blank to skip): ");
        var     lastVisit   = readOptionalDate("Last visit date (YYYY-MM-DD, optional): ");
        boolean followup    = readBoolean("Followup required? (y/n): ");
        try {
            dao.create(new PatientHistory(0, condition, duration, severity,
                    treatment, procedureId, lastVisit, followup));
            System.out.println("History record added.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAll() {
        try {
            List<PatientHistory> list = dao.readAll();
            if (list.isEmpty()) { System.out.println("No records found."); return; }
            list.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void update() {
        int     pid         = readId("History record PID to update: ");
        String  condition   = readRequired("Condition: ");
        String  duration    = readOptional("Symptom duration (optional): ");
        Integer severity    = readSeverity();
        String  treatment   = readOptional("Primary treatment (optional): ");
        Integer procedureId = readOptionalInt("Procedure ID (optional, blank to skip): ");
        var     lastVisit   = readOptionalDate("Last visit date (YYYY-MM-DD, optional): ");
        boolean followup    = readBoolean("Followup required? (y/n): ");
        try {
            dao.update(new PatientHistory(pid, condition, duration, severity,
                    treatment, procedureId, lastVisit, followup));
            System.out.println("History record updated.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void delete() {
        int pid = readId("History record PID to delete: ");
        try {
            dao.delete(pid);
            System.out.println("History record deleted.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

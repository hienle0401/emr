package emr.menu;

import emr.dao.PatientDAO;
import emr.model.Patient;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PatientMenu extends BaseMenu {
    private final PatientDAO dao = new PatientDAO();

    public PatientMenu(Scanner sc) { super(sc); }

    public void run() {
        while (true) {
            System.out.println("\n=== Patients ===");
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
        String name      = readRequired("Name: ");
        var    dob       = readDate("DOB (YYYY-MM-DD): ");
        String gender    = readRequired("Gender: ");
        String condition = readOptional("Medical condition (optional): ");
        String insurance = readOptional("Insurance (optional): ");
        try {
            dao.create(new Patient(0, name, dob, gender, condition, insurance));
            System.out.println("Patient added.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAll() {
        try {
            List<Patient> list = dao.readAll();
            if (list.isEmpty()) { System.out.println("No patients found."); return; }
            list.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void update() {
        int    pid       = readId("Patient ID to update: ");
        String name      = readRequired("New name: ");
        var    dob       = readDate("New DOB (YYYY-MM-DD): ");
        String gender    = readRequired("New gender: ");
        String condition = readOptional("New medical condition (optional): ");
        String insurance = readOptional("New insurance (optional): ");
        try {
            dao.update(new Patient(pid, name, dob, gender, condition, insurance));
            System.out.println("Patient updated.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void delete() {
        int pid = readId("Patient ID to delete: ");
        try {
            dao.delete(pid);
            System.out.println("Patient deleted.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

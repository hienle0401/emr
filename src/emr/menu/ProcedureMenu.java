package emr.menu;

import emr.dao.ProcedureDAO;
import emr.model.Procedure;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProcedureMenu extends BaseMenu {
    private final ProcedureDAO dao = new ProcedureDAO();

    public ProcedureMenu(Scanner sc) { super(sc); }

    public void run() {
        while (true) {
            System.out.println("\n=== Procedures ===");
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
        String name       = readRequired("Procedure name: ");
        String category   = readRequired("Category: ");
        String indication = readOptional("Primary indication (optional): ");
        String tool       = readOptional("Typical tool/device (optional): ");
        String recovery   = readOptional("Recovery time (optional): ");
        try {
            dao.create(new Procedure(0, name, category, indication, tool, recovery));
            System.out.println("Procedure added.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAll() {
        try {
            List<Procedure> list = dao.readAll();
            if (list.isEmpty()) { System.out.println("No procedures found."); return; }
            list.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void update() {
        int    id         = readId("Procedure ID to update: ");
        String name       = readRequired("New procedure name: ");
        String category   = readRequired("New category: ");
        String indication = readOptional("New primary indication (optional): ");
        String tool       = readOptional("New typical tool/device (optional): ");
        String recovery   = readOptional("New recovery time (optional): ");
        try {
            dao.update(new Procedure(id, name, category, indication, tool, recovery));
            System.out.println("Procedure updated.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void delete() {
        int id = readId("Procedure ID to delete: ");
        try {
            dao.delete(id);
            System.out.println("Procedure deleted.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

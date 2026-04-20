package emr.menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

abstract class BaseMenu {
    protected final Scanner sc;

    BaseMenu(Scanner sc) { this.sc = sc; }

    protected String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = sc.nextLine().trim();
            if (!v.isEmpty()) return v;
            System.out.println("Field is required.");
        }
    }

    protected String readOptional(String prompt) {
        System.out.print(prompt);
        String v = sc.nextLine().trim();
        return v.isEmpty() ? null : v;
    }

    protected LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return LocalDate.parse(sc.nextLine().trim()); }
            catch (DateTimeParseException e) { System.out.println("Invalid date. Use YYYY-MM-DD."); }
        }
    }

    protected LocalDate readOptionalDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = sc.nextLine().trim();
            if (v.isEmpty()) return null;
            try { return LocalDate.parse(v); }
            catch (DateTimeParseException e) { System.out.println("Invalid date. Use YYYY-MM-DD."); }
        }
    }

    protected int readId(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Invalid ID."); }
        }
    }

    protected Integer readOptionalInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = sc.nextLine().trim();
            if (v.isEmpty()) return null;
            try { return Integer.parseInt(v); }
            catch (NumberFormatException e) { System.out.println("Enter a valid integer or leave blank."); }
        }
    }

    protected Integer readSeverity() {
        while (true) {
            System.out.print("Severity (1-10, optional, blank to skip): ");
            String v = sc.nextLine().trim();
            if (v.isEmpty()) return null;
            try {
                int s = Integer.parseInt(v);
                if (s >= 1 && s <= 10) return s;
                System.out.println("Severity must be between 1 and 10.");
            } catch (NumberFormatException e) {
                System.out.println("Enter a number between 1 and 10, or leave blank.");
            }
        }
    }

    protected boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = sc.nextLine().trim().toLowerCase();
            if (v.equals("y")) return true;
            if (v.equals("n")) return false;
            System.out.println("Enter y or n.");
        }
    }

    protected int readMenuChoice() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Enter a number: "); }
        }
    }
}

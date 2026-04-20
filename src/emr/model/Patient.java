package emr.model;

import java.time.LocalDate;

public class Patient {
    private final int pid;
    private final String name;
    private final LocalDate dob;
    private final String gender;
    private final String medicalCondition;
    private final String insurance;

    public Patient(int pid, String name, LocalDate dob, String gender,
                   String medicalCondition, String insurance) {
        this.pid = pid;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.insurance = insurance;
    }

    public int getPid()                  { return pid; }
    public String getName()              { return name; }
    public LocalDate getDob()            { return dob; }
    public String getGender()            { return gender; }
    public String getMedicalCondition()  { return medicalCondition; }
    public String getInsurance()         { return insurance; }

    @Override
    public String toString() {
        return String.format("[%d] %s | DOB: %s | Gender: %s | Condition: %s | Insurance: %s",
                pid, name, dob, gender, medicalCondition, insurance);
    }
}

package emr.model;

import java.time.LocalDate;

public class PatientHistory {
    private final int pid;
    private final String condition;
    private final String symptomDuration;
    private final Integer severity;
    private final String primaryTreatment;
    private final Integer procedureId;
    private final LocalDate lastVisitDate;
    private final boolean followupRequired;

    public PatientHistory(int pid, String condition, String symptomDuration,
                          Integer severity, String primaryTreatment, Integer procedureId,
                          LocalDate lastVisitDate, boolean followupRequired) {
        this.pid = pid;
        this.condition = condition;
        this.symptomDuration = symptomDuration;
        this.severity = severity;
        this.primaryTreatment = primaryTreatment;
        this.procedureId = procedureId;
        this.lastVisitDate = lastVisitDate;
        this.followupRequired = followupRequired;
    }

    public int getPid()                  { return pid; }
    public String getCondition()         { return condition; }
    public String getSymptomDuration()   { return symptomDuration; }
    public Integer getSeverity()         { return severity; }
    public String getPrimaryTreatment()  { return primaryTreatment; }
    public Integer getProcedureId()        { return procedureId; }
    public LocalDate getLastVisitDate()  { return lastVisitDate; }
    public boolean isFollowupRequired()  { return followupRequired; }

    @Override
    public String toString() {
        return String.format(
            "[%d] Condition: %s | Severity: %s | Duration: %s | " +
            "Treatment: %s | Procedure: %s | Last Visit: %s | Followup: %s",
            pid, condition, severity, symptomDuration,
            primaryTreatment, procedureId, lastVisitDate, followupRequired);
    }
}

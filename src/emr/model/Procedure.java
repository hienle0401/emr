package emr.model;

public class Procedure {
    private final int id;
    private final String procedureName;
    private final String category;
    private final String primaryIndication;
    private final String typicalToolDevice;
    private final String recoveryTime;

    public Procedure(int id, String procedureName, String category, String primaryIndication,
                     String typicalToolDevice, String recoveryTime) {
        this.id = id;
        this.procedureName = procedureName;
        this.category = category;
        this.primaryIndication = primaryIndication;
        this.typicalToolDevice = typicalToolDevice;
        this.recoveryTime = recoveryTime;
    }

    public int getId()                     { return id; }
    public String getProcedureName()      { return procedureName; }
    public String getCategory()           { return category; }
    public String getPrimaryIndication()  { return primaryIndication; }
    public String getTypicalToolDevice()  { return typicalToolDevice; }
    public String getRecoveryTime()       { return recoveryTime; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Category: %s | Indication: %s | Tool: %s | Recovery: %s",
                id, procedureName, category, primaryIndication, typicalToolDevice, recoveryTime);
    }
}

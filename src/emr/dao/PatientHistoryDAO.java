package emr.dao;

import emr.db.DBConnection;
import emr.model.PatientHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientHistoryDAO {

    public void create(PatientHistory h) throws SQLException {
        String sql = "INSERT INTO patienthistory " +
                "(`condition`, symptom_duration, severity, primary_treatment, " +
                "procedure_id, last_visit_date, followup_required) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getCondition());
            ps.setString(2, h.getSymptomDuration());
            setNullableInt(ps, 3, h.getSeverity());
            ps.setString(4, h.getPrimaryTreatment());
            setNullableInt(ps, 5, h.getProcedureId());
            ps.setDate(6, h.getLastVisitDate() != null ? Date.valueOf(h.getLastVisitDate()) : null);
            ps.setBoolean(7, h.isFollowupRequired());
            ps.executeUpdate();
        }
    }

    public List<PatientHistory> readAll() throws SQLException {
        List<PatientHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM patienthistory";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void update(PatientHistory h) throws SQLException {
        String sql = "UPDATE patienthistory SET `condition`=?, symptom_duration=?, " +
                "severity=?, primary_treatment=?, procedure_id=?, last_visit_date=?, followup_required=? WHERE pid=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getCondition());
            ps.setString(2, h.getSymptomDuration());
            setNullableInt(ps, 3, h.getSeverity());
            ps.setString(4, h.getPrimaryTreatment());
            setNullableInt(ps, 5, h.getProcedureId());
            ps.setDate(6, h.getLastVisitDate() != null ? Date.valueOf(h.getLastVisitDate()) : null);
            ps.setBoolean(7, h.isFollowupRequired());
            ps.setInt(8, h.getPid());
            ps.executeUpdate();
        }
    }

    public void delete(int pid) throws SQLException {
        String sql = "DELETE FROM patienthistory WHERE pid=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ps.executeUpdate();
        }
    }

    private PatientHistory map(ResultSet rs) throws SQLException {
        int sev = rs.getInt("severity");
        Integer severity = rs.wasNull() ? null : sev;
        int proc = rs.getInt("procedure_id");
        Integer procedureId = rs.wasNull() ? null : proc;
        Date lastVisit = rs.getDate("last_visit_date");
        return new PatientHistory(
                rs.getInt("pid"),
                rs.getString("condition"),
                rs.getString("symptom_duration"),
                severity,
                rs.getString("primary_treatment"),
                procedureId,
                lastVisit != null ? lastVisit.toLocalDate() : null,
                rs.getBoolean("followup_required"));
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value != null) ps.setInt(index, value);
        else ps.setNull(index, Types.INTEGER);
    }
}

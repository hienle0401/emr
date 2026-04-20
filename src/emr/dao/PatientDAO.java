package emr.dao;

import emr.db.DBConnection;
import emr.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public void create(Patient p) throws SQLException {
        String sql = "INSERT INTO patients (name, dob, gender, medical_condition, insurance) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDate(2, Date.valueOf(p.getDob()));
            ps.setString(3, p.getGender());
            ps.setString(4, p.getMedicalCondition());
            ps.setString(5, p.getInsurance());
            ps.executeUpdate();
        }
    }

    public List<Patient> readAll() throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void update(Patient p) throws SQLException {
        String sql = "UPDATE patients SET name=?, dob=?, gender=?, medical_condition=?, insurance=? WHERE pid=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDate(2, Date.valueOf(p.getDob()));
            ps.setString(3, p.getGender());
            ps.setString(4, p.getMedicalCondition());
            ps.setString(5, p.getInsurance());
            ps.setInt(6, p.getPid());
            ps.executeUpdate();
        }
    }

    public void delete(int pid) throws SQLException {
        String sql = "DELETE FROM patients WHERE pid=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ps.executeUpdate();
        }
    }

    private Patient map(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getInt("pid"),
                rs.getString("name"),
                rs.getDate("dob").toLocalDate(),
                rs.getString("gender"),
                rs.getString("medical_condition"),
                rs.getString("insurance"));
    }
}

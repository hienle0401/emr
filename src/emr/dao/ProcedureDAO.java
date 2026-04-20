package emr.dao;

import emr.db.DBConnection;
import emr.model.Procedure;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedureDAO {

    public void create(Procedure p) throws SQLException {
        String sql = "INSERT INTO procedures (procedure_name, category, primary_indication, typical_tool_device, recovery_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getProcedureName());
            ps.setString(2, p.getCategory());
            ps.setString(3, p.getPrimaryIndication());
            ps.setString(4, p.getTypicalToolDevice());
            ps.setString(5, p.getRecoveryTime());
            ps.executeUpdate();
        }
    }

    public List<Procedure> readAll() throws SQLException {
        List<Procedure> list = new ArrayList<>();
        String sql = "SELECT * FROM procedures";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void update(Procedure p) throws SQLException {
        String sql = "UPDATE procedures SET procedure_name=?, category=?, primary_indication=?, typical_tool_device=?, recovery_time=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getProcedureName());
            ps.setString(2, p.getCategory());
            ps.setString(3, p.getPrimaryIndication());
            ps.setString(4, p.getTypicalToolDevice());
            ps.setString(5, p.getRecoveryTime());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM procedures WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Procedure map(ResultSet rs) throws SQLException {
        return new Procedure(
                rs.getInt("id"),
                rs.getString("procedure_name"),
                rs.getString("category"),
                rs.getString("primary_indication"),
                rs.getString("typical_tool_device"),
                rs.getString("recovery_time"));
    }
}

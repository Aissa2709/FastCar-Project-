package com.fastcar.dao;

import com.fastcar.model.Agent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgentDAO {

    public List<Agent> getAllAgents() {
        List<Agent> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM AGENT WHERE EST_ACTIF = TRUE ORDER BY NOM_AGENT")) {

            while (rs.next()) {
                list.add(new Agent(
                        rs.getString("NUMERO_AGENT"),
                        rs.getString("NOM_AGENT"),
                        rs.getString("PRENOM_AGENT")));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (AgentDAO): " + e.getMessage());
        }
        return list;
    }

    public Agent getAgentByNumero(String numero) {
        String sql = "SELECT * FROM AGENT WHERE NUMERO_AGENT = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numero);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Agent(
                        rs.getString("NUMERO_AGENT"),
                        rs.getString("NOM_AGENT"),
                        rs.getString("PRENOM_AGENT"));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (AgentDAO - getAgentByNumero): " + e.getMessage());
        }
        return null;
    }

    public boolean addAgent(Agent agent) {
        String sql = "INSERT INTO AGENT (NUMERO_AGENT, NOM_AGENT, PRENOM_AGENT) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, agent.getNumAgent());
            ps.setString(2, agent.getNom());
            ps.setString(3, agent.getPrenom());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (AgentDAO - addAgent): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAgent(String numero) {
        String sql = "UPDATE AGENT SET EST_ACTIF = FALSE WHERE NUMERO_AGENT = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numero);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (AgentDAO - deleteAgent): " + e.getMessage());
            return false;
        }
    }
}

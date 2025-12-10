package com.fastcar.dao;

import com.fastcar.model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public List<Client> getAllClients() {
        List<Client> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENT ORDER BY NOM_CLIENT")) {

            while (rs.next()) {
                list.add(new Client(
                        rs.getString("CIN"),
                        rs.getString("NOM_CLIENT"),
                        rs.getString("PRENOM_CLIENT"),
                        rs.getString("ADRESSE_CLIENT"),
                        rs.getString("TELEPHONE_CLIENT"),
                        rs.getString("EMAIL_CLIENT")));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (ClientDAO): " + e.getMessage());
        }
        return list;
    }

    public Client getClientByCin(String cin) {
        String sql = "SELECT * FROM CLIENT WHERE CIN = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cin);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Client(
                        rs.getString("CIN"),
                        rs.getString("NOM_CLIENT"),
                        rs.getString("PRENOM_CLIENT"),
                        rs.getString("ADRESSE_CLIENT"),
                        rs.getString("TELEPHONE_CLIENT"),
                        rs.getString("EMAIL_CLIENT"));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (ClientDAO - getClientByCin): " + e.getMessage());
        }
        return null;
    }

    public boolean addClient(Client client) {
        String sql = "INSERT INTO CLIENT (CIN, NOM_CLIENT, PRENOM_CLIENT, ADRESSE_CLIENT, TELEPHONE_CLIENT, EMAIL_CLIENT) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getCin());
            ps.setString(2, client.getNom());
            ps.setString(3, client.getPrenom());
            ps.setString(4, client.getAdresse());
            ps.setString(5, client.getTelephone());
            ps.setString(6, client.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (ClientDAO - addClient): " + e.getMessage());
            return false;
        }
    }

    public boolean updateClient(Client client) {
        String sql = "UPDATE CLIENT SET NOM_CLIENT = ?, PRENOM_CLIENT = ?, ADRESSE_CLIENT = ?, TELEPHONE_CLIENT = ?, EMAIL_CLIENT = ? WHERE CIN = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getAdresse());
            ps.setString(4, client.getTelephone());
            ps.setString(5, client.getEmail());
            ps.setString(6, client.getCin());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (ClientDAO - updateClient): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteClient(String cin) {
        String sql = "DELETE FROM CLIENT WHERE CIN = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cin);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (ClientDAO - deleteClient): " + e.getMessage());
            return false;
        }
    }
}

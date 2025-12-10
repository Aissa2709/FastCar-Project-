package com.fastcar.dao;

import com.fastcar.model.Voiture;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoitureDAO {

    public List<Voiture> getAllVoitures() {
        List<Voiture> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM VOITURE ORDER BY MARQUE, MODELE")) {

            while (rs.next()) {
                list.add(new Voiture(
                        rs.getString("MATRICULE"),
                        rs.getString("MARQUE"),
                        rs.getString("MODELE"),
                        rs.getDouble("PRIX_JOURNALIER"),
                        rs.getString("ETAT")));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (VoitureDAO): " + e.getMessage());
        }
        return list;
    }

    public List<Voiture> getVoituresDisponibles() {
        List<Voiture> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM VOITURE WHERE ETAT = 'Disponible' ORDER BY MARQUE, MODELE")) {

            while (rs.next()) {
                list.add(new Voiture(
                        rs.getString("MATRICULE"),
                        rs.getString("MARQUE"),
                        rs.getString("MODELE"),
                        rs.getDouble("PRIX_JOURNALIER"),
                        rs.getString("ETAT")));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (VoitureDAO - getVoituresDisponibles): " + e.getMessage());
        }
        return list;
    }

    public Voiture getVoitureByMatricule(String matricule) {
        String sql = "SELECT * FROM VOITURE WHERE MATRICULE = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricule);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Voiture(
                        rs.getString("MATRICULE"),
                        rs.getString("MARQUE"),
                        rs.getString("MODELE"),
                        rs.getDouble("PRIX_JOURNALIER"),
                        rs.getString("ETAT"));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (VoitureDAO - getVoitureByMatricule): " + e.getMessage());
        }
        return null;
    }

    public boolean updateVoitureEtat(String matricule, String nouvelEtat) {
        String sql = "UPDATE VOITURE SET ETAT = ? WHERE MATRICULE = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nouvelEtat);
            ps.setString(2, matricule);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (VoitureDAO - updateVoitureEtat): " + e.getMessage());
            return false;
        }
    }

    public boolean addVoiture(Voiture voiture) {
        String sql = "INSERT INTO VOITURE (MATRICULE, MARQUE, MODELE, PRIX_JOURNALIER, ETAT, KILOMETRAGE) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, voiture.getMatricule());
            ps.setString(2, voiture.getMarque());
            ps.setString(3, voiture.getModele());
            ps.setDouble(4, voiture.getPrixJournalier());
            ps.setString(5, voiture.getEtat());
            ps.setInt(6, 0);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (VoitureDAO - addVoiture): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteVoiture(String matricule) {
        String sql = "DELETE FROM VOITURE WHERE MATRICULE = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricule);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (VoitureDAO - deleteVoiture): " + e.getMessage());
            return false;
        }
    }
}

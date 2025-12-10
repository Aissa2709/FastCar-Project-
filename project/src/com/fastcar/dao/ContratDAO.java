package com.fastcar.dao;

import com.fastcar.model.Contrat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratDAO {

    public List<Contrat> getAllContrats() {
        List<Contrat> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM CONTRAT ORDER BY DATE_DEBUT DESC")) {

            while (rs.next()) {
                list.add(new Contrat(
                        rs.getString("NUMERO_CONTRAT"),
                        rs.getDate("DATE_DEBUT"),
                        rs.getDate("DATE_FIN"),
                        rs.getDouble("MONTANT_TOTAL"),
                        rs.getString("MODE_PAIEMENT"),
                        rs.getInt("KILOMETRAGE_DEBUT"),
                        rs.getString("CIN_CLIENT"),
                        rs.getString("MATRICULE_VOITURE"),
                        rs.getString("NUMERO_AGENT")));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (ContratDAO): " + e.getMessage());
        }
        return list;
    }

    public List<Contrat> getContratsActifs() {
        List<Contrat> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM CONTRAT WHERE STATUT = 'Actif' ORDER BY DATE_DEBUT DESC")) {

            while (rs.next()) {
                list.add(new Contrat(
                        rs.getString("NUMERO_CONTRAT"),
                        rs.getDate("DATE_DEBUT"),
                        rs.getDate("DATE_FIN"),
                        rs.getDouble("MONTANT_TOTAL"),
                        rs.getString("MODE_PAIEMENT"),
                        rs.getInt("KILOMETRAGE_DEBUT"),
                        rs.getString("CIN_CLIENT"),
                        rs.getString("MATRICULE_VOITURE"),
                        rs.getString("NUMERO_AGENT")));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (ContratDAO - getContratsActifs): " + e.getMessage());
        }
        return list;
    }

    public Contrat getContratByNumero(String numero) {
        String sql = "SELECT * FROM CONTRAT WHERE NUMERO_CONTRAT = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numero);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Contrat(
                        rs.getString("NUMERO_CONTRAT"),
                        rs.getDate("DATE_DEBUT"),
                        rs.getDate("DATE_FIN"),
                        rs.getDouble("MONTANT_TOTAL"),
                        rs.getString("MODE_PAIEMENT"),
                        rs.getInt("KILOMETRAGE_DEBUT"),
                        rs.getString("CIN_CLIENT"),
                        rs.getString("MATRICULE_VOITURE"),
                        rs.getString("NUMERO_AGENT"));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (ContratDAO - getContratByNumero): " + e.getMessage());
        }
        return null;
    }

    public boolean addContrat(Contrat c) {
        String sql = "INSERT INTO CONTRAT (NUMERO_CONTRAT, DATE_DEBUT, DATE_FIN, MONTANT_TOTAL, MODE_PAIEMENT, KILOMETRAGE_DEBUT, CIN_CLIENT, MATRICULE_VOITURE, NUMERO_AGENT, STATUT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Actif')";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getId());
            ps.setDate(2, new java.sql.Date(c.getDateDebut().getTime()));
            ps.setDate(3, new java.sql.Date(c.getDateFin().getTime()));
            ps.setDouble(4, c.getMontant());
            ps.setString(5, c.getModePaiement());
            ps.setInt(6, c.getKilometrageDebut());
            ps.setString(7, c.getCinClient());
            ps.setString(8, c.getMatVoiture());
            ps.setString(9, c.getNumAgent());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (ContratDAO - addContrat): " + e.getMessage());
            return false;
        }
    }

    public boolean updateContratStatut(String numero, String newStatut) {
        String sql = "UPDATE CONTRAT SET STATUT = ? WHERE NUMERO_CONTRAT = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatut);
            ps.setString(2, numero);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (ContratDAO - updateContratStatut): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteContrat(String numero) {
        String sql = "DELETE FROM CONTRAT WHERE NUMERO_CONTRAT = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numero);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error (ContratDAO - deleteContrat): " + e.getMessage());
            return false;
        }
    }
}


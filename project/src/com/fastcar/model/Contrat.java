package com.fastcar.model;

import java.util.Date;

public class Contrat {
    private String id;
    private Date dateDebut;
    private Date dateFin;
    private double montant;
    private String modePaiement;
    private int kilometrageDebut;
    // Foreign Keys (Stored as objects or just IDs, let's store IDs for simplicity)
    private String cinClient;
    private String matVoiture;
    private String numAgent;
    private String statut;

    public Contrat(String id, Date dateDebut, Date dateFin, double montant, String modePaiement, int km, String client,
            String voiture, String agent) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.kilometrageDebut = km;
        this.cinClient = client;
        this.matVoiture = voiture;
        this.numAgent = agent;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public double getMontant() {
        return montant;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public int getKilometrageDebut() {
        return kilometrageDebut;
    }

    public String getCinClient() {
        return cinClient;
    }

    public String getMatVoiture() {
        return matVoiture;
    }

    public String getNumAgent() {
        return numAgent;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}

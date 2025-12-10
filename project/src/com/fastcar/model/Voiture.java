package com.fastcar.model;

public class Voiture {
    private String matricule;
    private String marque;
    private String modele;
    private double prixJournalier;
    private String etat;

    public Voiture(String matricule, String marque, String modele, double prixJournalier, String etat) {
        this.matricule = matricule;
        this.marque = marque;
        this.modele = modele;
        this.prixJournalier = prixJournalier;
        this.etat = etat;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    public double getPrixJournalier() {
        return prixJournalier;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return matricule + " - " + marque + " " + modele;
    }
}

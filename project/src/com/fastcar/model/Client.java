package com.fastcar.model;

public class Client {
    // Columns: CIN, NOM_CLIENT, PRENOM_CLIENT, ADRESSE_CLIENT, TELEPHONE_CLIENT,
    // EMAIL_CLIENT
    private String cin;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;

    public Client(String cin, String nom, String prenom, String adresse, String telephone, String email) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
    }

    // Getters
    public String getCin() {
        return cin;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    // Useful for ComboBoxes: "AB123456 - NOM Prénom"
    @Override
    public String toString() {
        return cin + " - " + nom + " " + prenom;
    }
}

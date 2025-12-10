package com.fastcar.model;

public class Agent {
    // Columns: NUMERO_AGENT, NOM_AGENT, PRENOM_AGENT
    private String numAgent;
    private String nom;
    private String prenom;

    public Agent(String numAgent, String nom, String prenom) {
        this.numAgent = numAgent;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNumAgent() {
        return numAgent;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return numAgent + " (" + nom + ")";
    }
}

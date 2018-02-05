package com.example.juliensautereau.engarde;

import java.util.ArrayList;


public class Joueur {
    private Boolean statut;
    private Boolean sens;
    private int score;
    private int position;
    private ArrayList<Integer> cartes;

    public Joueur(Boolean statut, Boolean sens, int score, int position, ArrayList<Integer> cartes) {
        this.statut = statut;
        this.sens = sens;
        this.score = score;
        this.position = position;
        this.cartes = cartes;
    }

    public Boolean getSens() {
        return sens;
    }

    public void setSens(Boolean sens) {
        this.sens = sens;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<Integer> getCartes() {
        return cartes;
    }

    public void setCartes(ArrayList<Integer> cartes) {
        this.cartes = cartes;
    }

    public void removeCartes(Integer carte) {
        this.cartes.remove(this.cartes.indexOf(carte));
    }
}



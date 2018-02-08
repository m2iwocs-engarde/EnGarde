package com.example.juliensautereau.engarde;

import java.util.ArrayList;


public class Joueur {
    private Boolean statut;
    private Boolean sens;
    private int score;
    private int position;
    private ArrayList<Integer> cartes;

    public Joueur(Boolean statut, Boolean sens) {
        this.statut = statut;
        this.sens = sens;
        this.score = 0;
        if(sens)
            this.position = 1;
        else
            this.position = 23;
        this.cartes = new ArrayList<Integer>();
    }

    public Joueur(Boolean statut, Boolean sens, ArrayList<Integer> cartes) {
        this.statut = statut;
        this.sens = sens;
        this.score = 0;
        if(sens)
            this.position = 1;
        else
            this.position = 23;
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

    public void addCartes(Integer carte) {
        this.cartes.add(carte);
    }

    public void invStatut() { this.statut = !this.statut; }

    @Override
    public String toString(){
        String ret = this.statut + ";" + this.sens;

        for(int c : this.cartes){
            ret += ";" + c;
        }
        return ret;
    }
}
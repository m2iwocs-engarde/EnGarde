package com.example.juliensautereau.engarde;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;


public class Jeu extends AppCompatActivity{
    ArrayList<Integer> pioche = new ArrayList<Integer>();
    Integer defausseLast = 0;
    Joueur j1;
    Joueur j2;
    Joueur attaquant;
    Joueur defenseur;

    public void initialisation(){
        initPioche();
        creerJoueur();
    }

    public void initPioche(){
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        for(int i = 1 ; i <= 5 ; i++){
            tmp.add(i);tmp.add(i);tmp.add(i);tmp.add(i);tmp.add(i);
        }

        int rnd;

        while(!tmp.isEmpty()){
            rnd = (int) (Math.random()*tmp.size());
            pioche.add(tmp.get(rnd));
            tmp.remove(rnd);
        }
    }

    public void creerJoueur(){
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        for(int i = 0 ; i < 5 ; i++){
            tmp.add(pioche.get(0));
            pioche.remove(0);
        }
        j1 = new Joueur(true,true,0,1,tmp);
        tmp.clear();

        for(int i = 0 ; i < 5 ; i++){
            tmp.add(pioche.get(0));
            pioche.remove(0);
        }
        j2 = new Joueur(false,false, 0,23,tmp);
    }

    public Boolean avancer(Integer carte){
        if(attaquant.getSens()){
            if(attaquant.getPosition() + carte >= defenseur.getPosition()){
                //on depasse l'adversaire ou meme case
                return false; //interdiction d'avancer et de jouer la carte, il faut en choisir une autre
            }
            attaquant.setPosition(attaquant.getPosition() + carte);//coup valide

        }else{
            if(attaquant.getPosition() - carte >= defenseur.getPosition()){
                //on depasse l'adversaire ou meme case
                return false; //interdiction d'avancer et de jouer la carte, il faut en choisir une autre
            }
            attaquant.setPosition(attaquant.getPosition() - carte);//coup valide
        }

        attaquant.removeCartes(carte);
        defausseLast = carte;

        //fin du tour + envoi position

        return true;

    }




}


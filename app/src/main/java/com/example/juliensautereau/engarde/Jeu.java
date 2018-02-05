package com.example.juliensautereau.engarde;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;


public class Jeu extends AppCompatActivity{
    ArrayList<Integer> pioche = new ArrayList<Integer>();
    Integer defausseLast = 0;
    Joueur j1;
    Joueur j2;
    Joueur actif;
    Joueur inactif;

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
        j1 = new Joueur(true,true,0,1);
        pioche(j1);

        j2 = new Joueur(false,false, 0,23);
        pioche(j2);
    }

    public Boolean avancer(Integer carte){
        if(actif.getSens()){
            if(actif.getPosition() + carte >= inactif.getPosition()){
                //on depasse l'adversaire ou meme case
                return false; //interdiction d'avancer et de jouer la carte, il faut en choisir une autre
            }
            actif.setPosition(actif.getPosition() + carte);//coup valide

        }else{
            if(actif.getPosition() - carte >= inactif.getPosition()){
                //on depasse l'adversaire ou meme case
                return false; //interdiction d'avancer et de jouer la carte, il faut en choisir une autre
            }
            actif.setPosition(actif.getPosition() - carte);//coup valide
        }

        actif.removeCartes(carte);
        defausseLast = carte;

        //fin du tour + envoi position + pioche
        //possibilite de faire attaque indirect (attque direct)

        return true;

    }

    public Boolean reculer(Integer carte){
        if(actif.getSens()){
            if(actif.getPosition() - carte < 1){
                //on sort du plateau
                return false; //interdiction de reculer et de jouer la carte, il faut en choisir une autre
            }
            actif.setPosition(actif.getPosition() - carte);//coup valide

        }else{
            if(actif.getPosition() + carte > 23){
                //on sort du plateau
                return false; //interdiction de reculer et de jouer la carte, il faut en choisir une autre
            }
            actif.setPosition(actif.getPosition() + carte);//coup valide
        }

        actif.removeCartes(carte);
        defausseLast = carte;

        //fin du tour + envoi position + pioche

        return true;

    }

    public Boolean attaquer(ArrayList<Integer> cartes){
        for(int i = 1 ; i < cartes.size() ; i++){
            if(cartes.get(0) != cartes.get(i)){
                //cartes differentes
                return false;//interdiction d'attaquer et de jouer les cartes, il faut en choisir d'autres
            }
        }

        if(Math.abs(actif.getPosition() - inactif.getPosition())  !=  cartes.get(0)){
            //pas la bonne distance
            return false; //interdiction d'attaquer, il faut en choisir d'autres
        }

        //fin du tour + envoi une attaque + pioche

        return true;
    }



    public Boolean pioche(Joueur j){

        int nbCarte = (5 - j.getCartes().size());

        if(pioche.size() - nbCarte <= 0){
            return false; //plus de carte, fin du jeu
        }

        for(int i = 0; i < nbCarte ; i ++){
            j.addCartes(pioche.get(0));
            pioche.remove(0);
        }

        return true;
    }

    public Boolean parade(int valeur, int nombre){
        int nb = 0;
        for(int c : actif.getCartes()){
            if(c == valeur){
                nb++;
            }
        }

        if (nb >= nombre) {
            //parade a reussi
            for(int i = 0; i < nombre ; i ++){
                actif.removeCartes(valeur);
            }
            return true;
        }else{
            //defaite du joueur
            return false;
        }
    }




}


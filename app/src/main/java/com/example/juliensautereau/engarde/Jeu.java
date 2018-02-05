package com.example.juliensautereau.engarde;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;


public class Jeu extends AppCompatActivity{
    ArrayList<Integer> cartes = new ArrayList<Integer>();

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
            cartes.add(tmp.get(rnd));
            tmp.remove(rnd);
        }
    }

    public void creerJoueur(){
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        for(int i = 1 ; i <= 5 ; i++){
            tmp.add(i);tmp.add(i);tmp.add(i);tmp.add(i);tmp.add(i);
        }

        int rnd;

        while(!tmp.isEmpty()){
            rnd = (int) (Math.random()*tmp.size());
            cartes.add(tmp.get(rnd));
            tmp.remove(rnd);
        }
    }
}


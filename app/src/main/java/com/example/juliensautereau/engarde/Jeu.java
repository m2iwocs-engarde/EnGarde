package com.example.juliensautereau.engarde;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

//TODO : bouton fin du tour

/*public class Jeu extends AppCompatActivity{
    ArrayList<Integer> pioche = new ArrayList<Integer>();
    Integer defausseLast = 0;
    Integer taillePioche = 0;
    Joueur j1;
    Joueur j2;
    Joueur actif;
    Joueur inactif;
    ArrayList<String> messages = new ArrayList<String>();

    /*-----------------Time Line -------------------------------------------------------*/
    /*public void Jeu(){
        if(serveur){
            //j1
            initialisation();
            sendData(j2.toString());
            String brut = receiveData(); //TODO
            sendData(j1.toString());
            String brut2 = receiveData(); //TODO

            if(j1.getStatut()){
                tour();
            }

        }else{
            //j2
            String brut = receiveData();
            ArrayList<String> msg = parse(brut);
            if(msg.get(0).equals("J")){
                j2 = new Joueur(Boolean.parseBoolean(msg.get(1)),Boolean.parseBoolean(msg.get(2)));
                for(int i=3; i< msg.size();i++){
                    j2.addCartes(Integer.parseInt(msg.get(i)));
                }
            }else{
                //TODO ERROR
                Toast.makeText(this,"Echec de l'initialisation J2",Toast.LENGTH_SHORT).show();

            }

            sendData("O:1");

            //j1
            String brut2 = receiveData();
            ArrayList<String> msg2 = parse(brut2);
            if(msg.get(0).equals("J")){
                j1 = new Joueur(Boolean.parseBoolean(msg2.get(1)),Boolean.parseBoolean(msg2.get(2)));
            }else{
                //TODO ERROR
                Toast.makeText(this,"Echec de l'initialisation J1",Toast.LENGTH_SHORT).show();
            }

            sendData("O:2");


            while(game) {
                if (j2.getStatut()) {
                    sendData("T:2");
                    actif = j2;
                    inactif = j1;
                    tourActif(); //Tour de j2 + envoie des info Inactif
                }else{
                    String brut3 = receiveData(); //TODO Verif Syncro

                    actif = j1;
                    inactif = j2;

                    tourInactif(); //Mise a jour des actions de j1 TODO
                }
            }

        }
    }

    public ArrayList<String> parse(String message){
        String[] original = message.split(":");
        String[] contenu = original[1].split(";");

        ArrayList<String> messageArray = new ArrayList<String>();

        messageArray.add(original[0]);
        for(int i=0; i <contenu.length;i++){
            messageArray.add(contenu[i]);
        }

        return messageArray;
    }

    public void test(ArrayList<String> msg){

        switch(msg.get(0)){
            case "D" : // Mise a jour de la pile de défausse
                defausseLast = Integer.parseInt(msg.get(1));
                break;

            case "P" : // Joueur 2 demmande a piocher
                String ret = "PR:";
                for(Integer i : pioche(j2)){
                    ret += i + ";";
                }

                ret = ret.substring(0,ret.length()-1);
                sendData(ret);

                String brut = receiveData(); //TODO Verif Syncro 01
                sendData("PN:" + pioche.size());
                break;
            case "PN" : // Mise a jour de la taille de la pioche
                this.taillePioche = Integer.parseInt(msg.get(1));
                break;
            case "PR" : // J2 recoit ses cartes
                for(int i=1; i<msg.size();i++) {
                    j2.addCartes(Integer.parseInt(msg.get(i)));
                }
                sendData("OK"); //TODO Verif Syncro 01
                break;

            case "PA" : //Affiche le message :"Le coup a été parrer"
                //TODO
                break;

        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    /*-----------------------Début du tour--------------------------------------------------------------*/
   /* public void tourInactif(){

    }

    //Début du tour, verification que le joueur peut faire une action
    public void tourActif(){
        boolean parrade,reculer;

        parrade = peutParrer(cartes); //verification que le joueur peut parrer

        if(attaquerIndirect){ //si le joueur vient d'etre attaque indirectement
            //TODO Afficher bouton griser Reculer et Parrer
            reculer = peutReculer();//vérification que le joueur peut reculer


            //on ne degrise que les actions possibles
            if(reculer){
                //Dégriser bouton reculer
            }
            if(parrade){
                //Dégriser bouton reculer
            }

            if(!reculer && !parrade){//le joueur ne peut rien faire il a perdu
                //Perdu
            }
        }


        if(attaquer) {//si le joueur vient d'etre attaque directement
            if(parrade) {
                //Parade
            }else{ //le joueur ne peut pas parer il a perdu
                //Perdu
            }
        }

        /// FIN P1
    }

    //debut du tour d'attaque, verification que le joueur peut faire une action
    public void tourAttaque(){
        boolean attaquer,avancer,reculer;

        attaquer = peutAttaquer();//vérification que le joueur peut attaquer
        avancer = peutAvancer();//vérification que le joueur peut avancer
        reculer = peutReculer();//vérification que le joueur peut reculer

        //TODO affichage bts

        if(attaquer){
            //Dégriser bouton attaquer
        }
        if(avancer){
            //Dégriser bouton avancer
        }
        if(reculer){
            //Dégriser bouton reculer
        }


        if(!attaquer && !avancer && !reculer){//si aucune action possible, le joeuur a perdu
            //Perdu
        }

    }

    public void finTour(){
        String[] msg = null;
        for(String s : msg) { //TODO Syncro

        }
    }
    /*------------------------------------------------------------------------------------------*/

    /*---------------------Verification des actions --------------------------------------------*/

   /* public boolean peutParrer(ArrayList<Integer> cartes){
        int nb = 0;
        for(int c : actif.getCartes()){
            if(c == cartes.get(0)){
                nb++;
            }
        }

        //la parade est possible
        if (nb >= cartes.size()) {
            return true;
        }else{
            return false;
        }
    }

    public boolean peutReculer(){
        if(actif.getSens()){
            for(Integer c : actif.getCartes()){
                if(actif.getPosition() - c >= 1){

                    return true; //une carte est possible
                }
            }

        }else{
            for(Integer c : actif.getCartes()) {
                if (actif.getPosition() + c <= 23) {
                    return true;
                }
            }
        }
        return false; //aucune carte ne peut faire reculer le joueur
    }

    public Boolean peutAvancer(){
        if(actif.getSens()){
            for(Integer c : actif.getCartes()){
                if(actif.getPosition() + c < inactif.getPosition()){
                    return true; //une cart est possible
                }
            }

        }else{
            for(Integer c : actif.getCartes()) {
                if (actif.getPosition() - c > inactif.getPosition()) {
                    return true;
                }
            }
        }

        return false;

    }

    public Boolean peutAttaquer(){
        for(Integer c : actif.getCartes()) {
            if (Math.abs(actif.getPosition() - inactif.getPosition()) == c) {
                return true; //une carte est a la bonne distance
            }
        }
        return false; //aucune carte ne permet d'attaquer
    }

    /*------------------------------------------------------------------------------------------*/

    /* --------------------------------- Deb actionButton ------------------------------------- */

    /*public void actionButtonParrer(){
        if(parade(getCarteSelectioner())){
            String msg = "PA:";
            messages.add(msg);
            tourAttaque();
        }else {
            //Erreur msg: parrade doit être = a attaque
            Toast.makeText(this,"La parade a échoué",Toast.LENGTH_SHORT).show();
        }

    }

    public void actionButtonAttaquer(){
        ArrayList<Integer> carteSelectionée = getCarteSelectioner();

        if(attaquer(carteSelectionée)){
            String msg = "AD:";
            for(Integer i : carteSelectionée){
                msg += carteSelectionée + ";";
            }
            msg = msg.substring(0,msg.length()-1);
            messages.add(msg);
            finTour();
        }else{
            //Erreur msg: attaque imposible
            Toast.makeText(this,"L'attaque a échoué",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void actionButtonAttaquerIndirectement(){
        ArrayList<Integer> carteSelectionée = getCarteSelectioner();

        if(attaquer(carteSelectionée)){
            String msg = "AI:";
            for(Integer i : carteSelectionée){
                msg += carteSelectionée + ";";
            }
            msg = msg.substring(0,msg.length()-1);
            messages.add(msg);
            finTour();
        }else{
            //Erreur msg: attaque imposible
            Toast.makeText(this,"L'attaque indirect a échoué",Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public void actionButtonReculer(){
        int nb = -1;

        if(getCarteSelectioner().size()>1){
            //Erreur msg : Une seul carte doit être sélectionné
            Toast.makeText(this,"Reculer ne nécessite qu'une carte",Toast.LENGTH_SHORT).show();
            return ;
        }else {
            nb = getCarteSelectioner().get(0);
        }


        if(reculer(nb)) {
            String msg = "R:" + nb;
            messages.add(msg);

            finTour();
        }else{
            //Erreur msg : Déplacement imposible
            Toast.makeText(this,"Reculer est impossible",Toast.LENGTH_SHORT).show();
            return ;
        }
    }

    public void actionButtonAvancer(){
        int nb = -1;

        if(getCarteSelectioner().size()>1){
            //Erreur msg : Une seul carte doit être sélectionné
            Toast.makeText(this,"Avancer ne nécessite qu'une carte",Toast.LENGTH_SHORT).show();
            return ;
        }else {
            nb = getCarteSelectioner().get(0);
        }

        if(avancer(nb)) {

            String msg = "A:" + nb;
            messages.add(msg);

            finTour();
            //Fin du tour
        }else{
            //Erreur msg : Déplacement imposible
            Toast.makeText(this,"Avancer est impossible",Toast.LENGTH_SHORT).show();
            return ;
        }
    }

    /* -------------- Fin actionButton ---------------------------------------------------------- */

    /*---------------  Fonctions ---------------------------------------------------------------- */
    /*public ArrayList<Integer> getCarteSelectioner(){
        ArrayList<Integer> ret = new ArrayList<Integer>();

        //TODO Récupéré les toogle buttons (numeros de cartes)

        return ret;
    }



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
        j1 = new Joueur(true,true);
        pioche(j1);

        j2 = new Joueur(false,false);
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



    public ArrayList<Integer> pioche(Joueur j){

        ArrayList<Integer> ret = new ArrayList<>();

        int nbCarte = (5 - j.getCartes().size());

        if(pioche.size() - nbCarte < 0){
            ret.add(-1);
            return ret; //plus de carte, fin du jeu
        }

        if(pioche.size() - nbCarte == 0){
            ret.add(-1);
            return ret; //finir le tour cas special voir regle TODO
        }

        for(int i = 0; i < nbCarte ; i ++){
            int p = pioche.get(0);

            j.addCartes(p);
            pioche.remove(0);

            ret.add(p);
        }

        this.taillePioche = pioche.size();

        return ret;
    }

    public Boolean parade(ArrayList<Integer> cartes){//cartes jouees lors de l'attaque
        int nb = 0;
        for(int c : actif.getCartes()){
            if(c == cartes.get(0)){
                nb++;
            }
        }

        if (nb >= cartes.size()) {
            //parade a reussi
            for(int i = 0; i < cartes.size() ; i ++){
                actif.removeCartes(cartes.get(0));
            }
            return true;
        }else{
            //defaite du joueur
            return false;
        }
    }
*/
//} 
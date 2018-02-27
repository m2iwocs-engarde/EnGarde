package com.example.juliensautereau.engarde;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.juliensautereau.engarde.common.activities.SampleActivityBase;

import java.util.ArrayList;

//TODO : bouton fin du tour

public class Jeu extends SampleActivityBase {
    ArrayList<Integer> pioche = new ArrayList<Integer>();
    Integer defausseLast = 0;
    Integer taillePioche = 0;
    Joueur j1;
    Joueur j2;
    Joueur actif;
    Joueur inactif;
    ArrayList<String> messages = new ArrayList<String>();

    boolean game = true;
    boolean breakInactif = false;

    boolean attaquer = false;
    boolean attaquerIndirect = false;

    ArrayList<Integer> attCartes = new ArrayList<Integer>();

    boolean serveur;

    //

    ToggleButton c1,c2,c3,c4,c5;

    Button btn_avancer,btn_reculer,btn_attaqueD,btn_attaqueI,btn_parer,btn_retraite;

    Button btn_pioche, btn_defausse;

    /*-----------------Time Line -------------------------------------------------------*/
    public void startGame(){

        if(serveur){
            //j1
            initialisation();
            sendData("J2:" + j2.toString());
            String brut = receiveData();
            test(parse(brut));

            sendData("J1:" + j1.toString());
            brut = receiveData();
            test(parse(brut));

            if (j1.getStatut()) {

                actif = j1;
                inactif = j2;

                tourActif();
            }else{

                actif = j2;
                inactif = j1;

                tourInactif();
            }

        }else{
            //j2
            String brut = receiveData();
            test(parse(brut));

            sendData("OK:");

            brut = receiveData();
            test(parse(brut));

            sendData("OK:");

            if (j2.getStatut()) {

                actif = j2;
                inactif = j1;

                tourActif(); //Tour de j2 + envoie des info Inactif

            }else{

                actif = j1;
                inactif = j2;

                tourInactif(); //Mise a jour des actions de j1 TODO
            }

        }
    }

    public void newTurn(){
        j1.invStatut();
        j2.invStatut();

        if(serveur){
            if (j1.getStatut()) {

                actif = j1;
                inactif = j2;

                setEnableCard(true);

                tourActif();
            }else{
                sendData("T:2");

                actif = j2;
                inactif = j1;

                setEnableCard(false);

                tourInactif();
            }
        }else{
            if (j2.getStatut()) {

                setEnableCard(true);

                actif = j2;
                inactif = j1;

                tourActif();

            }else{
                sendData("T:1");

                actif = j1;
                inactif = j2;

                setEnableCard(false);

                tourInactif();
            }
        }
    }

    public ArrayList<String> parse(String message){
        String[] original = message.split(":");
        String[] contenu = null;
        if(original.length > 1){
            contenu = original[1].split(";");
        }

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
                    sendData("OK:"); //TODO Verif Syncro
                break;

            case "P" : // Joueur 2 demmande a piocher
                    String ret = "PR:";
                    for(Integer i : pioche(j2)){
                        ret += i + ";";
                    }

                    ret = ret.substring(0,ret.length()-1);
                    sendData(ret);

                    String brut = receiveData(); //TODO Verif Syncro

                break;

            case "PR" : // J2 recoit ses cartes
                    int nbCarte = 0;
                    for(int i=1; i<msg.size();i++) {
                        nbCarte++;
                        j2.addCartes(Integer.parseInt(msg.get(i)));
                    }
                    this.taillePioche = this.taillePioche - nbCarte;
                    sendData("OK:"); //TODO Verif Syncro
                break;

            case "PA" : //Affiche le message :"Le coup a été parrer"
                    //TODO
                    sendData("OK:"); //TODO Verif Syncro
                break;

            case "PN" : // Mise a jour de la taille de la pioche
                    this.taillePioche = Integer.parseInt(msg.get(1));
                break;

            case "OK" :
                break;

            case "J1" :
                    j1 = new Joueur(Boolean.parseBoolean(msg.get(1)),Boolean.parseBoolean(msg.get(2)));
                break;

            case "J2" :
                    ArrayList<Integer> cartes = new ArrayList<Integer>();

                    for(int i=2; i < msg.size(); i++){
                        cartes.add(Integer.parseInt(msg.get(i)));
                    }

                    j2 = new Joueur(Boolean.parseBoolean(msg.get(1)),Boolean.parseBoolean(msg.get(2)),cartes);
                break;

            case "T":
                    if(serveur && Integer.parseInt(msg.get(0)) == 1) this.breakInactif = true;
                    if(!serveur && Integer.parseInt(msg.get(0)) == 2) this.breakInactif = true;
                break;

            case "AD":
                    this.attaquer = true;
                    this.attCartes.clear();

                    for(int i=0; i<msg.size(); i++){
                        this.attCartes.add(Integer.parseInt(msg.get(i)));
                    }
                break;

            case "AI":
                    this.attaquerIndirect = true;
                    this.attCartes.clear();

                    for(int i=0; i<msg.size(); i++){
                        this.attCartes.add(Integer.parseInt(msg.get(i)));
                    }
                break;

            case "PERDU":
                    perdu();
                break;

            case "GAGNE":
                    gagne();
                break;
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    /*-----------------------Début du tour--------------------------------------------------------------*/
    public void tourInactif(){
        while(!breakInactif){
            String brut = receiveData(); //TODO Verif Syncro
            test(parse(brut));
        }
        this.breakInactif = false;
    }

    //Début du tour, verification que le joueur peut faire une action
    public void tourActif(){
        boolean parrade,reculer;

        if(attaquerIndirect){ //si le joueur vient d'etre attaque indirectement
            //TODO Afficher bouton griser Reculer et Parrer

            this.attaquerIndirect = false;

            reculer = peutReculer();//vérification que le joueur peut reculer

            parrade = peutParrer(attCartes); //verification que le joueur peut parrer

            //on ne degrise que les actions possibles
            if(reculer){
                //Dégriser bouton reculer
                btn_retraite.setEnabled(true);
            }
            if(parrade){
                //Dégriser bouton reculer
                btn_parer.setEnabled(true);
            }

            if(!reculer && !parrade){//le joueur ne peut rien faire il a perdu
                perduSend();
            }
        }


        if(attaquer) {//si le joueur vient d'etre attaque directement

            this.attaquer = false;

            parrade = peutParrer(attCartes); //verification que le joueur peut parrer

            if(parrade) {
                btn_parer.setEnabled(true);
            }else{ //le joueur ne peut pas parer il a perdu
                perduSend();
            }
        }

        if(!attaquer && !attaquerIndirect){
            tourAttaque();
        }

    }

    //debut du tour d'attaque, verification que le joueur peut faire une action
    public void tourAttaque(){
        boolean attaquer,avancer,reculer;

        attaquer = peutAttaquer();//vérification que le joueur peut attaquer
        avancer = peutAvancer();//vérification que le joueur peut avancer
        reculer = peutReculer();//vérification que le joueur peut reculer

        btn_parer.setEnabled(false);
        btn_retraite.setEnabled(false);

        //TODO affichage bts

        if(attaquer){
            //Dégriser bouton attaquer
            btn_attaqueD.setEnabled(true);
        }
        if(avancer){
            //Dégriser bouton avancer
            btn_avancer.setEnabled(true);
        }
        if(reculer){
            //Dégriser bouton reculer
            btn_reculer.setEnabled(true);
        }

        if(!attaquer && !avancer && !reculer){//si aucune action possible, le joeur a perdu
            perduSend();
        }
    }

    public void tourAttaqueIndirect(){
        boolean a = peutAttaquer();

        btn_attaqueD.setEnabled(false);
        btn_avancer.setEnabled(false);
        btn_reculer.setEnabled(false);

        if(a){
            //Dégriser bouton attaquer indirectement
            btn_attaqueI.setEnabled(true);
        }

        //TODO Dégriser Fin du tour
    }

    public void finTour(){

        btn_attaqueD.setEnabled(false);
        btn_avancer.setEnabled(false);
        btn_reculer.setEnabled(false);
        btn_attaqueI.setEnabled(false);
        //TODO Griser Fin du tour

        String[] msg = null;

        for(String s : msg) {
            sendData(s);
            String brut = receiveData();
            test(parse(brut));
        }

        if(serveur){
            pioche(j1);
            sendData("PN:" + this.pioche.size());
        }else{
            sendData("P:");
            String brut = receiveData();
            test(parse(brut));
        }

        newTurn();
    }

    /*------------------------------------------------------------------------------------------*/

    /*---------------------Verification des actions --------------------------------------------*/

    public boolean peutParrer(ArrayList<Integer> cartes){
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

    public void actionButtonParer(View v){
        if(parade(getCarteSelectioner())){
            String msg = "PA:";
            messages.add(msg);
            tourAttaque();
        }else {
            //Erreur msg: parrade doit être = a attaque
            Toast.makeText(this,"La parade a échoué",Toast.LENGTH_SHORT).show();
        }

    }

    public void actionButtonAttaquer(View v){
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

    public void actionButtonAttaquerIndirectement(View v){
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


    public void actionButtonReculer(View v){
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

    public void actionButtonAvancer(View v){
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
    public void perduSend(){
        sendData("PERDU:");
        perdu();
    }

    public void perdu(){
        Toast.makeText(this,"Vous avez perdu",Toast.LENGTH_SHORT).show();
    }

    public void gagneSend(){
        sendData("GAGNE:");
        gagne();
    }

    public void gagne(){
        Toast.makeText(this,"Vous avez gagne",Toast.LENGTH_SHORT).show();
    }

    /*---------------  Fonctions ---------------------------------------------------------------- */
    public ArrayList<Integer> getCarteSelectioner(){
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

    public void setEnableCard(boolean b){
        c1.setEnabled(b);
        c2.setEnabled(b);
        c3.setEnabled(b);
        c4.setEnabled(b);
        c5.setEnabled(b);
    }

    /////////////////////////

    //TODO A supp tmp (ou modif)
    public void sendData(String s){
        fragment.sendMessage(s);
    }

    public String receiveData(){
        return fragment.receiveData();
    }

    private BluetoothChatFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(savedInstanceState == null) {
            System.out.println("--------------------------------------------------------------------RAERZERTZRZTERTERT ");
            //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //fragment = new BluetoothChatFragment();

            FragmentTransaction transaction = MainActivity.transaction;
            fragment = MainActivity.fragment;

            serveur = DeviceListActivity.serveur;
            System.out.println("serveur " + serveur);

        }

       //// fragment = new BluetoothChatFragment();//INTITALISATION FACTIF

        serveur = DeviceListActivity.serveur;
        System.out.println("serveur " + serveur);
                // Init

        //TODO a finir
        c1 = findViewById(R.id.carte1);
        c2 = findViewById(R.id.carte2);
        c3 = findViewById(R.id.carte3);
        c4 = findViewById(R.id.carte4);
        c5 = findViewById(R.id.carte5);

        btn_avancer = findViewById(R.id.avancer);
        btn_reculer = findViewById(R.id.reculer);
        btn_attaqueD = findViewById(R.id.attaqueD);
        btn_attaqueI = findViewById(R.id.attaqueI);
        btn_parer = findViewById(R.id.parer);
        btn_retraite = findViewById(R.id.retraite);

        //TODO a finir
        btn_defausse = findViewById(R.id.defausse);
        btn_pioche = findViewById(R.id.pioche);

        // Start

        startGame();
    }

    public void onPause(){
        super.onPause();
    }

    //Rotation de l'ecran
    protected void onSaveInstanceState (Bundle donnees) {
        super.onSaveInstanceState(donnees);

       /* donnees.putIntegerArrayList("resultat", reponse);
        donnees.putIntegerArrayList("jeu", grille);
        donnees.putLong("chrono", chronometer.getBase());*/
    }

    protected void onRestoreInstanceState (Bundle donnees) {
        super.onRestoreInstanceState(donnees);

       /* grille = donnees.getIntegerArrayList("jeu");
        reponse = donnees.getIntegerArrayList("resultat");
        temps = donnees.getLong("chrono");*/
    }

    protected void onResume(){
        super.onResume();

       /* reponse1.setText("" + reponse.get(0));
        reponse2.setText("" + reponse.get(1));
        reponse3.setText("" + reponse.get(2));
        reponse4.setText("" + reponse.get(3));
        reponse5.setText("" + reponse.get(4));
        reponse6.setText("" + reponse.get(5));

        //en cas de rotation on garde le chrono
        if(temps != 0){
            chronometer.setBase(temps);
        }*/
    }

}

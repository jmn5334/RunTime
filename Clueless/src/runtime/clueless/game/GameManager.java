package runtime.clueless.game;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import runtime.clueless.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by tesfaz1 on 4/20/17.
 */
public class GameManager {

    private static GameManager instance = null;

    public static GameManager getInstance() {
        if(instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    public static GameManager ptr() {
        return getInstance();
    }

    RoomCard winningRoomCard = null;
    WeaponCard winningWeaponCard = null;
    SuspectCard winningSuspectCard = null;

    Button[] playerChipsList;

    Player currentPlayer;
    ArrayList<Hallway> hallwayList = new ArrayList<>(13);
    ArrayList<Room> roomList = new ArrayList<>(10);

    public GameManager(){
        // first create the rooms and the hallway
        for(int n = 0; n<13; n++){
            hallwayList.add(new Hallway());
            if(n<10)
                roomList.add(new Room());

        }


        // set the room locaitons
        setRoomLocations();
        setPixelLocation();

        initCards();

    }

    ArrayList<RoomCard> roomCards = new ArrayList<>(9);
    ArrayList<SuspectCard> suspectCards = new ArrayList<>(6);
    ArrayList<WeaponCard> weaponCards = new ArrayList<>(6);

    public ArrayList<RoomCard> getRoomCards(){ return roomCards;}
    public ArrayList<SuspectCard> getSuspectCards(){ return suspectCards;}
    public ArrayList<WeaponCard> getWeaponCards(){ return weaponCards; }

    public boolean isGuessCorrect( RoomCard r, SuspectCard s, WeaponCard w){
        if( winningSuspectCard.name.matches(s.name) && winningWeaponCard.name.matches(w.name) && winningRoomCard.name.matches(r.name))
            return true;

        return false;
    }

    public void initCards(){

        long seed = System.nanoTime();
        ArrayList<Room> r = roomList;

        roomCards.add(new RoomCard("study")); r.get(1).setCard(roomCards.get(0));
        roomCards.add(new RoomCard("hall"));r.get(2).setCard(roomCards.get(1));
        roomCards.add(new RoomCard("lounge"));r.get(3).setCard(roomCards.get(2));
        roomCards.add(new RoomCard("library"));r.get(4).setCard(roomCards.get(3));
        roomCards.add(new RoomCard("billiard room"));r.get(5).setCard(roomCards.get(4));
        roomCards.add(new RoomCard("dining room"));r.get(6).setCard(roomCards.get(5));
        roomCards.add(new RoomCard("conservatory"));r.get(7).setCard(roomCards.get(6));
        roomCards.add(new RoomCard("ballroom"));r.get(8).setCard(roomCards.get(7));
        roomCards.add(new RoomCard("kitchen"));r.get(9).setCard(roomCards.get(8));
        Collections.shuffle(roomCards, new Random(seed));

        //Rope, Lead Pipe, Knife, Wrench, Candlestick, Revolver
        weaponCards.add(new WeaponCard("rope"));
        weaponCards.add(new WeaponCard("lead pipe"));
        weaponCards.add(new WeaponCard("knife"));
        weaponCards.add(new WeaponCard("wrench"));
        weaponCards.add(new WeaponCard("candlestick"));
        weaponCards.add(new WeaponCard("revolver"));
        Collections.shuffle(weaponCards, new Random(seed));

        //Colonel Mustard— yellow; Miss Scarlet— red; Professor Plum— purple; Mr. Green— green; Mrs. White— white; and Mrs. Peacock— blue
        suspectCards.add(new SuspectCard("Colenel Mustard"));
        suspectCards.add(new SuspectCard("Miss Scarlet"));
        suspectCards.add(new SuspectCard("Professor Plum"));
        suspectCards.add(new SuspectCard("Mr. Green"));
        suspectCards.add(new SuspectCard("Mrs. White"));
        suspectCards.add(new SuspectCard("Mrs. Peacock"));
        Collections.shuffle(suspectCards, new Random(seed));

        winningRoomCard = roomCards.get(0);
        winningWeaponCard = weaponCards.get(0);
        winningSuspectCard = suspectCards.get(0);
         seed = System.nanoTime();
        Collections.shuffle(roomCards, new Random(seed));
        Collections.shuffle(weaponCards, new Random(seed));
        Collections.shuffle(suspectCards, new Random(seed));

        System.out.println(" The winning combination room:"+winningRoomCard.name+" weapon:"+winningWeaponCard.name+"  suspect:"+winningSuspectCard.name);


    }

    ArrayList<Player> playersList = new ArrayList<>(7);

    private void initPlayers(){

        int[] startup_player_hall_locations={0,2,3,8,11,12,5};
        String[] startup_player_names = {"none","Miss. Scarlet","Prof. Plum","Mrs. Peacock", "Mr. Green","Mr. White","Col. Mustard"};

        ArrayList<Integer> playerid = new ArrayList<>(7);

        for(int n=1; n<7; n++)
            playerid.add(n);

        long seed = System.nanoTime();
        Collections.shuffle(playerid, new Random(seed));


        for(int n=0; n<7; n++)
            playersList.add(new Player());

        for(int n=0; n<6; n++) {
            int playerIndex = n+1;

            int randIndex = playerid.get(n);

            int h = startup_player_hall_locations[randIndex];
            Hallway hall = hallwayList.get(h);
            String pname = startup_player_names[randIndex];

            Player p = new Player();
            p.setLabel(pname);
            p.setPlace(hall);
            p.setPlayerChip(playerChipsList[playerIndex]);
            int x = p.getPlace().getX();
            int y = p.getPlace().getY();
            playerChipsList[playerIndex].setLayoutX(x);
            playerChipsList[playerIndex].setLayoutY(y);
            System.out.println(" Setting up Player "+(n+1)+" index="+randIndex+" hallway="+hall+" pixel x="+x+" y="+y);
            playersList.add(playerIndex,p);
        }

    }

    public void chooseCurrentPlayer(int m){
        currentPlayer = playersList.get(m);

        for(int n=1; n<7; n++) {

            if(m!=n)
            playerChipsList[n].setVisible(false);
            else
                playerChipsList[n].setVisible(true);
        }

    }
    public Player getCurrentPlayer(){ return currentPlayer; }

    public void setPlayerChipArray(Button[] b){
        playerChipsList = b;

        // initialize the players list
        initPlayers();
    }

    public void setRoomLocations(){

        ArrayList<Hallway> h = hallwayList;
        ArrayList<Room> r = roomList;

        // 1st quadrant setup
        r.get(1).setRight(h.get(1));
        h.get(1).setLeft(r.get(1));
        r.get(1).setBelow(h.get(3));
        h.get(3).setAbove(r.get(1));

        r.get(2).setLeft(h.get(1));
        h.get(1).setRight(r.get(2));
        r.get(2).setRight(h.get(2));
        h.get(2).setLeft(r.get(2));

        r.get(2).setBelow(h.get(4));
        h.get(4).setAbove(r.get(2));
        r.get(5).setAbove(h.get(4));
        h.get(4).setBelow(r.get(5));

        r.get(5).setLeft(h.get(6));
        h.get(6).setRight(r.get(5));
        r.get(4).setRight(h.get(6));
        h.get(6).setLeft(r.get(4));

        r.get(4).setAbove(h.get(3));
        h.get(3).setBelow(r.get(4));

        // end of 1st quadrant

        // 2nd quadrant
        r.get(2).setRight(h.get(2));
        h.get(2).setLeft(r.get(2));
        h.get(2).setRight(r.get(3));
        r.get(3).setBelow(h.get(5));
        r.get(3).setLeft(h.get(2));

        r.get(6).setLeft(h.get(7));
        h.get(7).setRight(r.get(6));
        r.get(6).setAbove(h.get(5));
        h.get(5).setBelow(r.get(6));
        h.get(5).setAbove(r.get(3));
        h.get(7).setLeft(r.get(5));

        r.get(2).setBelow(h.get(4));
        h.get(4).setAbove(r.get(2));
        r.get(5).setAbove(h.get(4));
        h.get(4).setBelow(r.get(5));
        r.get(5).setRight(h.get(7));

        // 3rd quadrant
        r.get(6).setBelow(h.get(10));
        h.get(10).setAbove(r.get(6));
        h.get(10).setBelow(r.get(9));
        r.get(9).setAbove(h.get(10));
        r.get(9).setLeft(h.get(12));
        h.get(12).setLeft(r.get(8));
        h.get(12).setRight(r.get(9));
        r.get(8).setLeft(h.get(12));
        r.get(9).setAbove(h.get(10));
        h.get(9).setBelow(r.get(8));
        h.get(9).setAbove(r.get(5));
        r.get(8).setAbove(h.get(9));

        // 4th quadrant
        r.get(8). setLeft(h.get(11));
        r.get(8).setRight(h.get(12));
        h.get(11).setRight(r.get(8));
        h.get(11).setLeft(r.get(7));
        r.get(7).setAbove(h.get(8));
        h.get(8).setBelow(r.get(7));
        h.get(8).setAbove(r.get(4));
        r.get(4).setBelow(h.get(8));
        r.get(7).setRight(h.get(11));
        r.get(5).setBelow(h.get(9));
        r.get(9).setDiagonal(r.get(1));
        r.get(1).setDiagonal(r.get(9));
        r.get(7).setDiagonal(r.get(3));
        r.get(3).setDiagonal(r.get(7));



    }

    public void setPixelLocation(){
        ArrayList<Hallway> h = hallwayList;
        ArrayList<Room> r = roomList;

        r.get(1).setPixel(116,96);
        r.get(2).setPixel(243,96);
        r.get(3).setPixel(372,96);
        r.get(4).setPixel(116,217);
        r.get(5).setPixel(243,217);
        r.get(6).setPixel(372,217);
        r.get(7).setPixel(116,335);
        r.get(9).setPixel(372,335);
        r.get(8).setPixel(243,335);

        h.get(1).setPixel(176,96);
        h.get(2).setPixel(308,96);
        h.get(3).setPixel(119,152);
        h.get(4).setPixel(243,155);
        h.get(5).setPixel(372,155);
        h.get(6).setPixel(179,217);
        h.get(7).setPixel(308,217);
        h.get(8).setPixel(116,276);
        h.get(9).setPixel(245,276);
        h.get(10).setPixel(372,276);
        h.get(11).setPixel(178,335);
        h.get(12).setPixel(306,335);

    }

    public boolean canMoveAbove(){ return
            (currentPlayer==null)? false : currentPlayer.getPlace().canMoveAbove(); }
    public boolean canMoveBelow(){ return (currentPlayer==null)? false : currentPlayer.getPlace().canMoveBelow(); }
    public boolean canMoveLeft(){ return (currentPlayer==null)? false : currentPlayer.getPlace().canMoveLeft(); }
    public boolean canMoveRight(){ return (currentPlayer==null)? false : currentPlayer.getPlace().canMoveRight(); }
    public boolean canMoveDiagonal() {return (currentPlayer==null)? false : currentPlayer.getPlace().canMoveDiagonal();}

    public void moveUp(){ if(currentPlayer!=null) currentPlayer.moveUp();}
    public void moveDown(){ if(currentPlayer!=null) currentPlayer.moveDown();}
    public void moveLeft(){ if(currentPlayer!=null) currentPlayer.moveLeft();}
    public void moveRight(){ if(currentPlayer!=null) currentPlayer.moveRight();}
    public void moveDiagonal(){if(currentPlayer!=null) currentPlayer.moveDiagonal();}

}

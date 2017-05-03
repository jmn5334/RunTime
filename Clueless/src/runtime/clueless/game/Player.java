package runtime.clueless.game;

import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * Created by tesfaz1 on 4/20/17.
 */
public class Player {

    Place currentLocation;

    Button playerChip;

    String label = "p";

    ArrayList<RoomCard> roomCards = new ArrayList<>();
    ArrayList<SuspectCard> suspectCards = new ArrayList<>();
    ArrayList<WeaponCard> weaponCards = new ArrayList<>();

    public Player(){


    }

    public void setLabel(String l){ label = l;}
    public String getLabel(){ return label;}

    public void setPlace(Place p ){ currentLocation = p; p.setPlayer(this);}
    public Place getPlace(){ return currentLocation; }

    public void setPlayerChip(Button b){ playerChip = b;}
    public Button getPlayerChip(){return playerChip;}
    private void move(){
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        playerChip.setLayoutX(x);
        playerChip.setLayoutY(y);

        System.out.println(" Player Action: " + label + " has moved to " + currentLocation.getLabel()+"  x="+currentLocation.getX()+" y="+currentLocation.getY());


    }

    public void moveLeft(){
        if(currentLocation.canMoveLeft()) {
            Place p = currentLocation.getLeft();
            currentLocation = p;
            p.setPlayer(this);
            move();
        }
    }

    public void moveRight(){
        if(currentLocation.canMoveRight()) {
            Place p = currentLocation.getRight();
            currentLocation = p;
            p.setPlayer(this);
            move();
        }
    }

    public void moveUp(){
        if(currentLocation.canMoveAbove()) {
            Place p = currentLocation.getAbove();
            currentLocation = p;
            p.setPlayer(this);
            move();
        }
    }

    public void moveDown(){
        if(currentLocation.canMoveBelow()) {
            Place p = currentLocation.getBelow();
            currentLocation = p;
            p.setPlayer(this);
            move();
        }
    }

    public void moveDiagonal() {
        if(currentLocation.canMoveDiagonal()) {
            Place p = currentLocation.getDiagonal();
            currentLocation = p;
            p.setPlayer(this);
            move();
        }
    }

}

package runtime.clueless.game;

import javafx.scene.control.Button;

/**
 * Created by tesfaz1 on 4/20/17.
 */
public class Player {

    Place currentLocation;

    Button playerChip;

    String label = "p";

    public Player(){


    }

    public void setLabel(String l){ label = l;}
    public String getLabel(){ return label;}

    public void setPlace(Place p ){ currentLocation = p;}
    public Place getPlace(){ return currentLocation; }

    public void setPlayerChip(Button b){ playerChip = b;}

    private void move(){
        int x = currentLocation.getX();
        int y = currentLocation.getY();
        playerChip.setLayoutX(x);
        playerChip.setLayoutY(y);

        System.out.println(" Player Action: " + label + " has moved to " + currentLocation.getLabel());


    }

    public void moveLeft(){
        if(currentLocation.canMoveLeft()) {
            Place p = currentLocation.getLeft();
            currentLocation = p;
            move();
        }
    }

    public void moveRight(){
        if(currentLocation.canMoveRight()) {
            Place p = currentLocation.getRight();
            currentLocation = p;
            move();
        }
    }

    public void moveUp(){
        if(currentLocation.canMoveAbove()) {
            Place p = currentLocation.getAbove();
            currentLocation = p;
            move();
        }
    }

    public void moveDown(){
        if(currentLocation.canMoveBelow()) {
            Place p = currentLocation.getBelow();
            currentLocation = p;
            move();
        }
    }

}

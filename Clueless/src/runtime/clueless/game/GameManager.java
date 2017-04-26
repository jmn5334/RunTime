package runtime.clueless.game;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import runtime.clueless.Controller;

import java.util.ArrayList;

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


    Player currentPlayer;
    ArrayList<Hallway> hallwayList = new ArrayList<>(13);
    ArrayList<Room> roomList = new ArrayList<>(10);

    public GameManager(){

        // this is a temporary instantiation for testing the gui
        currentPlayer = new Player();
        currentPlayer.setLabel("Prof. Plum");

        for(int n = 0; n<13; n++){
            hallwayList.add(new Hallway());
            if(n<10)
                roomList.add(new Room());

        }

        setRoomLocations();
        setPixelLocation();

        currentPlayer.setPlace(roomList.get(1));
    }

    public Player getPlayer(){ return currentPlayer; }

    public void setPlayerChip(Button b){ currentPlayer.setPlayerChip(b);}

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

    public boolean canMoveAbove(){ return currentPlayer.getPlace().canMoveAbove(); }
    public boolean canMoveBelow(){ return currentPlayer.getPlace().canMoveBelow(); }
    public boolean canMoveLeft(){ return currentPlayer.getPlace().canMoveLeft(); }
    public boolean canMoveRight(){ return currentPlayer.getPlace().canMoveRight(); }
    public boolean canMoveDiagonal() {return currentPlayer.getPlace().canMoveDiagonal();}

    public void moveUp(){ currentPlayer.moveUp();}
    public void moveDown(){ currentPlayer.moveDown();}
    public void moveLeft(){ currentPlayer.moveLeft();}
    public void moveRight(){ currentPlayer.moveRight();}
    public void moveDiagonal(){currentPlayer.moveDiagonal();}

}

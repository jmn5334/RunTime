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
    }


    public void setPixelLocation(){
        ArrayList<Hallway> h = hallwayList;
        ArrayList<Room> r = roomList;

        r.get(1).setPixel(116,96);
        r.get(2).setPixel(243,96);
        r.get(3).setPixel(372,96);
        r.get(4).setPixel(116,217);
        r.get(5).setPixel(243,217);

        h.get(1).setPixel(176,96);
        h.get(3).setPixel(119,152);
        h.get(4).setPixel(243,155);
        h.get(6).setPixel(179,217);
    }

    public boolean canMoveAbove(){ return currentPlayer.getPlace().canMoveAbove(); }
    public boolean canMoveBelow(){ return currentPlayer.getPlace().canMoveBelow(); }
    public boolean canMoveLeft(){ return currentPlayer.getPlace().canMoveLeft(); }
    public boolean canMoveRight(){ return currentPlayer.getPlace().canMoveRight(); }

    public void moveUp(){ currentPlayer.moveUp();}
    public void moveDown(){ currentPlayer.moveDown();}
    public void moveLeft(){ currentPlayer.moveLeft();}
    public void moveRight(){ currentPlayer.moveRight();}

}

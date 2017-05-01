/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.game;

import java.util.ArrayList;

/**
 *
 * @author jmnew
 */
public class JPlayer {
    
    private JBoard board;
    private JSuspect suspect;
    private final String name;
    
    public JPlayer(String name){
        board = new JBoard();
        suspect = null;
        this.name = name;
    }
    
    /**
     * @return the suspect
     */
    public JSuspect getSuspect() {
        return suspect;
    }

    /**
     * @param suspect the suspect to set
     */
    public void setSuspect(JSuspect suspect) {
        this.suspect = suspect;
    }

    public ArrayList<JHallway> getHallways() {
        return board.getHallways();
    }

    public ArrayList<JRoom> getRooms() {
        return board.getRooms();
    }
    
    public ArrayList<JSuspect> getSuspects() {
        return board.getSuspects();
    }

    public ArrayList<JWeapon> getWeapons() {
        return board.getWeapons();
    }
    
}

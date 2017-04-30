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
public class JHallway {
    
    private final int id;
    private JSuspect suspect;
    private ArrayList<JRoom> adjacentRooms;
    
    public JHallway(int id, JSuspect suspect){
        this.id = id;
        this.suspect = suspect;
    }
    
    public boolean setAdjacentRooms(ArrayList<JRoom> adjacentRooms){
        if(adjacentRooms.size() != 2){
            this.adjacentRooms = adjacentRooms;
            return true;
        }
        else{
            System.out.println("Incorrect number of hallways provided.");
            return false;
        }    
    }
    
    //returns true on successfuly add, false if occupied
    public boolean addSuspect(JSuspect s){
        if(suspect == null){
            suspect = s;
            return true;
        }
        else{
            System.out.println("addSuspect failed. Hallway is occupied!");
            return false;
        }
    }
    
    public void removeSuspect(){
        suspect = null;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the suspect
     */
    public JSuspect getSuspect() {
        return suspect;
    }

    /**
     * @return the adjacentRooms
     */
    public ArrayList<JRoom> getAdjacentRooms() {
        return adjacentRooms;
    }
}

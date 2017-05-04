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
 * 
 * This class creates the clue gameboard. All players and server need a copy
 * of this object to play.
 */
public class JBoard {
    
    //NOTE: Class constructor is at the bottom of the file as it is very large
    
    private final ArrayList<JHallway> hallways;
    private final ArrayList<JRoom> rooms;
    private final ArrayList<JSuspect> suspects;
    private final ArrayList<JWeapon> weapons;
    
    //return true if stuck, false if not
    public boolean isStuck(JSuspect s){
        
        boolean isStuck = false;
        
        //check if this player was moved on a suggestion, if so not stuck by biz rule definition
        if(s.isWasMovedOnSuggest())
            return false;
        
        //check if in hallway or room
        JHallway hall = s.getHallwayLocation();
        JRoom room = s.getRoomLocation();
        
        //if null we are in a room, can't be stuck in hallway
        if (hall == null) {

            //check for secret passage, can't be stuck if we have one
            if (room.getSecretPassage() == null) {

                ArrayList<JHallway> halls = room.getAdjacentHallways();

                //if any halls are empty set this to true
                isStuck = true;
                for (JHallway h : halls) {
                    if (h.getSuspect() == null) {
                        isStuck = false;
                    }
                }
            }
        }

        return isStuck;
    }
    
    //FIND METHODS
    public JRoom findRoom(String room){
        
        JRoom foundRoom = null;
        
        for(JRoom r: rooms){
            if(r.getName().equals(room)){
                foundRoom = r;
            }
        }
        
        return foundRoom;
    }
    
    public JHallway findHallway(int hall){
        
        JHallway foundHall = null;
        
        for(JHallway h: hallways){
            if(h.getId() == hall){
                foundHall = h;
            }
        }
        
        return foundHall;
    }
    
    public JSuspect findSuspect(String suspect){
        
        JSuspect foundSuspect = null;
        
        for(JSuspect s: suspects){
            if(s.getName().equals(suspect)){
                foundSuspect = s;
            }
        }
        
        return foundSuspect;
    }
    
    public JWeapon findWeapon(String weapon){
        
        JWeapon foundWeapon= null;
        
        for(JWeapon w: weapons){
            if(w.getName().equals(weapon)){
                foundWeapon = w;
            }
        }
        
        return foundWeapon;
    }
    
    //moves suspect to a new location
    //returns true for success, false if move is invalid
    public boolean moveSuspectToHallway(JSuspect s, JHallway dest){
        
        boolean isRoom;
        
        //check if the hallway is occupied
        if(dest.getSuspect() != null){
            System.out.println("Hallway is occupied.");
            return false;
        }  
        
        //is suspect in hallway or room?
        switch (s.inRoom()) {
            case 1:
                isRoom = true;
                break;
            case 0:
                isRoom = false;
                break;
            default:
                return false;
        }
        
        //if we have a room check adjacent rooms for this hallway
        if(isRoom){
            JRoom rLocation = s.getRoomLocation();
            
            //if adjacent then move suspect
            if(rLocation.isHallwayAdjacent(dest)){
                rLocation.removeSuspect(s);
                dest.addSuspect(s);
                s.setHallwayLocation(dest);
                s.setWasMovedOnSuggest(false);
            }
            else{
                return false;
            }   
        }
        else{
            System.out.println("Can't move from hallway to hallway.");
            return false; //impossible to move to a hallway from a hallway
        }
        
        return true;
    }
    
    public boolean moveSuspectToRoom(JSuspect s, JRoom dest){
        
        boolean isRoom;
        
        //is suspect in hallway or room?
        switch (s.inRoom()) {
            case 1:
                isRoom = true;
                break;
            case 0:
                isRoom = false;
                break;
            default:
                return false;
        }
        
        //if we have a room check for secret passage
        if(isRoom){
            JRoom rLocation = s.getRoomLocation();
            
            //check for secret passage and if it leads to our dest
            if(rLocation.getSecretPassage() != null && rLocation.getSecretPassage() == dest){
                rLocation.removeSuspect(s);
                dest.addSuspect(s);
                s.setRoomLocation(dest);
                s.setWasMovedOnSuggest(false);
            }
            else{
                System.out.println("This room is not accessible via secret passage.");
                return false;
            }
        }
        //suspect is in a hallway
        else{
            JHallway hLocation = s.getHallwayLocation();
            //check if the dest room is adjacent to suspect's hallway
            if(hLocation.isRoomAdjacent(dest)){
                hLocation.removeSuspect();
                dest.addSuspect(s);
                s.setRoomLocation(dest);
            }
            else {
                System.out.println("This room is not accessible from this hallway");
                return false; //impossible to move to a hallway from a hallway
            }
        }
        
        return true;
    }
    
    //on a suggestion this moves the suspect and weapon to a room
    public boolean moveOnSuggestion(JSuspect s, JWeapon w, JRoom dest){
        
        //move the suspect
        //is suspect in hallway or room?
        switch (s.inRoom()) {
            case 1:
                s.getRoomLocation().removeSuspect(s);
                break;
            case 0:
                s.getHallwayLocation().removeSuspect();
                break;
            default:
                return false;
        }
        dest.addSuspect(s);
        s.setRoomLocation(dest);
        s.setWasMovedOnSuggest(true);
        
        //move weapon
        w.getRoomLocation().removeWeapon(w);
        dest.addWeapon(w);
        w.setRoomLocation(dest);
 
        return true;
    }
    
    //constructor... creates all game objects        
    public JBoard(){
        
        //create suspects
        JSuspect s0 = new JSuspect("Miss Scarlet");
        JSuspect s1 = new JSuspect("Colonel Mustard");
        JSuspect s2 = new JSuspect("Mrs. White");
        JSuspect s3 = new JSuspect("Mr. Green");
        JSuspect s4 = new JSuspect("Mrs. Peacock");
        JSuspect s5 = new JSuspect("Professor Plum");
        
        //create weapons
        JWeapon w0 = new JWeapon("wrench");
        JWeapon w1 = new JWeapon("candlestick");
        JWeapon w2 = new JWeapon("knife");
        JWeapon w3 = new JWeapon("rope");
        JWeapon w4 = new JWeapon("lead pipe");
        JWeapon w5 = new JWeapon("revolver");
        
        //create the hallways
        JHallway h0 = new JHallway(0,null);
        JHallway h1 = new JHallway(1,s0);
        JHallway h2 = new JHallway(2,s5);
        JHallway h3 = new JHallway(3,null);
        JHallway h4 = new JHallway(4,s1);
        JHallway h5 = new JHallway(5,null);
        JHallway h6 = new JHallway(6,null);
        JHallway h7 = new JHallway(7,s4);
        JHallway h8 = new JHallway(8,null);
        JHallway h9 = new JHallway(9,null);
        JHallway h10 = new JHallway(10,s3);
        JHallway h11 = new JHallway(11,s2);
        
        //add locations to suspects
        s0.setHallwayLocation(h1);
        s1.setHallwayLocation(h4);
        s2.setHallwayLocation(h11);
        s3.setHallwayLocation(h10);
        s4.setHallwayLocation(h7);
        s5.setHallwayLocation(h2);
        
        //create room weapons lists
        ArrayList<JWeapon> wList0 = new ArrayList<>();
        ArrayList<JWeapon> wList1 = new ArrayList<>();
        ArrayList<JWeapon> wList2 = new ArrayList<>();
        ArrayList<JWeapon> wList3 = new ArrayList<>();
        ArrayList<JWeapon> wList4 = new ArrayList<>();
        ArrayList<JWeapon> wList5 = new ArrayList<>();
        ArrayList<JWeapon> wList6 = new ArrayList<>();
        ArrayList<JWeapon> wList7 = new ArrayList<>();
        ArrayList<JWeapon> wList8 = new ArrayList<>();
        
        //add weapons to a list
        wList0.add(w0);
        wList1.add(w1);
        wList2.add(w2);
        wList3.add(w3);
        wList4.add(w4);
        wList5.add(w5);
        
        //create room suspect lists
        ArrayList<JSuspect> sList0 = new ArrayList<>();
        ArrayList<JSuspect> sList1 = new ArrayList<>();
        ArrayList<JSuspect> sList2 = new ArrayList<>();
        ArrayList<JSuspect> sList3 = new ArrayList<>();
        ArrayList<JSuspect> sList4 = new ArrayList<>();
        ArrayList<JSuspect> sList5 = new ArrayList<>();
        ArrayList<JSuspect> sList6 = new ArrayList<>();
        ArrayList<JSuspect> sList7 = new ArrayList<>();
        ArrayList<JSuspect> sList8 = new ArrayList<>();
        
        //create rooms
        JRoom r0 = new JRoom("Study",wList0,sList0);
        JRoom r1 = new JRoom("Hall",wList1,sList1);
        JRoom r2 = new JRoom("Lounge",wList2,sList2);
        JRoom r3 = new JRoom("Library",wList3,sList3);
        JRoom r4 = new JRoom("Billiard Room",wList4,sList4);
        JRoom r5 = new JRoom("Dining Room",wList5,sList5);
        JRoom r6 = new JRoom("Conservatory",wList6,sList6);
        JRoom r7 = new JRoom("Ballroom",wList7,sList7);
        JRoom r8 = new JRoom("Kitchen",wList8,sList8);
        
        //add room location to weapons
        w0.setRoomLocation(r0);
        w1.setRoomLocation(r1);
        w2.setRoomLocation(r2);
        w3.setRoomLocation(r3);
        w4.setRoomLocation(r4);
        w5.setRoomLocation(r5);

        //package rooms for hallways
        ArrayList<JRoom> rList0 = new ArrayList<>();
        ArrayList<JRoom> rList1 = new ArrayList<>();
        ArrayList<JRoom> rList2 = new ArrayList<>();
        ArrayList<JRoom> rList3 = new ArrayList<>();
        ArrayList<JRoom> rList4 = new ArrayList<>();
        ArrayList<JRoom> rList5 = new ArrayList<>();
        ArrayList<JRoom> rList6 = new ArrayList<>();
        ArrayList<JRoom> rList7 = new ArrayList<>();
        ArrayList<JRoom> rList8 = new ArrayList<>();
        ArrayList<JRoom> rList9 = new ArrayList<>();
        ArrayList<JRoom> rList10 = new ArrayList<>();
        ArrayList<JRoom> rList11 = new ArrayList<>();
        
        //add rooms to lists
        rList0.add(r0);
        rList0.add(r1);
        rList1.add(r1);
        rList1.add(r2);
        rList2.add(r0);
        rList2.add(r3);
        rList3.add(r1);
        rList3.add(r4);
        rList4.add(r2);
        rList4.add(r5);
        rList5.add(r3);
        rList5.add(r4);
        rList6.add(r4);
        rList6.add(r5);
        rList7.add(r3);
        rList7.add(r6);
        rList8.add(r4);
        rList8.add(r7);
        rList9.add(r5);
        rList9.add(r8);
        rList10.add(r6);
        rList10.add(r7);
        rList11.add(r7);
        rList11.add(r8);
        
        //set room lists for the hallways
        h0.setAdjacentRooms(rList0);
        h1.setAdjacentRooms(rList1);
        h2.setAdjacentRooms(rList2);
        h3.setAdjacentRooms(rList3);
        h4.setAdjacentRooms(rList4);
        h5.setAdjacentRooms(rList5);
        h6.setAdjacentRooms(rList6);
        h7.setAdjacentRooms(rList7);
        h8.setAdjacentRooms(rList8);
        h9.setAdjacentRooms(rList9);
        h10.setAdjacentRooms(rList10);
        h11.setAdjacentRooms(rList11);
        
        //package hallways for rooms
        ArrayList<JHallway> hList0 = new ArrayList<>();
        ArrayList<JHallway> hList1 = new ArrayList<>();
        ArrayList<JHallway> hList2 = new ArrayList<>();
        ArrayList<JHallway> hList3 = new ArrayList<>();
        ArrayList<JHallway> hList4 = new ArrayList<>();
        ArrayList<JHallway> hList5 = new ArrayList<>();
        ArrayList<JHallway> hList6 = new ArrayList<>();
        ArrayList<JHallway> hList7 = new ArrayList<>();
        ArrayList<JHallway> hList8 = new ArrayList<>();
        
        //add hallways to lists
        hList0.add(h0);
        hList0.add(h2);
        hList1.add(h0);
        hList1.add(h1);
        hList1.add(h3);
        hList2.add(h1);
        hList2.add(h4);
        hList3.add(h2);
        hList3.add(h5);
        hList3.add(h7);
        hList4.add(h3);
        hList4.add(h5);
        hList4.add(h6);
        hList4.add(h8);
        hList5.add(h4);
        hList5.add(h6);
        hList5.add(h9);
        hList6.add(h7);
        hList6.add(h10);
        hList7.add(h8);
        hList7.add(h10);
        hList7.add(h11);
        hList8.add(h9);
        hList8.add(h11);
        
        //set hallway lists and secret passage, if there is one, for each room
        r0.setAdjacentLocations(hList0,r8);
        r1.setAdjacentLocations(hList1,null);
        r2.setAdjacentLocations(hList2,r6);
        r3.setAdjacentLocations(hList3,null);
        r4.setAdjacentLocations(hList4,null);
        r5.setAdjacentLocations(hList5,null);
        r6.setAdjacentLocations(hList6,r2);
        r7.setAdjacentLocations(hList7,null);
        r8.setAdjacentLocations(hList8,r0);
        
        //that was exhausting... no we can finally add the rooms and hallways to the board
        suspects = new ArrayList<>();
        weapons = new ArrayList<>();
        hallways = new ArrayList<>();
        rooms = new ArrayList<>();
        
        //add suspects
        suspects.add(s0);
        suspects.add(s1);
        suspects.add(s2);
        suspects.add(s3);
        suspects.add(s4);
        suspects.add(s5);
        
        //add weapons
        weapons.add(w0);
        weapons.add(w1);
        weapons.add(w2);
        weapons.add(w3);
        weapons.add(w4);
        weapons.add(w5);
        
        //add hallways
        hallways.add(h0);
        hallways.add(h1);
        hallways.add(h2);
        hallways.add(h3);
        hallways.add(h4);
        hallways.add(h5);
        hallways.add(h6);
        hallways.add(h7);
        hallways.add(h8);
        hallways.add(h9);
        hallways.add(h10);
        hallways.add(h11);
        
        //add rooms
        rooms.add(r0);
        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);
        rooms.add(r4);
        rooms.add(r5);
        rooms.add(r6);
        rooms.add(r7);
        rooms.add(r8); 
    }
    
    /**
     * @return the hallways
     */
    public ArrayList<JHallway> getHallways() {
        return hallways;
    }

    /**
     * @return the rooms
     */
    public ArrayList<JRoom> getRooms() {
        return rooms;
    }

    /**
     * @return the suspects
     */
    public ArrayList<JSuspect> getSuspects() {
        return suspects;
    }

    /**
     * @return the weapons
     */
    public ArrayList<JWeapon> getWeapons() {
        return weapons;
    }
    
    //for testing
    public void printBoard(){
        
        System.out.println("************CLUELESS GAMEBOARD**************");
        
        System.out.println("************ROOMS ON BOARD******************");
        
        for (JRoom room : getRooms()) {
            room.printRoom();
        }
        
        System.out.println("************HALLWAYS ON BOARD***************");
        
        for (JHallway hallway : getHallways()) {
            hallway.printHallway();
        }
        
        System.out.println("********************************************");
        
    }
}

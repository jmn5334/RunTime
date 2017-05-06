/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.game;

import java.util.ArrayList;
import runtime.clueless.networking.GameMsg;

/**
 *
 * @author jmnew
 */
public class JPlayer {
    
    private final JBoard board;
    private JSuspect suspect;
    private final String name;
    private int id;
    private ArrayList<JCard> cards;
    private boolean movedOnSuggest;
    
    public JPlayer(String name, boolean onServer){
        if(!onServer)
            board = new JBoard();
        else
            board = null;
        suspect = null;
        this.name = name;
        id = -99;
        cards = new ArrayList<>();
        movedOnSuggest = false;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void addCard(JCard c){
        cards.add(c);
    }
    
    //interface to update board, replicates commands done on server
    public boolean updateBoard(String sName, String wName, String destName, int hall_id, GameMsg.sub_cmd cmd){
        
        //find obj ref to suspect
        ArrayList<JSuspect> suspects = board.getSuspects();
        JSuspect ourSuspect = null;
        
        for(JSuspect s : suspects){
            if(s.getName().equals(sName))
                ourSuspect = s;
        }
        
        if(ourSuspect == null){
            System.out.println("updateBoard: failed to find suspect.");
            return false;
        }
        
        switch(cmd){
            case move2hall:{
                //find ref to hallway
                ArrayList<JHallway> hallways = board.getHallways();
                JHallway ourHall = null;
                
                for(JHallway h : hallways){
                    if(hall_id == h.getId()){
                        ourHall = h;
                    }
                }
                
                if(ourHall == null){
                    System.out.println("updateBoard: failed to find hall.");
                    return false;
                }
                
                board.moveSuspectToHallway(ourSuspect, ourHall);
                break;
            }
            case move2room: {
                //find obj ref to dest room
                ArrayList<JRoom> rooms = board.getRooms();
                JRoom ourRoom = null;

                for (JRoom r : rooms) {
                    if (r.getName().equals(destName)) {
                        ourRoom = r;
                    }
                }

                if (ourRoom == null) {
                    System.out.println("updateBoard: failed to find room.");
                    return false;
                }

                board.moveSuspectToRoom(ourSuspect, ourRoom);

                break;
            }
            case moveOnSuggest: {
                //find obj ref to dest room
                ArrayList<JRoom> rooms = board.getRooms();
                JRoom ourRoom = null;

                for (JRoom r : rooms) {
                    if (r.getName().equals(destName)) {
                        ourRoom = r;
                    }
                }

                if (ourRoom == null) {
                    System.out.println("updateBoard: failed to find room.");
                    return false;
                }
                
                //find ref to weapon
                ArrayList<JWeapon> weapons = board.getWeapons();
                JWeapon ourWeapon = null;
                
                for(JWeapon w: weapons){
                    if(w.getName().equals(wName)){
                        ourWeapon = w;
                    }
                }
                
                if(ourWeapon == null){
                    System.out.println("updateBoard: failed to find weapon.");
                    return false;
                }
                
                board.moveOnSuggestion(ourSuspect, ourWeapon, ourRoom);

                break;
            }
            default:{
                System.out.println("updateBoard: Recieved bad sub_cmd.");
                return false;
            }
                
                
        }
        
        return true;
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
        return getBoard().getHallways();
    }

    public ArrayList<JRoom> getRooms() {
        return getBoard().getRooms();
    }
    
    public ArrayList<JSuspect> getSuspects() {
        return getBoard().getSuspects();
    }

    public ArrayList<JWeapon> getWeapons() {
        return getBoard().getWeapons();
    }
    
    /**
     * @return the board
     */
    public JBoard getBoard() {
        return board;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the cards
     */
    public ArrayList<JCard> getCards() {
        return cards;
    }

    /**
     * @param cards the cards to set
     */
    public void setCards(ArrayList<JCard> cards) {
        this.cards = cards;
    }
    
    /**
     * @return the movedOnSuggest
     */
    public boolean isMovedOnSuggest() {
        return movedOnSuggest;
    }

    /**
     * @param movedOnSuggest the movedOnSuggest to set
     */
    public void setMovedOnSuggest(boolean movedOnSuggest) {
        this.movedOnSuggest = movedOnSuggest;
    }
}

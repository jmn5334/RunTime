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
public class TestBoard {
    
    public static void main(String [ ] args){
        
        //test intial board configuration
        //JBoard board = new JBoard();
        JPlayer player = new JPlayer("Jake",false);
        
        player.getBoard().printBoard();
        
        ArrayList<JSuspect> choices = player.getSuspects();
        ArrayList<JWeapon> weaponChoices = player.getWeapons();
        
        //find professor plum
        for(JSuspect s : choices){
            if(s.getName().equals("Mr. Green"))
                player.setSuspect(s);
        }
        
        ArrayList<JHallway> hallwayChoices = player.getHallways();
        ArrayList<JRoom> roomChoices = player.getRooms();
        
        //TEST MOVEMENT
        
        
        System.out.println("TESTING GAME MOVEMENT");
        //go to room
        if(player.getBoard().moveSuspectToRoom(player.getSuspect(), roomChoices.get(6)))
            System.out.println("Move succeeded!  PASS");
        else
            System.out.println("Move failed  FAIL");
        
        //go to hallway
        if(player.getBoard().moveSuspectToHallway(player.getSuspect(), hallwayChoices.get(7)))
            System.out.println("Move succeeded!  FAIL");
        else
            System.out.println("Move failed  PASS");
        
        //go to room
        if(player.getBoard().moveSuspectToRoom(player.getSuspect(), roomChoices.get(6)))
            System.out.println("Move succeeded! FAIL");
        else
            System.out.println("Move failed  PASS");
        
        //go to room
        if(player.getBoard().moveSuspectToRoom(player.getSuspect(), roomChoices.get(2)))
            System.out.println("Move succeeded!  PASS");
        else
            System.out.println("Move failed  FAIL");
        
        //go to room
        if(player.getBoard().moveSuspectToRoom(player.getSuspect(), roomChoices.get(0)))
            System.out.println("Move succeeded! FAIL");
        else
            System.out.println("Move failed PASS");
        
        //go to room
        if(player.getBoard().moveSuspectToRoom(player.getSuspect(), roomChoices.get(6)))
            System.out.println("Move succeeded! PASS");
        else
            System.out.println("Move failed FAIL");
        
        System.out.println("END MOVEMENT TEST");
        
        player.getBoard().printBoard();
        
        System.out.println("TESTING SUGGESTIONS");
        
        player.getBoard().moveOnSuggestion(choices.get(0), weaponChoices.get(4), roomChoices.get(7));
        player.getBoard().moveOnSuggestion(choices.get(5), weaponChoices.get(1), roomChoices.get(8));
        player.getBoard().moveOnSuggestion(choices.get(3), weaponChoices.get(5), roomChoices.get(4));

        player.getBoard().printBoard();
        
        System.out.println("Check board output for pass");
        
        System.out.println("END TESTING SUGGESTIONS");
        
    }
    
}

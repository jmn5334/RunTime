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
        JBoard board = new JBoard();
        JPlayer player = new JPlayer("Jake");
        
        board.printBoard();
        
        ArrayList<JSuspect> choices = player.getSuspects();
        
        //find professor plum
        for(JSuspect s : choices){
            if(s.getName().equals("Mr. Green"))
                player.setSuspect(s);
        }
        
        ArrayList<JHallway> hallwayChoices = player.getHallways();
        
        if(board.moveSuspectToHallway(player.getSuspect(), hallwayChoices.get(8)))
            System.out.println("Move succeeded!");
        else
            System.out.println("Move failed");
        
    }
    
}

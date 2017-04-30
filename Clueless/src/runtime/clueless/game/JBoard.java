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
    
    private ArrayList<JHallway> hallways;
    private ArrayList<JRoom> rooms;
            
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
        
        
    }
}

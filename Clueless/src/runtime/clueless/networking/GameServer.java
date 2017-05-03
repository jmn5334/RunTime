/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import runtime.clueless.game.JBoard;
import runtime.clueless.game.JCard;
import runtime.clueless.game.JPlayer;
import runtime.clueless.game.JRoom;
import runtime.clueless.game.JSuspect;
import runtime.clueless.game.JWeapon;

/**
 *
 * @author jmnew
 */
public class GameServer {
    
    //TODO move this
    public static void main(String [ ] args){
        
        GameServer gs;
        gs =  new GameServer(1);
        
        gs.acceptClients();
        
        gs.startGame();
        
        while(true){
            try {
                // System.out.println(gs.msg.text);
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private ServerSocket server;
    private final int MAX_THREADS = 6;
    private int numClients;
    private final int numPlayers;
    private final Thread[] threads = new Thread[MAX_THREADS];
    private final ClientThread[] cthreads = new ClientThread[MAX_THREADS];
    
    //biz logic variables
    private final ArrayList<JCard> deck;
    private final ArrayList<JCard> caseFile;
    private JBoard board;
    private ArrayList<JPlayer> players;
    
    //shared object
    public static GameMsg msg;
    public static volatile int turn;

    public GameServer(int numPlayers){
        System.out.println("GameServer is being created.");
        server = null;
        numClients = 0;
        this.numPlayers = numPlayers;
        msg = new GameMsg();
        turn = -99;
        
        //biz logic initialization
        deck = new ArrayList<>();
        caseFile = new ArrayList<>();
        players = new ArrayList<>();
        
        //create cards
        initCards();
    }
    
    
    public void acceptClients(){
        try{
            server = new ServerSocket(5000);
        }catch (IOException e){
            System.out.println("Could not listen on port 5000");
            System.exit(-1);
        }
        while(true){
            try{
                //make sure we are under the max and the number desired by the user
                if(numClients < MAX_THREADS && numClients < numPlayers){
                    
                    
                    System.out.println("Waiting for client connections...");
                    cthreads[numClients] = new ClientThread(server.accept(),numClients);
                    
                    System.out.println("Recieved a client connect request!!!");
                    System.out.println("Creating thread "+Integer.toString(numClients)+"...");
                    threads[numClients] = new Thread(cthreads[numClients]);
                    threads[numClients].start();

                    numClients++;
                }
                else{
                    System.out.println("Done accepting clients.");
                    return;
                }
            } catch (IOException e){
                System.out.println("Accept failed: 5000");
                System.exit(-1);
            }
        }
    }
    
    public boolean verifyResponse(String messageType, int cid){
        
            boolean success = true;
        
            //verify that msg worked, otherwise return failure
            switch (msg.command) {
                case ack:
                    System.out.println("Successfully sent "+messageType+" to client "+Integer.toString(cid));
                    break;
                case invalid:
                    System.out.println("Client returned that our cmd was invalid. Failed to send "+messageType+" to client "+Integer.toString(cid));
                    success = false;
                    break;
                default:
                    System.out.println("Recieved invalid response for "+messageType+" from client "+Integer.toString(cid));
                    success = false;
                    break;
            }
            
            return success;
    }
    
    public boolean negotiatePlayers(){
        
        boolean success = true;

        for (int i = 0; i < numClients; i++) {

            msg.name = "Server";
            msg.command = GameMsg.cmd.init;
            msg.id = i;

            turn = i;
            serverWait();
            
            if(verifyResponse("init",i)){
                JPlayer p = new JPlayer(msg.name,true);
                p.setId(i);
                p.setSuspect(board.findSuspect(msg.suspect));
                players.add(p);
            }
            else{
                success = false;
            }
            
            
        }

        return success;
        
    }
    
    //starts the game once everyone has joined
    public void startGame(){
        
        //send state to threads
        System.out.println("Sending board state to all clients.");
        int retries = 3;
        while(!sendBoardState() && retries > 0){
            System.out.println("Retrying to send board state.");
            retries--;
        }
        System.out.println("Done sending board state to all clients.");
        
        negotiatePlayers();
        
        dealCards();//TODO not implemented
    
        //BEGIN STATE MACHINE HERE
        
        //game managing assets
        
        
        //turn terminating conditions
        boolean hasMoved,
                hasSuggested,
                hasAccused,
                isStuck,
                hasSurrendered;
        
        //game terminating conditions
        boolean haveWinner = false;
        
        //continue until we have a winner
        while(!haveWinner){
            
            //intitialize turn state
            hasMoved = false;
            hasSuggested = false;
            hasAccused = false;
            isStuck = false;
            hasSurrendered = false;
            
            //continue turn until we've met terminating conditions
            while(!isStuck && !hasSurrendered && !hasAccused){

                //check if stuck
                
                
            }
        }       
  
    }
    
    public void dealCards(){
        
    }
    
    public final void initCards(){
        
        long seed = System.nanoTime(); 
        
        //get board objects that correspond to cards
        ArrayList<JRoom> rooms = board.getRooms();
        ArrayList<JSuspect> suspects = board.getSuspects();
        ArrayList<JWeapon> weapons = board.getWeapons();
        
        //split cards for shuffling
        ArrayList<JCard> roomCards = new ArrayList<>(),
                         suspectCards = new ArrayList<>(),
                         weaponCards = new ArrayList<>();
        
        //create room cards
        for(JRoom r : rooms){
            JCard c = new JCard(r.getName(),JCard.card_type.room);
            roomCards.add(c);
        }
        
        //create suspect cards
        for(JSuspect s : suspects){
            JCard c = new JCard(s.getName(),JCard.card_type.suspect);
            suspectCards.add(c);
        }
        
        //create weapon cards
        for(JWeapon w : weapons){
            JCard c = new JCard(w.getName(),JCard.card_type.weapon);
            weaponCards.add(c);
        }
        
        Collections.shuffle(roomCards, new Random(seed));
        Collections.shuffle(suspectCards, new Random(seed));
        Collections.shuffle(weaponCards, new Random(seed));
        
        //remove an element from each and put into case file
        caseFile.add(roomCards.remove(0));
        caseFile.add(suspectCards.remove(0));
        caseFile.add(weaponCards.remove(0));
        
        System.out.println("Winning cards are "+caseFile.get(0).getName()+","+caseFile.get(1).getName()+","+caseFile.get(2).getName());
        
        //add remaining cards to deck and reshuffle
        deck.addAll(roomCards);
        deck.addAll(suspectCards);
        deck.addAll(weaponCards);
        
        //reshuffle
        Collections.shuffle(deck, new Random(seed));
        
    }
    
    public boolean sendBoardState() {
        
        boolean success = true;

        for (int i = 0; i < numClients; i++) {

            msg.name = "Server";
            msg.command = GameMsg.cmd.board_state;
            msg.id = i;
            //TODO: set boardstate

            turn = i;
            serverWait();
            
            //verify that msg worked, otherwise return failure
            switch (msg.command) {
                case ack:
                    System.out.println("Successfully sent board state to client "+Integer.toString(i));
                    break;
                case invalid:
                    System.out.println("Client returned that our cmd was invalid. Failed to send board state to client "+Integer.toString(i));
                    success = false;
                    break;
                default:
                    System.out.println("Recieved invalid response from client "+Integer.toString(i));
                    success = false;
                    break;
            }
        }

        return success;
    }
    
    public void startTurn(){
        
    }
    
    public void endGame(){
        
    }
    
    public void killPlayer(){
        
    }
    
    public void askNextPlayer(){
        
    }
    
    public void nooneHadIt(){
        
    }
    
    //wait for a thread to do its business
    private void serverWait(){
        while(turn != -99){
            try {
                sleep(25);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }
    
    /**
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable{
        try{
            server.close();
        } catch (IOException e) {
            System.out.println("Could not close socket");
            System.exit(-1);
        } finally {         //called whether an exception is thrown or not
            super.finalize();
        }
    }
    
}

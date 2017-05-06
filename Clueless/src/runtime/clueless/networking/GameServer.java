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
        
        String sPort;
        String sNumPlayers;
        
        //validate that we got host and player args
        if(args.length != 2){
            System.out.println("Incorrect number of args provided!");
            System.out.println("Using defaults port=5000 and numPlayers=1");
            sPort = "5000";
            sNumPlayers = "1";
        }
        else {
            sPort = args[0];
            sNumPlayers = args[1];
        }

        if(sPort.equals("")||sNumPlayers.equals("")){
            System.out.println("Arguments are empty. Server not started.");
            System.exit(-1);
        }
            
        //convert to ints
        int port = Integer.parseInt(sPort);
        int numPlayers = Integer.parseInt(sNumPlayers);
        
        GameServer gs;
        gs =  new GameServer(port,numPlayers);
        
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
    private final int port;
    private final Thread[] threads = new Thread[MAX_THREADS];
    private final ClientThread[] cthreads = new ClientThread[MAX_THREADS];
    
    //biz logic variables
    private final ArrayList<JCard> deck;
    private final ArrayList<JCard> caseFile;
    private final JBoard board;
    private final ArrayList<JPlayer> players;
    private ArrayList<JPlayer> activePlayers;
    
    //shared object
    public static GameMsg msg;
    public static volatile int turn;

    public GameServer(int port, int numPlayers){
        System.out.println("GameServer is being created.");
        server = null;
        numClients = 0;
        this.numPlayers = numPlayers;
        this.port = port;
        msg = new GameMsg();
        turn = -99;
        
        //biz logic initialization
        deck = new ArrayList<>();
        caseFile = new ArrayList<>();
        players = new ArrayList<>();
        board = new JBoard();
        activePlayers = new ArrayList<>();
        
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
                //System.exit(-1);
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
            
            ArrayList<JSuspect> suspects = board.getSuspects();
            
            //randomally assign a suspect to each player
            msg.suspect = suspects.get(i).getName();

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
                activePlayers.add(p);
            }
            else{
                success = false;
            }
            
            
        }

        return success;
        
    }
    
    //starts the game once everyone has joined
    public void startGame(){
        
        negotiatePlayers();
        
        dealCards();
    
        //BEGIN STATE MACHINE HERE
        
        //turn terminating conditions
        boolean hasMoved,
                hasSuggested,
                hasAccused,
                hasSurrendered,
                hasGone;
        
        //game terminating conditions
        boolean haveWinner = false;
        boolean allLosers = false;
        
        //LCVs
        int i = 0;
        int currentId;
        
        //continue until we have a winner
        while(!haveWinner && !allLosers){
            
            //reset the player index if we hit max to restart turn rotation
            if(i >= activePlayers.size()){
                i = 0;
            }
            
            //get id of next player
            currentId = activePlayers.get(i).getId();
            
            //intitialize turn state
            hasMoved = false;
            hasSuggested = false;
            hasAccused = false;
            hasSurrendered = false;
            hasGone = false;

            //check for player being stuck, if so they can only accuse
            if(board.isStuck(activePlayers.get(i).getSuspect())){
                hasMoved = true;
                hasSuggested = true;
                sendUpdate(activePlayers.get(i).getName()+"is stuck. Can only accuse.");
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //continue turn until we've met terminating conditions
            while (!hasSurrendered && !hasAccused) {

                //send start_turn to client
                sendTurn(currentId,hasGone);

                //check what command it is, if a move or suggest, needs to be validated
                //perform logic based on what the current player did
                switch (msg.command) {
                    case move:
                        //verify that we haven't moved already
                        if(!hasMoved){
                            
                            boolean wasMoved;
                            
                            //set the msg to contain the suspect
                            msg.suspect = activePlayers.get(i).getSuspect().getName();
                            
                            //figure out if this is a hallway or a room
                            if(msg.isRoom)
                                wasMoved = board.moveSuspectToRoom(activePlayers.get(i).getSuspect(), board.findRoom(msg.dest));
                            else
                                wasMoved = board.moveSuspectToHallway(activePlayers.get(i).getSuspect(), board.findHallway(msg.destId));
                            
                            if(wasMoved){
                                hasMoved = true; //we will send board state msg after switch statement
                                
                                //set moved on suggest to false as this player just chose to move
                                activePlayers.get(i).setMovedOnSuggest(false);
                                
                                if(msg.isRoom)
                                    //send board
                                    sendBoardState("Player "+activePlayers.get(i).getName()+"("+activePlayers.get(i).getSuspect().getName()
                                            +") moved to "+msg.dest,GameMsg.sub_cmd.move2room);
                                else
                                    sendBoardState("Player "+activePlayers.get(i).getName()+"("+activePlayers.get(i).getSuspect().getName()
                                            +") moved to Hallway "+Integer.toString(msg.destId),GameMsg.sub_cmd.move2hall);
                            }
                            else{
                                sendInvalid(currentId,"Move failed! Choose an accessible location.");
                            }
                        }
                        else{
                            sendInvalid(currentId,"You've already moved! Please select another option.");
                        }
                        break;
                    case suggest:
                        if((!hasSuggested && activePlayers.get(i).isMovedOnSuggest()) || (!hasSuggested && hasMoved)){         
                            
                            String sSuspect = msg.suspect;
                            String sWeapon = msg.weapon;
                            String sRoom = msg.dest;
                            
                            //check that the player is actually in this room
                            if (board.findRoom(msg.dest) == activePlayers.get(i).getSuspect().getRoomLocation()) {

                                if (board.moveOnSuggestion(board.findSuspect(msg.suspect), board.findWeapon(msg.weapon), board.findRoom(msg.dest))) {
                                    
                                    hasSuggested = true; //will send board update
                                    
                                    //find the player with this suspect and set they're movedOnSuggest
                                    for(JPlayer x : players){
                                        if(x.getSuspect().getName().equals(msg.suspect))
                                            x.setMovedOnSuggest(true);
                                    }
                                    
                                    //when you suggest you forfeit the right to move
                                    hasMoved = true;
                                    
                                    //send board update
                                    sendBoardState("Player "+activePlayers.get(i).getName()+"("+activePlayers.get(i).getSuspect().getName()
                                            +") has suggested ("+msg.suspect+","+msg.weapon+","+msg.dest+").",GameMsg.sub_cmd.moveOnSuggest);
                                    
                                    //begin suggetion logic - loop through remaining players
                                    //need to map index back to all players as all can reveal cards
                                    int startingPlayer = currentId;
                                    int j;
                                    
                                    if(startingPlayer >= players.size() - 1)
                                        j = 0;
                                    else
                                        j = startingPlayer + 1;
                                    
                                    boolean respValid = false;
                                    //loop until we get back around to suggester
                                    while(j != startingPlayer && !respValid){
                                        
                                        //try until we get valid response
                                        while (!respValid) {

                                            //card sending code here
                                            sendRevealCard(players.get(j).getId(), msg.suspect, msg.weapon, msg.dest);

                                            //validate response
                                            if (msg.command == GameMsg.cmd.send_card_client) {
                                                if (msg.card.equals(sRoom) || msg.card.equals(sWeapon) || msg.card.equals(sSuspect)) {

                                                    //send card to initial suggester
                                                    sendUpdate("Card passed from "+players.get(j).getName()+" to "+players.get(currentId).getName()+"."); //add more here later
                                                    sendInvalid(startingPlayer, players.get(j).getName()+" revealed "+msg.card); //probably should use something else, but it works
                                                    respValid = true;
                                                    break;
                                                } else {
                                                    sendInvalid(j, "Card sent doesn't match suggestion. Send another card or pass");
                                                    try {
                                                        //pause so the player sees they're mistake
                                                        sleep(5000);
                                                    } catch (InterruptedException ex) {
                                                        Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            } else if (msg.command == GameMsg.cmd.pass) {
                                                //player doesn't have card so move on
                                                sendUpdate(players.get(j).getName()+" could not reveal a card for ("+sSuspect+","+sWeapon+","+sRoom+").");
                                                break;
                                            } else {
                                                sendInvalid(currentId, "Invalid message type. Please send a card");
                                            }
                                        }   
                                        
                                        j++;
                                        //if we reach end of array reset to 0
                                        if(j == players.size())
                                            j = 0;

                                    }      
                                } else {
                                    sendInvalid(currentId, "Suggestion failed. Try again.");
                                }

                                
                            } else {
                                sendInvalid(currentId, "You can't suggest from a room you aren't in. Try again.");
                            }
                        }
                        else{
                            if(hasSuggested)
                                sendInvalid(currentId,"You've already suggested! Please select another option.");
                            else
                                sendInvalid(currentId,"Please move before suggesting.");
                        }
                        break;
                    case accuse:
                        
                        //check accusation against the casefile
                        
                        haveWinner = true;
                        for(JCard c : caseFile){
                            if(!(c.getName().equals(msg.suspect) || c.getName().equals(msg.weapon) || c.getName().equals(msg.dest)))
                                haveWinner = false;
                        }
                        String s = msg.suspect;
                        String w = msg.weapon;
                        String r = msg.dest;
                        
                        //if they won, continue to end the game, if not kill the player
                        if (!haveWinner) {
                            sendKill(currentId);
                            sendUpdate(activePlayers.get(i).getName() + " lost. Wrongly accused: (" + s + "," + w + "," + r + ").");
                            activePlayers.remove(i);
                            try {
                                sleep(10000); //give time for the loser to see this
                            } catch (InterruptedException ex) {
                                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else{
                            sendWinner(currentId);
                            notifyLosers(currentId);
                        }
                        
                        hasAccused = true;
                        
                        //check if this is the last player, if so send out everybody lost
                        if(activePlayers.isEmpty()){
                            sendUpdate("Game over. Noone solved the mystery.");
                            allLosers = true;
                        }
                        
                        break;
                    case end_turn:
                        hasSurrendered = true;
                        break;
                    default:
                        sendInvalid(currentId,"Recieved invalid command at Game Server!");
                }
                hasGone = true;
                
            } //end turn loop
            i++;
        }//end game loop    
        
        while(true){
            try {
                sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void notifyLosers(int winner){
        
        for (int i = 0; i < numClients; i++) {

            //only send to losers
            if (!(players.get(i).getId() == winner)) {
                
                msg.name = "Server";
                msg.command = GameMsg.cmd.game_over;
                msg.id = i;
                msg.text = players.get(winner).getName() + " has won. You lost...";

                turn = i;
                serverWait();
            }
        }
    }
    
    public void sendUpdate(String s){
        
        for (int i = 0; i < numClients; i++) {

            msg.name = "Server";
            msg.command = GameMsg.cmd.update;
            msg.id = i;
            msg.text = s;

            turn = i;
            serverWait();
        }
        
    }
    
    public void sendWinner(int i){
        msg.name = "Server";
        msg.command = GameMsg.cmd.game_over;
        msg.id = i;
        msg.text = "You won!!!";

        turn = i;
        serverWait(); 
    }
    
    //sends message with move instructions to client
    public boolean sendBoardState(String s, GameMsg.sub_cmd scmd) {
        
        boolean success = true;

        for (int i = 0; i < numClients; i++) {

            msg.name = "Server";
            msg.command = GameMsg.cmd.board_state;
            msg.id = i;  
            msg.subcommand = scmd;
            msg.text = s;

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
    
    public void sendKill(int i){
        msg.name = "Server";
        msg.command = GameMsg.cmd.kill_player;
        msg.id = i;
        msg.text = "Accusation incorrect!!! You've lost.";

        turn = i;
        serverWait();
    }
    
    public void sendCard(int i, String s) {
        msg.name = "Server";
        msg.command = GameMsg.cmd.send_card_server;
        msg.id = i;
        msg.card = s;

        turn = i;
        serverWait();
    }
    
    public void sendRevealCard(int i,String s, String w, String p){
        
        msg.name = "Server";
        msg.command = GameMsg.cmd.reveal_card;
        msg.text = "Suggestion from "+players.get(i).getName()+" ("+s+","+w+","+p+").Please reveal one of these cards.";
        
        turn = i;
        serverWait(); 
    }
    
    public void sendInvalid(int i, String err){
        msg.name = "Server";
        msg.command = GameMsg.cmd.invalid;
        msg.id = i;
        msg.text = err;

        turn = i;
        serverWait();
    }
    
    //sends turn and recieves message
    public void sendTurn(int i, boolean hasGone){
        
        for (int j = 0;j<numClients;j++) {

            msg.name = "Server";
            msg.id = j;

            if (!(players.get(j).getId() == i)) {
                msg.command = GameMsg.cmd.update;
                if(!hasGone)
                    msg.text = players.get(i).getName() + " is starting their turn.";

                turn = j;
                serverWait();
            }

        }
        msg.name = "Server";
        msg.id = i;

        msg.command = GameMsg.cmd.start_turn;
        if(!hasGone)
            msg.text = "Please start your turn.";

        turn = i;
        serverWait();
    }
    
    public boolean dealCards(){
        
        boolean success = true;
        
        int j = 0;
        
        //deal cards to each player
        for(int i=0;i<deck.size();i++){
            if(j == players.size())
                j = 0;
            players.get(j).addCard(deck.get(i));
            j++;
        }

        //pass cards out
        for (int i = 0; i < numClients; i++) {
            
            ArrayList<JCard> cards = players.get(i).getCards();
            
            for (JCard c : cards) {
                msg.name = "Server";
                msg.command = GameMsg.cmd.send_card_server;
                msg.id = i;
                msg.card = c.getName();

                turn = i;
                serverWait();

                if (!verifyResponse("send_card_server", i)) {
                    success = false;
                } 
            }
        }

        return success;
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
        caseFile.add(suspectCards.remove(0));
        caseFile.add(weaponCards.remove(0));
        caseFile.add(roomCards.remove(0));
        
        System.out.println("Winning cards are "+caseFile.get(0).getName()+","+caseFile.get(1).getName()+","+caseFile.get(2).getName());
        
        //add remaining cards to deck and reshuffle
        deck.addAll(roomCards);
        deck.addAll(suspectCards);
        deck.addAll(weaponCards);
        
        //reshuffle
        Collections.shuffle(deck, new Random(seed));
        
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
            //System.exit(-1);
        } finally {         //called whether an exception is thrown or not
            super.finalize();
        }
    }
    
}

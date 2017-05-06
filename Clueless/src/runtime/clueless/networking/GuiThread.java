/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import runtime.clueless.game.JCard;
import runtime.clueless.game.JPlayer;
import runtime.clueless.gui.fxml.MainGUIFXML;
import static runtime.clueless.networking.GameClient.Gmsg;
import static runtime.clueless.networking.GameClient.Gturn;
import static runtime.clueless.networking.GameClient.updateGUI;

/**
 *
 * @author jmnew
 */
public class GuiThread implements Runnable {
    
    private final Socket socket;
    private int id;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isFirstMsg;
    private final JPlayer player;
    private final MainGUIFXML gui;
    
    //contructor
    public GuiThread(Socket client, JPlayer player, MainGUIFXML gui){
        this.socket = client;
        this.id = -1;   //init to negative until id is negotiated
        this.in = null;
        this.out = null;
        this.isFirstMsg = true;
        System.out.println("GuiThread object being created.");
        this.player = player;
        this.gui = gui;
        Gturn = 1;
    }
    
    @Override
    public void run(){
        
        System.out.println("GUI thread running...");
        
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("past input stream");
            
        } catch (IOException e){
            System.out.println("in or out failed");
            System.exit(-1);
        }
        
        waitForMsg();
    }
    
    public void waitForMsg(){
        
        while (true) {

            boolean isValid = true;

            //read message
            try {
                //wait for object
                //while(!(in.available() > 0)){
                Gmsg = (GameMsg) in.readObject();
                //}
            } catch (IOException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }

            //grab id out of message if it's the first one
            if (isFirstMsg) {
                id = Gmsg.id;
                isFirstMsg = false;
            }
            //TODO: Add error handling for getting message with wrong id

            //print msg
            System.out.println("Client recieved message:");
            System.out.println(Gmsg.text);
            System.out.println(commandToString());
            System.out.println();

            //check command
            switch (Gmsg.command) {
                case init: {
                    handleInit();
                    break;
                }
                case start_turn: {
                    handleStartTurn();
                    break;
                }
                case reveal_card: {
                    break;
                }
                case send_card_server: {
                    handleSendCard();
                    break;
                }
                case kill_player: {
                    handleKillPlayer();
                    break;
                }
                case game_over: {
                    handleGameOver();
                    break;
                }
                case board_state: {
                    handleBoardState();
                    break;
                }
                case update:{
                    handleUpdate();
                    break;
                }
                case invalid:{
                    break;
                }
                default: {
                    //bad_message
                    System.out.println("Error recieved unsupported message type from server.");
                    isValid = false;
                }
            }

            //send ack or invalid message back to server
            if (isValid) {
                //Gmsg.command = GameMsg.cmd.ack;
            } else {
                Gmsg.command = GameMsg.cmd.invalid;
            }

            try {
                out.writeObject(Gmsg);
            } catch (IOException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
    
    public void handleBoardState(){
        System.out.println("handling board state!!!!!!!");
        
        //update gui with text
        gui.updateMsgField(Gmsg.text);
        
        if (null != Gmsg.subcommand) {
            //modify board
            switch (Gmsg.subcommand) {
                case move2hall:
                    player.getBoard().moveSuspectToHallway(player.getBoard().findSuspect(Gmsg.suspect), player.getBoard().findHallway(Gmsg.destId));
                    break;
                case move2room:
                    player.getBoard().moveSuspectToRoom(player.getBoard().findSuspect(Gmsg.suspect), player.getBoard().findRoom(Gmsg.dest));
                    break;
                case moveOnSuggest:
                    player.getBoard().moveOnSuggestion(player.getBoard().findSuspect(Gmsg.suspect), player.getBoard().findWeapon(Gmsg.weapon), player.getBoard().findRoom(Gmsg.dest));
                    break;
                default:
                    break;
            }
        }
        setDefaultMsg();
    }
    
    public void guiWait(){
        //set turn to 0
        Gturn = 0;
        
        setDefaultMsg();
        
        //need to wait for user input
        while (Gturn != 1) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void handleUpdate(){
        
        System.out.println("handling update!!!!!!!");
        
        //update gui with text
        gui.updateMsgField(Gmsg.text);
        
        setDefaultMsg();

    }
    
    public void handleGameOver(){
        System.out.println("handling game over!!!!!!!");
        
        //update gui with text
        gui.updateMsgField(Gmsg.text);
        
        gui.disableAllButtons(true);
        
        setDefaultMsg();
    }
    
    public void handleKillPlayer(){
        
        System.out.println("handling kill player!!!!!!!");
        
        //update gui with text
        gui.updateMsgField(Gmsg.text);
        
        gui.disableAllButtons(true);
        
        setDefaultMsg();
    }
    
    public void handleStartTurn() {
        
        System.out.println("handling start turn!!!!!!!");
        
        //update gui with text
        gui.updateMsgField(Gmsg.text);
        
        //enable buttons for turn
        gui.disableTurnButtons(false);

        guiWait();
    }
    
    public void handleSendCard(){
        JCard c = new JCard(Gmsg.card,Gmsg.ctype);
        player.addCard(c);
        setDefaultMsg();
        
        gui.refreshCards();
        gui.updateMsgField("Recieving card...");
        Gmsg.command = GameMsg.cmd.ack;
    }
    
    public void handleInit(){
        
        //set values from server
        player.setId(Gmsg.id);
        player.setSuspect(player.getBoard().findSuspect(Gmsg.suspect));
        
        setDefaultMsg();
        
        //set message to send back
        Gmsg.name = "clientX";
        Gmsg.command = GameMsg.cmd.ack;
    
    }
    
    public String commandToString(){
        
        String s;
        
        switch (Gmsg.command){
            case init:{
                s = "init";
                break;
            }
            case start_turn:{
                s = "start_turn";
                break;
            }
            case reveal_card:{
                s = "reveal_card";
                break;
            }
            case send_card_server:{
                s = "send_card_server";
                break;
            }
            case kill_player:{
                s = "kill_player";
                break;
            }
            case game_over:{
                s = "game_over";
                break;
            }
            case board_state:{
                s = "board_state";
                break;
            }
            case stuck:{
                s = "stuck";
                break;
            }
            case update:{
                s = "update";
                break;
            }
            default:{
                s = "INVALID CMD: possibly forgot to add to this function";
            }
        }
        
        return s;
    }
    
    public void setDefaultMsg(){
        Gmsg.id = id;
        Gmsg.text = "Not set";
        Gmsg.name = "GUIThread";
        Gmsg.command = GameMsg.cmd.ack;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static runtime.clueless.networking.GameClient.Gmsg;

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
    
    //contructor
    public GuiThread(Socket client){
        this.socket = client;
        this.id = -1;   //init to negative until id is negotiated
        this.in = null;
        this.out = null;
        this.isFirstMsg = true;
        System.out.println("GuiThread object being created.");
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
        
        try {
            sendMove();
        } catch (IOException ex) {
            Logger.getLogger(GuiThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GuiThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GuiThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void waitForMsg(){
        
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
        if(isFirstMsg){
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
        switch (Gmsg.command){
            case start_turn:{
                break;
            }
            case reveal_card:{
                break;
            }
            case send_card_server:{
                break;
            }
            case kill_player:{
                break;
            }
            case game_over:{
                break;
            }
            case board_state:{
                //setBoardState();
                //TODO implement a way to set board state on GUI
                break;
            }
            default:{
                //bad_message
                System.out.println("Error recieved unsupported message type from server.");
                isValid = false;
            }
        }

        setDefaultMsg();

        //send ack or invalid message back to server
        if (isValid) {
            Gmsg.command = GameMsg.cmd.ack;
        } else {
            Gmsg.command = GameMsg.cmd.invalid;
        }

        try {
            out.writeObject(Gmsg);
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public String commandToString(){
        
        String s;
        
        switch (Gmsg.command){
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
            default:{
                s = "INVALID CMD: possibly forgot to add to this function";
            }
        }
        
        return s;
    }
    
    public void sendMove() throws IOException, ClassNotFoundException, InterruptedException{
        //out.println("MOVE MSG");
        Gmsg.text = "New message from client!!!";
        while(true){
            out.writeObject(Gmsg);
            //sleep(5);
        }
        //System.out.println(Gmsg.text);
    }
    
    public void sendAccusation(){
        //out.println("ACCUSATION MSG");
    }
    
    public void sendSuggestion(){
        //out.println("SUGGESTION MSG");
    }
    
    public void revealCard(){
        //out.println("REVEAL CARD MSG");
    }
    
    public void setDefaultMsg(){
        Gmsg.id = id;
        Gmsg.text = "Not set";
        Gmsg.name = "GUIThread";
    }
}

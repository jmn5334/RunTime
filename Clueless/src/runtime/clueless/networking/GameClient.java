/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmnew
 */
public class GameClient {
    
    private Socket socket;
    private GameMsg msg;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int id;
    private String name;
    private boolean isFirstMsg;
    
    public GameClient() {
        System.out.println("GameClient is being created.");
        socket = null;
        out = null;
        in = null;
        msg = new GameMsg();
        name = "client";
        id = -99;
        isFirstMsg = true;
    }

    public void connectToServer() {
        //Create socket connection
        try {
            socket = new Socket("10.0.0.201", 5000);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream()); //just uncommented had a hang here for some reason
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }
    
    public void waitForMsg(){
        
        boolean isValid = true;
        
        //read message
        try {
            //wait for object
            //while(!(in.available() > 0)){
                msg = (GameMsg) in.readObject();
            //}
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //grab id out of message if it's the first one
        if(isFirstMsg){
            id = msg.id;
            isFirstMsg = false;
        }
        //TODO: Add error handling for getting message with wrong id
        
        //print msg
        System.out.println("Client recieved message:");
        System.out.println(msg.text);
        System.out.println(commandToString());
        System.out.println();
        
        //check command
        switch (msg.command){
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
            msg.command = GameMsg.cmd.ack;
        } else {
            msg.command = GameMsg.cmd.invalid;
        }

        try {
            out.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void sendMove() throws IOException, ClassNotFoundException, InterruptedException{
        //out.println("MOVE MSG");
        msg.text = "New message from client!!!";
        while(true){
            out.writeObject(msg);
            //sleep(5);
        }
        //System.out.println(msg.text);
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
        msg.id = id;
        msg.text = "Not set";
        msg.name = name;
    }
    
    public String commandToString(){
        
        String s;
        
        switch (msg.command){
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
            default:{
                s = "INVALID CMD";
            }
        }
        
        return s;
    }
    
}

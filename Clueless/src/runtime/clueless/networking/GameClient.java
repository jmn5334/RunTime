/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import runtime.clueless.game.JPlayer;
import runtime.clueless.gui.fxml.MainGUIFXML;


/**
 *
 * @author jmnew
 */
public class GameClient {
    
    private Socket socket;
    private String host;
    private int port;
    private JPlayer p;
    
    //ref to GUI to pass to thread
    private final MainGUIFXML gui;
    
    //shared object
    public static GameMsg Gmsg;
    public static volatile int updateGUI;
    public static volatile int Gturn;
    
    //thread that acts on behalf of this class
    private GuiThread gThread;
    private Thread thread;
    
    public GameClient(JPlayer p, MainGUIFXML gui) {
        System.out.println("GameClient is being created.");
        socket = null;
        Gmsg = new GameMsg();
        thread = null;
        this.p = p;
        this.gui = gui;
        updateGUI = 0;
    }

    public void connectToServer() {
        //Create socket connection
        try {
            socket = new Socket("10.0.0.201", 5000);
            
            System.out.println("Connecting to server...");
            System.out.println("Creating thread...");
            
            //create thread with this socket
            gThread = new GuiThread(socket,p,gui);
            thread = new Thread(gThread);
            
            System.out.println("DONE thread creation.");
            
            System.out.println("Starting thread...");
            
            thread.start(); 

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }
    
    //send move using guithread
    public void sendMove(String dest, int destId, boolean isRoom){
        
        //set up message
        Gmsg.isRoom = isRoom;
        Gmsg.dest = dest;
        Gmsg.destId = destId;
        
        Gturn = 1;
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
    
    public void endTurn(){
        Gmsg.command = GameMsg.cmd.end_turn;
        Gturn = 1;
    }

}

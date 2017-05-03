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
import static runtime.clueless.networking.GameServer.msg;
import static runtime.clueless.networking.GameServer.turn;

/**
 *
 * @author jmnew
 */
public class ClientThread implements Runnable {
    
    private final Socket client;
    private int id;
    
    //contructor
    public ClientThread(Socket client, int id){
        this.client = client;
        this.id = id;
    }
    
    @Override
    public void run(){

        ObjectInputStream in;
        ObjectOutputStream out;
        
        in = null;
        out = null;
        
        try{
            
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            
        } catch (IOException e){
            System.out.println("in or out failed");
            System.exit(-1);
        }
        
        while(true){
            try{
                
                //wait for turn
                System.out.println("Thread "+Integer.toString(id)+" is waiting...");
                
                while(turn != id){
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                System.out.println("Thread "+Integer.toString(id)+" is doing stuff and things.");
                
                //send message to client
                out.writeObject(msg);

                //wait for object
                msg = (GameMsg) in.readObject();

                //recieve message from client and write to shared msg object
                //set turn to invalid value
                System.out.println("Thread "+Integer.toString(id)+" is ending turn.");
                turn = -99;
                
            } catch (IOException e){
                System.out.println("Read failed");
                //System.exit(-1);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

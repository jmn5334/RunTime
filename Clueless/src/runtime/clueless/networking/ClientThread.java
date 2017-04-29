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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static runtime.clueless.networking.GameServer.msg;

/**
 *
 * @author jmnew
 */
public class ClientThread implements Runnable {
    private final Socket client;
    //private GameMsg msg;
    //private String textArea;
    
    //contructor
    public ClientThread(Socket client){
        this.client = client;
        //msg = new GameMsg();
        //this.textArea = "";
    }
    
    @Override
    public void run(){
        String line;
        /*
        BufferedReader in;
        PrintWriter out;
        */
        ObjectInputStream in;
        ObjectOutputStream out;
        
        in = null;
        out = null;
        
        try{
            /*
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true); 
            */
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e){
            System.out.println("in or out failed");
            System.exit(-1);
        }
        
        while(true){
            try{
                /*
                //line = in.readLine();
                //send data back to client
                out.println(line);
                //append data to text area
                textArea += line;
                System.out.println(textArea);
                */
                msg = (GameMsg) in.readObject();
                //System.out.println(msg.text);
            } catch (IOException e){
                //System.out.println("Read failed");
                //System.exit(-1);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

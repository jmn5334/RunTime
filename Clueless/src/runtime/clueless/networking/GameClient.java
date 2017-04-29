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

/**
 *
 * @author jmnew
 */
public class GameClient {
    
    private Socket socket;
    //private PrintWriter out;
    //private BufferedReader in;
    private GameMsg msg;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public GameClient() {
        System.out.println("GameClient is being created.");
        socket = null;
        out = null;
        in = null;
        msg = new GameMsg();
    }

    public void connectToServer() {
        //Create socket connection
        try {
            socket = new Socket("10.0.0.201", 5000);
            /*
            out = new PrintWriter(socket.getOutputStream(),
                    true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            */
            //in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
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
    
    //test method
    public void sayHi(){
        //out.println("Hi buddy or my guy!!!");
    }
    
}

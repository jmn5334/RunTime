/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author jmnew
 */
public class GameClient {
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    public GameClient() {
        System.out.println("GameClient is being created.");
        socket = null;
        out = null;
        in = null;
    }

    public void listenSocket() {
        //Create socket connection
        try {
            socket = new Socket("10.0.0.201", 5000);
            out = new PrintWriter(socket.getOutputStream(),
                    true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
        while(true){
            out.println("Hi buddy!!!");
        }
    }
    
}

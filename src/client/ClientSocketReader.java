package client;

import javafx.application.Platform;

import java.io.*;
import java.net.*;

/**
 * Created by wiktortendera on 01/03/15.
 */
public class ClientSocketReader implements Runnable {
    private final Controller controller;
    public PrintWriter out;
    public BufferedReader in;

    public ClientSocketReader(Controller controller)  {
        this.controller = controller;
        String hostName = "192.168.1.102";
        int portNumber = 3001;
        try {
            Socket kkSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {


        try {
            String fromServer;
            out.println("register All " + Client.name);

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                final String finalFromServer = fromServer;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.parseLine(finalFromServer);
                    }
                });

                if (fromServer.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }
}

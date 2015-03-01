package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class KnockKnockProtocol {
    public String processInput(String theInput) {
        String theOutput = null;
        if(theInput == null)
            return null;
        String command = theInput.split(" ")[0];
        if(command.equals("register")) {
            String name = theInput.split(" ")[2];
            theOutput = getList();
        } else if (command.equals("send")){
            StringBuffer stringBuffer =  new StringBuffer("send ");
            String[] words = theInput.split(" ");
            for(int i = 2; i < words.length; i++) {
                stringBuffer.append(words[i] + " ");
            }
            theOutput = stringBuffer.toString();
        } else {
            theOutput = "Unknown command";
        }
        return theOutput;
    }

    public String getList () {
        StringBuffer buffer = new StringBuffer("list ");
        for(Server.SocketThread s : Server.threads) {
            buffer.append(s.name + " ");
        }
        return buffer.toString();
    }
}

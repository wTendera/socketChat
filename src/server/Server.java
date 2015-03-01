package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by wiktortendera on 01/03/15.
 */
public class Server {
    public static ArrayList<SocketThread> threads;
    public static KnockKnockProtocol kkp;

    public static class SocketThread implements Runnable {

        private Socket socket;
        private KnockKnockProtocol kkp;
        public PrintWriter out;
        public String name;

        public SocketThread(Socket socket, KnockKnockProtocol kkp) {
            this.socket = socket;
            this.kkp = kkp;
        }

        @Override
        public void run() {
            String inputLine, outputLine;
            out = null;
            BufferedReader in = null;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                 in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            try {
                while ((inputLine = in.readLine()) != null) {
                    isRegister(inputLine);
                    outputLine = kkp.processInput(inputLine);
                    Server.findReceivers(inputLine, outputLine);

                }
                Server.removeThread(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void isRegister(String inputLine) {
            if(inputLine != null && inputLine.split(" ")[0].equals("register")) {
                this.name = inputLine.split(" ")[2];
            }
        }

    }

    private static void findReceivers(String inputLine, String outputLine) {
        if(inputLine == null || inputLine.matches(""))
            return;
        String words[] = inputLine.split(" ");
        String toWho = words[1];
        String fromWho = words.length > 2 ? words[2] : "All";
        for(SocketThread socketThread : threads) {
            if(toWho.equals("All") || toWho.equals(socketThread.name) || fromWho.equals(socketThread.name))
                socketThread.out.println(outputLine);
        }
    }

    private static void removeThread(SocketThread socketThread) {
        threads.remove(socketThread);
        for (SocketThread s : threads) {
            s.out.println(kkp.getList());
        }
    }

    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(3001);
        threads = new ArrayList<>();
        kkp = new KnockKnockProtocol();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            SocketThread socketThread = new SocketThread(clientSocket, kkp);
            threads.add(socketThread);
            new Thread(socketThread).start();
        }
    }
}

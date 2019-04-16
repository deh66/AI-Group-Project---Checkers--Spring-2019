/**
 * 
 */
package ija.projekt.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;

/**
 * @author David Molnar
 */
public class Server extends NetworkMan {

    /**
     * 
     */
    protected ServerSocket ss;

    /**
     * 
     * @param port
     * @param name
     */
    public Server(int port, String name) {
        super(port, name);
    }

    /**
     * 
     */
    @Override
    public void connect() {
        if (this.connectThread != null && this.receiveThread.isAlive()) {
            return;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    ss = new ServerSocket(port);
                    s = ss.accept();
                    in = new BufferedReader(new InputStreamReader(
                            s.getInputStream()));
                    out = new PrintStream(s.getOutputStream());
                    receive();
                } catch (IOException ex) {
                    handleExceptions(ex);
                }
            }
        };

        this.connectThread = new Thread(r);
        this.connectThread.start();
    }

    /**
     * 
     */
    @Override
    public void dispose() {
        super.dispose();

        try {
            if (ss != null) {
                ss.close();
            }
        } catch (IOException ex) {
            handleExceptions(ex);
        }
    }

    /**
     * 
     * @param message
     * @return
     */
    protected boolean parseRequest(String message) {
        String par = parseCommand(REQUEST, message);
        if (par != null) {
            sendAccept(par + " trying to connect...");
            this.opponentName = par;
            return true;
        }

        return false;
    }

    /**
     * 
     */
    public void setAccepted() {
        send(NetworkMan.ACCEPTED + this.name);

        sendConnected("Connected to " + this.opponentName);
    }

}

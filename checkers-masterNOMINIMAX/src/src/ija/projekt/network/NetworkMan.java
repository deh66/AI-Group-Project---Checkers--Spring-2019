package ija.projekt.network;

import ija.projekt.basis.Position;
import ija.projekt.basis.Step;
import ija.projekt.players.NetworkPlayer.NetworkPlayerInitListener;
import ija.projekt.players.NetworkPlayer.NetworkPlayerReceiveListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author David Molnar
 */
public abstract class NetworkMan {

    /**
     * 
     */
    protected static final String ACCEPTED = "accepted";
    
    /**
     * 
     */
    protected static final String CLOSED = "closed";
    
    /**
     * 
     */
    protected static final String REQUEST = "request";
    
    /**
     * 
     */
    protected static final String SEPARATOR = "-";

    /**
     * 
     */
    protected Thread connectThread;
    
    /**
     * 
	 */
    protected boolean created;

    /**
     * 
     */
    protected BufferedReader in;
    
    /**
     * 
     */
    protected List<NetworkPlayerInitListener> initListeners = new ArrayList<NetworkPlayerInitListener>();

    /**
     * 
	 */
    protected String name;
    
    /**
     * 
     */
    protected String opponentName;

    /**
     * 
     */
    protected PrintStream out;
    
    /**
     * 
     */
    protected int port;
    
    /**
     * 
     */
    protected List<NetworkPlayerReceiveListener> receiveListeners = new ArrayList<NetworkPlayerReceiveListener>();

    /**
     * 
     */
    protected Thread receiveThread;
    
    /**
     * 
     */
    protected Socket s;
    
    /**
     * 
     * @param port
     * @param name
     */
    public NetworkMan(int port, String name) {
        this.port = port;
        this.name = name;
    }

    /**
     * 
     * @param toAdd
     */
    public void addInitListener(NetworkPlayerInitListener toAdd) {
        initListeners.add(toAdd);
    }

    /**
     * 
     * @param toAdd
     */
    public void addReceiveListener(NetworkPlayerReceiveListener toAdd) {
        receiveListeners.add(toAdd);
    }

    
    /**
     * 
     */
    public abstract void connect();

    /**
     * 
     */
    public void dispose() {
        try {
            if (out != null) {
                out.close();
            }

            if (in != null) {
                in.close();
            }

            if (s != null) {
                s.close();
            }
        } catch (IOException ex) {
            handleExceptions(ex);
        }

        if (this.connectThread != null) {
            this.connectThread.interrupt();
        }

        if (this.receiveThread != null) {
            this.receiveThread.interrupt();
        }
    }

    /**
     * 
	 * @return
	 */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @return
     */
    public String getOponentName() {
        return this.opponentName;
    }

    /**
     * 
     * @param ex
     */
    protected void handleExceptions(Exception ex) {
        String message = ex.getMessage();

        if (ex instanceof UnknownHostException) {
            message = "Unknown host: " + message;
        }

        if (ex instanceof BindException) {
            message = "Port (" + this.port + ") already in use";
        }

        for (NetworkPlayerInitListener npl : initListeners) {
            npl.errorHandler(message);
        }

        for (NetworkPlayerReceiveListener npl : receiveListeners) {
            npl.errorHandler("Fatal error: opponent not responding. Game closed.");
        }

        ex.printStackTrace();
    }

    /**
     * 
	 * @return
	 */
    public boolean isCreated() {
        return this.created;
    }

    /**
     * 
     * @param command
     * @param message
     * @return
     */
    protected String parseCommand(String command, String message) {
        String cmd = message.substring(0, command.length());
        String par = message.substring(command.length());
        if (cmd.equals(command)) {
            return par;
        }

        return null;
    }

    /**
     * 
     * @param message
     */
    protected void parseInput(String message) {
        if (!this.created) {
            if (this instanceof Server) {
                Server server = (Server) this;
                if (!server.parseRequest(message)) {
                    return;
                }
            } else {
                Client client = (Client) this;
                if (!client.parseAccepted(message)) {
                    return;
                }
            }
        } else {
            /*
             * if (message.equals(CLOSED)) { dispose(); return; }
             */

            Step step = parseStep(message);

            if (step != null) {
                sendReceived(step);
            }
        }
    }

    /**
     * 
     * @param step
     * @return
     */
    protected String parseStep(Step step) {
        if (step == null) {
            return null;
        }

        Position from = step.getFrom();
        Position to = step.getTo();

        return from.getImagCol() + SEPARATOR + from.getImagRow() + SEPARATOR
                + to.getImagCol() + SEPARATOR + to.getImagRow();
    }

    /**
     * 
     * @param message
     * @return
     */
    protected Step parseStep(String message) {
        if (message == null) {
            return null;
        }

        String parts[] = message.split(SEPARATOR);

        if (parts.length == 4) {
            int fromR = -1;
            int toR = -1;

            try {
                fromR = Integer.parseInt(parts[1]);
                toR = Integer.parseInt(parts[3]);
            } catch (NumberFormatException ex) {
                return null;
            }

            Position from = new Position(null, parts[0].charAt(0), fromR, true);
            Position to = new Position(null, parts[2].charAt(0), toR, true);

            if (from != null && to != null) {
                return new Step(from, to);
            }
        }

        return null;
    }

    /**
     * 
     */
    public void receive() {
        if (this.receiveThread != null && this.receiveThread.isAlive()) {
            return;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                String message = "";

                try {
                    message = in.readLine();
                } catch (IOException ex) {
                    handleExceptions(ex);
                    return;
                }

                parseInput(message);
            }
        };

        this.receiveThread = new Thread(r);
        this.receiveThread.start();
    }

    /**
     * 
     * @param step
     */
    public void send(Step step) {
        send(parseStep(step));
    }

    /**
     * 
     * @param message
     */
    public void send(String message) {
        out.print(message + "\n");
        out.flush();
    }

    /**
     * 
     * @param status
     */
    protected void sendAccept(String status) {
        for (NetworkPlayerInitListener npl : initListeners) {
            npl.accept(status);
        }
    }

    /**
     * 
     * @param status
     */
    protected void sendConnected(String status) {
        this.created = true;

        for (NetworkPlayerInitListener npl : initListeners) {
            npl.connected(status);
        }
    }

    /**
     * 
     * @param step
     */
    protected void sendReceived(Step step) {
        for (NetworkPlayerReceiveListener npl : receiveListeners) {
            npl.received(step);
        }
    }
}

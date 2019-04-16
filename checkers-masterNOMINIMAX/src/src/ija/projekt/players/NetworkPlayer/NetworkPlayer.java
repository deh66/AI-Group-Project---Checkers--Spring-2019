/**
 * 
 */
package ija.projekt.players.NetworkPlayer;

import ija.projekt.basis.Position;
import ija.projekt.basis.Step;
import ija.projekt.game.Player;
import ija.projekt.network.NetworkMan;
import ija.projekt.network.Server;

/**
 * @author David Molnar
 */
public class NetworkPlayer extends Player implements
        NetworkPlayerReceiveListener, NetworkPlayerInitListener {

    /**
     * 
     */
    protected String lastErrorMessage;
    
    /**
     * 
	 */
    protected NetworkMan nm;

    /**
     * 
     * @param white
     */
    public NetworkPlayer(boolean white) {
        super(white);
        this.lastErrorMessage = "";
    }

    /**
     * 
     * @param white
     * @param nm
     */
    public NetworkPlayer(boolean white, NetworkMan nm) {
        super(white);

        this.nm = nm;
        if (this.nm != null) {
            this.nm.addReceiveListener(this);
            this.nm.addInitListener(this);
        }
    }

    /**
     * 
     * @param newPlayer
     */
    public NetworkPlayer(NetworkPlayer newPlayer) {
        super(newPlayer);

        this.nm = newPlayer.nm;
        this.lastErrorMessage = newPlayer.lastErrorMessage;
    }

    /**
     * 
     */
    @Override
    public void accept(String status) {
        // TODO Auto-generated method stub

    }

    /**
     * 
     */
    public void cancel() {
        dispose();
    }

    /**
     * 
     */
    public void connect() {
        if (this.nm != null) {
            this.nm.connect();
        }
    }

    /**
     * 
     */
    @Override
    public void connected(String status) {
        setName(getOponentName());
    }

    /**
     * 
     */
    @Override
    public void dispose() {
        if (this.nm != null) {
            this.nm.dispose();
        }
    }

    /**
     * 
     */
    @Override
    public void errorHandler(String message) {
        this.lastErrorMessage = message;
        refreshGui();
    }

    /**
     * 
     * @return
     */
    public String getLastError() {
        return this.lastErrorMessage;
    }

    
    /**
     * 
     */
    public String getNameFromNetwork() {
        if (this.nm != null) {
            return this.nm.getName();
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public String getOponentName() {
        if (this.nm != null) {
            return this.nm.getOponentName();
        }
        return null;
    }

    /**
     * 
     */
    @Override
    public void received(Step step) {
        if (step != null) {
            Position from = this.game.getPositionAt(step.getFrom());
            Position to = this.game.getPositionAt(step.getTo());

            doMove(from, to);
        }
    }

    /**
     * 
     */
    public void setAccepted() {
        if (this.nm != null && this.nm instanceof Server) {
            Server server = (Server) this.nm;
            server.setAccepted();
        }
    }

    /**
     * 
     */
    @Override
    public void yourTurn() {
        Step lastStep = game.getLastStep();

        if (lastStep != null && this.nm != null) {
            this.nm.send(lastStep);
        }

        if (this.nm != null) {
            this.nm.receive();
        }
    }
}

/**
 * 
 */
package ija.projekt.game;

import ija.projekt.basis.Position;
import ija.projekt.basis.Step;
import ija.projekt.gui.GuiUpdate.GuiUpdate;

/**
 * @author David Molnar
 */
public abstract class Player extends GuiUpdate {

    /**
     * 
	 */
    protected Game game;
    
    /**
     * 
	 */
    protected boolean isInteractive;
    
    /**
     * 
     */
    protected String name;

    /**
     * 
	 */
    protected boolean white;

    /**
     * 
     * @param white
     */
    public Player(boolean white) {
        this.white = white;
        this.isInteractive = false;
    }

    /**
     * 
     * @param newPlayer
     */
    public Player(Player newPlayer) {
        this(newPlayer.white);
        this.game = newPlayer.game;
        this.isInteractive = newPlayer.isInteractive;
        this.name = newPlayer.name;
    }

    /**
     * 
     */
    public abstract void dispose();

    /**
     * 
     * @param from
     * @param to
     */
    public void doMove(Position from, Position to) {
        if (this.game != null) {
            this.game.doMove(from, to);
        }
    }
    
    /**
     * 
     * @param step
     */
    public void doMove(Step step) {
        if (this.game != null) {
            this.game.doMove(step.getFrom(), step.getTo());
        }
    }
    
    /**
     * 
     * @return
     */
    public Game getGame() {
    	return this.game;
    }

    /**
     * 
     * @return
     */
    public boolean isBlack() {
        return !this.white;
    }

    /**
     * 
	 * @return
	 */
    public boolean isInteractive() {
        return this.isInteractive;
    }

    /**
     * 
	 * @return
	 */
    public boolean isWhite() {
        return this.white;
    }

    /**
     * 
     * @param game
     */
    public void setGame(Game game) {
    	this.game = game;
    }

    /**
     * 
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     */
    @Override
    public String toString() {
        String result = this.isWhite() ? "white" : "black";

        if (this.name != null) {
            result = this.name + " (" + result + ")";
        }
        return result;
    }
    
    /**
     * 
     */
    public abstract void yourTurn();
}

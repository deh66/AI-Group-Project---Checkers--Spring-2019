/**
 * 
 */
package ija.projekt.players;

import ija.projekt.game.Player;

/**
 * @author David Molnar
 */
public class HumanPlayer extends Player {

    /**
     * 
     * @param white
     */
    public HumanPlayer(boolean white) {
        super(white);
        this.name = "Human";
        this.isInteractive = true;
    }

    /**
     * 
     * @param newPlayer
     */
    public HumanPlayer(HumanPlayer newPlayer) {
        super(newPlayer);
        this.name = newPlayer.name;
        this.isInteractive = newPlayer.isInteractive;
    }

    /**
     * 
     */
    @Override
    public void dispose() {

    }

    /**
     * 
     */
    @Override
    public void yourTurn() {

    }
}

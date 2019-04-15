/**
 * 
 */
package ija.projekt.players.NetworkPlayer;

import ija.projekt.basis.Step;

/**
 * 
 * @author David Molnar
 */
public interface NetworkPlayerReceiveListener {

    /**
     * 
     * @param message
     */
    public void errorHandler(String message);

    /**
     * 
     * @param step
     */
    public void received(Step step);

}
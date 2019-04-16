/**
 * 
 */
package ija.projekt.players.NetworkPlayer;

/**
 * 
 * @author David Molnar
 */
public interface NetworkPlayerInitListener {

    /**
     * 
     * @param status
     */
    public void accept(String status);

    /**
     * 
     * @param status
     */
    public void connected(String status);

    /**
     * 
     * @param message
     */
    public void errorHandler(String message);

}

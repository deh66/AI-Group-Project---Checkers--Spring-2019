/**
 * 
 */
package ija.projekt.log;

import ija.projekt.game.Game;

import java.io.File;

/**
 * @author David Molnar
 */
public interface IFileLog {

    /**
     * 
     * @param file
     * @return
     */
    public abstract Game open(File file);

    /**
     * 
     * @param file
     * @param game
     * @return
     */
    public abstract boolean save(File file, Game game);
}

/**
 * 
 */
package ija.projekt.gui.GuiUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author David Molnar
 */
public class GuiUpdate {

    /**
     * 
     */
    protected List<GuiUpdateListener> listeners = new ArrayList<GuiUpdateListener>();

    /**
     * 
     * @param toAdd
     */
    public void addListener(GuiUpdateListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * 
     */
    public void refreshGui() {
        for (GuiUpdateListener listener : listeners) {
            listener.refreshGui();
        }
    }
}

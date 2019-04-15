/**
 * 
 */
package ija.projekt.replay;

import ija.projekt.game.Game;
import ija.projekt.gui.GamePanel;
import ija.projekt.gui.replay.ReplyListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

/**
 * @author David Molnar
 */
public class Replay {

    /**
     * 
	 */
    public enum ReplayStatus {
        /**
         * 
		 */
        CANNOT_OPEN, 
        /**
         * 
		 */
        OK
    }
    
    /**
     * 
     */
    protected DefaultComboBoxModel<GameReplay> gameCBModel = new DefaultComboBoxModel<GameReplay>();

    /**
     * 
     */
    protected List<ReplyListener> listeners = new ArrayList<ReplyListener>();

    /**
     * 
     */
    protected boolean replayMode;
    
    /**
     * 
	 */
    protected GamePanel replayPanel;

    /**
     * 
	 */
    protected ReplayStatus status;

    /**
     * 
     */
    protected int stepDelay;

    /**
     * 
	 */
    protected boolean stepping;

    /**
     * 
     */
    protected Thread thread;

    /**
     * 
     */
    public Replay() {
        this.replayMode = false;
        this.stepping = false;
        this.status = ReplayStatus.OK;

        this.replayPanel = new GamePanel(null);
        this.replayPanel.setInteractivity(false);
        this.replayPanel.setName("replaypanel");
    }

    /**
     * 
     * @param game
     */
    public void addGame(Game game) {
        if (game != null) {
            GameReplay newGame = new GameReplay(game);
            this.gameCBModel.addElement(newGame);
            this.gameCBModel.setSelectedItem(newGame);

            setSelectedGame(newGame);
        } else {
            status = ReplayStatus.CANNOT_OPEN;
            replayRefreshGui();
        }
    }

    /**
     * 
     * @param listener
     */
    public void addListener(ReplyListener listener) {
        this.listeners.add(listener);
    }

    /**
     * 
     */
    public void displayedStatus() {
        this.status = ReplayStatus.OK;
    }

    /**
     * 
     * @return
     */
    public DefaultComboBoxModel<GameReplay> getComboBoxModel() {
        return this.gameCBModel;
    }

    /**
     * 
	 * @return
	 */
    public GamePanel getReplayPanel() {
        return this.replayPanel;
    }

    /**
     * 
     * @return
     */
    public GameReplay getSelectedGame() {
        Object obj = this.gameCBModel.getSelectedItem();
        if (obj instanceof GameReplay) {
            // status = ReplayStatus.OK;
            return (GameReplay) obj;
        }
        return null;
    }

    /**
     * 
	 * @return
	 */
    public ReplayStatus getStatus() {
        return this.status;
    }

    /**
     * 
     * @param ms
     * @return
     */
    public int getStepDelay(int ms) {
        return this.stepDelay;
    }

    /**
     * 
     */
    public void init() {
        replayRefreshGui();
    }

    /**
     * 
     * @return
     */
    public boolean isActive() {
        return this.replayMode;
    }

    /**
     * 
	 * @return
	 */
    public boolean isStepping() {
        return this.stepping;
    }

    /**
     * 
     * @param game
     */
    public void nextStep(GameReplay game) {
        if (game == null) {
            return;
        }

        game.nextStep();
        replayRefreshGui();
    }

    /**
     * 
     * @param game
     */
    public void removeGame(Game game) {
        if (game != null) {
            this.gameCBModel.removeElement(game);

            int index = this.gameCBModel.getSize();
            Game lastGame = null;
            if (index >= 0) {
                lastGame = this.gameCBModel.getElementAt(index - 1);
            }

            setSelectedGame(lastGame);
        }
    }

    /**
     * 
     */
    public void replayModeEnded() {
        for (ReplyListener listener : listeners) {
            listener.replayModeEnded();
        }
    }

    /**
     * 
     */
    public void replayModeStarted() {
        for (ReplyListener listener : listeners) {
            listener.replayModeStarted();
        }
    }

    /**
     * 
     */
    public void replayRefreshGui() {
        replayPanel.refresh();

        for (ReplyListener listener : listeners) {
            listener.replayRefreshGui();
        }
    }

    /**
     * 
     * @param game
     */
    public void setSelectedGame(Game game) {
        this.gameCBModel.setSelectedItem(game);
        this.replayPanel.setGame(game);

        replayRefreshGui();
    }

    /**
     * 
	 * @param ms
	 */
    public void setStepDelay(int ms) {
        this.stepDelay = ms;
    }

    /**
     * 
     */
    public void startReplayMode() {
        this.replayMode = true;

        // replayRefreshGui();
        replayModeStarted();
    }

    /**
     * 
     * @param game
     */
    public void stepBack(GameReplay game) {
        if (game == null) {
            return;
        }

        game.stepBack();
        replayRefreshGui();
    }

    /**
     * 
     * @param game
     * @param step
     */
    public void stepTo(final GameReplay game, final int step) {
        if (game == null) {
            return;
        }

        if (step < 0 || step >= game.getStepCount()) {
            return;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    stepping = true;

                    boolean way = game.getActualStepIndex() < step;

                    while (stepping && game.getActualStepIndex() != step) {
                        if (way) {
                            game.nextStep();
                        } else {
                            game.stepBack();
                        }

                        replayRefreshGui();
                        Thread.sleep(stepDelay);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    stepping = false;
                } finally {
                    stepping = false;
                    replayRefreshGui();
                }
            }
        };

        this.thread = new Thread(r);
        this.thread.start();
    }

    /**
     * 
     */
    public void stopReplaying() {
        this.stepping = false;
        replayRefreshGui();
    }

    /**
     * 
     */
    public void stopReplayMode() {
        this.replayMode = false;

        if (this.thread != null) {
            this.thread.interrupt();
        }

        replayRefreshGui();
        replayModeEnded();
    }
}

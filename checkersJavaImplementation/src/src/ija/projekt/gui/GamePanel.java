/**
 * 
 */
package ija.projekt.gui;

import ija.projekt.basis.Round;
import ija.projekt.game.Game;
import ija.projekt.replay.GameReplay;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author David Molnar
 */
public class GamePanel extends JPanel {

    /**
     * 
	 */
    protected Game game;
    
    /**
     * 
	 */
    protected GameBoard gameBoard;

    /**
     * 
     */
    protected JList<Round> logList;

    /**
	 */
    protected String name;

    /**
     * Create the panel.
     */
    public GamePanel(Game game) {
        setBorder(new EmptyBorder(0, 1, 0, 0));
        this.game = game;

        this.setLayout(new BorderLayout());

        this.gameBoard = new GameBoard(this.game);
        this.logList = createLogList();

        this.add(gameBoard, BorderLayout.CENTER);
        this.add(createScrollBar(this.logList), BorderLayout.EAST);

        this.setVisible(false);

        refresh();
    }

    /**
     * 
     * @return
     */
    protected JList<Round> createLogList() {
        DefaultListModel<Round> logModel = new DefaultListModel<Round>();
        JList<Round> logList = new JList<Round>(logModel);

        logList.setBorder(new EmptyBorder(0, 0, 0, 0));
        logList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // logList.setPreferredSize(new Dimension(120, this.getHeight()));

        return logList;
    }

    /**
     * 
     * @param list
     * @return
     */
    protected JScrollPane createScrollBar(JList<Round> list) {
        JScrollPane sp = new JScrollPane(list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(120, this.getHeight()));
        return sp;
    }

    /**
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GamePanel) {
            GamePanel gp = (GamePanel) obj;
            if (this.name.equals(gp.name)) {
                return true;
            }
        }
        return false;
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
    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    /**
     * 
	 * @return
	 */
    @Override
    public String getName() {
        return this.name;
    }
    
    /**
     * 
     */
    public void refresh() {
        this.gameBoard.repaint();
        refreshLog();
    }

    /**
     * 
     */
    private void refreshLog() {
        if (this.game == null) {
            return;
        }

        java.util.List<Round> rounds = this.game.getRounds();
        DefaultListModel<Round> logModel = (DefaultListModel<Round>) (logList
                .getModel());

        logModel.clear();

        for (Round round : rounds) {
            logModel.addElement(round);
        }

        if (this.game instanceof GameReplay) {
            GameReplay gr = (GameReplay) this.game;
            int index = gr.getActualStepIndex() / 2;
            this.logList.setSelectedIndex(index);
        }

        this.logList.revalidate();
    }

    /**
     * 
	 * @param game
	 */
    public void setGame(Game game) {
        this.game = game;
        this.gameBoard.setGame(game);
        refresh();
    }

    /**
     * 
     * @param mode
     */
    public void setInteractivity(boolean mode) {
        if (this.gameBoard != null) {
            this.gameBoard.setInteractivity(mode);
        }
    }

    /**
	 * @param name
	 */
    @Override
    public void setName(String name) {
        this.name = name;
    }
}

/**
 * 
 */
package ija.projekt.gui;

import ija.projekt.basis.Step;
import ija.projekt.game.Game;
import ija.projekt.game.Game.StepStatus;
import ija.projekt.game.Player;
import ija.projekt.gui.GuiUpdate.GuiUpdateListener;
import ija.projekt.gui.replay.ReplayDialog;
import ija.projekt.gui.replay.ReplyListener;
import ija.projekt.log.BasicNotationLog;
import ija.projekt.log.IFileLog;
import ija.projekt.log.XMLLog;
import ija.projekt.players.ComputerPlayer;
import ija.projekt.players.HumanPlayer;
import ija.projekt.players.NetworkPlayer.NetworkPlayer;
import ija.projekt.replay.GameReplay;
import ija.projekt.replay.Replay;
import ija.projekt.replay.Replay.ReplayStatus;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

/**
 * @author  David Molnar
 */
public class ApplicationWindow extends JFrame implements ActionListener,
        GuiUpdateListener, ReplyListener {

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look
            // and feel.
        }

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        ApplicationWindow frame = new ApplicationWindow();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Display the window.
        frame.setVisible(true);
    }

    /**
     * 
     */
    public static void run() {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * 
     */
    protected JPanel containerPanel = new JPanel();
    
    /**
     * 
     */
    protected JFileChooser fc;

    /**
     * 
	 */
    protected Game game;
    
    /**
     * 
     */
    protected JMenu gameMenu;
    
    /**
     * 
	 */
    protected GamePanel gamePanel;

    /**
     * 
	 */
    protected Replay replay;
    
    /**
     * 
     */
    protected JMenuItem replayStartMenuItem;
    
    /**
     * 
     */
	protected JMenuItem replayStopMenuItem;

	/**
	 * 
	 */
    protected File saveFile;

    /**
     * 
     */
    public ApplicationWindow() {
        super("Checkers");

        setBounds(0, 0, 1024, 768);

        addWindowListener(new WindowAdapter() { 
            @Override
            public void windowClosing(WindowEvent e) { exit(); } } );
         
        this.containerPanel.setLayout(new BorderLayout());
        getContentPane().add(containerPanel, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());

        JPanel statusBar = createStatusBar();
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        this.fc = createFileChooser();

        this.replay = new Replay();
        ReplayDialog rd = new ReplayDialog(this, this.replay);

        this.replay.addListener(this);
        this.replay.addListener(rd);
        this.replay.init();

        setStatusBar("Select \"Game -> New\" to start new game.");

        createGame(new ComputerPlayer(false));
    }

    /**
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("quit".equals(e.getActionCommand())) {
            exit();
        } else if ("about".equals(e.getActionCommand())) {
            showAbout();
        } else if ("new_human".equals(e.getActionCommand())) {
            createGame(new HumanPlayer(false));
        } else if ("new_computer".equals(e.getActionCommand())) {
            createGame(new ComputerPlayer(false));
        } else if ("new_network".equals(e.getActionCommand())) {
            createNetworkGame();
        } else if ("close".equals(e.getActionCommand())) {
            confirmClose();
        } else if ("save".equals(e.getActionCommand())) {
            saveGame();
        } else if ("save_as".equals(e.getActionCommand())) {
            saveAsGame();
        } else if ("open".equals(e.getActionCommand())) {
            openGame();
        } else if ("hint".equals(e.getActionCommand())) {
            showHint();
        } else if ("replay_start".equals(e.getActionCommand())) {
            setReplayMode(true);
        } else if ("replay_stop".equals(e.getActionCommand())) {
            setReplayMode(false);
        }
    }

    /**
     * 
     */
    protected void closeGame() {
        if (this.gamePanel != null) {
            if (this.game != null) {
                this.game.dispose();
            }

            this.game = null;
            this.gamePanel = null;
            this.saveFile = null;

            setGamePanel(this.gamePanel);

            setStatusBar("Game closed.");
            setPlayerBar("");
            this.setTitle("Game closed.");
            this.repaint();
        }
    }

    /**
     * 
     * @return
     */
    protected boolean confirmClose() {
        if (confirmSave()) {
            closeGame();
            return true;
        }

        return false;
    }

    /**
     * 
     * @return
     */
    protected boolean confirmSave() {
        if (this.game != null && this.game.isDirty()) {
            int n = JOptionPane.showOptionDialog(this,
                    "Do you want to save the game before closing?",
                    "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (n == JOptionPane.YES_OPTION) {
                saveGame();
                closeGame();
                return true;
            } else if (n == 1) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * 
     * @return
     */
    protected JFileChooser createFileChooser() {
        JFileChooser fc = new JFileChooser("../examples");
        fc.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".xml")
                        || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Checker XML files";
            }
        });

        fc.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".dat")
                        || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Checker specific dat files";
            }
        });

        return fc;
    }

    /**
     * 
     * @param player2
     */
    protected void createGame(Player player2) {
        if (!confirmClose()) {
            return;
        }

        startGame(new Game(new HumanPlayer(player2.isBlack()), player2));
    }

    /**
     * 
     * @return
     */
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        JMenuItem menuItem;

        // File -> Quit
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // Game
        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menu.setName("game");
        menuBar.add(menu);
        gameMenu = menu;

        // Game -> New
        JMenu menu2 = new JMenu("New");
        menu2.setMnemonic(KeyEvent.VK_G);
        menu.add(menu2);

        // Game -> New -> New Game with Human
        menuItem = new JMenuItem("Human");
        menuItem.setMnemonic(KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new_human");
        menuItem.addActionListener(this);
        menu2.add(menuItem);

        // Game -> New -> New Game with Human
        menuItem = new JMenuItem("Computer");
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new_computer");
        menuItem.addActionListener(this);
        menu2.add(menuItem);

        // Game -> New -> New Game with Human
        menuItem = new JMenuItem("Network");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new_network");
        menuItem.addActionListener(this);
        menu2.add(menuItem);

        // Game -> Open
        menuItem = new JMenuItem("Open");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("open");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // Game -> Save
        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("save");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // Game -> Save as
        menuItem = new JMenuItem("Save as");
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("save_as");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // Game -> Close
        menuItem = new JMenuItem("Close");
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("close");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // Game -> Hint
        menuItem = new JMenuItem("Hint");
        menuItem.setMnemonic(KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("hint");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // Replay
        menu = new JMenu("Replay");
        menu.setMnemonic(KeyEvent.VK_R);
        menuBar.add(menu);

        menuItem = new JMenuItem("Start");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("replay_start");
        menuItem.addActionListener(this);
        menuItem.setName("replay_start");
        replayStartMenuItem = menuItem;
        menu.add(menuItem);

        menuItem = new JMenuItem("Stop");
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("replay_stop");
        menuItem.addActionListener(this);
        menuItem.setName("replay_stop");
        replayStopMenuItem = menuItem;
        menu.add(menuItem);

        // Help
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);

        // Help -> About
        menuItem = new JMenuItem("About");
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                ActionEvent.ALT_MASK));
        menuItem.setActionCommand("about");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

    /**
     * 
     */
    protected void createNetworkGame() {
        if (!confirmClose()) {
            return;
        }

        NetworkGameDialog ngd = new NetworkGameDialog(this);
        Game game = ngd.showDialog();

        if (game != null) {
            startGame(game);
        }
    }

    /**
     * 
     * @return
     */
    protected JPanel createStatusBar() {
        JPanel statusPanel = new JPanel();
        statusPanel.setName("statusPanel");

        statusPanel
                .setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        statusPanel.setPreferredSize(new Dimension(this.getWidth(), 40));
        statusPanel.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel statusLabel = new JLabel("status");
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        statusLabel.setName("statusLabel");
        statusPanel.add(statusLabel);

        JLabel playerLabel = new JLabel("");
        playerLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        playerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        playerLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        playerLabel.setName("playerLabel");
        statusPanel.add(playerLabel);

        return statusPanel;
    }

    /**
     * 
     */
    public void exit() {
        if (confirmClose()) {
            this.dispose();
        } else {
            
        }
    }

    /**
     * 
     */
    protected void openGame() {
        if (!confirmClose()) {
            return;
        }

        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fc.getSelectedFile();
        // File file = new File("c:\\users\\dimitrij\\desktop\\abc.xml");

        IFileLog fileLog = file.getName().toLowerCase().endsWith(".dat") ? new BasicNotationLog()
                : (file.getName().toLowerCase().endsWith(".xml") ? new XMLLog()
                        : new BasicNotationLog());

        Game newGame = Game.open(file, fileLog);

        if (newGame != null) {
            startGame(newGame);
            this.saveFile = file;
            setStatusBar("Game opened.");

            if (newGame.getPlayer2() instanceof NetworkPlayer) {
                showWarningDialog(
                        "You can open a network game file, but can not continue playing.",
                        "Warning");
                setPlayerBar("");
            }
        } else {
            setStatusBar("Game can not be opened.");
        }
    }

    /**
     * 
     */
    @Override
    public void refreshGui() {
        if (this.gamePanel != null) {
            this.gamePanel.refresh();
        }

        if (this.game != null) {
            Player player = this.game.getCurrentPlayer();

            if (player != null) {
                setPlayerBar(player.toString() + " player's turn");
            } else {
                setPlayerBar("");
            }

            String message = "";

            StepStatus status = this.game.getLastStepStatus();

            if (status != null) {
                switch (status) {
                case POS_FIG_NULL:
                    message = "Can not move this position.";
                    break;
                case NO_FIGURE:
                    message = "There is no figure at position.";
                    break;
                case NOT_CURRENT_PLAYER:
                    message = "You are not the current player.";
                    break;
                case CANT_MOVE:
                    message = "This figure can not move to this position.";
                    break;
                case MUST_MOVE:
                    message = "There is a move you must do!";
                    break;
                case NOT_DEFINED:
                    message = "Unknonwn error.";
                    break;
                case OK:
                    Step lastStep = this.game.getLastStep();
                    if (lastStep != null)
                        message = "Last step: " + lastStep.toString();
                    break;
                }
            }

            if (player != null && player instanceof NetworkPlayer) {
                NetworkPlayer np = (NetworkPlayer) player;
                String errorMessage = np.getLastError();

                if (errorMessage != null && errorMessage.equals("") == false) {
                    message = errorMessage;
                    game.setFinished(true);
                }
            }

            if (this.game.getFinished()) {
                Player winner = this.game.getWinner();
                message = "Finished: " + winner.toString() + " won.";
                setPlayerBar("");
                showInfoDialog(message, "Information");
            }

            setStatusBar(message);
            this.setTitle(game.toString());
        } else {
            setStatusBar("");
            setPlayerBar("");
        }
    }

    /**
     * 
     */
    @Override
    public void replayModeEnded() {
        setGamePanel(this.gamePanel);
        setStatusBar("Replay Mode has been turned off.");
        setPlayerBar("");
    }

    /**
     * 
     */
    @Override
    public void replayModeStarted() {
        setGamePanel(this.replay.getReplayPanel());
        setPlayerBar("Replay Mode ON");
    }

    /**
     * 
     */
    @Override
    public void replayRefreshGui() {
        boolean active = this.replay.isActive();

        gameMenu.setEnabled(!active);
        replayStartMenuItem.setEnabled(!active);
        replayStopMenuItem.setEnabled(active);

        GameReplay gr = this.replay.getSelectedGame();

        String status = "";

        if (gr != null) {
            Step s = gr.getActualStep();
            int stepCount = gr.getStepCount();
            int actualStepIndex = gr.getActualStepIndex() + 1;

            status = "Last step: " + (s != null ? s.toString() : "<none>");

            if (s != null) {
                status += " [" + actualStepIndex + "/" + stepCount + "]";
            }
        }

        if (this.replay.isStepping()) {
            status = "[Stepping] " + status;
        }

        ReplayStatus rs = this.replay.getStatus();

        if (rs == ReplayStatus.CANNOT_OPEN) {
            status = "Can not open file: can not understand format.";
        }

        this.replay.displayedStatus();

        setStatusBar(status);

        this.getContentPane().repaint();
    }

    /**
     * 
     */
    protected void saveAsGame() {
        if (this.game != null
                && fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.saveFile = fc.getSelectedFile();
            IFileLog fileLog = this.saveFile.getName().toLowerCase()
                    .endsWith(".dat") ? new BasicNotationLog() : (this.saveFile
                    .getName().toLowerCase().endsWith(".xml") ? new XMLLog()
                    : new BasicNotationLog());
            this.game.setFileType(fileLog);
            saveGame();
        }
    }

    /**
     * 
     */
    protected void saveGame() {
        if (this.saveFile != null) {
            if (this.game != null && this.game.save(this.saveFile)) {
                setStatusBar("Saved!");
            } else {
                setStatusBar("Not saved!");
            }
        } else {
            saveAsGame();
        }
    }

    /**
     * 
	 * @param gp
	 */
    protected void setGamePanel(GamePanel gp) {
        this.containerPanel.removeAll();

        if (gp == null) {
            return;
        }

        this.containerPanel.add(gp, BorderLayout.CENTER);
        gp.setVisible(true);
        this.getContentPane().repaint();
    }

    /**
     * 
     * @param message
     * @param dest
     */
    protected void setMessagePanel(String message, String dest) {
        for (Component component : this.getContentPane().getComponents()) {
            if (component.getName() == "statusPanel") {
                JLabel statusLabel = (JLabel) ((JPanel) component)
                        .getComponent(0);
                statusLabel.setText(message);
            }
        }
    }

    /**
     * 
     * @param message
     */
    public void setPlayerBar(String message) {
        for (Component component : this.getContentPane().getComponents()) {
            if (component.getName() == "statusPanel") {
                JLabel statusLabel = (JLabel) ((JPanel) component)
                        .getComponent(1);
                statusLabel.setText(message);
            }
        }
    }

    /**
     * 
     * @param mode
     */
    protected void setReplayMode(boolean mode) {
        if (mode) {
            if (this.game != null) {
                this.replay.addGame(this.game);
            }
            this.replay.startReplayMode();
        } else {
            this.replay.stopReplayMode();
        }
    }

    /**
     * 
     * @param message
     */
    public void setStatusBar(String message) {
        for (Component component : this.getContentPane().getComponents()) {
            if (component.getName() == "statusPanel") {
                JLabel statusLabel = (JLabel) ((JPanel) component)
                        .getComponent(0);
                statusLabel.setText(message);
            }
        }
    }

    /**
     * 
     */
    protected void showAbout() {
        AboutDialog about = new AboutDialog(this);
        about.showDialog();
    }

    /**
     * 
     */
    public void showHint() {
        if (this.game != null) {
            ComputerPlayer player = new ComputerPlayer(this.game
                    .getCurrentPlayer().isWhite());
            java.util.List<Step> steps = player.getAllStep(this.game);

            String message = "You can make the following step(s): \n";

            for (Step step : steps) {
                message += step.toString() + "\n";
            }

            showInfoDialog(message, "Hint");
        }
    }

    /**
     * 
     * @param message
     * @param title
     */
    public void showInfoDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 
     * @param message
     * @param title
     */
    public void showWarningDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 
     * @param game
     */
    protected void startGame(Game game) {
        this.game = game;

        game.addListener(this);
        game.setFileType(new BasicNotationLog());

        gamePanel = new GamePanel(game);
        gamePanel.setName("gamepanel");
        gamePanel.setGame(game);

        this.game.getPlayer1().addListener(this);
        this.game.getPlayer2().addListener(this);

        setGamePanel(gamePanel);

        game.start();

        setStatusBar("New game started.");
        this.setTitle(game.toString());
    }
}

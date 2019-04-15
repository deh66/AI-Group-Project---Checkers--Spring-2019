/**
 * 
 */
package ija.projekt.gui.replay;

import ija.projekt.game.Game;
import ija.projekt.log.BasicNotationLog;
import ija.projekt.log.IFileLog;
import ija.projekt.log.XMLLog;
import ija.projekt.players.HumanPlayer;
import ija.projekt.replay.GameReplay;
import ija.projekt.replay.Replay;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DefaultFormatter;

/**
 * @author  David Molanr
 */
public class ReplayDialog extends JDialog implements ActionListener,
        ReplyListener {

    /**
     * 
     */
    protected JPanel cbPanel;
    
    /**
     * 
     */
	private final JPanel contentPanel = new JPanel();
	
	/**
	 * 
	 */
	protected JPanel delayPanel;
	
	/**
	 * 
	 */
	protected JSpinner delaySpinner;
	
	/**
	 * 
	 */
    protected JButton editBtn, exitBtn, nextStepBtn, stepBackBtn, stepToBtn, removeBtn, stopBtn;
    
    /**
     * 
     */
    protected JFileChooser fc;
    
    /**
     * 
     */
	protected JComboBox<GameReplay> gameListCB;
	
	/**
	 * 
	 */
	protected JPanel openPanel;
	
	/**
	 * 
	 */
	protected JFrame parent;

    /**
     * 
	 */
    protected Replay replay;
    
    /**
     * 
     */
    protected JPanel replayPanel;

    /**
     * Create the dialog.
     */
    public ReplayDialog(JFrame parent, Replay rp) {
        super(parent, "Replay Panel", Dialog.ModalityType.MODELESS);
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.parent = parent;
        this.replay = rp;
        this.fc = createFileChooser();

        initComponents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                replay.stopReplayMode();
            }
        });

        this.replay.setStepDelay(getSleepTime());
    }

    /**
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("open".equals(e.getActionCommand())) {
            openGame();
        } else if ("add".equals(e.getActionCommand())) {
            addNotation();
        } else if ("edit".equals(e.getActionCommand())) {
            editNotation();
        } else if ("close".equals(e.getActionCommand())) {
            this.replay.removeGame(getSelectedGame());
        } else if ("exit".equals(e.getActionCommand())) {
            this.replay.stopReplayMode();
        } else if ("nextstep".equals(e.getActionCommand())) {
            this.replay.nextStep(getSelectedGame());
        } else if ("stepto".equals(e.getActionCommand())) {
            goToStep(getSelectedGame());
        } else if ("stepback".equals(e.getActionCommand())) {
            this.replay.stepBack(getSelectedGame());
        } else if ("stop".equals(e.getActionCommand())) {
            this.replay.stopReplaying();
        }
    }

    /**
     * 
     * @param game
     */
    protected void addGame(Game game) {
        this.replay.addGame(game);
    }

    /**
     * 
     */
    protected void addNotation() {
        GameReplay newGame = showAddDialog(new GameReplay(new Game(
                new HumanPlayer(true), new HumanPlayer(false))));

        if (newGame != null) {
            addGame(newGame);
            this.replay.replayRefreshGui();
        }
    }

    /**
     * 
     */
    public void close() {
        setVisible(false);
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
     * @return
     */
    protected GameReplay editNotation() {
        GameReplay game = showAddDialog(getSelectedGame());
        this.replay.replayRefreshGui();
        return game;
    }

    /**
     * 
     * @return
     */
    protected GameReplay getSelectedGame() {
        return this.replay.getSelectedGame();
    }

    /**
     * 
     * @return
     */
    protected int getSleepTime() {
        Object obj = this.delaySpinner.getValue();
        if (obj instanceof Integer) {
            return (Integer) obj;
        }

        return 0;
    }

    /**
     * 
     * @param game
     */
    protected void goToStep(GameReplay game) {
        if (game == null) {
            return;
        }

        SelectStepDialog dialog = new SelectStepDialog(this, game);
        int step = dialog.showDialog();

        if (step > -1) {
            this.replay.stepTo(getSelectedGame(), step);
        }
    }

    /**
     * 
     */
    protected void initComponents() {
        setTitle("Replay Panel");

        setBounds(100, 100, 354, 201);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        cbPanel = new JPanel();
        cbPanel.setBounds(5, 5, 338, 23);
        contentPanel.add(cbPanel);

        gameListCB = new JComboBox<GameReplay>();
        cbPanel.setLayout(new BorderLayout(0, 0));
        gameListCB.setModel(this.replay.getComboBoxModel());
        gameListCB.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    replay.setSelectedGame((Game) item);
                }
            }
        });
        cbPanel.add(gameListCB);

        openPanel = new JPanel();
        openPanel.setBounds(5, 80, 338, 33);
        contentPanel.add(openPanel);

        JButton openBtn = new JButton("Open");
        openBtn.setActionCommand("open");
        openBtn.addActionListener(this);
        openPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        openPanel.add(openBtn);

        JButton addBtn = new JButton("Add");
        addBtn.setActionCommand("add");
        addBtn.addActionListener(this);
        openPanel.add(addBtn);

        removeBtn = new JButton("Remove");
        removeBtn.setActionCommand("close");
        removeBtn.addActionListener(this);

        editBtn = new JButton("Edit");
        editBtn.setActionCommand("edit");
        editBtn.addActionListener(this);
        openPanel.add(editBtn);
        openPanel.add(removeBtn);

        exitBtn = new JButton("Exit");
        exitBtn.setActionCommand("exit");
        exitBtn.addActionListener(this);
        openPanel.add(exitBtn);

        replayPanel = new JPanel();
        replayPanel.setBounds(5, 36, 338, 33);
        contentPanel.add(replayPanel);

        nextStepBtn = new JButton("Next step");
        nextStepBtn.setActionCommand("nextstep");
        nextStepBtn.addActionListener(this);
        replayPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        replayPanel.add(nextStepBtn);

        stepToBtn = new JButton("Step to");
        stepToBtn.setActionCommand("stepto");
        stepToBtn.addActionListener(this);
        replayPanel.add(stepToBtn);

        stepBackBtn = new JButton("Step back");
        stepBackBtn.setActionCommand("stepback");
        stepBackBtn.addActionListener(this);
        replayPanel.add(stepBackBtn);

        stopBtn = new JButton("Stop");
        stopBtn.setActionCommand("stop");
        stopBtn.addActionListener(this);
        stopBtn.setName("stop");
        replayPanel.add(stopBtn);

        delayPanel = new JPanel();
        delayPanel.setBounds(82, 130, 183, 31);
        contentPanel.add(delayPanel);
        delayPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Step Delay:");
        lblNewLabel.setBounds(0, 4, 63, 14);
        delayPanel.add(lblNewLabel);

        delaySpinner = new JSpinner();
        delaySpinner.setBounds(64, 0, 83, 23);
        delaySpinner.setValue(1000);
        JComponent comp = delaySpinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        delaySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                replay.setStepDelay(getSleepTime());
            }
        });
        delayPanel.add(delaySpinner);

        JLabel lblNewLabel_1 = new JLabel("ms");
        lblNewLabel_1.setBounds(157, 4, 46, 14);
        delayPanel.add(lblNewLabel_1);

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        statusPanel.setName("statusPanel");
        getContentPane().add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setLayout(new BorderLayout(0, 0));
    }

    /**
     * 
     */
    protected void openGame() {
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fc.getSelectedFile();

        IFileLog fileLog = file.getName().toLowerCase().endsWith(".dat") ? new BasicNotationLog()
                : (file.getName().toLowerCase().endsWith(".xml") ? new XMLLog()
                        : new BasicNotationLog());

        Game newGame = Game.open(file, fileLog);

        addGame(newGame);
    }

    /**
     * 
     */
    @Override
    public void replayModeEnded() {
        close();
    }

    /**
     * 
     */
    @Override
    public void replayModeStarted() {
        this.showDialog();
    }

    /**
     * 
     */
    @Override
    public void replayRefreshGui() {
        boolean mode = this.replay.isStepping();
        GameReplay selectedGame = this.replay.getSelectedGame();
        boolean sc = !mode && selectedGame != null;

        setComponentsEnabled(!mode, cbPanel);
        setComponentsEnabled(!mode, openPanel);
        setComponentsEnabled(!mode, delayPanel);
        
        setComponentsEnabled(sc && selectedGame.getStepCount() > 0, replayPanel);
        this.nextStepBtn.setEnabled(sc && selectedGame.getActualStepIndex() < (selectedGame.getStepCount() - 1));
        this.stepToBtn.setEnabled(sc && selectedGame.getStepCount() > 0);
        //this.stepBackBtn.setEnabled(sc && selectedGame.getActualStepIndex() >= 0);

        this.removeBtn.setEnabled(sc);
        this.editBtn.setEnabled(sc);

        this.stopBtn.setEnabled(mode);
        this.exitBtn.setEnabled(true);

        this.gameListCB.repaint();
    }

    /**
     * 
     * @param enabled
     * @param panel
     */
    protected void setComponentsEnabled(boolean enabled, JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            component.setEnabled(enabled);
        }
    }

    /**
     * 
     * @param game
     * @return
     */
    protected GameReplay showAddDialog(GameReplay game) {
        if (game != null) {
            BasicNotationEditDialog dialog = new BasicNotationEditDialog(this,
                    game);

            Game result = dialog.showDialog();

            if (result instanceof GameReplay) {
                game.setActualStepToLast();
                return game;
            }
        }
        return null;
    }

    /**
     * 
     */
    public void showDialog() {
        pack();
        setBounds(100, 100, 354, 201);
        setLocationRelativeTo(this.parent);
        setVisible(true);
    }
}
/**
 * 
 */
package ija.projekt.gui;

import ija.projekt.game.Game;
import ija.projekt.game.Player;
import ija.projekt.network.Client;
import ija.projekt.network.NetworkMan;
import ija.projekt.network.Server;
import ija.projekt.players.HumanPlayer;
import ija.projekt.players.NetworkPlayer.NetworkPlayer;
import ija.projekt.players.NetworkPlayer.NetworkPlayerInitListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author David Molnar
 */
public class NetworkGameDialog extends JDialog implements ActionListener,
        NetworkPlayerInitListener {

    /**
     * 
     */
    private JButton cancelButton, okButton;
    
    /**
     * 
     */
    private JRadioButton clientRadio;
    
    /**
     * 
     */
    private JTextField clientIPTxtBox, clientPortTxtBox, nameTxtField, serverPortTxtBox;
    
    /**
     * 
     */
    private JPanel clientPanel, contentPanel, serverPanel, statusPanel;
    
    /**
     * 
	 */
    protected Game game;
    
    /**
     * 
     */
    protected JFrame parent;
    
    /**
     * 
	 */
    protected NetworkPlayer player;

    /**
     * 
     */
    private JRadioButton serverRadio;
    
    /**
     * 
     */
    private JLabel statusLabel;

    /**
     * Create the dialog.
     */
    public NetworkGameDialog(JFrame parent) {
        super(parent, "Network Game", Dialog.ModalityType.DOCUMENT_MODAL);

        this.parent = parent;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        initComponents();

        setDefaultState();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (cancelWarning()) {
                    close();
                }
            }
        });
    }

    /**
     * 
     */
    @Override
    public void accept(String message) {
        status(message);
        this.okButton.setEnabled(true);
    }

    /**
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("clientRadio".equals(e.getActionCommand())
                || "serverRadio".equals(e.getActionCommand())) {
            refreshServerClientPanel();
        } else if ("OK".equals(e.getActionCommand())) {
            if (player == null) {
                startNewGame();
            } else {
                this.player.setAccepted();
            }
        } else if ("Cancel".equals(e.getActionCommand())) {
            if (this.player == null) {
                close();
            } else {
                cancelWarning();
            }
        }
    }

    /**
     * 
     */
    protected void cancel() {
        if (this.player != null) {
            this.player.cancel();
        }
        setDefaultState();
    }

    /**
     * 
     * @return
     */
    protected boolean cancelWarning() {
        if (this.player == null) {
            return true;
        }

        if (JOptionPane.showOptionDialog(this,
                "Are you sure you want to cancel?", "Network Game",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null) == JOptionPane.YES_OPTION) {
            cancel();
            return true;
        }

        return false;
    }

    /**
     * 
     */
    protected void close() {
        this.setVisible(false);
    }

    /**
     * 
     */
    @Override
    public void connected(String message) {
        status(message);

        Player player1 = new HumanPlayer(player.isBlack());
        player1.setName(player.getNameFromNetwork());

        this.game = new Game(player1, player);

        close();
    }

    /**
     * 
     * @param white
     * @param nm
     */
    protected void createNetworkPlayer(boolean white, NetworkMan nm) {
        this.player = new NetworkPlayer(white, nm);
        nm.addInitListener(this);
        this.player.connect();
    }

    /**
     * 
     */
    @Override
    public void errorHandler(String message) {
        cancel();
        status(message);
    }

    /**
     * 
     */
    protected void initComponents() {
        setResizable(false);
        setBounds(100, 100, 341, 301);

        getContentPane().setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Server (white)");
        lblNewLabel.setBounds(10, 54, 95, 14);
        contentPanel.add(lblNewLabel);

        serverRadio = new JRadioButton("");
        serverRadio.setActionCommand("serverRadio");
        serverRadio.setSelected(true);
        serverRadio.addActionListener(this);
        serverRadio.setBounds(10, 78, 21, 23);
        contentPanel.add(serverRadio);

        JLabel lblNewLabel_3 = new JLabel("Client (black)");
        lblNewLabel_3.setBounds(10, 122, 111, 14);
        contentPanel.add(lblNewLabel_3);

        clientRadio = new JRadioButton("");
        clientRadio.setBounds(10, 145, 28, 23);
        clientRadio.setActionCommand("clientRadio");
        clientRadio.addActionListener(this);
        contentPanel.add(clientRadio);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        okButton = new JButton("Start");
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        ButtonGroup group = new ButtonGroup();
        group.add(clientRadio);
        group.add(serverRadio);

        serverPanel = new JPanel();
        serverPanel.setBounds(32, 71, 293, 36);
        contentPanel.add(serverPanel);
        serverPanel.setLayout(null);

        JLabel lblNewLabel_2 = new JLabel("Port:");
        lblNewLabel_2.setBounds(5, 11, 36, 14);
        serverPanel.add(lblNewLabel_2);

        serverPortTxtBox = new JTextField();
        serverPortTxtBox.setText("12345");
        serverPortTxtBox.setBounds(35, 6, 86, 23);
        serverPanel.add(serverPortTxtBox);
        serverPortTxtBox.setColumns(10);

        clientPanel = new JPanel();
        clientPanel.setBounds(32, 141, 293, 37);
        contentPanel.add(clientPanel);
        clientPanel.setLayout(null);

        JLabel label = new JLabel("IP:");
        label.setBounds(6, 8, 14, 14);
        clientPanel.add(label);

        clientIPTxtBox = new JTextField();
        clientIPTxtBox.setText("127.0.0.1");
        clientIPTxtBox.setBounds(24, 5, 112, 23);
        clientPanel.add(clientIPTxtBox);
        clientIPTxtBox.setColumns(10);

        JLabel lblNewLabel_5 = new JLabel("Port:");
        lblNewLabel_5.setBounds(167, 9, 36, 14);
        clientPanel.add(lblNewLabel_5);

        clientPortTxtBox = new JTextField();
        clientPortTxtBox.setText("12345");
        clientPortTxtBox.setBounds(197, 5, 86, 23);
        clientPanel.add(clientPortTxtBox);
        clientPortTxtBox.setColumns(10);

        statusPanel = new JPanel();
        statusPanel.setBounds(10, 175, 315, 53);
        contentPanel.add(statusPanel);
        statusPanel.setLayout(null);

        statusLabel = new JLabel(
                "Please choose: you will be server or the client");
        statusLabel.setBounds(10, 19, 295, 14);
        statusPanel.add(statusLabel);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblYourName = new JLabel("Your name:");
        lblYourName.setBounds(10, 15, 64, 14);
        contentPanel.add(lblYourName);

        nameTxtField = new JTextField();
        nameTxtField.setBounds(75, 11, 250, 23);
        contentPanel.add(nameTxtField);
        nameTxtField.setColumns(10);
    }

    /**
     * 
     * @param number
     * @return
     */
    protected int parsePort(String number) {
        int result = -1;

        try {
            result = Integer.parseInt(number);
        } catch (NumberFormatException ex) {

        }

        return result;
    }

    /**
     * 
     */
    protected void refreshServerClientPanel() {
        setComponentsEnabled(!clientRadio.isSelected(), serverPanel);
        setComponentsEnabled(!serverRadio.isSelected(), clientPanel);
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
     * @param enabled
     */
    protected void setControlEnabled(boolean enabled) {
        setComponentsEnabled(enabled, serverPanel);
        setComponentsEnabled(enabled, clientPanel);
        setComponentsEnabled(enabled, contentPanel);

        okButton.setEnabled(enabled);
    }

    /**
     * 
     */
    protected void setDefaultState() {
        setControlEnabled(true);

        okButton.setText("Start");

        refreshServerClientPanel();

        this.player = null;

        status("Please choose: you will be server or the client");
    }

    /**
     * 
     * @return
     */
    public Game showDialog() {
        pack();
        setBounds(100, 100, 341, 301);
        setLocationRelativeTo(this.parent);
        setVisible(true);
        return this.game;
    }

    /**
     * 
     */
    protected void startNewGame() {
        String name = nameTxtField.getText();

        if (name.equals("")) {
            status("Choose name!");
            return;
        }

        String portString = clientRadio.isSelected() ? clientPortTxtBox
                .getText() : serverPortTxtBox.getText();

        int port = parsePort(portString);

        if (port <= 0 || port > 65536) {
            status("Port is not valid.");
            return;
        }

        if (clientRadio.isSelected()) {
            setControlEnabled(false);
            status("Connecting...");

            Client client = new Client(clientIPTxtBox.getText(), port, name);
            createNetworkPlayer(true, client);
        } else {
            okButton.setText("Accept");
            setControlEnabled(false);
            status("Waiting for client...");

            Server server = new Server(port, name);
            createNetworkPlayer(false, server);
        }
    }

    /**
     * 
     * @param message
     */
    protected void status(String message) {
        this.statusLabel.setText(message);
    }
}

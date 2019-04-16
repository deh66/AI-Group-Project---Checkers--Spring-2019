/**
 * 
 */
package ija.projekt.gui.replay;

import ija.projekt.basis.Step;
import ija.projekt.replay.GameReplay;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author David Molnar
 */
public class SelectStepDialog extends JDialog implements ActionListener {

    /**
     * 
     */
    private final JPanel contentPanel = new JPanel();
    
    /**
     * 
     */
    protected JList<Step> list;
    
    /**
     * 
     */
    protected DefaultListModel<Step> listModel;
    
    /**
     * 
     */
    protected Window parent;
    
    /**
     * 
     */
    protected int selectedStep;

    /**
     * Create the dialog.
     */
    public SelectStepDialog(Window parent, GameReplay game) {
        super(parent, "Select Step", Dialog.ModalityType.DOCUMENT_MODAL);

        this.parent = parent;
        this.selectedStep = -1;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initComponents();

        if (game == null) {
            return;
        }

        for (Step step : game.getSteps()) {
            listModel.addElement(step);
        }

        this.list.setSelectedIndex(game.getActualStepIndex());
    }

    /**
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("OK".equals(e.getActionCommand())) {
            this.selectedStep = this.list.getSelectedIndex();
            close();
        } else if ("Cancel".equals(e.getActionCommand())) {
            this.selectedStep = -1;
            close();
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
     * @param list
     * @return
     */
    protected JScrollPane createScrollBar(JList<Step> list) {
        JScrollPane sp = new JScrollPane(list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        return sp;
    }

    /**
     * 
     */
    protected void initComponents() {
        setTitle("Select Step");
        setBounds(100, 100, 156, 489);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            list = new JList<Step>();
            listModel = new DefaultListModel<Step>();
            list.setModel(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setCellRenderer(new DefaultListCellRenderer() {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public int getHorizontalAlignment() {
                    return CENTER;
                }
            });
            // contentPanel.add(list);
            contentPanel.add(createScrollBar(list));
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(this);
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(this);
                buttonPane.add(cancelButton);
            }
        }
    }

    /**
     * 
     * @return
     */
    public int showDialog() {
        pack();
        setBounds(100, 100, 156, 489);
        setLocationRelativeTo(this.parent);
        setVisible(true);
        return this.selectedStep;
    }
}

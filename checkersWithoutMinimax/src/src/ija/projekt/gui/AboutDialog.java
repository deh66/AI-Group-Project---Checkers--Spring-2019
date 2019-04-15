/**
 * 
 */
package ija.projekt.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author David Molnar
 */
public class AboutDialog extends JDialog implements ActionListener {

    /**
     * 
     */
    private final JPanel contentPanel = new JPanel();
    
    /**
     * 
     */
    protected Window parent;

    /**
     * Create the dialog.
     */
    public AboutDialog(Window parent) {
        super(parent, "About", Dialog.ModalityType.DOCUMENT_MODAL);
        setResizable(false);

        this.parent = parent;

        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new GridLayout(3, 1, 0, 0));
        {
            JLabel lblNewLabel_1 = new JLabel("Checkers");
            lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
            lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(lblNewLabel_1);

            JLabel lblNewLabel_2 = new JLabel(
                    "Created by: xmolna02@stud.fit.vutbr.cz");
            lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(lblNewLabel_2);

            JLabel lblNewLabel_3 = new JLabel("Date: 2013-05-15");
            lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(lblNewLabel_3);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(this);
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
    }

    /**
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("OK".equals(e.getActionCommand())) {
            close();
        }
    }

    /**
     * 
     */
    protected void close() {
        this.setVisible(false);
        dispose();
    }

    /**
     * 
     */
    public void showDialog() {
        pack();
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(this.parent);
        setVisible(true);
    }
}

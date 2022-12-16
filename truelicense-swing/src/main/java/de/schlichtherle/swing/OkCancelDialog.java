/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * A simple dialog which contains internationalized ok and cancel buttons
 * in the south of a border layout.
 *
 * @deprecated The class {@link javax.swing.JOptionPane} should be used instead.
 *
 * @author  Christian Schlichtherle
 */
public class OkCancelDialog extends EnhancedDialog {
    
    private static final String CLASS_NAME
            = "de/schlichtherle/swing/OkCancelDialog".replace('/', '.'); // beware of code obfuscation!
    private static final ResourceBundle resources
            = ResourceBundle.getBundle(CLASS_NAME);
    private static final long serialVersionUID = 1L;

    private boolean okButtonPressed; // default is false
    
    /**
     * The panel which contains just the ok and cancel button in a flow layout.
     */
    protected final JPanel okCancelPanel = new JPanel();
    
    /** Creates a new OkCancelDialog */
    public OkCancelDialog() {
        initComponents();
    }
    
    /** Creates a new OkCancelDialog */
    public OkCancelDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /** Creates a new OkCancelDialog */
    public OkCancelDialog(Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    private void initComponents() {
        final JSeparator separator = new JSeparator();

        final EnhancedButton okButton
                = new EnhancedButton(resources.getString("okButton"));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onOkButtonPressed(evt);
            }
        });

        final EnhancedButton cancelButton
                = new EnhancedButton(resources.getString("cancelButton"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onCancelButtonPressed(evt);
            }
        });

        okCancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);

        final JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(separator);
        south.add(okCancelPanel);

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);
        setDefaultButton(okButton);

        pack();
    }

    public void setVisible(boolean visible) {
        if (visible && !isVisible())
            okButtonPressed = false; // reset when showing
        super.setVisible(visible);
    }

    /**
     * Returns {@code true} if and only if this dialog has been closed by
     * pressing the OK button.
     */
    public boolean isOkButtonPressed() {
        return okButtonPressed;
    }

    /**
     * Performs this dialog's action when the ok button has been pressed.
     * The implementations in this class sets the {@code okButtonPressed}
     * property and hides this dialog.
     */
    protected void onOkButtonPressed(ActionEvent evt) {
        okButtonPressed = true;
        setVisible(false);
    }

    private void onCancelButtonPressed(ActionEvent evt) {
        setVisible(false);
    }
}

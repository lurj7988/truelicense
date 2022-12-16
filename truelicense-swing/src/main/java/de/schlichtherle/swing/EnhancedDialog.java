/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * Adds some enhancements to a {@link JDialog}}. <p> <ul> <li>It performs the
 * default close action when the escape key is pressed. This is done by calling
 * the {@code escapeKeyPressed()} method which may be overriden in sub classes.
 * </li> <li>It offers the method {@code setDefaultButton(JButton)} to
 * compensate for a deficiency of many look and feels which do not highlight the
 * default button. <li>The default button is highlighted in bold face and it
 * requests the focus whenever this {@code Component} is shown. </li> </ul>
 *
 * @author Christian Schlichtherle
 */
public class EnhancedDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    //
    // Constructors.
    //
    public EnhancedDialog() throws HeadlessException {
        super();
        initComponents();
    }

    public EnhancedDialog(Frame owner) throws HeadlessException {
        super(owner);
        initComponents();
    }

    public EnhancedDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        initComponents();
    }

    public EnhancedDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
        initComponents();
    }

    public EnhancedDialog(Frame owner, String title, boolean modal)
    throws HeadlessException {
        super(owner, title, modal);
        initComponents();
    }

    public EnhancedDialog(
            Frame owner,
            String title,
            boolean modal,
            GraphicsConfiguration gc)
    throws HeadlessException {
        super(owner, title, modal, gc);
        initComponents();
    }

    public EnhancedDialog(Dialog owner)
            throws HeadlessException {
        super(owner);
        initComponents();
    }

    public EnhancedDialog(Dialog owner, boolean modal)
            throws HeadlessException {
        super(owner, modal);
        initComponents();
    }

    public EnhancedDialog(Dialog owner, String title)
            throws HeadlessException {
        super(owner, title);
        initComponents();
    }

    public EnhancedDialog(Dialog owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);
        initComponents();
    }

    public EnhancedDialog(
            Dialog owner,
            String title,
            boolean modal,
            GraphicsConfiguration gc)
            throws HeadlessException {
        super(owner, title, modal, gc);
        initComponents();
    }

    private void initComponents() {
        getRootPane().registerKeyboardAction(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        onEscapeKeyPressed(evt);
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Performs this dialog's action when the escape key has been pressed. The
     * implementation in this class simply hides this dialog.
     *
     * @param evt the action event.
     */
    protected void onEscapeKeyPressed(ActionEvent evt) {
        setVisible(false);
    }

    protected JRootPane createRootPane() {
        return new EnhancedRootPane();
    }

    public void setDefaultButton(JButton button) {
        final JRootPane rp = getRootPane();
        if (rp instanceof EnhancedRootPane) {
            ((EnhancedRootPane) rp).setRestorableDefaultButton(button);
        } else {
            rp.setDefaultButton(button);
        }
    }

    public JButton getDefaultButton() {
        return getRootPane().getDefaultButton();
    }
}

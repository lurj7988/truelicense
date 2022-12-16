/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.license.wizard;

import java.awt.Component;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

/**
 * A simple wrapper for JOptionPane dialogs to provide additional comfort.
 *
 * @author Christian Schlichtherle
 */
public class Dialogs extends JOptionPane {
    private static final long serialVersionUID = 1L;
    
    public static void showMessageDialog(Component parentComponent,
                                         Object message,
                                         String title,
                                         int messageType) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
    }
    
    /** You cannot instantiate this class. */
    protected Dialogs() { }
}

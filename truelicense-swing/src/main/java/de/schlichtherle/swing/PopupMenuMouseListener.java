/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 * A mouse listener that brings up a popup menu.
 *
 * @author Christian Schlichtherle
 */
public class PopupMenuMouseListener extends MouseAdapter {

    private final JPopupMenu popup;
    
    /**
     * Creates a new instance of {@code PopupMenuMouseListener}.
     *
     * @param popup The popup menu to show if the popup menu mouse gesture
     *        has been triggered.
     */
    public PopupMenuMouseListener(final JPopupMenu popup) {
        this.popup = popup;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger())
            popup.show(e.getComponent(), e.getX(), e.getY());
    }
}

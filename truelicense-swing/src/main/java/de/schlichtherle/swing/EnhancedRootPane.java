/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JButton;
import javax.swing.JRootPane;

/**
 * Enhances its base class by requesting the focus for its default button
 * whenever the parent window is shown again.
 *
 * @author Christian Schlichtherle
 */
public class EnhancedRootPane extends JRootPane {
    private static final long serialVersionUID = 1L;

    private /* transient? */ JButton defaultButton;
    private transient ComponentAdapter parentListener;

    private ComponentAdapter getParentListener() {
        if (null == parentListener) {
            parentListener = new ComponentAdapter() {
                public void componentShown(ComponentEvent evt) {
                    if (null != defaultButton)
                        defaultButton.requestFocusInWindow();
                }
            };
        }
        return parentListener;
    }

    public void setRestorableDefaultButton(final JButton defaultButton) {
        final JButton oldDefault = this.defaultButton;
        /*if (null != oldDefault)
            oldDefault.setFont(Defaults.labelPlainFont);*/
        if (oldDefault != defaultButton) {
            this.defaultButton = defaultButton;
            if (null != defaultButton) {
                //defaultButton.setFont(Defaults.labelBoldFont);
                defaultButton.requestFocusInWindow();
                getParent().addComponentListener(getParentListener());
            } else {
                getParent().removeComponentListener(getParentListener());
            }
        }
        super.setDefaultButton(defaultButton);
    }
}

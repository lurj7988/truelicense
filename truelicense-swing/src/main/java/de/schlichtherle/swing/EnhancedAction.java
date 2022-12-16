/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * This class decodes it's constructor's text to set the mnemonic to the
 * first character following an ampersand ({@code '&'}) which is not an
 * ampersand itself. Any single instances of ampersands are removed from
 * the text.
 *
 * @author Christian Schlichtherle
 */
public abstract class EnhancedAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new instance of EnhancedAction
     * with the given name.
     */
    public EnhancedAction(String name) {
        this(name, null, null);
    }
    
    /**
     * Creates a new instance of EnhancedAction
     * with the given name and tool tip.
     */
    public EnhancedAction(String name, String toolTip) {
        this(name, toolTip, null);
    }
    
    /**
     * Creates a new instance of EnhancedAction
     * with the given name, tool tip and icon.
     */
    public EnhancedAction(String name, String toolTip, Icon icon) {
        /*if (icon != null && name != null && !"".equals(name.trim()))
            name = " " + name;*/
        MnemonicText mt = new MnemonicText(name);
        String text = mt.getText();
        putValue(NAME, text);
        if (mt.getMnemonicIndex() >= 0)
            putValue(MNEMONIC_KEY, new Integer(mt.getMnemonic()));
        if (toolTip != null)
            putValue(SHORT_DESCRIPTION, toolTip);
        if (icon != null)
            putValue(SMALL_ICON, icon);
    }
}

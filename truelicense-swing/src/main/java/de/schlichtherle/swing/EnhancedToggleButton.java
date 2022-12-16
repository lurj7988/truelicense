/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 * The {@link #setText(String)} method of this subclass of {@code JToogleButton}
 * scans the string for the first occurence of the character {@code &} to set
 * the button's mnemonic.
 *
 * @author Christian Schlichtherle
 */
public class EnhancedToggleButton extends JToggleButton {
    private static final long serialVersionUID = 1L;
    
    public EnhancedToggleButton() {
        super();
    }
    
    public EnhancedToggleButton(Icon icon) {
        super(icon);
    }
    
    public EnhancedToggleButton(String text) {
        super(text);
    }
    
    public EnhancedToggleButton(Action a) {
        super(a);
    }

    public EnhancedToggleButton(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * Sets the text of the button whereby the first single occurence of the
     * character {@code '&'} is used to determine the next character as the
     * mnemonic for this button.
     * <p>
     * All single occurences of {@code '&'} are removed from the text
     * and all double occurences are replaced by a single {@code '&'}
     * before passing the result to the super classes implementation.
     * <p>
     * Note that if the resulting text is HTML, the index of the mnemonic
     * character is ignored and the look and feel will (if at all) highlight
     * the first occurence of the mnemonic character.
     */
    public void setText(String text) {
        MnemonicText ti = new MnemonicText(text);
        text = ti.getText();
        super.setText(text);
        if (ti.getMnemonicIndex() >= 0) {
            setMnemonic(ti.getMnemonic());
            if (!ti.isHtmlText())
                setDisplayedMnemonicIndex(ti.getMnemonicIndex());
        }
    }
}

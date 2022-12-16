/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

/**
 * The {@link #setText(String)} method of this subclass of {@code JRadioButton}
 * scans the string for the first occurence of the character {@code &} to set
 * the button's mnemonic.
 *
 * @author Christian Schlichtherle
 */
public class EnhancedRadioButton extends JRadioButton {
    private static final long serialVersionUID = 1L;
    
    public EnhancedRadioButton () {
    }
     
    public EnhancedRadioButton(Icon icon) {
        super(icon);
    }

    public EnhancedRadioButton(Action a) {
        super(a);
    }

    public EnhancedRadioButton(Icon icon, boolean selected) {
        super(icon, selected);
    }
    
    public EnhancedRadioButton (String text) {
        super(text);
    }

    public EnhancedRadioButton (String text, boolean selected) {
        super(text, selected);
    }

    public EnhancedRadioButton(String text, Icon icon) {
        super(text, icon);
    }

    public EnhancedRadioButton (String text, Icon icon, boolean selected) {
        super(text, icon, selected);
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

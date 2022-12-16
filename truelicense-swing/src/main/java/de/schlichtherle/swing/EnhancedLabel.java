/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * The {@link #setText(String)} method of this subclass of {@code JLabel}
 * scans the string for the first occurence of the character {@code &} to set
 * the label's mnemonic.
 *
 * @author Christian Schlichtherle
 */
public class EnhancedLabel extends JLabel {
    private static final long serialVersionUID = 1L;
    
    public EnhancedLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }
            
    public EnhancedLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }

    public EnhancedLabel(String text) {
        super(text);
    }

    public EnhancedLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public EnhancedLabel(Icon image) {
        super(image);
    }

    public EnhancedLabel() {
        super();
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
            setDisplayedMnemonic(ti.getMnemonic());
            if (!ti.isHtmlText())
                setDisplayedMnemonicIndex(ti.getMnemonicIndex());
        }
    }
}

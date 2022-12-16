/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.io.Serializable;

/**
 * Decodes the constructor's string parameter to retrieve extended information
 * which can be used to configure the mnemonic for a {@code JComponent}.
 *
 * @author Christian Schlichtherle
 */
public class MnemonicText implements Serializable {

    private static final String HTML_TAG = "<html>";
    private static final long serialVersionUID = 1L;

    private final String text;
    private final int mnemonicIndex;
    private final char mnemonic;
    private final boolean isHtmlText;
    
    /**
     * Scans the given {@code text} for the character {@code '&'} to
     * retrieve the mnemonic and its index.
     * <p>
     * Every occurence of {@code '&'} will be removed from the text unless
     * it is followed by another {@code '&'}
     * in which case this sequence is substituted by a single {@code '&'}.
     */
    public MnemonicText(final String text) {
        int mnemonicIndex = -1;
        char mnemonic = 0;
        final StringBuffer buf = new StringBuffer(text.length());
        int l = text.length();
        for (int i = 0; i < l; i++) {
            char c = text.charAt(i);
            if (c == '&') {
                if (++i >= l) // skip this ampersand
                    break;
                if ((c = text.charAt(i)) != '&' && mnemonicIndex == -1)  {
                    mnemonic = c;
                    mnemonicIndex = buf.length();
                }
            }
            buf.append(c);
        }
        if (buf.length() != l)
            this.text = buf.toString();
        else
            this.text = text; // text and buf are equal

        this.mnemonicIndex = mnemonicIndex;
        this.mnemonic = mnemonic;
        String trim = text.trim();
        isHtmlText = trim.substring(0, Math.min(HTML_TAG.length(), trim.length()))
                .equalsIgnoreCase(HTML_TAG);
    }

    /**
     * Returns the decoded text.
     */
    public String getText() {
        return text;
    }

    /** Returns the index of the mnemonic character or -1 if not existent. */
    public int getMnemonicIndex() {
        return mnemonicIndex;
    }
    
    /**
     * Returns the mnemonic character.
     * This value is undefined if {@code {@link #getMnemonicIndex()} < 0}
     * is true.
     */
    public char getMnemonic() {
        return mnemonic;
    }

    /**
     * Returns true if the decoded text starts with an HTML tag.
     */
    public final boolean isHtmlText() {
        return isHtmlText;
    }
}

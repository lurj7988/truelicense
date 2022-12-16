/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * Provides some static fields used as Swing GUI defaults.
 *
 * @author Christian Schlichtherle
 */
public class Defaults {

    //
    // Default font attribubtes of JLabels
    //
    
    public static final Font labelPlainFont = new JLabel("").getFont(); // NOI18N
    public static final int labelFontStyle = labelPlainFont.getStyle();
    public static final int labelFontSize = labelPlainFont.getSize();
    public static final Font labelBoldFont = labelPlainFont.deriveFont(Font.BOLD);
    public static final Font monoPlainFont
            = new Font("Monospaced", Font.PLAIN, labelFontSize); // NOI18N
    public static final Font monoBoldFont
            = new Font("Monospaced", Font.BOLD,  labelFontSize); // NOI18N
    
    //
    // Some useful borders
    //
    
    public static final Border emptyBorder
            = BorderFactory.createEmptyBorder();
    public static final Border emptyTitledBorder
            = InvisibleTitledBorder.getInstance();

    /** You cannot instantiate this class. */
    protected Defaults() { }

}

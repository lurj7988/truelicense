/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Represents an {@link EmptyBorder} with the same insets as a
 * {@link TitledBorder}.
 *
 * @deprecated Not required.
 * @author Christian Schlichtherle
 */
public class InvisibleTitledBorder extends EmptyBorder {

    private static InvisibleTitledBorder INSTANCE = createEmptyTitledBorder();
    private static final long serialVersionUID = 1L;

    /**
     * Returns a new empty titled border.
     * 
     * @return A new empty titled border.
     * @deprecated Use {@link #getInstance} instead.
     */
    public static InvisibleTitledBorder createEmptyTitledBorder() {
        TitledBorder prototype = BorderFactory.createTitledBorder(null, "non-empty"); // NOI18N
        return new InvisibleTitledBorder(prototype.getBorderInsets(new JPanel()));
    }

    /**
     * Returns the default instance of this class.
     * 
     * @return The default instance of this class.
     */
    public static InvisibleTitledBorder getInstance() {
        return INSTANCE;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param borderInsets the border insets.
     */
    protected InvisibleTitledBorder(Insets borderInsets) {
        super(borderInsets);
        assert 0 != borderInsets.top;
        assert 0 != borderInsets.right;
        assert 0 != borderInsets.bottom;
        assert 0 != borderInsets.left;
    }
}

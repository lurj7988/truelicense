/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing.model;

import de.schlichtherle.swing.EnhancedDocument;

/**
 * @deprecated Use the class {@link EnhancedDocument} instead.
 *
 * @author  Christian Schlichtherle
 * @version $Id$
 */
public class EnhancedTextFieldDocument extends EnhancedDocument {

    public EnhancedTextFieldDocument() {
    }

    public EnhancedTextFieldDocument(Content c) {
        super(c);
    }

    public EnhancedTextFieldDocument(String str) {
        super(str);
    }

    public void firePropertyChange(
            String propertyName,
            boolean oldValue,
            boolean newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}
/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * Like its super class, but pays attention to the property {@code editable}
 * of the class {@link EnhancedDocument} when used as its document model
 * (which is the case by default).
 *
 * @author Christian Schlichtherle
 */
public class EnhancedTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    private transient Listener listener;

    private Listener listener() {
        if (null == listener)
            listener = new Listener();
        return listener;
    }

    public EnhancedTextField() {
    }

    public EnhancedTextField(String text) {
        super(text);
    }

    public EnhancedTextField(int columns) {
        super(columns);
    }

    public EnhancedTextField(String text, int columns) {
        super(text, columns);
    }

    public EnhancedTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    /**
     * As a supplement to its super class implementation, this implementation
     * also applies the property change to its model if it is an instance of
     * {@link EnhancedDocument}.
     *
     * @see JTextField#setEditable
     */
    public void setEditable(final boolean newEditable) {
        changeEditable(isEditable(), newEditable);
    }

    private void changeEditable(
            final boolean oldEditable,
            final boolean newEditable) {
        if (newEditable == oldEditable)
            return;

        super.setEditable(newEditable);

        final Document model = super.getDocument();
        if (model instanceof EnhancedComboBoxModel)
            ((EnhancedComboBoxModel) model).setEditable(newEditable);
    }

    /**
     * Creates the default implementation of the model
     * to be used at construction if one isn't explicitly 
     * given.
     * An instance of {@link EnhancedDocument} is returned by the
     * implementation in this class.
     *
     * @return The default model implementation.
     */
    protected Document createDefaultModel() {
        return new EnhancedDocument();
    }

    /**
     * As a supplement to its super class implementation, the implementation
     * in this class also inherits the properties from its model if it is
     * an instance of {@link EnhancedDocument}.
     *
     * @see JTextField#setDocument
     */
    public void setDocument(final Document newDocument) {
        final Document oldDocument = getDocument();
        if (oldDocument instanceof EnhancedDocument)
            ((EnhancedDocument) oldDocument).removePropertyChangeListener(
                    EnhancedDocument.PROPERTY_EDITABLE, listener());

        super.setDocument(newDocument);

        if (newDocument instanceof EnhancedDocument) {
            final EnhancedDocument edoc
                    = (EnhancedDocument) newDocument;
            edoc.addPropertyChangeListener(
                    EnhancedDocument.PROPERTY_EDITABLE, listener());
            setEditable(edoc.isEditable());
        }
    }

    private final class Listener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            changeEditable( Boolean.TRUE.equals(evt.getOldValue()),
                            Boolean.TRUE.equals(evt.getNewValue()));
        }
    }
}

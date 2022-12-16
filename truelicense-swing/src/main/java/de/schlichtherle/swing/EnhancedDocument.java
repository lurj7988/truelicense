/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * A {@link PlainDocument} which introduces bound properties
 * to notify {@link EnhancedTextField} observers.
 * Adds some text methods for convenience as well.
 *
 * @author Christian Schlichtherle
 * @since TrueSwing 1.28 (refactored from
 *        {@code de.schlichtherle.swing.model.EnhancedTextFieldDocument}).
 */
public class EnhancedDocument extends PlainDocument {

    /** The name of the property {@code editable}. */
    public static final String PROPERTY_EDITABLE = "editable";
    private static final long serialVersionUID = 1L;
    
    private boolean editable = true;

    protected PropertyChangeSupport changeSupport;

    public EnhancedDocument() {
    }

    public EnhancedDocument(Content c) {
        super(c);
    }

    public EnhancedDocument(String str) {
        setText(this, str);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean newEditable) {
        boolean oldEditable = isEditable();
        if (oldEditable == newEditable)
            return;

        this.editable = newEditable;
        firePropertyChange(PROPERTY_EDITABLE, oldEditable, newEditable);
    }

    /**
     * @return The entire text of this document model - never {@code null}.
     */
    public String getText() {
        return getText(this);
    }

    /**
     * Replaces the entire text of this document model with the given string.
     */
    public void setText(String str) {
        setText(this, str);
    }

    static String getText(Document doc) {
        try {
            return doc.getText(0, doc.getLength());
        } catch (BadLocationException cannotHappen) {
            throw new AssertionError(cannotHappen);
        }
    }

    static void setText(Document doc, String str) {
        try {
            final int docLen = doc.getLength();
            if (doc instanceof AbstractDocument) {
                ((AbstractDocument) doc).replace(0, docLen, str, null);
            } else {
                doc.remove(0, docLen);
                doc.insertString(0, str, null);
            }
        } catch (BadLocationException cannotHappen) { 
            throw new AssertionError(cannotHappen);
        }
    }

    /**
     * Identical to {@code getText()}.
     */
    public String toString() {
        return getText();
    }

    //
    // Property change support.
    // This is derived from javax.swing.JComponent, but does use
    // PropertyChangeSupport instead of SwingPropertyChangeSupport.
    //

    /**
     * Reports a bound property change.
     *
     * @param propertyName The programmatic name of the property
     *        that was changed.
     * @param oldValue The old value of the property (as a boolean).
     * @param newValue The new value of the property (as a boolean).
     *
     * @see #firePropertyChange(java.lang.String, boolean, boolean)
     */
    protected void firePropertyChange(
            String propertyName,
            boolean oldValue, boolean newValue) {
        if (null != changeSupport)
            changeSupport.firePropertyChange(
                    propertyName,
                    Boolean.valueOf(oldValue),
                    Boolean.valueOf(newValue));
    }

    /**
     * Adds a {@code PropertyChangeListener} to the listener list.
     * The listener is registered for all properties.
     * <p>
     * A {@code PropertyChangeEvent} will get fired in response
     * to setting a bound property.
     *
     * @param listener The {@code PropertyChangeListener} to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (null == changeSupport)
            changeSupport = new PropertyChangeSupport(this);
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a {@code PropertyChangeListener} from the listener list.
     * This removes a {@code PropertyChangeListener} that was registered
     * for all properties.
     *
     * @param listener The {@code PropertyChangeListener} to be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (null != changeSupport)
            changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Adds a {@code PropertyChangeListener} for a specific property.
     * The listener will be invoked only when a call on
     * {@code firePropertyChange} names that specific property.
     * <p>
     * If listener is {@code null}, no exception is thrown and no
     * action is performed.
     *
     * @param propertyName The name of the property to listen on.
     * @param listener The {@code PropertyChangeListener} to be added.
     */
    public void addPropertyChangeListener(
            String propertyName,
            PropertyChangeListener listener) {
	if (null == changeSupport)
	    changeSupport = new PropertyChangeSupport(this);
	changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a {@code PropertyChangeListener} for a specific property.
     * If listener is {@code null}, no exception is thrown and no
     * action is performed.
     *
     * @param propertyName  the name of the property that was listened on
     * @param listener  the {@code PropertyChangeListener} to be removed
     */
    public void removePropertyChangeListener(
            String propertyName,
            PropertyChangeListener listener) {
	if (null != changeSupport)
            changeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
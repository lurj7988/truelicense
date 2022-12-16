/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.MutableComboBoxModel;

/**
 * A {@link DefaultComboBoxModel} which introduces bound properties
 * to notify {@link EnhancedComboBoxModel} observers.
 *
 * @author Christian Schlichtherle
 * @since TrueLicense 1.28
 */
public class EnhancedComboBoxModel
extends DefaultComboBoxModel
implements MutableComboBoxModel {
    private static final long serialVersionUID = 2L;

    /** The name of the property {@code enabled}. */
    public static final String PROPERTY_ENABLED = "enabled";

    /** The name of the property {@code editable}. */
    public static final String PROPERTY_EDITABLE = "editable";

    private boolean enabled = true;
    private boolean editable;

    private transient PropertyChangeSupport changeSupport;

    /**
     * Constructs an empty {@code EnhancedComboBoxModel} object.
     */
    public EnhancedComboBoxModel() {
    }

    /**
     * Constructs an {@code EnhancedComboBoxModel} object initialized
     * with an array of objects.
     *
     * @param items An array of objects.
     */
    public EnhancedComboBoxModel(final Object items[]) {
        super(items);
    }

    /**
     * Constructs an {@code EnhancedComboBoxModel} object initialized
     * with a vector.
     *
     * @param v A Vector object.
     */
    public EnhancedComboBoxModel(Vector v) {
        super(v);
    }

    /**
     * Returns the value of the {@code enabled} property.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the value of the {@code enabled} property for this combo
     * box model. This is a bound property.
     * 
     * @param newEnabled If this is {@code false}, then any observer
     *        should ignore any attempt to change its selection
     *        (though the user may still be able to change the list elements).
     *
     * @see #PROPERTY_ENABLED
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public void setEnabled(final boolean newEnabled) {
        final boolean oldEnabled = isEnabled();
        if (newEnabled == oldEnabled)
            return;

        this.enabled = newEnabled;
        firePropertyChange(PROPERTY_ENABLED, oldEnabled, newEnabled);
    }

    /**
     * Returns the value of the {@code editable} property.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the value of the {@code editable} property for this combo
     * box model. This is a bound property.
     * 
     * @param newEditable If this is {@code false}, then any observer
     *        should ignore any attempt to mutate it's list elements
     *        (though the user may still be able to change the current selection).
     *
     * @see #PROPERTY_EDITABLE
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public void setEditable(final boolean newEditable) {
        final boolean oldEditable = isEditable();
        if (newEditable == oldEditable)
            return;

        this.editable = newEditable;
        firePropertyChange(PROPERTY_EDITABLE, oldEditable, newEditable);
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

/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package com.nexes.wizard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import javax.swing.Icon;

/**
 * The model for the Wizard component, which tracks the text, icons, and enabled
 * state of each of the buttons, as well as the current panel that is displayed.
 * 
 * @author Robert Eckstein (original code)
 * @author Christian Schlichtherle (revision)
 */
public class WizardModel {

    /**
     * Identification string for the current panel.
     */
    public static final String CURRENT_PANEL_DESCRIPTOR_PROPERTY
            = "currentPanelDescriptorProperty"; // NOI18N
    /**
     * Property identification String for the Back button's text
     */
    public static final String BACK_BUTTON_TEXT_PROPERTY
            = "backButtonTextProperty"; // NOI18N
    /**
     * Property identification String for the Back button's icon
     */
    public static final String BACK_BUTTON_ICON_PROPERTY
            = "backButtonIconProperty"; // NOI18N
    /**
     * Property identification String for the Back button's enabled state
     */
    public static final String BACK_BUTTON_ENABLED_PROPERTY
            = "backButtonEnabledProperty"; // NOI18N
    /**
     * Property identification String for the Next button's text
     */
    public static final String NEXT_BUTTON_TEXT_PROPERTY
            = "nextButtonTextProperty"; // NOI18N
    /**
     * Property identification String for the Next button's icon
     */
    public static final String NEXT_BUTTON_ICON_PROPERTY
            = "nextButtonIconProperty"; // NOI18N
    /**
     * Property identification String for the Next button's enabled state
     */
    public static final String NEXT_BUTTON_ENABLED_PROPERTY
            = "nextButtonEnabledProperty"; // NOI18N
    /**
     * Property identification String for the Cancel button's text
     */
    public static final String CANCEL_BUTTON_TEXT_PROPERTY
            = "cancelButtonTextProperty"; // NOI18N
    /**
     * Property identification String for the Cancel button's icon
     */
    public static final String CANCEL_BUTTON_ICON_PROPERTY
            = "cancelButtonIconProperty"; // NOI18N
    /**
     * Property identification String for the Cancel button's enabled state
     */
    public static final String CANCEL_BUTTON_ENABLED_PROPERTY
            = "cancelButtonEnabledProperty"; // NOI18N
    private WizardPanelDescriptor currentPanelDescriptor;
    private HashMap panels;
    private HashMap buttonsText;
    private HashMap buttonsIcon;
    private HashMap buttonsEnabled;
    private PropertyChangeSupport changeSupport;

    /**
     * Default constructor.
     */
    public WizardModel() {
        panels = new HashMap();
        buttonsText = new HashMap();
        buttonsIcon = new HashMap();
        buttonsEnabled = new HashMap();
        changeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Returns the currently displayed WizardPanelDescriptor.
     *
     * @return The currently displayed WizardPanelDescriptor
     */
    public WizardPanelDescriptor getCurrentPanelDescriptor() {
        return currentPanelDescriptor;
    }

    /**
     * Registers the WizardPanelDescriptor in the model using the
     * Object-identifier specified.
     *
     * @param id Object-based identifier
     * @param descriptor WizardPanelDescriptor that describes the panel
     */
    public void registerPanel(Object id, WizardPanelDescriptor descriptor) {
        //  Place a reference to it in a hashtable so we can access it later
        //  when it is about to be displayed.
        panels.put(id, descriptor);

    }

    /**
     * Sets the current panel to that identified by the Object passed in.
     *
     * @param id the panel identifier
     * @return boolean indicating success or failure
     */
    public boolean setCurrentPanel(Object id) {
        final WizardPanelDescriptor npd = (WizardPanelDescriptor) panels.get(id);
        if (null == npd) return false;
        final WizardPanelDescriptor opd = currentPanelDescriptor;
        currentPanelDescriptor = npd;
        firePropertyChange(CURRENT_PANEL_DESCRIPTOR_PROPERTY, opd, npd);
        return true;
    }

    public Object getBackButtonText() {
        return buttonsText.get(BACK_BUTTON_TEXT_PROPERTY);
    }

    public void setBackButtonText(Object newText) {
        final Object oldText = getBackButtonText();
        if (!newText.equals(oldText)) {
            buttonsText.put(BACK_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(BACK_BUTTON_TEXT_PROPERTY, oldText, newText);
        }
    }

    public Object getNextButtonText() {
        return buttonsText.get(NEXT_BUTTON_TEXT_PROPERTY);
    }

    public void setNextButtonText(Object newText) {
        final Object oldText = getNextButtonText();
        if (!newText.equals(oldText)) {
            buttonsText.put(NEXT_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(NEXT_BUTTON_TEXT_PROPERTY, oldText, newText);
        }
    }

    public Object getCancelButtonText() {
        return buttonsText.get(CANCEL_BUTTON_TEXT_PROPERTY);
    }

    public void setCancelButtonText(Object newText) {
        final Object oldText = getCancelButtonText();
        if (!newText.equals(oldText)) {
            buttonsText.put(CANCEL_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(CANCEL_BUTTON_TEXT_PROPERTY, oldText, newText);
        }
    }

    public Icon getBackButtonIcon() {
        return (Icon) buttonsIcon.get(BACK_BUTTON_ICON_PROPERTY);
    }

    public void setBackButtonIcon(Icon newIcon) {
        final Object oldIcon = getBackButtonIcon();
        if (!newIcon.equals(oldIcon)) {
            buttonsIcon.put(BACK_BUTTON_ICON_PROPERTY, newIcon);
            firePropertyChange(BACK_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
        }
    }

    public Icon getNextButtonIcon() {
        return (Icon) buttonsIcon.get(NEXT_BUTTON_ICON_PROPERTY);
    }

    public void setNextButtonIcon(Icon newIcon) {
        final Object oldIcon = getNextButtonIcon();
        if (!newIcon.equals(oldIcon)) {
            buttonsIcon.put(NEXT_BUTTON_ICON_PROPERTY, newIcon);
            firePropertyChange(NEXT_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
        }
    }

    public Icon getCancelButtonIcon() {
        return (Icon) buttonsIcon.get(CANCEL_BUTTON_ICON_PROPERTY);
    }

    public void setCancelButtonIcon(Icon newIcon) {
        final Icon oldIcon = getCancelButtonIcon();
        if (!newIcon.equals(oldIcon)) {
            buttonsIcon.put(CANCEL_BUTTON_ICON_PROPERTY, newIcon);
            firePropertyChange(CANCEL_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
        }
    }

    public Boolean getBackButtonEnabled() {
        return (Boolean) buttonsEnabled.get(BACK_BUTTON_ENABLED_PROPERTY);
    }

    public void setBackButtonEnabled(Boolean newValue) {
        final Boolean oldValue = getBackButtonEnabled();
        if (!equals(newValue, oldValue)) {
            buttonsEnabled.put(BACK_BUTTON_ENABLED_PROPERTY, newValue);
            firePropertyChange(BACK_BUTTON_ENABLED_PROPERTY, oldValue, newValue);
        }
    }

    public Boolean getNextButtonEnabled() {
        return (Boolean) buttonsEnabled.get(NEXT_BUTTON_ENABLED_PROPERTY);
    }

    public void setNextButtonEnabled(Boolean newValue) {
        final Boolean oldValue = getNextButtonEnabled();
        if (!equals(newValue, oldValue)) {
            buttonsEnabled.put(NEXT_BUTTON_ENABLED_PROPERTY, newValue);
            firePropertyChange(NEXT_BUTTON_ENABLED_PROPERTY, oldValue, newValue);
        }
    }

    public Boolean getCancelButtonEnabled() {
        return (Boolean) buttonsEnabled.get(CANCEL_BUTTON_ENABLED_PROPERTY);
    }

    public void setCancelButtonEnabled(Boolean newValue) {
        final Boolean oldValue = getCancelButtonEnabled();
        if (!equals(newValue, oldValue)) {
            buttonsEnabled.put(CANCEL_BUTTON_ENABLED_PROPERTY, newValue);
            firePropertyChange(CANCEL_BUTTON_ENABLED_PROPERTY, oldValue, newValue);
        }
    }

    private static boolean equals(final Object o1, final Object o2) {
        return o1 == o2 || null != o1 && o1.equals(o2);
    }

    public void addPropertyChangeListener(PropertyChangeListener p) {
        changeSupport.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(PropertyChangeListener p) {
        changeSupport.removePropertyChangeListener(p);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}

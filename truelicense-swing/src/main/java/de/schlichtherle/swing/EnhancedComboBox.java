/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Like its super class, but pays attention to the extended properties of the
 * {@link EnhancedComboBoxModel} and {@link EnhancedDocument} classes if they
 * are used as the combo box model or the document of the editor component.
 * These model classes are used by default by this class.
 *
 * @author Christian Schlichtherle
 * @since TrueSwing 1.28
 */
public class EnhancedComboBox extends JComboBox {

    /** @deprecated This field is unused and will vanish in the next major version number. */
    public static final String PROPERTY_PUSHING_PROPERTY_CHANGES
            = "pushingPropertyChanges";
    private static final long serialVersionUID = 1L;

    private transient PropertyChangeListener controller;

    private PropertyChangeListener getController() {
        final PropertyChangeListener controller = this.controller;
        return null != controller
                ? controller
                : (this.controller = new Controller());
    }

    public EnhancedComboBox(ComboBoxModel model) {
        // Don't call super class constructor with model argument:
        // This would call setModel while controller is not yet initialized!
        changeModel(super.getModel(), model);
    }

    public EnhancedComboBox(final Object items[]) {
        // Don't call super class constructor with model argument:
        // This would call setModel while controller is not yet initialized!
        changeModel(super.getModel(), new EnhancedComboBoxModel(items));
    }

    public EnhancedComboBox(Vector items) {
        // Don't call super class constructor with model argument:
        // This would call setModel while controller is not yet initialized!
        changeModel(super.getModel(), new EnhancedComboBoxModel(items));
    }

    public EnhancedComboBox() {
        // Don't call super class constructor with model argument:
        // This would call setModel while controller is not yet initialized!
        changeModel(super.getModel(), new EnhancedComboBoxModel());
    }

    /**
     * As a supplement to its super class implementation, the implementation
     * in this class also inherits the properties from its model if it is
     * an instance of {@link EnhancedComboBoxModel}.
     *
     * @see JComboBox#setModel
     */
    public void setModel(ComboBoxModel newModel) {
        changeModel(getModel(), newModel);
    }

    public void setEditor(ComboBoxEditor newEditor) {
        changeEditor(getEditor(), newEditor);
    }

    /**
     * Returns the value of the {@code document} property of the editor
     * component if this combo box is editable.
     *
     * @throws IllegalStateException If this combo box is not editable.
     * @since TrueSwing 1.30
     */
    public Document getDocument() {
        if (!isEditable())
            throw new IllegalStateException();
            
        final JTextComponent text = getTextComponent();
        return text != null ? text.getDocument() : null;
    }

    /**
     * Sets the value of the {@code document} property of the editor
     * component if this combo box is editable.
     * Also inherits the properties from its model if it is
     * an instance of {@link EnhancedDocument}.
     *
     * @param newDoc The new value of the {@code document} property.
     * @throws IllegalStateException If this combo box is not editable.
     * @since TrueSwing 1.30
     */
    public void setDocument(Document newDoc) {
        if (!isEditable())
            throw new IllegalStateException();

        changeDocument(getDocument(), newDoc);
    }

    /**
     * As a supplement to its super class implementation, the implementation
     * in this class also applies the property change to its model if it is
     * an instance of {@link EnhancedComboBoxModel}.
     *
     * @see JComboBox#setEnabled
     */
    public void setEnabled(boolean newEnabled) {
        changeEnabled(isEnabled(), newEnabled);
    }

    /**
     * As a supplement to its super class implementation, this implementation
     * also applies the property change to its model if it is an instance of
     * {@link EnhancedComboBoxModel}.
     *
     * @see JComboBox#setEditable
     */
    public void setEditable(boolean newEditable) {
        changeEditable(isEditable(), newEditable);
    }

    private void changeModel(
            final ComboBoxModel oldModel,
            final ComboBoxModel newModel) {
        if (newModel == oldModel)
            return;

        if (oldModel instanceof EnhancedComboBoxModel)
            ((EnhancedComboBoxModel) oldModel).removePropertyChangeListener(
                    getController());

        super.setModel(newModel);

        if (newModel instanceof EnhancedComboBoxModel) {
            final EnhancedComboBoxModel enhNewModel
                    = (EnhancedComboBoxModel) newModel;
            enhNewModel.addPropertyChangeListener(getController());
            final boolean editable = enhNewModel.isEditable();
            changeEditable(super.isEditable(), editable);
            final boolean enabled = enhNewModel.isEnabled();
            changeEnabled(super.isEnabled(), enabled);
        }
    }

    private void changeEditor(
            final ComboBoxEditor oldCBE,
            final ComboBoxEditor newCBE) {
        if (newCBE == oldCBE)
            return;

        JTextComponent oldText = null;
        if (oldCBE != null) {
            final Component component = oldCBE.getEditorComponent();
            if (component instanceof JTextComponent)
                oldText = (JTextComponent) component;
        }

        super.setEditor(newCBE);

        JTextComponent newText = null;
        if (newCBE != null) {
            final Component component = newCBE.getEditorComponent();
            if (component instanceof JTextComponent)
                newText = (JTextComponent) component;
        }

        changeText(oldText, newText);
    }

    private void changeText(
            final JTextComponent oldTC,
            final JTextComponent newTC) {
        if (newTC == oldTC)
            return;

        Document oldDocument = null;
        if (oldTC != null) {
            oldTC.removePropertyChangeListener("document", getController());
            oldDocument = oldTC.getDocument();
        }

        Document newDocument = null;
        if (newTC != null) {
            newTC.addPropertyChangeListener("document", getController());
            newDocument = newTC.getDocument();
        }

        changeDocument(oldDocument, newDocument);
    }

    private void changeDocument(
            final Document oldDoc,
            final Document newDoc) {
        if (newDoc == oldDoc)
            return;

        if (oldDoc instanceof EnhancedDocument) {
            final EnhancedDocument enhOldDoc = (EnhancedDocument) oldDoc;
            enhOldDoc.removePropertyChangeListener(
                    EnhancedDocument.PROPERTY_EDITABLE, getController());
        }

        final JTextComponent text = getTextComponent();
        if (text != null)
            text.setDocument(newDoc);

        if (newDoc instanceof EnhancedDocument) {
            final EnhancedDocument enhNewDoc = (EnhancedDocument) newDoc;
            enhNewDoc.addPropertyChangeListener(
                    EnhancedDocument.PROPERTY_EDITABLE, getController());
            // Map the editable property of the EnhancedDocument to the
            // enabled property of this component.
            // This prevents the combo box from invalidating the editor
            // component by setting itself to be non-editable.
            final boolean editable = enhNewDoc.isEditable();
            changeEnabled(super.isEnabled(), editable);
        }
    }

    private void changeEditable(
            final boolean oldEditable,
            final boolean newEditable) {
        if (newEditable == oldEditable)
            return;

        super.setEditable(newEditable);

        final ComboBoxModel model = super.getModel();
        if (model instanceof EnhancedComboBoxModel)
            ((EnhancedComboBoxModel) model).setEditable(newEditable);

        final JTextComponent text = getTextComponent();
        if (text != null) {
            final Document oldDoc = text.getDocument();
            if (oldDoc instanceof EnhancedDocument) {
                ((EnhancedDocument) oldDoc).setEditable(newEditable);
            } else if (oldEditable == false) {
                assert newEditable == true;
                final EnhancedDocument newDoc = new EnhancedDocument();
                assert newDoc.isEditable();
                changeDocument(oldDoc, newDoc);
            }
        }
    }

    private void changeEnabled(
            final boolean oldEnabled,
            final boolean newEnabled) {
        if (newEnabled == oldEnabled)
            return;

        super.setEnabled(newEnabled);

        final ComboBoxModel model = super.getModel();
        if (model instanceof EnhancedComboBoxModel)
            ((EnhancedComboBoxModel) model).setEnabled(newEnabled);
    }

    /**
     * If the {@code editor} property is set and the editor component is
     * actually a {@link JTextComponent}, then this object is returned,
     * or {@code null} otherwise.
     */
    private JTextComponent getTextComponent() {
        final ComboBoxEditor editor = super.getEditor();
        if (editor != null) {
            final Component comp = editor.getEditorComponent();
            if (comp instanceof JTextComponent)
                return (JTextComponent) comp;
        }
        return null;
    }

    /**
     * Implements the reaction to any updates for all properties observed by
     * this class.
     */
    private final class Controller implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            final Object source = e.getSource();
            final String property = e.getPropertyName();
            final JTextComponent text = getTextComponent();
            if (text != null && source == text.getDocument()) {
                assert EnhancedDocument.PROPERTY_EDITABLE.equals(property);
                // Map the editable property of the EnhancedDocument to the
                // enabled property of this component.
                // This prevents the combo box from invalidating the editor
                // component by setting itself to be non-editable.
                changeEnabled(  Boolean.TRUE.equals(e.getOldValue()),
                                Boolean.TRUE.equals(e.getNewValue()));
            } else if (source == text) {
                assert "document".equals(property);
                changeDocument( (Document) e.getOldValue(),
                                (Document) e.getNewValue());
            } else {
                assert source == getModel();
                if (EnhancedComboBoxModel.PROPERTY_EDITABLE.equals(property))
                    changeEditable( Boolean.TRUE.equals(e.getOldValue()),
                                    Boolean.TRUE.equals(e.getNewValue()));
                else if (EnhancedComboBoxModel.PROPERTY_ENABLED.equals(property))
                    changeEnabled(  Boolean.TRUE.equals(e.getOldValue()),
                                    Boolean.TRUE.equals(e.getNewValue()));
            }
        }
    }
}

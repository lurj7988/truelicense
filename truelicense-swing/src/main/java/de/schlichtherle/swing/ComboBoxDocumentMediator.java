/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.ComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.Document;

/**
 * A control which mediates between a {@link ComboBoxModel} and a
 * {@link Document}.
 * This allows to have the document text updated whenever the current
 * selection of the comboBoxModel box model changes and vice versa.
 * <p>
 * This class is most useful if used with an {@link EnhancedComboBoxModel}
 * and an {@link EnhancedDocument}, in which case the document is also made
 * editable/ineditable whenever the comboBoxModel box is enabled/disabled
 * and vice versa.
 * 
 * @deprecated Altough this class is working properly, its use is not
 *             recommended anymore.
 *             This is because this class may generate a series of events
 *             when the combo box model changes which cause a lot of
 *             confusing changes on the document before the final value
 *             is set.
 *             Instead of this class, you should make the combo box editable
 *             first and then assign the document to the combo boxes editor
 *             component like this:
 *             {@code ((JTextComponent) combo.getEditor().getEditorComponent()).setDocument(doc)}
 * @author Christian Schlichtherle
 * @since TrueLicense 1.28
 */
public class ComboBoxDocumentMediator implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Listener listener = new Listener();
    private Document document;
    private ComboBoxModel comboBoxModel;

    /**
     * Used to inhibit mutual recursive event firing.
     */
    private transient boolean recursion;

    public Document getDocument() {
        return document;
    }
    
    public void setDocument(final Document newDoc) {
        final Document oldDoc = getDocument();
        if (newDoc == oldDoc)
            return;

        if (null != oldDoc) {
            oldDoc.removeDocumentListener(listener);
            if (oldDoc instanceof EnhancedDocument)
                ((EnhancedDocument) oldDoc).removePropertyChangeListener(
                        EnhancedDocument.PROPERTY_EDITABLE, listener);
        }

        document = newDoc;

        if (null != newDoc) {
            newDoc.addDocumentListener(listener);
            documentUpdated();
            if (newDoc instanceof EnhancedDocument) {
                ((EnhancedDocument) newDoc).addPropertyChangeListener(
                        EnhancedDocument.PROPERTY_EDITABLE, listener);
                documentEditableChanged();
            }
        }
    }
    
    public ComboBoxModel getComboBoxModel() {
        return comboBoxModel;
    }
    
    public void setComboBoxModel(final ComboBoxModel newCBM) {
        final ComboBoxModel oldCBM = getComboBoxModel();
        if (newCBM == oldCBM)
            return;

        if (null != oldCBM) {
            oldCBM.removeListDataListener(listener);
            if (oldCBM instanceof EnhancedComboBoxModel)
                ((EnhancedComboBoxModel) oldCBM).removePropertyChangeListener(
                        EnhancedComboBoxModel.PROPERTY_ENABLED, listener);
        }

        comboBoxModel = newCBM;

        if (null != newCBM) {
            newCBM.addListDataListener(listener);
            comboBoxModelSelectionChanged();
            if (newCBM instanceof EnhancedComboBoxModel) {
                ((EnhancedComboBoxModel) newCBM).addPropertyChangeListener(
                        EnhancedComboBoxModel.PROPERTY_ENABLED, listener);
                comboBoxModelEnabledChanged();
            }
        }
    }

    private void documentUpdated() {
        if (lock())
            return;
        try {
            final ComboBoxModel cbm = getComboBoxModel();
            if (cbm == null)
                return;

            final Document doc = getDocument();
            final String text = getText(doc);
            cbm.setSelectedItem(text);
        } finally {
            unlock();
        }
    }

    private void documentEditableChanged() {
        if (lock())
            return;
        try {
            final ComboBoxModel cbm = getComboBoxModel();
            if (cbm instanceof EnhancedComboBoxModel) {
                final EnhancedComboBoxModel ecbm = (EnhancedComboBoxModel) cbm;
                final boolean e = ((EnhancedDocument) getDocument()).isEditable();
                ecbm.setEditable(e);
                ecbm.setEnabled(e);
            }
        } finally {
            unlock();
        }
    }

    private void comboBoxModelSelectionChanged() {
        if (lock())
            return;
        try {
            final Document doc = getDocument();
            if (doc == null)
                return;

            final ComboBoxModel cbm = getComboBoxModel();
            final Object item = cbm.getSelectedItem();

            setText(doc, item != null ? item.toString() : null);
        } finally {
            unlock();
        }
    }

    private void comboBoxModelEnabledChanged() {
        if (lock())
            return;
        try {
            final Document doc = getDocument();
            if (doc instanceof EnhancedDocument)
                ((EnhancedDocument) doc).setEditable(
                        ((EnhancedComboBoxModel) getComboBoxModel()).isEnabled());
        } finally {
            unlock();
        }
    }

    /**
     * Locks out mutual recursive event notification.
     * 
     * @return Whether or not recursion was already locked.
     */
    private boolean lock() {
        if (recursion)
            return true;
        recursion = true;
        return false;
    }

    /**
     * Unlocks mutual recursive event notification.
     */
    private void unlock() {
        recursion = false;
    }

    public static String getText(Document doc) {
        return EnhancedDocument.getText(doc);
    }

    public static void setText(Document doc, String str) {
        EnhancedDocument.setText(doc, str);
    }

    private final class Listener
    implements DocumentListener, ListDataListener, PropertyChangeListener, Serializable {
        private static final long serialVersionUID = 1L;

        public void insertUpdate(DocumentEvent e) {
            documentUpdated();
        }

        public void removeUpdate(DocumentEvent e) {
            documentUpdated();
        }

        public void changedUpdate(DocumentEvent e) {
            documentUpdated();
        }

        public void intervalAdded(ListDataEvent e) {
        }

        public void intervalRemoved(ListDataEvent e) {
        }

        public void contentsChanged(ListDataEvent e) {
            // Negative indices are a ComboBoxModel's way of telling us that
            // the current selection has changed rather than a value in
            // the comboBoxModel.
            if (e.getIndex0() < 0 && e.getIndex1() < 0)
                comboBoxModelSelectionChanged();
        }

        public void propertyChange(PropertyChangeEvent e) {
            final String property = e.getPropertyName();
            if ("enabled".equals(property))
                comboBoxModelEnabledChanged();
            else if ("editable".equals(property))
                documentEditableChanged();
            else
                throw new AssertionError(
                        "Received change event for unknown property: "
                        + property);
        }
    }
}

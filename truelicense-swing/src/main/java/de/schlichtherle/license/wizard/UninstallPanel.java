/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.license.wizard;

import com.nexes.wizard.WizardPanelDescriptor;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.swing.Defaults;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 * @author Christian Schlichtherle
 */
public class UninstallPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final LicenseManager manager;

    public UninstallPanel(LicenseManager manager) {
        this.manager = manager;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        prompt = new javax.swing.JTextArea();
        uninstallButton = new de.schlichtherle.swing.EnhancedButton();

        setLayout(new java.awt.GridBagLayout());

        prompt.setEditable(false);
        prompt.setFont(Defaults.labelBoldFont);
        prompt.setLineWrap(true);
        prompt.setText(Resources.getString("UninstallPanel.prompt.text", new Object[] {manager.getLicenseParam().getSubject()})); // NOI18N
        prompt.setWrapStyleWord(true);
        prompt.setBorder(null);
        prompt.setOpaque(false);
        prompt.setPreferredSize(new java.awt.Dimension(370, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        add(prompt, gridBagConstraints);

        uninstallButton.setText(Resources.getString("UninstallPanel.uninstallButton.text")); // NOI18N
        uninstallButton.setName("uninstall");
        uninstallButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uninstallButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(uninstallButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void uninstallButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uninstallButtonActionPerformed
        try {
            manager.uninstall();
            uninstallButton.setEnabled(false);
            fireLicenseUninstalled();
        } catch (Exception failure) {
            Dialogs.showMessageDialog(
                    this,
                    failure.getLocalizedMessage(),
                    Resources.getString("UninstallPanel.failure.title"),
                    Dialogs.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_uninstallButtonActionPerformed

    /**
     * Registers LicenseUninstalledListener to receive events.
     * @param listener The listener to register.
     */
    public void addLicenseUninstalledListener(LicenseUninstalledListener listener) {
        if (null == listenerList)
            listenerList = new EventListenerList();
        listenerList.add(LicenseUninstalledListener.class, listener);
    }

    /**
     * Removes LicenseUninstalledListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public void removeLicenseUninstalledListener(LicenseUninstalledListener listener) {
        if (null != listenerList)
            listenerList.remove(LicenseUninstalledListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     */
    protected void fireLicenseUninstalled() {
        if (listenerList == null)
            return;
        Object[] listeners = listenerList.getListenerList ();
        LicenseUninstalledEvent e = null;
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == LicenseUninstalledListener.class) {
                if (e == null)
                    e = new LicenseUninstalledEvent(this);
                ((LicenseUninstalledListener) listeners[i+1]).licenseUninstalled(e);
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea prompt;
    private de.schlichtherle.swing.EnhancedButton uninstallButton;
    // End of variables declaration//GEN-END:variables
    
    public static class Descriptor extends WizardPanelDescriptor {
        public static final String IDENTIFIER = "UNINSTALL_PANEL"; // NOI18N
        
        public Descriptor(LicenseManager manager) {
            setPanelDescriptorIdentifier(IDENTIFIER);
            final UninstallPanel panel = new UninstallPanel(manager);
            setPanelComponent(panel);
            panel.addLicenseUninstalledListener(new LicenseUninstalledListener() {
                public void licenseUninstalled(LicenseUninstalledEvent evt) {
                    getWizardModel().setNextButtonEnabled(Boolean.TRUE);
                }
            });
        }

        public Object getNextPanelDescriptor() {
            return FINISH;
        }

        public Object getBackPanelDescriptor() {
            return WelcomePanel.Descriptor.IDENTIFIER;
        }  
        
        public void aboutToDisplayPanel() {
            ((UninstallPanel) getPanelComponent()).uninstallButton.setEnabled(true);
            getWizardModel().setNextButtonEnabled(Boolean.FALSE);
        }
    }
}

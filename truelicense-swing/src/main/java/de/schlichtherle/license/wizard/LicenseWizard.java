/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.license.wizard;

import com.nexes.wizard.Wizard;
import com.nexes.wizard.WizardPanelDescriptor;
import de.schlichtherle.license.LicenseManager;
import java.awt.Dialog;
import java.awt.Frame;

/**
 * This is the internationalised license management wizard class.
 * It can be used to install and verify licenses visually for the
 * application's user.
 *
 * @author Christian Schlichtherle
 */
public class LicenseWizard extends Wizard {
    
    public LicenseWizard(LicenseManager manager) {
        init(manager);
    }

    public LicenseWizard(LicenseManager manager, Dialog owner) {
        super(owner);
        init(manager);
    }

    public LicenseWizard(LicenseManager manager, Frame owner) {
        super(owner);
        init(manager);
    }

    private void init(final LicenseManager manager) {
        super.setTitle(Resources.getString("LicenseWizard.title",
                manager.getLicenseParam().getSubject()));

        final WizardPanelDescriptor welcome
                = new WelcomePanel.Descriptor(manager);
        super.registerWizardPanel(
                welcome.getPanelDescriptorIdentifier(), welcome);

        final WizardPanelDescriptor install
                = new InstallPanel.Descriptor(manager);
        super.registerWizardPanel(
                install.getPanelDescriptorIdentifier(), install);

        final WizardPanelDescriptor license
                = new LicensePanel.Descriptor(manager);
        super.registerWizardPanel(
                license.getPanelDescriptorIdentifier(), license);

        final WizardPanelDescriptor uninstall
                = new UninstallPanel.Descriptor(manager);
        super.registerWizardPanel(
                uninstall.getPanelDescriptorIdentifier(), uninstall);

        super.setCurrentPanel(welcome.getPanelDescriptorIdentifier());
    }
}

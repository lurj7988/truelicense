/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.license.wizard;

/**
 * Instances of this class can be notified when a license certificate is
 * successfully installed.
 *
 * @author Christian Schlichtherle
 */
public interface LicenseInstalledListener extends java.util.EventListener {
    
    public abstract void licenseInstalled(LicenseInstalledEvent event);
}

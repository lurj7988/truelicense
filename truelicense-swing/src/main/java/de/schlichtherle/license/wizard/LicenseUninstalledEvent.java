/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.license.wizard;

/**
 * Fired when a license certificate has been successfully uninstalled.
 *
 * @author Christian Schlichtherle
 */
public class LicenseUninstalledEvent extends java.util.EventObject {
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new instance of LicenseUninstalledEvent.
     */
    public LicenseUninstalledEvent(Object source) {
        super(source);
    }
}

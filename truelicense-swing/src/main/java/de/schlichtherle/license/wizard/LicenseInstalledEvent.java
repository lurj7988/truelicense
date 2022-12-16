/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.license.wizard;
import de.schlichtherle.license.LicenseContent;

/**
 * Fired when a license certificate has been successfully installed.
 *
 * @author Christian Schlichtherle
 */
public class LicenseInstalledEvent extends java.util.EventObject {
    private static final long serialVersionUID = 1L;
    
    private final LicenseContent content;
    
    /**
     * Creates a new instance of LicenseInstalledEvent.
     *
     * @param content The license content that has been successfully installed.
     */
    public LicenseInstalledEvent(Object source, LicenseContent content) {
        super(source);
        this.content = content;
    }
    
    public LicenseContent getContent() {
        return content;
    }
}

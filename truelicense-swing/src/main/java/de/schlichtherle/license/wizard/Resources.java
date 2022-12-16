/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.license.wizard;

import de.schlichtherle.util.ObfuscatedString;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Looks up the resources for this package in a Resource Bundle. Provided for
 * comfort.
 *
 * @author Christian Schlichtherle
 */
class Resources {

    private static final String CLASS_NAME = new ObfuscatedString(new long[]{
                0x8B9EA304EF608728L, 0xA34ED1A7B378050AL, 0xC9DFE6DD932AC968L,
                0x44605C4A9DD4AA3DL, 0xA5A1963401AAB538L, 0x293B8CF14D848670L,
                0x5DC0C8608F547392L}).toString(); /* => "de.schlichtherle.license.wizard.Resources" */

    private static final ResourceBundle resources
            = ResourceBundle.getBundle(CLASS_NAME);

    /**
     * Looks up a string resource identified by {@code key} in
     * {@code resources}.
     */
    public static String getString(String key) {
        return resources.getString(key);
    }

    /**
     * Looks up a string resource identified by {@code key} in
     * {@code resources} and formats it as a message using
     * {@code MessageFormat.format} with the given {@code arguments}.
     */
    public static String getString(String key, Object[] arguments) {
        return MessageFormat.format(getString(key), arguments);
    }

    /**
     * Looks up a string resource identified by {@code key} in
     * {@code resources} and formats it as a message using
     * {@code MessageFormat.format} with the given singular {@code argument}.
     */
    public static String getString(String key, Object argument) {
        return MessageFormat.format(getString(key), new Object[]{argument});
    }

    /** You cannot instantiate this class. */
    protected Resources() { }
}

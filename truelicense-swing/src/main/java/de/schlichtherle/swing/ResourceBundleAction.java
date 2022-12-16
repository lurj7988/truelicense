/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 * An {@link Action} which loads its properties from a resource bundle,
 * allowing to localize names, tool tips, icons and other properties
 * in a convenient manner.
 * <p>
 * For each action key in the {@code Action} interface, the
 * key is transformed by setting its initial character to lowercase and
 * prepending it with {@code "<i>prefix</i>."}, where
 * {@code <i>prefix</i>} is the parameter provided to the constructor.
 * These transformed keys are then used to lookup the values in the resource
 * bundle and stored as the value for the action key.
 * <p>
 * If the action key is equal to {@code "Name"}(which is the string
 * value of {@link Action#NAME}), then the value in the resource bundle is
 * looked up with just the prefix provided to the constructor as the key in
 * the resource bundle, too.
 * In any case, the key {@code "<i>prefix</i>.name"} takes precedence.
 * If the value contains an ampersand character, the position of the
 * following character is taken as the mnemonic index, unless explicitly set
 * by another entry in resource bundle for the {@link Action#MNEMONIC_KEY}
 * property.
 * <p>
 * If the action key is equal to {@code "SmallIcon"} (which is the string
 * value of {@link Action#SMALL_ICON}), then the value in the resource bundle
 * is processed in the following manner in order to form a relative path name
 * which is then looked up as a resource file on the class path and loaded
 * as an {@link ImageIcon}:
 * <ul>
 * <li>If the value begins with a {@code '/'}, then this character is
 *     cut off and the remaining string is taken as the relative path name.
 * <li>Otherwise, the value is prepended by {@code "images/16x16/actions/"}
 *     to form the relative path name.
 * </ul>
 * <p>
 * <b>Example:</b> Consider a file with the relative path name
 * {@code "com/acme/PlayerPanel.properties"} is present on your
 * class path with the following contents:
 * <pre>
 *  playAction=&Play
 *  playAction.shortDescription=Play/Pause audio.
 *  playAction.smallIcon=player_play.png
 *
 *  pauseAction=&Suspend
 *  pauseAction.name=&Pause
 *  pauseAction.smallIcon=player_pause.png
 *
 *  stopAction.name=&Stop
 *  stopAction.shortDescription=Stop audio.
 *  stopAction.smallIcon=/images/16x16/actions/player_stop.png
 *
 *  ...
 * </pre>
 * Then the following code
 * <pre>
 *  Action play  = new ResourceBundleAction("com.acme.PlayerPanel"     , "playAction"       );
 *  Action pause = new ResourceBundleAction("com.acme.PlayerPanel"     , "pauseAction", play);
 *  Action stop  = new ResourceBundleAction( com.acme.PlayerPanel.class, "stopAction"       );
 * </pre>
 * would create the following actions:
 * <p>
 * The {@code play} action would have the label {@code "Play"}, the
 * tool tip {@code "Play/Pause audio."} and the icon which is loaded from
 * the relative path {@code "images/16x16/actions/player_play.png"} on
 * the class path.
 * <p>
 * The {@code pause} action would have the label {@code "Pause"}
 * (not {@code "Suspend"}), the tool tip {@code "Play/Pause audio."}
 * and the icon which is loaded from the relative path
 * {@code "images/16x16/actions/player_pause.png"} on the class path.
 * Note that this action inherits its tool tip from the {@code play}
 * action.
 * <p>
 * The {@code stop} action would have the label {@code "Stop"}, the
 * tool tip {@code "Stop audio."} and the icon which is loaded from the
 * relative path {@code "images/16x16/actions/player_stop.png"} on the
 * class path.
 * <p>
 * Another difference between these actions is that the {@code run} and
 * {@code pause} actions are processed by the current thread's context
 * class loader, whereas the {@code stop} action is processed using the
 * same class loader which loaded the {@code com.acme.PlayerPanel} class
 * or the system class loader if the former is {@code null} (which would
 * be the case if this class was loaded by the boot strap class loader).
 *
 * @see Action
 *
 * @author Christian Schlichtherle
 * @since TrueSwing 1.28
 */
public abstract class ResourceBundleAction extends AbstractAction {

    private static final String[] actionKeys = (String[]) Collections
            .list(new ActionKeyEnumeration()).toArray(new String[8]);

    private final Action delegate;

    /**
     * Constructs a new {@code ResourceBundleAction}.
     *
     * @param clazz The class which's name and class loader is used to locate
     *        the resource bundle on the class path.
     * @param prefix The prefix to use when looking up the action's properties.
     *        This must <em>not</em> contain the trailing {@code '.'}.
     */
    public ResourceBundleAction(Class clazz, String prefix) {
        this(clazz, prefix, null);
    }

    /**
     * Constructs a new {@code ResourceBundleAction}.
     *
     * @param clazz The class which's name and class loader is used to locate
     *        the resource bundle on the class path.
     * @param prefix The prefix to use when looking up the action's properties.
     *        This must <em>not</em> contain the trailing {@code '.'}.
     * @param delegate The action to inherit values from in case the resource
     *        bundle does not define an own value for this action.
     */
    public ResourceBundleAction(
            final Class clazz,
            final String prefix,
            final Action delegate) {
        this(clazz.getName(), getLoader(clazz), prefix, delegate);
    }

    /**
     * Constructs a new {@code ResourceBundleAction}.
     *
     * @param baseName The base name for the resource bundle.
     *        The current thread's context class loader is used to load the
     *        resource bundle and the resource file for this action's optional
     *        icon.
     * @param prefix The prefix to use when looking up the action's properties.
     *        This must <em>not</em> contain the trailing {@code '.'}.
     */
    public ResourceBundleAction(
            final String baseName,
            final String prefix) {
        this(baseName, Thread.currentThread().getContextClassLoader(), prefix, null);
    }

    /**
     * Constructs a new {@code ResourceBundleAction}.
     *
     * @param baseName The base name for the resource bundle.
     * @param classLoader The class loader used to load the resource bundle
     *        and the resource file for this action's optional icon.
     * @param prefix The prefix to use when looking up the action's properties.
     *        This must <em>not</em> contain the trailing {@code '.'}.
     */
    public ResourceBundleAction(
            final String baseName,
            final ClassLoader classLoader,
            final String prefix) {
        this(baseName, classLoader, prefix, null);
    }

    /**
     * Constructs a new {@code ResourceBundleAction}.
     *
     * @param baseName The base name for the resource bundle.
     * @param classLoader The class loader used to load the resource bundle
     *        and the resource file for this action's optional icon.
     * @param prefix The prefix to use when looking up the action's properties.
     *        This must <em>not</em> contain the trailing {@code '.'}.
     * @param delegate The action to inherit values from in case the resource
     *        bundle does not define an own value for this action.
     */
    public ResourceBundleAction(
            final String baseName,
            final ClassLoader classLoader,
            final String prefix,
            final Action delegate) {
        this.delegate = delegate;

        loadResources(baseName, classLoader, prefix);
    }

    private static ClassLoader getLoader(final Class clazz) {
        final ClassLoader cl = clazz.getClassLoader();
        if (cl != null)
            return cl;
        else
            return ClassLoader.getSystemClassLoader();
    }

    private void loadResources(
            final String baseName,
            final ClassLoader classLoader,
            String prefix) {

        final ResourceBundle resources = ResourceBundle.getBundle(
                baseName, Locale.getDefault(), classLoader);

        // Load the value for the prefix for the name, allowing it to be
        // overridden by the key (prefix + ".name").
        String nameValue;
        try {
            nameValue = resources.getString(prefix);
        } catch (MissingResourceException ignored) {
            // The resource bundle does not have a value for this key.
            nameValue = null;
        }
        Integer mnemonicValue = null;

        prefix += '.';

        final int l = actionKeys.length;
        for (int i = 0; i < l; i++) {
            final String actionKey = actionKeys[i];
            final String resourceKey = prefix + fixCase(actionKey);
            try {
                String resourceValue = resources.getString(resourceKey);
                assert resourceValue != null;
                if (NAME.equals(actionKey)) {
                    nameValue = resourceValue; // defer processing
                } else if (SMALL_ICON.equals(actionKey)) {
                    if (resourceValue.charAt(0) == '/')
                        resourceValue = resourceValue.substring(1);
                    else
                        resourceValue = "images/16x16/actions/" + resourceValue;
                    final URL resource = classLoader.getResource(resourceValue);
                    putValue(SMALL_ICON, new ImageIcon(resource)); // may throw NPE
                } else if (ACCELERATOR_KEY.equals(actionKey)) {
                    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(resourceValue));
                } else if (MNEMONIC_KEY.equals(actionKey)) {
                    try {
                        mnemonicValue = Integer.decode(resourceValue);
                    } catch (NumberFormatException ex) {
                        mnemonicValue = new Integer(resourceValue.charAt(0));
                    }
                } else {
                    putValue(actionKey, resourceValue);
                }
            } catch (MissingResourceException ignored) {
                // The resource bundle does not have a value for this key.
            }
        }

        if (nameValue != null) {
            // Post process value of NAME, but give any value explicitly
            // provided for MNEMONIC_KEY precedence.
            final MnemonicText mt = new MnemonicText(nameValue);
            final String text = mt.getText();
            putValue(NAME, text);
            if (mnemonicValue == null && mt.getMnemonicIndex() >= 0)
                mnemonicValue = new Integer(mt.getMnemonic());
            if (mnemonicValue != null)
                putValue(MNEMONIC_KEY, mnemonicValue);
        }
    }

    private static String fixCase(final String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public Object getValue(String key) {
        final Object value = super.getValue(key);
        if (value != null)
            return value;
        else if (delegate != null)
            return delegate.getValue(key);
        else
            return null;
    }

    private static class ActionKeyEnumeration implements Enumeration {
        private final Field[] fields;
        private int i;
        private Object key;

        public ActionKeyEnumeration() {
            fields = Action.class.getFields();
        }

        public boolean hasMoreElements() {
            if (key != null)
                return true;

            try {
                while (i < fields.length) {
                    final Field field = fields[i++];
                    final int modifiers = field.getModifiers();
                    if (!Modifier.isPublic(modifiers) || !Modifier.isStatic(modifiers))
                        continue;
                    final Object value = field.get(null);
                    if (value instanceof String) {
                        key = value;
                        return true;
                    }
                }
            } catch (IllegalAccessException ex) {
                throw new AssertionError(ex);
            }

            return false;
        }

        public Object nextElement() {
            if (!hasMoreElements())
                throw new NoSuchElementException();

            try {
                return key;
            } finally {
                key = null;
            }
        }
    }
}

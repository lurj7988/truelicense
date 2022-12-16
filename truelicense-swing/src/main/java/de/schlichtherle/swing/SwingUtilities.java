/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Window;

/**
 * Provides static utility methods for Swing.
 * 
 * @author Christian Schlichtherle
 */
public class SwingUtilities {

    /**
     * Executes the given runnable from the AWT Event Dispatcher Thread (EDT),
     * even if this method is executed by the EDT.
     * 
     * @param  r the runnable to execute.
     * @throws java.lang.reflect.InvocationTargetException Iff the current
     *         thread is <em>not</em> the EDT and the given runnable throws an
     *         exception.
     */
    public static void runOnEventDispatchThread(final Runnable r)
    throws java.lang.reflect.InvocationTargetException {
        if (EventQueue.isDispatchThread()) {
            r.run();
        } else {
            try {
                EventQueue.invokeAndWait(r);
            } catch (InterruptedException ex) {
                ex.printStackTrace(); // TODO
            }
        }
    }

    /**
     * Returns the ancestor {@link Window} of the given {@code component} or
     * {@code null} if the component is not (yet) placed in a {@code Window}.
     * 
     * @param  component the component to start searching for an ancestor
     *         window.
     * @return the nullable ancestor window. 
     */
    public static Window getAncestorWindow(final Component component) {
        Container parent = component.getParent();
        while (parent != null && !(parent instanceof Window))
            parent = parent.getParent();
        return (Window) parent;
    }

    protected SwingUtilities() { } // You cannot instantiate this class
}

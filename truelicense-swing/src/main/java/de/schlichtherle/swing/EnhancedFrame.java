/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.swing;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;

/**
 * Adds some enhancements to a {@link JFrame}.
 * <p>
 * <ul>
 * <li>It offers the method {@code setDefaultButton(JButton)} to compensate
 *     for a deficiency of many look and feels which do not highlight the
 *     default button.
 * <li>The default button is highlighted in bold face and it requests the
 *     focus whenever this {@code Component} is shown.
 * <li>It looks up the resource file
 *     {@code "images/applIcon.[png|jpeg|jpg|gif]"} or
 *     {@code "resources/applIcon.[png|jpeg|jpg|gif]"} on the class path,
 *     loads it and uses it as the minimized icon for this frame.
 * </ul>
 *
 * @author Christian Schlichtherle
 */
public class EnhancedFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String suffixes[] = {
        ".png",
        ".jpeg",
        ".jpg",
        ".gif",
    };
    
    //
    // Constructors.
    //
    
    public EnhancedFrame() throws HeadlessException {
        super();
        initComponents();
    }

    public EnhancedFrame(GraphicsConfiguration gc) {
        super(gc);
        initComponents();
    }

    public EnhancedFrame(String title) throws HeadlessException {
        super(title);
        initComponents();
    }
    
    public EnhancedFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
        initComponents();
    }
    
    /**
     * This method is called from all constructors.
     */
    private void initComponents() {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();

        URL url = null;
        for (int i = 0, l = suffixes.length; i < l && url == null; i++) {
            final String suffix = suffixes[i];
            url = cl.getResource("images/applIcon" + suffix);
            if (url == null)
                url = cl.getResource("resources/applIcon" + suffix);
        }
        if (url != null)
            setIconImage(Toolkit.getDefaultToolkit().getImage(url));
    }

    protected JRootPane createRootPane() {
        return new EnhancedRootPane();
    }
    
    /**
     * Boldfaces {@code button} and sets it as the default button for this
     * dialog.
     * This method compensates for some look and feels which do not highlight
     * the default button.
     * <p>
     * Make sure that the given button really is on this dialog.
     */
    public void setDefaultButton(JButton button) {
        final JRootPane rp = getRootPane();
        if (rp instanceof EnhancedRootPane)
            ((EnhancedRootPane) rp).setRestorableDefaultButton(button);
        else
            rp.setDefaultButton(button);
    }
    
    public JButton getDefaultButton() {
        return getRootPane().getDefaultButton();
    }
}

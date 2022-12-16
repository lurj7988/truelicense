/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package com.nexes.wizard;

import de.schlichtherle.swing.EnhancedButton;
import de.schlichtherle.swing.EnhancedDialog;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Implements a basic wizard dialog, where the developer can insert one or more
 * Components to act as panels. These panels can be navigated through
 * arbitrarily using the 'Next' or 'Back' buttons, or the dialog itself can be
 * closed using the 'Cancel' button. Note that even though the dialog uses a
 * CardLayout manager, the order of the panels is not necessarily linear. Each
 * panel determines at runtime what its next and previous panel should be.
 * 
 * @author Robert Eckstein (original code)
 * @author Christian Schlichtherle (revision)
 */
public class Wizard implements PropertyChangeListener {

    /**
     * Indicates that the 'Finish' button was pressed to close the dialog.
     */
    public static final int FINISH_RETURN_CODE = 0;
    /**
     * Indicates that the 'Cancel' button was pressed to close the dialog.
     */
    public static final int CANCEL_RETURN_CODE = 1;
    /**
     * Indicates that the dialog closed due to an internal error.
     */
    public static final int ERROR_RETURN_CODE = 2;
    /**
     * The String-based action command for the 'Next' button.
     */
    public static final String NEXT_BUTTON_ACTION_COMMAND
            = "NextButtonActionCommand"; // NOI18N
    /**
     * The String-based action command for the 'Back' button.
     */
    public static final String BACK_BUTTON_ACTION_COMMAND
            = "BackButtonActionCommand"; // NOI18N
    /**
     * The String-based action command for the 'Cancel' button.
     */
    public static final String CANCEL_BUTTON_ACTION_COMMAND
            = "CancelButtonActionCommand"; // NOI18N
    private static final ResourceBundle resources
            = ResourceBundle.getBundle("com/nexes/wizard/Resources"); // beware of code obfuscation!
    /**
     * The default text used for the 'Back' button. Good candidate for i18n.
     */
    public static final String DEFAULT_BACK_BUTTON_TEXT
            = resources.getString("backButton");
    /**
     * The default text used for the 'Next' button. Good candidate for i18n.
     */
    public static final String DEFAULT_NEXT_BUTTON_TEXT
            = resources.getString("nextButton");
    /**
     * The default text used for the 'Finish' button. Good candidate for i18n.
     */
    public static final String DEFAULT_FINISH_BUTTON_TEXT
            = resources.getString("finishButton");
    /**
     * The default text used for the 'Cancel' button. Good candidate for i18n.
     */
    public static final String DEFAULT_CANCEL_BUTTON_TEXT
            = resources.getString("cancelButton");
    private WizardModel wizardModel;
    private WizardController wizardController;
    private EnhancedDialog wizardDialog;
    private int returnCode;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private EnhancedButton backButton;
    private EnhancedButton nextButton;
    private EnhancedButton cancelButton;

    /**
     * Default constructor. This method creates a new WizardModel object and
     * passes it into the overloaded constructor.
     */
    public Wizard() {
        this(new WizardModel(), (Frame) null);
    }

    /**
     * This method creates a new WizardModel object and passes it into the
     * overloaded constructor. It accepts a java.awt.Dialog object as the
     * javax.swing.JDialog's parent.
     *
     * @param owner The java.awt.Dialog object that is the owner of this dialog.
     */
    public Wizard(Dialog owner) {
        this(new WizardModel(), owner);
    }

    /**
     * This method creates a new WizardModel object and passes it into the
     * overloaded constructor. It accepts a java.awt.Dialog object as the
     * javax.swing.JDialog's parent.
     *
     * @param owner The java.awt.Frame object that is the owner of the
     * javax.swing.JDialog.
     */
    public Wizard(Frame owner) {
        this(new WizardModel(), owner);
    }

    /**
     * This constructor accepts a WizardModel object and a java.awt.Dialog
     * object as the wizard's JDialog's parent.
     *
     * @param model The WizardModel object that serves as the model for the
     * wizard dialog.
     * @param owner The java.awt.Dialog that is the owner of the generated
     * javax.swing.JDialog.
     */
    public Wizard(WizardModel model, Dialog owner) {
        wizardDialog = new EnhancedDialog(owner);
        wizardModel = model;
        initComponents();
    }

    /**
     * This constructor accepts a WizardModel object and a java.awt.Frame object
     * as the wizard's javax.swing.JDialog's parent.
     *
     * @param model The WizardModel object that serves as the model for this
     * component.
     * @param owner The java.awt.Frame object that serves as the parent of the
     * generated javax.swing.JDialog.
     */
    public Wizard(WizardModel model, Frame owner) {
        wizardDialog = new EnhancedDialog(owner);
        wizardModel = model;
        initComponents();
    }

    /**
     * Returns an instance of the JDialog that this class created. This is
     * useful in the event that you want to change any of the JDialog parameters
     * manually.
     *
     * @return The JDialog instance that this class created.
     */
    public JDialog getDialog() {
        return wizardDialog;
    }

    /**
     * Returns the owner of the generated javax.swing.JDialog.
     *
     * @return The owner (java.awt.Frame or java.awt.Dialog) of the
     * javax.swing.JDialog generated by this class.
     */
    public Component getOwner() {
        return wizardDialog.getOwner();
    }

    /**
     * Returns the current title of the generated dialog.
     *
     * @return The String-based title of the generated dialog.
     */
    public String getTitle() {
        return wizardDialog.getTitle();
    }

    /**
     * Sets the title of the generated javax.swing.JDialog.
     *
     * @param s The title of the dialog.
     */
    public void setTitle(String s) {
        wizardDialog.setTitle(s);
    }

    /**
     * Returns the modality of the dialog.
     *
     * @return A boolean indicating whether or not the generated
     * javax.swing.JDialog is modal.
     */
    public boolean isModal() {
        return wizardDialog.isModal();
    }

    /**
     * Sets the modality of the generated javax.swing.JDialog.
     *
     * @param b the modality of the dialog
     */
    public void setModal(boolean b) {
        wizardDialog.setModal(b);
    }

    /**
     * Convenience method that packs and displays the modal wizard dialog.
     *
     * @return An integer that identifies how the dialog was closed.
     *         See RETURN_CODE constants at the beginning of this class.
     * @throws NullPointerException If the panel to be displayed initially has
     *         not been set.
     * @see    #setCurrentPanel(Object)
     */
    public int showModalDialog() {
        setModal(true);
        getDialog().setVisible(true);
        return returnCode;
    }

    /**
     * Returns the current model of the wizard dialog.
     *
     * @return A WizardModel instance, which serves as the model for the wizard
     * dialog.
     */
    public WizardModel getModel() {
        return wizardModel;
    }

    /**
     * Add a Component as a panel for the wizard dialog by registering its
     * {@link WizardPanelDescriptor} object. Each panel is identified by a
     * unique Object-based identifier (often a String), which can be used by the {@link #setCurrentPanel(Object)}
     * method to display the panel at runtime.
     *
     * @param id An Object-based identifier used to identify the
     *        {@code WizardPanelDescriptor} object.
     * @param panel The {@code WizardPanelDescriptor} object which contains
     * helpful information about the panel.
     */
    public void registerWizardPanel(Object id, WizardPanelDescriptor panel) {
        //  Add the incoming panel to our JPanel display that is managed by
        //  the CardLayout layout manager.
        cardPanel.add(panel.getPanelComponent(), id);

        //  Set a callback to the current wizard.
        panel.setWizard(this);

        //  Place a reference to it in the model. 
        wizardModel.registerPanel(id, panel);

        getDialog().pack();
    }

    /**
     * Displays the panel identified by the object passed in. This is the same
     * Object-based identifier used when registering the panel.
     *
     * @param id The Object-based identifier of the panel to be displayed.
     */
    public void setCurrentPanel(Object id) {
        //  Get the hashtable reference to the panel that should
        //  be displayed. If the identifier passed in is null, then close
        //  the dialog.
        if (null == id) close(ERROR_RETURN_CODE);

        final WizardPanelDescriptor opd = wizardModel.getCurrentPanelDescriptor();
        if (null != opd) opd.aboutToHidePanel();

        wizardModel.setCurrentPanel(id);
        wizardModel.getCurrentPanelDescriptor().aboutToDisplayPanel();
        cardLayout.show(cardPanel, id.toString());
        wizardModel.getCurrentPanelDescriptor().displayingPanel();
    }

    /**
     * Method used to listen for property change events from the model and
     * update the dialog's graphical components as necessary.
     *
     * @param evt PropertyChangeEvent passed from the model to signal that one
     *        of its properties has changed value.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WizardModel.CURRENT_PANEL_DESCRIPTOR_PROPERTY))
            wizardController.resetButtonsToPanelRules();
        else if (evt.getPropertyName().equals(WizardModel.BACK_BUTTON_TEXT_PROPERTY))
            backButton.setText(evt.getNewValue().toString());
        else if (evt.getPropertyName().equals(WizardModel.NEXT_BUTTON_TEXT_PROPERTY))
            nextButton.setText(evt.getNewValue().toString());
        else if (evt.getPropertyName().equals(WizardModel.CANCEL_BUTTON_TEXT_PROPERTY))
            cancelButton.setText(evt.getNewValue().toString());
        else if (evt.getPropertyName().equals(WizardModel.BACK_BUTTON_ENABLED_PROPERTY))
            backButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
        else if (evt.getPropertyName().equals(WizardModel.NEXT_BUTTON_ENABLED_PROPERTY))
            nextButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
        else if (evt.getPropertyName().equals(WizardModel.CANCEL_BUTTON_ENABLED_PROPERTY))
            cancelButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
        else if (evt.getPropertyName().equals(WizardModel.BACK_BUTTON_ICON_PROPERTY))
            backButton.setIcon((Icon) evt.getNewValue());
        else if (evt.getPropertyName().equals(WizardModel.NEXT_BUTTON_ICON_PROPERTY))
            nextButton.setIcon((Icon) evt.getNewValue());
        else if (evt.getPropertyName().equals(WizardModel.CANCEL_BUTTON_ICON_PROPERTY))
            cancelButton.setIcon((Icon) evt.getNewValue());
    }

    /**
     * Retrieves the last return code set by the dialog.
     *
     * @return An integer that identifies how the dialog was closed.
     *         See RETURN_CODE constants at the beginning of this class.
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * Closes the dialog and sets the return code to the integer parameter.
     *
     * @param code The return code.
     */
    void close(int code) {
        returnCode = code;
        wizardDialog.setVisible(false);
    }

    /**
     * This method initializes the components for the wizard dialog: it creates
     * a JDialog with a CardLayout panel with a small empty border, as well as
     * three buttons at the bottom.
     */
    private void initComponents() {
        wizardModel.addPropertyChangeListener(this);
        wizardController = new WizardController(this);

        backButton = new EnhancedButton();
        backButton.setName("back");
        backButton.setActionCommand(BACK_BUTTON_ACTION_COMMAND);
        backButton.addActionListener(wizardController);

        nextButton = new EnhancedButton();
        nextButton.setName("next");
        nextButton.setActionCommand(NEXT_BUTTON_ACTION_COMMAND);
        nextButton.addActionListener(wizardController);

        cancelButton = new EnhancedButton();
        cancelButton.setName("cancel");
        cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);
        cancelButton.addActionListener(wizardController);

        final Border border = new EmptyBorder(new Insets(10, 10, 10, 10));

        final Box buttonBox = new Box(BoxLayout.LINE_AXIS);
        buttonBox.setBorder(border);
        buttonBox.add(backButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(cancelButton);

        //  Create the buttons with a separator above them, then place them
        //  on the east side of the panel with a small amount of space between
        //  the back and the next button, and a larger amount of space between
        //  the next button and the cancel button.
        final JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(new JSeparator(), BorderLayout.NORTH);
        buttonPanel.add(buttonBox, BorderLayout.EAST);

        cardLayout = new CardLayout();

        cardPanel = new JPanel(cardLayout);
        cardPanel.setBorder(border);

        wizardDialog.setName("wizardDialog");
        wizardDialog.setLayout(new BorderLayout());
        wizardDialog.add(cardPanel, BorderLayout.CENTER);
        wizardDialog.add(buttonPanel, BorderLayout.SOUTH);
        wizardDialog.setDefaultButton(nextButton);

        if (null == wizardModel.getBackButtonText())
            wizardModel.setBackButtonText(Wizard.DEFAULT_BACK_BUTTON_TEXT);
        if (null == wizardModel.getNextButtonText())
            wizardModel.setNextButtonText(Wizard.DEFAULT_NEXT_BUTTON_TEXT);
        if (null == wizardModel.getCancelButtonText())
            wizardModel.setCancelButtonText(Wizard.DEFAULT_CANCEL_BUTTON_TEXT);
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.gui.dialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ExecutionException;

import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

public class CommonDialogs {
    // Fields
    static BufferedImageIcon ICON = null;
    static String DEFAULT_TITLE = null;
    public static final int YES_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int DEFAULT_OPTION = -1;
    public static final int CANCEL = -1;

    private static String getExtension(final String s) {
        String ext = null;
        final var i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i).toLowerCase();
        }
        return ext;
    }

    private static String getNameWithoutExtension(final String s) {
        String ext = null;
        final var i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(0, i);
        } else {
            ext = s;
        }
        return ext;
    }

    /**
     * Gets the image to use instead of the default icons.
     *
     * @return The icon image, as a BufferedImageIcon from the Graphics library.
     */
    public static BufferedImageIcon icon() {
        return CommonDialogs.ICON;
    }

    /**
     * Sets the default title for dialogs.
     *
     * @param title The default title
     */
    public static void setDefaultTitle(final String title) {
        CommonDialogs.DEFAULT_TITLE = title;
    }

    /**
     * Sets the image to use instead of the default icons.
     *
     * @param icon The image - should be a BufferedImageIcon from the Graphics
     *             library.
     */
    public static void setIcon(final BufferedImageIcon icon) {
        CommonDialogs.ICON = icon;
    }

    /**
     * Displays a yes/no confirm dialog.
     *
     * @param prompt The confirmation prompt.
     * @param title  The dialog title.
     * @return A JOptionPane constant specifying what the user clicked.
     */
    public static int showConfirmDialog(final String prompt, final String title) {
        try {
            return InputDialog.showConfirmDialog(prompt, title, CommonDialogs.ICON).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return CommonDialogs.CANCEL;
        }
    }

    public static int showCustomDialog(final String prompt, final String title, final String[] buttonNames) {
        try {
            return InputDialog.showDialog(prompt, title, CommonDialogs.ICON, buttonNames).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return CommonDialogs.CANCEL;
        }
    }

    public static int showCustomDialogWithDefault(final String prompt, final String title, final String[] buttonNames,
            final String defaultButton) {
        try {
            return InputWithDefaultDialog.showDialog(prompt, title, CommonDialogs.ICON, buttonNames, defaultButton)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return CommonDialogs.CANCEL;
        }
    }

    // Methods
    /**
     * Displays a dialog.
     *
     * @param msg The dialog message.
     */
    public static void showDialog(final String msg) {
        new ShowDialogTask(msg).run();
    }

    // Methods
    /**
     * Displays a dialog in a new thread.
     *
     * @param msg The dialog message.
     */
    public static void showDialogLater(final String msg) {
        AppTaskManager.runTask(new ShowDialogTask(msg));
    }

    /**
     * Displays an error dialog with the default title.
     *
     * @param msg The dialog message.
     */
    public static void showErrorDialog(final String msg) {
        new ShowDialogTask(msg).run();
    }

    /**
     * Displays an error dialog with a title.
     *
     * @param msg   The dialog message.
     * @param title The dialog title.
     */
    public static void showErrorDialog(final String msg, final String title) {
        new ShowDialogWithTitleTask(msg, title).run();
    }

    /**
     * Displays an error dialog with the default title in a new thread.
     *
     * @param msg The dialog message.
     */
    public static void showErrorDialogLater(final String msg) {
        AppTaskManager.runTask(new ShowDialogTask(msg));
    }

    /**
     * Displays an error dialog with a title in a new thread.
     *
     * @param msg   The dialog message.
     * @param title The dialog title.
     */
    public static void showErrorDialogLater(final String msg, final String title) {
        AppTaskManager.runTask(new ShowDialogWithTitleTask(msg, title));
    }

    public static File showFileOpenDialog(final File dir, final FilenameFilter filter, final String prompt) {
        final var choices = dir.list(filter);
        if (choices == null || choices.length == 0) {
            Sounds.play(Sound.WARNING);
            CommonDialogs.showErrorDialog(Strings.loadDialog(DialogString.EMPTY_FILE_LIST),
                    Strings.loadDialog(DialogString.LOAD));
        } else {
            final var ext = CommonDialogs.getExtension(choices[0]);
            for (var z = 0; z < choices.length; z++) {
                choices[z] = CommonDialogs.getNameWithoutExtension(choices[z]);
            }
            final var value = CommonDialogs.showInputDialog(prompt, Strings.loadDialog(DialogString.LOAD), choices,
                    choices[0]);
            if (value != null) {
                return new File(dir.getAbsolutePath() + File.separator + value + ext);
            }
        }
        return null;
    }

    public static File showFileSaveDialog(final File dir, final String prompt) {
        final var value = CommonDialogs.showTextInputDialog(prompt, Strings.loadDialog(DialogString.SAVE));
        if (value != null) {
            return new File(dir.getAbsolutePath() + File.separator + value);
        }
        return null;
    }

    public static int showImageListDialog(final String labelText, final String title,
            final BufferedImageIcon[] possibleValues, final int initialValue) {
        try {
            return ImageListDialog.showDialog(labelText, title, possibleValues, initialValue).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return CommonDialogs.CANCEL;
        }
    }

    public static int showImageListWithDescDialog(final String labelText, final String title,
            final BufferedImageIcon[] possibleValues, final int initialValue, final String descValue,
            final String... possibleDescriptions) {
        try {
            return ImageListWithDescDialog
                    .showDialog(labelText, title, possibleValues, initialValue, descValue, possibleDescriptions).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return CommonDialogs.CANCEL;
        }
    }

    /**
     * Displays an input dialog, allowing the user to pick from a list.
     *
     * @param prompt        The input prompt.
     * @param title         The dialog title.
     * @param choices       The list of choices.
     * @param defaultChoice The default choice, which should be one of the list
     *                      entries.
     * @return The choice picked
     */
    public static String showInputDialog(final String prompt, final String title, final String[] choices,
            final String defaultChoice) {
        try {
            return ListDialog.showDialog(prompt, title, CommonDialogs.ICON, choices, defaultChoice).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return null;
        }
    }

    public static String showListWithDescDialog(final String labelText, final String title,
            final String[] possibleValues, final String initialValue, final String descValue,
            final String... possibleDescriptions) {
        try {
            return ListWithDescDialog
                    .showDialog(labelText, title, possibleValues, initialValue, descValue, possibleDescriptions).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return null;
        }
    }

    /**
     * Displays a password input dialog, allowing the user to enter a value.
     *
     * @param prompt The input prompt.
     * @param title  The dialog title.
     * @return The value the user input.
     */
    public static char[] showPasswordInputDialog(final String prompt, final String title) {
        try {
            return PasswordInputDialog.showDialog(prompt, title, CommonDialogs.ICON).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return null;
        }
    }

    /**
     * Displays a text input dialog, allowing the user to enter a value.
     *
     * @param prompt The input prompt.
     * @param title  The dialog title.
     * @return The value the user input.
     */
    public static String showTextInputDialog(final String prompt, final String title) {
        try {
            return TextInputDialog.showDialog(prompt, title, CommonDialogs.ICON, null).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return null;
        }
    }

    /**
     * Displays a text input dialog, allowing the user to enter a value.
     *
     * @param prompt The input prompt.
     * @param title  The dialog title.
     * @return The value the user input.
     */
    public static String showTextInputDialogWithDefault(final String prompt, final String title,
            final String defaultValue) {
        try {
            return TextInputDialog.showDialog(prompt, title, CommonDialogs.ICON, defaultValue).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return null;
        }
    }

    /**
     * Displays a dialog with a title.
     *
     * @param msg   The dialog message.
     * @param title The dialog title.
     */
    public static void showTitledDialog(final String msg, final String title) {
        new ShowDialogWithTitleTask(msg, title).run();
    }

    /**
     * Displays a dialog with a title in a new thread.
     *
     * @param msg   The dialog message.
     * @param title The dialog title.
     */
    public static void showTitledDialogLater(final String msg, final String title) {
        AppTaskManager.runTask(new ShowDialogWithTitleTask(msg, title));
    }

    /**
     * Displays a yes/no/cancel confirm dialog.
     *
     * @param prompt The confirmation prompt.
     * @param title  The dialog title.
     * @return A JOptionPane constant specifying what the user clicked.
     */
    public static int showYNCConfirmDialog(final String prompt, final String title) {
        try {
            return InputDialog.showYNCConfirmDialog(prompt, title, CommonDialogs.ICON).get();
        } catch (InterruptedException | ExecutionException e) {
            AppTaskManager.error(e);
            return CommonDialogs.CANCEL;
        }
    }

    // Constructor
    private CommonDialogs() {
        // Do nothing
    }
}

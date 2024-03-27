package com.puttysoftware.lasertank.gui.dialog;

import java.util.concurrent.ExecutionException;

import com.puttysoftware.lasertank.error.ErrorHandlerInstaller;

class ShowDialogWithTitleTask implements Runnable {
    private final String msg, title;

    ShowDialogWithTitleTask(final String message, final String customTitle) {
	this.msg = message;
	this.title = customTitle;
    }

    @Override
    public void run() {
	try {
	    GeneralDialog.showDialog(this.msg, this.title, CommonDialogs.ICON).get();
	} catch (InterruptedException | ExecutionException e) {
	    ErrorHandlerInstaller.handleError(e);
	}
    }
}

package com.puttysoftware.lasertank.gui.dialog;

import java.util.concurrent.ExecutionException;

import com.puttysoftware.lasertank.error.ErrorHandlerInstaller;

class ShowDialogTask implements Runnable {
    private final String msg;

    ShowDialogTask(final String message) {
	this.msg = message;
    }

    @Override
    public void run() {
	try {
	    GeneralDialog.showDialog(this.msg, CommonDialogs.DEFAULT_TITLE, CommonDialogs.ICON).get();
	} catch (InterruptedException | ExecutionException e) {
	    ErrorHandlerInstaller.handleError(e);
	}
    }
}

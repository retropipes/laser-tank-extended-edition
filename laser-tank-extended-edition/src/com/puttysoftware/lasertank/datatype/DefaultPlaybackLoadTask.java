package com.puttysoftware.lasertank.datatype;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.lasertank.fileio.utility.ResourceStreamReader;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

class DefaultPlaybackLoadTask implements Runnable {
	private static String[] DEFAULT_PLAYBACK_CACHE;

	private static String[] defaultChoicesList() {
		if (DefaultPlaybackLoadTask.DEFAULT_PLAYBACK_CACHE == null) {
			final var temp = new ArrayList<String>();
			final var llpath = "/asset/list/v4playbacks.list"; //$NON-NLS-1$
			try (final var is = LaserTankPlayback.class.getResourceAsStream(llpath);
					final var rsr = new ResourceStreamReader(is, "UTF-8")) { //$NON-NLS-1$
				var line = Strings.loadCommon(CommonString.EMPTY);
				while (line != null) {
					// Read line
					line = rsr.readString();
					if (line != null) {
						// Parse line
						temp.add(line);
					}
				}
			} catch (final IOException ioe) {
				AppTaskManager.logErrorDirectly(ioe);
			}
			final var size = temp.size();
			DefaultPlaybackLoadTask.DEFAULT_PLAYBACK_CACHE = temp.toArray(new String[size]);
		}
		return DefaultPlaybackLoadTask.DEFAULT_PLAYBACK_CACHE;
	}

	@Override
	public void run() {
		final var choices = DefaultPlaybackLoadTask.defaultChoicesList();
		final var filename = CommonDialogs.showInputDialog(null, null, choices, choices[0]);
		if (filename != null) {
			new LaserTankPlaybackLoadTask(new File(filename)).run();
		}
	}
}

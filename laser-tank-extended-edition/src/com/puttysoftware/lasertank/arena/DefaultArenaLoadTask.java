package com.puttysoftware.lasertank.arena;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.lasertank.datatype.LaserTankV4LevelLoadTask;
import com.puttysoftware.lasertank.fileio.utility.ResourceStreamReader;
import com.puttysoftware.lasertank.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.tasks.AppTaskManager;

class DefaultArenaLoadTask implements Runnable {
	private static String[] DEFAULT_LEVEL_CACHE;

	private static String[] defaultChoicesList() {
		if (DefaultArenaLoadTask.DEFAULT_LEVEL_CACHE == null) {
			final var temp = new ArrayList<String>();
			final var llpath = "/asset/list/v4levels.list"; //$NON-NLS-1$
			try (final var is = ArenaManager.class.getResourceAsStream(llpath);
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
			DefaultArenaLoadTask.DEFAULT_LEVEL_CACHE = temp.toArray(new String[size]);
		}
		return DefaultArenaLoadTask.DEFAULT_LEVEL_CACHE;
	}

	@Override
	public void run() {
		final var choices = DefaultArenaLoadTask.defaultChoicesList();
		final var filename = CommonDialogs.showInputDialog(null, null, choices, choices[0]);
		if (filename != null) {
			final var ollt = new LaserTankV4LevelLoadTask(filename);
			ollt.run();
		}
	}
}

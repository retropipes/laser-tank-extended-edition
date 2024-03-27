package com.puttysoftware.lasertank.utility;

public class TaskRunner {
    public static void runTask(final Runnable task) {
	Thread.ofPlatform().start(task);
    }

    public static Thread runTrackedTask(final Runnable task) {
	return Thread.ofPlatform().start(task);
    }

    private TaskRunner() {
    }
}

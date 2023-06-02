/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.asset.music;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

public class LTEMusicPlayer {
    private static final int SAMPLE_RATE = 41000;
    private static Module module;
    static IBXM ibxm;
    volatile static boolean playing;
    private static int interpolation;
    private static Thread playThread;

    synchronized static int getAudio(final int[] mixBuf) {
	return LTEMusicPlayer.ibxm.getAudio(mixBuf);
    }

    public static boolean isPlaying() {
	return LTEMusicPlayer.playThread != null && LTEMusicPlayer.playThread.isAlive();
    }

    public synchronized static void play(final LTEMusicIndex index) throws IOException {
	if (LTEMusicPlayer.isPlaying()) {
	    LTEMusicPlayer.stopPlaying();
	}
	final var source = index.getURL();
	try (var inputStream = source.openStream()) {
	    final var moduleData = inputStream.readAllBytes();
	    LTEMusicPlayer.module = new Module(moduleData);
	    LTEMusicPlayer.ibxm = new IBXM(LTEMusicPlayer.module, LTEMusicPlayer.SAMPLE_RATE);
	    LTEMusicPlayer.ibxm.setInterpolation(LTEMusicPlayer.interpolation);
	    LTEMusicPlayer.playing = true;
	    LTEMusicPlayer.playThread = new Thread(() -> {
		final var mixBuf = new int[LTEMusicPlayer.ibxm.getMixBufferLength()];
		final var outBuf = new byte[mixBuf.length * 4];
		final var audioFormat = new AudioFormat(LTEMusicPlayer.SAMPLE_RATE, 16, 2, true, true);
		try (var audioLine = AudioSystem.getSourceDataLine(audioFormat)) {
		    audioLine.open();
		    audioLine.start();
		    while (LTEMusicPlayer.playing) {
			final var count = LTEMusicPlayer.getAudio(mixBuf);
			var outIdx = 0;
			for (int mixIdx = 0, mixEnd = count * 2; mixIdx < mixEnd; mixIdx++) {
			    var ampl = mixBuf[mixIdx];
			    if (ampl > 32767) {
				ampl = 32767;
			    }
			    if (ampl < -32768) {
				ampl = -32768;
			    }
			    outBuf[outIdx] = (byte) (ampl >> 8);
			    outIdx++;
			    outBuf[outIdx] = (byte) ampl;
			    outIdx++;
			}
			audioLine.write(outBuf, 0, outIdx);
		    }
		    audioLine.drain();
		} catch (final Exception e) {
		    // Ignore
		}
	    });
	    LTEMusicPlayer.playThread.start();
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    public synchronized static void stopPlaying() {
	LTEMusicPlayer.playing = false;
	try {
	    if (LTEMusicPlayer.playThread != null) {
		LTEMusicPlayer.playThread.join();
	    }
	} catch (final InterruptedException e) {
	}
    }

    public LTEMusicPlayer() {
	// Do nothing
    }
}

/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.asset.sound;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

class SoundResource implements Runnable {
	private final URL soundURL;

	public SoundResource(final URL resURL) {
		this.soundURL = resURL;
	}

	@Override
	public void run() {
		if (this.soundURL != null) {
			try (var inputStream = SoundResource.this.soundURL.openStream()) {
				try (var audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
					final var format = audioInputStream.getFormat();
					final var info = new DataLine.Info(SourceDataLine.class, format);
					try (var res = AudioSystem.getLine(info); var line = (SourceDataLine) res) {
						if (line != null) {
							line.open(format);
							line.start();
							var nBytesRead = 0;
							final var abData = new byte[SoundPlayer.EXTERNAL_BUFFER_SIZE];
							try {
								while (nBytesRead != -1) {
									nBytesRead = audioInputStream.read(abData, 0, abData.length);
									if (nBytesRead >= 0) {
										line.write(abData, 0, nBytesRead);
									}
								}
							} catch (final IOException e) {
								throw new SoundException(e);
							} finally {
								line.drain();
							}
						}
					} catch (final LineUnavailableException e) {
						throw new SoundException(e);
					}
				} catch (final UnsupportedAudioFileException | IOException e) {
					throw new SoundException(e);
				}
			} catch (final IOException e) {
				throw new SoundException(e);
			}
		}
	}
}

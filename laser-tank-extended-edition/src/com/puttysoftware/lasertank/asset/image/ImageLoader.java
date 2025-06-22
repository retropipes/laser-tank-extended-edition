/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.asset.image;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.lasertank.tasks.AppTaskManager;

public class ImageLoader {
    public static BufferedImageIcon load(final ImageIndex image, final URL url) {
	return ImageCache.getCachedImage(image.getName(), url);
    }

    public static BufferedImageIcon load(final String name, final URL url) {
	return ImageCache.getCachedImage(name, url);
    }

    /**
     * @param name  
     */
    static BufferedImageIcon loadUncached(final String name, final URL url) {
	try {
	    final var image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    AppTaskManager.error(ie);
	    return null;
	}
    }
}

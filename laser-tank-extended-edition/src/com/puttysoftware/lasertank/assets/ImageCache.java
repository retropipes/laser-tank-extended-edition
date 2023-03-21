/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.assets;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;

class ImageCache {
	// Fields
	private static ImageCacheEntry[] cache;
	private final static int CACHE_INCREMENT = 20;
	private static int CACHE_SIZE = 0;
	private static String IMAGE_DISABLED = "_disabled"; //$NON-NLS-1$

	private static void addToCache(final String name, final BufferedImageIcon bii) {
		if (ImageCache.cache == null) {
			ImageCache.cache = new ImageCacheEntry[ImageCache.CACHE_INCREMENT];
		}
		if (ImageCache.CACHE_SIZE == ImageCache.cache.length) {
			ImageCache.expandCache();
		}
		ImageCache.cache[ImageCache.CACHE_SIZE] = new ImageCacheEntry();
		ImageCache.cache[ImageCache.CACHE_SIZE].setEntry(bii);
		ImageCache.cache[ImageCache.CACHE_SIZE].setNameEntry(name);
		ImageCache.CACHE_SIZE++;
	}

	private static void expandCache() {
		final var tempCache = new ImageCacheEntry[ImageCache.cache.length + ImageCache.CACHE_INCREMENT];
		for (var x = 0; x < ImageCache.CACHE_SIZE; x++) {
			tempCache[x] = ImageCache.cache[x];
		}
		ImageCache.cache = tempCache;
	}

	// Methods
	static void flushCache() {
		ImageCache.cache = null;
		ImageCache.CACHE_SIZE = 0;
	}

	static BufferedImageIcon getCachedImage(final ArenaObject obj, final boolean useText) {
		String name;
		final var custom = obj.getCustomText();
		if (useText && custom != null) {
			if (obj.isEnabled()) {
				name = obj.getImageName() + custom;
			} else {
				name = obj.getImageName() + custom + ImageCache.IMAGE_DISABLED;
			}
		} else if (obj.isEnabled()) {
			name = obj.getImageName();
		} else {
			name = obj.getImageName() + ImageCache.IMAGE_DISABLED;
		}
		if (!ImageCache.isInCache(name)) {
			final var bii = Images.getUncachedImage(obj, useText);
			ImageCache.addToCache(name, bii);
		}
		for (final ImageCacheEntry element : ImageCache.cache) {
			if (name.equals(element.getNameEntry())) {
				return element.getEntry();
			}
		}
		return null;
	}

	private static boolean isInCache(final String name) {
		if (ImageCache.cache == null) {
			ImageCache.cache = new ImageCacheEntry[ImageCache.CACHE_INCREMENT];
		}
		for (var x = 0; x < ImageCache.CACHE_SIZE; x++) {
			if (name.equals(ImageCache.cache[x].getNameEntry())) {
				return true;
			}
		}
		return false;
	}

	// Constructor
	private ImageCache() {
		// Do nothing
	}
}
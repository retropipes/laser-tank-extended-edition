package com.puttysoftware.lasertank.asset.image;

import java.net.URL;
import java.util.ArrayList;

class ImageCache {
    // Fields
    private static ArrayList<ImageCacheEntry> cache;
    private static boolean cacheCreated = false;

    private static void createCache() {
	if (!ImageCache.cacheCreated) {
	    // Create the cache
	    ImageCache.cache = new ArrayList<>();
	    ImageCache.cacheCreated = true;
	}
    }

    // Methods
    public static BufferedImageIcon getCachedImage(final String name, final URL url) {
	if (!ImageCache.cacheCreated) {
	    ImageCache.createCache();
	}
	for (final ImageCacheEntry entry : ImageCache.cache) {
	    if (name.equals(entry.name())) {
		// Found
		return entry.image();
	    }
	}
	// Not found: Add to cache
	final var newImage = ImageScaler.getScaledImage(ImageLoader.loadUncached(name, url));
	final var newEntry = new ImageCacheEntry(newImage, name);
	ImageCache.cache.add(newEntry);
	return newImage;
    }
}
package me.senwang.newpdademo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by hitbe_000 on 1/22/2015.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{

	/**
	 * @param maxSize for caches that do not override {@link #sizeOf}, this is
	 *                the maximum number of entries in the cache. For all other caches,
	 *                this is the maximum sum of the sizes of the entries in this cache.
	 */
	public LruBitmapCache(int maxSize) {
		super(maxSize);
	}

	public LruBitmapCache(Context context) {
		this(getCacheSize(context));
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

	public static int getCacheSize(Context context) {
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		final int screenWidth = displayMetrics.widthPixels;
		final int screenHeight = displayMetrics.heightPixels;
		final int screenBytes = screenWidth * screenWidth * 4;

		return screenBytes * 3;
	}
}

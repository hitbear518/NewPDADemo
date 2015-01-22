package me.senwang.newpdademo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by hitbe_000 on 1/22/2015.
 */
public class MySingleton {

	private static MySingleton sInstance;
	private static Context sContext;

	public static synchronized MySingleton getInstance(Context context) {
		if (sContext == null) {
			sContext = context.getApplicationContext();
		}
		if (sInstance == null) {
			sInstance = new MySingleton();
		}
		return sInstance;
	}

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private MySingleton() {
		mRequestQueue = getRequestQueue();

//		mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
//			private final LruCache<String, Bitmap> cache = new LruCache<>(20);
//
//			@Override
//			public Bitmap getBitmap(String url) {
//				return cache.get(url);
//			}
//
//			@Override
//			public void putBitmap(String url, Bitmap bitmap) {
//				cache.put(url, bitmap);
//			}
//		});
		mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(sContext)));
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(sContext);
		}
		return mRequestQueue;
	}

	public <T> void addToRequest(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
}

package me.senwang.newpdademo;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

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

		mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(sContext)));
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			Cache cache = new DiskBasedCache(sContext.getCacheDir(), 1024 * 1024);
//			CookieManager cm = new CookieManager();
//			CookieHandler.setDefault(cm);
			Network network = new BasicNetwork(new HurlStack());
			mRequestQueue = new RequestQueue(cache, network);
			mRequestQueue.start();
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

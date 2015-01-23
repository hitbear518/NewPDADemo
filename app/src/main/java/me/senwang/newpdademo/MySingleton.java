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

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

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
			CookieStore cookieStore = new CookieStore() {
				@Override
				public void add(URI uri, HttpCookie cookie) {

				}

				@Override
				public List<HttpCookie> get(URI uri) {
					return null;
				}

				@Override
				public List<HttpCookie> getCookies() {
					return null;
				}

				@Override
				public List<URI> getURIs() {
					return null;
				}

				@Override
				public boolean remove(URI uri, HttpCookie cookie) {
					return false;
				}

				@Override
				public boolean removeAll() {
					return false;
				}
			};
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

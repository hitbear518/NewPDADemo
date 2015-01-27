package me.senwang.newpdademo;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by Wang Sen on 1/27/2015.
 * Last modified:
 * By:
 */
public interface TestInterface {
	@POST("/mobile/stock.php")
	public void testStocks(@QueryMap Map<String, String> params, Callback<String> callback);
}

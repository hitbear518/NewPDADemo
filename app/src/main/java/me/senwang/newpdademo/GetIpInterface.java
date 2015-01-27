package me.senwang.newpdademo;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Wang Sen on 1/27/2015.
 */
public interface GetIpInterface {
	@POST("/mobile/map.php?ua=mobile")
	void getIp(@Query("sid") String sid, Callback<WdtIpResult> ipResultCallback);
}

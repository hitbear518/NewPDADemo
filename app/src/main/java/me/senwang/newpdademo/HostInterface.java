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
public interface HostInterface {
	@POST("/mobile/prepare.php")
	void getLicense(Callback<WdtLicenseResult> callback);

	@POST("/mobile/login.php")
	void login(@QueryMap Map<String, String> params, Callback<WdtLoginResult> callback);

	@POST("/mobile/warehouse.php?mine=1")
	void getWarehouses(Callback<WdtWarehouseResult> callback);

	@POST("/mobile/stock.php")
	public void getStocks(@QueryMap Map<String, String> params, Callback<WdtStockResult> callback);

	@POST("/mobile/stock_fast_pd.php")
	public void fastPd(@QueryMap Map<String, String> params, Callback<WdtFastPdResult> callback);
}

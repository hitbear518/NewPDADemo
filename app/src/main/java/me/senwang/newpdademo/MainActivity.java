package me.senwang.newpdademo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class MainActivity extends Activity {


	private EditText mSidEdit;
	private EditText mUserNameEdit;
	private EditText mPasswordEdit;
	private TextView mTextView;

	private String mIp;
	private byte[] mLicense;
	private String mSession;

	private final RestAdapter mGetIpRestAdapter = new RestAdapter.Builder()
			.setEndpoint("http://erp.wangdian.cn")
			.build();

	private RestAdapter mHostRestAdapter;
	private HostInterface mHostInterface;
	private TestInterface mTestInterface;

	private Stock mTestStock;
	private FastPdResult mFastPdResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

		String test = "111";
		boolean testb = test.equals(null);


		mSidEdit = (EditText) findViewById(R.id.sid_edit);
		mUserNameEdit = (EditText) findViewById(R.id.user_name_edit);
		mPasswordEdit = (EditText) findViewById(R.id.password_edit);
		mTextView = (TextView) findViewById(R.id.text);

		CookieManager cm = new CookieManager();
		CookieHandler.setDefault(cm);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		switch (id) {
		case R.id.action_next:
			setProgressBarIndeterminateVisibility(true);
			if (mIp == null) {
//				request(WdtRequestCopy.RequestUrl.REQUEST_IP, WdtRequestCopy.Param.getRequestIpParams(mSidEdit.getText().toString()), mGetIpListener);
				GetIpInterface getIpInterface = mGetIpRestAdapter.create(GetIpInterface.class);
				getIpInterface.getIp(mSidEdit.getText().toString(), mGetIpCallback);
			} else if (mLicense == null) {
//				request(WdtRequestCopy.RequestUrl.getLicenseUrl(mIp), null, mGetLicenseListener);
				mHostInterface.getLicense(mGetLicenseCallback);
			} else if (mSession == null){
				String password = mPasswordEdit.getText().toString();
				byte[] pwdMd5Bytes = WdtRequestCopy.Param.md5Bytes(password);
				String pwdMd5Str = new String(Hex.encodeHex(pwdMd5Bytes));
//				request(WdtRequestCopy.RequestUrl.getLoginUrl(mIp),
//						WdtRequestCopy.Param.getLoginParams(mSidEdit.getText().toString(),mUserNameEdit.getText().toString(), mLicense, pwdMd5Str),
//						mLoginListener);
				mHostInterface.login(WdtRequestCopy.Param.getLoginParams(mSidEdit.getText().toString(), mUserNameEdit.getText().toString(), mLicense, pwdMd5Str),
						mLoginCallback);
			} else if (mTestStock == null) {
//				request(WdtRequestCopy.RequestUrl.getWarehousesUrl(mIp), WdtRequestCopy.Param.getWarehousesParams(), mWarehousesListener);
//				mHostInterface.getWarehouses(mGetWarehousesCallback);
				Map<String, String> testParams = new HashMap<>();
				testParams.put("warehouse_no", "WH001");
				testParams.put("spec_no", "penblack6");
//				testParams.put("page_no", "1");
//				testParams.put("page_size", "1");
				mHostInterface.getStocks(testParams, mGetStocksCallback);
//				mTestInterface.testStocks(testParams, mTestCallBack);
			} else if (mFastPdResult == null) {
				Map<String, String> testParams = new HashMap<>();
				testParams.put("warehouse_no", "WH001");
				testParams.put("spec_no", "penblack6");
				testParams.put("old_stock", String.valueOf(mTestStock.stockNum));
				testParams.put("new_stock", String.valueOf(mTestStock.stockNum + 1));
				mHostInterface.fastPd(testParams, mFastPdCallback);
			} else {
				setProgressBarIndeterminateVisibility(false);
			}
			return true;
		case R.id.action_reset:
			mIp = null;
			mLicense = null;
			mSession = null;
			mTextView.setText("");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void request(String url, Map<String, String> params, Response.Listener<JSONObject> listener) {
		WdtRequestCopy req = new WdtRequestCopy(url, params, listener, mErrorListener);
		MySingleton.getInstance(this).addToRequest(req);
		setProgressBarIndeterminateVisibility(true);
	}

	private final Response.Listener<JSONObject> mGetIpListener = new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				int code = response.getInt(WdtRequestCopy.Result.CODE);
				if (code == 0) {
					mIp = response.getString(WdtRequestCopy.Result.IP);
					mTextView.append("\nIP: " + mIp);
				} else {
					mTextView.append("\nMessage: " + response.getString(WdtRequestCopy.Result.MESSAGE));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mTextView.append("\nException Message: " + e.getMessage());
			}
			setProgressBarIndeterminateVisibility(false);
		}
	};

	private final Response.Listener<JSONObject> mGetLicenseListener = new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				int code = response.getInt(WdtRequestCopy.Result.CODE);
				if (code == 0) {
					String licenseHex = response.getString(WdtRequestCopy.Result.PK);
					mLicense = Hex.decodeHex(licenseHex.toCharArray());

					mTextView.append("\nLicense: " + licenseHex);
				} else {
					mTextView.append("\nMessage: " + response.getString(WdtRequestCopy.Result.MESSAGE));
				}
			} catch (JSONException | DecoderException e) {
				e.printStackTrace();
				mTextView.append("\n Exception Message: " + e.getMessage());
			}
			setProgressBarIndeterminateVisibility(false);
		}
	};

	private final Response.Listener<JSONObject> mLoginListener = new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				int code = response.getInt(WdtRequestCopy.Result.CODE);
				if (code == 0) {
					mSession = response.getString(WdtRequestCopy.Result.SESSION);
					mTextView.append("\nSession: " + mSession);
				} else {
					mTextView.append("\nMessage: " + response.getString(WdtRequestCopy.Result.MESSAGE));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mTextView.append("\nException Message: " + e.getMessage());
			}
			setProgressBarIndeterminateVisibility(false);
		};
	};

	private final Response.Listener<JSONObject> mWarehousesListener = new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				int code = response.getInt(WdtRequestCopy.Result.CODE);
				if (code == 0) {
					mTextView.setText(response.toString());
				} else {
					mTextView.append("\nMessage: " + response.getString(WdtRequestCopy.Result.MESSAGE));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mTextView.append("\nException Message: " + e.getMessage());
			}
			setProgressBarIndeterminateVisibility(false);
		}
	};

	private final Response.ErrorListener mErrorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			mTextView.append("\nError Message: " + error.getMessage());
			setProgressBarIndeterminateVisibility(false);
		}
	};

	private abstract class HttpCallback<T> implements Callback<T> {
		@Override
		public void success(T result, retrofit.client.Response response) {
			if (result instanceof HttpResult) {
				HttpResult httpResult = (HttpResult) result;
				if (httpResult.code == 0) {
					onSuccess(result);
				} else {
					mTextView.append("\nIllegal result, code = " + httpResult.code + ", message: " + httpResult.message);
				}
			} else {
				mTextView.append("\nIncorrect type, type: " + result.getClass().getSimpleName());
			}
			setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void failure(RetrofitError error) {
			mTextView.append("\nError Message: " + error.getMessage());
			setProgressBarIndeterminateVisibility(false);
		}

		protected abstract void onSuccess(T result);
	}

	private final HttpCallback<IpResult> mGetIpCallback = new HttpCallback<IpResult>() {
		@Override
		protected void onSuccess(IpResult result) {
			mIp = result.ip;
			mTextView.append("IP: " + mIp);
			mHostRestAdapter = new RestAdapter.Builder()
					.setEndpoint("http://" + mIp)
					.build();
			mHostInterface = mHostRestAdapter.create(HostInterface.class);
			RestAdapter testRestAdapter = new RestAdapter.Builder()
					.setEndpoint("http://" + mIp)
					.setConverter(new StringConverter())
					.build();
			mTestInterface = testRestAdapter.create(TestInterface.class);
		}
	};

	private final HttpCallback<LicenseResult> mGetLicenseCallback = new HttpCallback<LicenseResult>() {
		@Override
		protected void onSuccess(LicenseResult result) {
			String licenseHex = result.pk;
			try {
				mLicense = Hex.decodeHex(licenseHex.toCharArray());
				mTextView.append("\nLicense: " + licenseHex);
			} catch (DecoderException e) {
				Log.e(getClass().getSimpleName(), "Exception parsing license: " + e.getMessage(), e);
				mTextView.append("\nException: " + e.getMessage());
			}
		}
	};

	private final HttpCallback<LoginResult> mLoginCallback = new HttpCallback<LoginResult>() {
		@Override
		protected void onSuccess(LoginResult result) {
			mSession = result.session;
			mTextView.append("\nSession: " + mSession);
		}
	};

	private final HttpCallback<WarehouseResult> mGetWarehousesCallback = new HttpCallback<WarehouseResult>() {
		@Override
		protected void onSuccess(WarehouseResult result) {
			mTextView.append("\nWarehouses: \n");
			for (Warehouse warehouse : result.warehouses) {
				String json = new Gson().toJson(warehouse, Warehouse.class);
				mTextView.append(json + "\n");
			}
		}
	};

	private final HttpCallback<StockResult> mGetStocksCallback = new HttpCallback<StockResult>() {
		@Override
		protected void onSuccess(StockResult result) {
			mTextView.append("\nTest Stock: \n");
			mTestStock = result.stocks.get(0);
			mTextView.append("specNo: " + mTestStock.specNo + ", warehouse_no: " + mTestStock.warehouseNo + ", stockNum: " + mTestStock.stockNum);
			mFastPdResult = null;
		}
	};

	private final HttpCallback<FastPdResult> mFastPdCallback = new HttpCallback<FastPdResult>() {
		@Override
		protected void onSuccess(FastPdResult result) {
			mTextView.append("\nPdNo: " + result.pdNo);
			mTestStock = null;
		}
	};

	private Callback<String> mTestCallBack = new Callback<String>() {
		@Override
		public void success(String s, retrofit.client.Response response) {
			mTextView.append("\nTest: " + s);
		}

		@Override
		public void failure(RetrofitError error) {
			mTextView.append("\nError Message: " + error.getMessage());
			setProgressBarIndeterminateVisibility(false);
		}
	};
}
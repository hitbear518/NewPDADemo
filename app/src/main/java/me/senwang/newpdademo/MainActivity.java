package me.senwang.newpdademo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MainActivity extends Activity {


	private EditText mSidEdit;
	private EditText mUserNameEdit;
	private EditText mPasswordEdit;
	private TextView mTextView;

	private String mHost;
	private byte[] mLicense;
	private String mSession;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

		mSidEdit = (EditText) findViewById(R.id.sid_edit);
		mUserNameEdit = (EditText) findViewById(R.id.user_name_edit);
		mPasswordEdit = (EditText) findViewById(R.id.password_edit);
		mTextView = (TextView) findViewById(R.id.text);
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
			if (mHost == null) {
				request(WdtRequestCopy.RequestUrl.REQUEST_IP, WdtRequestCopy.Param.getRequestIpParams(mSidEdit.getText().toString()), mGetIpListener);
			} else if (mLicense == null) {
				request(WdtRequestCopy.RequestUrl.getLicenseUrl(mHost), null, mGetLicenseListener);
			} else if (mSession == null){
				String password = mPasswordEdit.getText().toString();
				byte[] pwdMd5Bytes = WdtRequestCopy.Param.md5Bytes(password);
				String pwdMd5Str = new String(Hex.encodeHex(pwdMd5Bytes));
				request(WdtRequestCopy.RequestUrl.getLoginUrl(mHost),
						WdtRequestCopy.Param.getLoginParams(mSidEdit.getText().toString(),mUserNameEdit.getText().toString(), mLicense, pwdMd5Str),
						mLoginListener);
			} else {
				request(WdtRequestCopy.RequestUrl.getWarehousesUrl(mHost), WdtRequestCopy.Param.getWarehousesParams(), mWarehousesListener);
			}
			return true;
		case R.id.action_reset:
			mHost = null;
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
					mHost = response.getString(WdtRequestCopy.Result.IP);
					mTextView.append("\nHost: " + mHost);
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
				mTextView.append("\n Exception Message: " + e.getMessage());
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
}
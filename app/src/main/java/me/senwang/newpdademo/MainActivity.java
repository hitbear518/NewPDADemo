package me.senwang.newpdademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

	public static final String REQUEST_IP_URL = "http://erp.wangdian.cn/mobile/map.php";

	private EditText mSidEdit;
	private EditText mUserNameEdit;
	private EditText mPasswordEdit;
	private TextView mTextView;

	private String mHost;

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

	private void fetchIp() {
		JSONObject jsonRequest = new JSONObject();
		try {
			String sid = mSidEdit.getText().toString();
			jsonRequest.put("sid", sid);
			jsonRequest.put("ua", "mobile");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest request = new JsonObjectRequest(REQUEST_IP_URL, jsonRequest, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					int code = response.getInt("code");
					if (code == 0) {
						mHost = response.getString("host");
						mTextView.append("\nHost: " + mHost);
					} else {
						mTextView.append("\n" + response.toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mTextView.append("\n" + e.getMessage());
				}
//				setSupportProgressBarIndeterminateVisibility(false);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mTextView.setText(error.getMessage());
//				setSupportProgressBarIndeterminateVisibility(false);
			}
		});

		MySingleton.getInstance(this).addToRequest(request);
		setProgressBarIndeterminateVisibility(true);
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
		case R.id.action_req_ip:
			fetchIp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
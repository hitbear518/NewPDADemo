package me.senwang.newpdademo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

	private String mHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		final TextView textView = (TextView) findViewById(R.id.text);
//
//		RequestQueue queue = Volley.newRequestQueue(this);
//		String url = "http://www.google.com";
//
//		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				textView.setText("Response is: " + response.substring(0, 500));
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				textView.setText("That didn't work!");
//			}
//		});
//
//		queue.add(stringRequest);
//		final ImageView imageView = (ImageView) findViewById(R.id.image	);
//		String url = "http://i.imgur.com/7spzG.png";
//
//		ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
//			@Override
//			public void onResponse(Bitmap response) {
//				imageView.setImageBitmap(response);
//			}
//		}, 0, 0, null, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
//			}
//		});
//
//		MySingleton.getInstance(this).addToRequest(request);

//		final String IMAGE_URL = "http://developer.android.com/images/training/system-ui.png";
//		final ImageView imageView = (ImageView) findViewById(R.id.image);
//		final ImageLoader imageLoader = MySingleton.getInstance(this).getImageLoader();
//		imageLoader.get(IMAGE_URL, ImageLoader.getImageListener(imageView, 0, 0));
//
//		final NetworkImageView networkImageView = (NetworkImageView) findViewById(R.id.network_image);
//		networkImageView.setImageUrl(IMAGE_URL, imageLoader);

		String sid = "duoduotest";

		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("sid", sid);
			jsonRequest.put("ua", "mobile");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://erp.wangdian.cn/mobile/map.php", jsonRequest, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					int code = response.getInt("code");
					if (code == 0) {
						mHost = (String) response.get("host");
						Toast.makeText(getApplicationContext(), mHost, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
			}
		});

		MySingleton.getInstance(this).addToRequest(jsonObjectRequest);
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
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
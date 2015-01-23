package me.senwang.newpdademo;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by hitbe_000 on 1/23/2015.
*/
class WdtRequest extends Request<JSONObject> {
	private final Response.Listener<JSONObject> mListener;
	private final Map<String,String> mParams;

	/**
	 * Creates a new request with the given method.
	 *
	 * @param method the request {@link Method} to use
	 * @param url URL to fetch the string at
	 * @param listener Listener to receive the String response
	 * @param errorListener Error listener, or null to ignore errors
	 */
	public WdtRequest(int method, String url, Map<String,String> params,
					  Response.Listener<JSONObject> listener,
					  Response.ErrorListener errorListener) {
		super(method, url, errorListener);
		mParams = params;
		mListener = listener;
	}

    /*@Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> params = new HashMap<String, String>();
        params.put("Content-Type","application/x-www-form-urlencoded");
        return params;
    }*/

	@Override
	public Map<String, String> getParams() throws AuthFailureError {
		return mParams;
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString =
					new String(response.data, "UTF-8");
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	public RetryPolicy getRetryPolicy() {
		return new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	}
}


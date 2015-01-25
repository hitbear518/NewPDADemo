package me.senwang.newpdademo;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hitbe_000 on 1/23/2015.
 */
public class WdtRequestCopy extends Request<JSONObject> {

	public static class RequestUrl {
		public static final String REQUEST_IP = "http://erp.wangdian.cn/mobile/map.php";

		public static String getLicenseUrl(String host) {
			return "http://" + host + "/mobile/prepare.php";
		}

		public static String getLoginUrl(String host) {
			return "http://" + host + "/mobile/login.php";
		}

		public static String getWarehousesUrl(String host) {
			return "http://" + host + "/mobile/warehouse.php";
		}
	}

	public static class Param {
		public static final String UA = "ua"; // Public Key?
		public static final String SID = "sid";
		public static final String NICK = "nick";
		public static final String TIMESTAMP = "timestamp";
		public static final String SIGN = "sign";
		public static final String MINE = "mine";

		public static Map<String, String> getRequestIpParams(String sid) {
			Map<String, String> params = new HashMap<>();
			params.put(Param.SID, sid);
			params.put(Param.UA, "mobile");
			return params;
		}

		public static Map<String, String> getLoginParams(String sid, String nick, byte[] license, String pwdMd5) {
			Map<String, String> params = new HashMap<>();
			params.put(SID, sid);
			params.put(NICK, nick);
			String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
			params.put(TIMESTAMP, timestamp);
			String sign = makeSign(sid, nick, pwdMd5, timestamp, license);
			params.put(SIGN, sign);
			return params;
		}

		public static Map<String, String> getWarehousesParams() {
			Map<String, String> params = new HashMap<>();
			params.put(MINE, "1");
			return params;
		}

		private static String makeSign(String sid, String nick, String pwdMd5, String timestamp, byte[] license) {
			byte[] signMd5Bytes = md5Bytes("auth" + sid + nick + pwdMd5 + timestamp);
			byte[] pwdMd5Bytes = new byte[0];
			try {
				pwdMd5Bytes = Hex.decodeHex(pwdMd5.toCharArray());
			} catch (DecoderException e) {
				e.printStackTrace();
			}
			byte[] data = new byte[signMd5Bytes.length + pwdMd5Bytes.length];
			System.arraycopy(pwdMd5Bytes, 0, data, 0, pwdMd5Bytes.length);
			System.arraycopy(signMd5Bytes, 0, data, pwdMd5Bytes.length, signMd5Bytes.length);

			BigInteger x = new BigInteger(1, data);
			BigInteger e = new BigInteger("65537");
			BigInteger n = new BigInteger(1, license);

			BigInteger r = x.modPow(e, n);

			byte[] buff = r.toByteArray();
			if (buff[0] == 0) {
				byte[] tmp = new byte[buff.length - 1];
				System.arraycopy(buff, 1, tmp, 0, tmp.length);
				buff = tmp;
			}

			return new String(Hex.encodeHex(buff));
		}

		public static byte[] md5Bytes(String s) {
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.update(s.getBytes("UTF-8"));
				return digest.digest();
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static class Result {
		public static final String CODE = "code";
		public static final String IP = "ip";
		public static final String MESSAGE = "message";
		public static final String PK = "pk";
		public static final String SESSION = "session";
	}

	private final Response.Listener<JSONObject> mListener;
	private final Map<String, String> mParams;

	public WdtRequestCopy(int method, String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errlistener) {
		super(method, url, errlistener);
		mParams = params;
		mListener = listener;
	}

	public WdtRequestCopy(String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		this(Method.POST, url, params, listener, errorListener);
	}

	@Override
	public Map<String, String> getParams() {
		return mParams;
	}

	@Override
	public RetryPolicy getRetryPolicy() {
		return new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException | JSONException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);
	}
}

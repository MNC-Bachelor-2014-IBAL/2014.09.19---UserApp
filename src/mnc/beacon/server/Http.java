package mnc.beacon.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

public class Http {
	Object url = null;

	public Object getURL() {
		return url;
	}

	public String post(String url, Map params) {

		HttpClient client = new DefaultHttpClient();
		String result = null;

		try {
			HttpPost post = new HttpPost(url);
			System.out.print("POST : " + post.getURI());

			List<NameValuePair> paramList = convertParam(params);
			post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
			ResponseHandler<String> rh = new BasicResponseHandler();
			result = client.execute(post, rh);
			jsonParserList(result);
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return "error";
	}

	public void jsonParserList(String result) throws JSONException {

		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(result);
			JSONObject object = (JSONObject) obj;

			JSONArray array = (JSONArray) object.get("datasend");
			JSONObject j = (JSONObject) array.get(0);
			System.out.println(j.get("ID"));
			System.out.println(j.get("LOCATION"));
			url = j.get("map");

			Log.i("url", (String) url);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * GET ��û POST �� ����
	 */

	public String get(String url, Map params) {
		HttpClient client = new DefaultHttpClient();
		String result = null;

		try {
			List<NameValuePair> paramList = convertParam(params);
	
			HttpGet get = new HttpGet(url + "?"
					+ URLEncodedUtils.format(paramList, "UTF-8"));
			
			Log.i("test", "GET : " + get.getURI());

			ResponseHandler<String> rh = new BasicResponseHandler();
			result = client.execute(get, rh);
			//jsonParserList(result);

			return result.trim();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return "error";
	}

	private List<NameValuePair> convertParam(Map params) {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		Iterator<String> keys = params.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			paramList.add(new BasicNameValuePair(key, params.get(key)
					.toString()));
		}

		return paramList;
	}

}

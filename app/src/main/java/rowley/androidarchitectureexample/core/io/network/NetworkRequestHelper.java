package rowley.androidarchitectureexample.core.io.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rowley.androidarchitectureexample.core.io.request.DataRequest;

/**
 * Basic helper to wrap OKHTTP work
 */
public class NetworkRequestHelper {
    private final String TAG = NetworkRequestHelper.class.getSimpleName();

    private Context context;

    public NetworkRequestHelper(Context context) {
        this.context = context;
    }

    public JsonArray sendRequestAndWait(DataRequest requestParams) {
        String resultString = null;
        JsonArray result = null;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(requestParams != null && activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            StringBuilder urlBuilder = new StringBuilder(requestParams.getService().getBaseUrl());
            if(!TextUtils.isEmpty(requestParams.getGetMethod())) {
                urlBuilder.append(requestParams.getGetMethod());
            }
            if(requestParams.getParams() != null && requestParams.getParams().size() > 0) {
                urlBuilder.append("?");
                StringBuilder paramBuilder = new StringBuilder();
                for(String key : requestParams.getParams().keySet()) {
                    if(paramBuilder.length() > 0) {
                        paramBuilder.append("&");
                    }
                    urlBuilder.append(key).append("=").append(requestParams.getParams().get(key));
                }
                urlBuilder.append("?").append(paramBuilder);
            }

            String url = urlBuilder.toString();

            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url);

            if(requestParams.getHeaders() != null && requestParams.getHeaders().size() > 0) {
                for(String key : requestParams.getHeaders().keySet()) {
                    builder.addHeader(key, requestParams.getHeaders().get(key));
                }
            }

            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();

                resultString = response.body().string();
                Log.d(TAG, "ResultString: " + resultString);

                if(!TextUtils.isEmpty(resultString)) {
                    JsonParser parser = new JsonParser();
                    result = parser.parse(resultString).getAsJsonArray();
                }
            } catch(IOException e) {
                e.printStackTrace();
            } catch(JsonSyntaxException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage(), e);
            }
        }

        return result;
    }
}

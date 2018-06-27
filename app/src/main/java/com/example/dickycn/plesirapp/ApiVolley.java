package com.example.dickycn.plesirapp;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shin on 2/24/2017.
 */

public class ApiVolley {


    RequestQueue requestQueue;
    private ImageLoader mImageLoader;

    public ApiVolley(final Context context, JSONObject jsonBody, String requestMethod, String REST_URL, final String successDialog, final String failDialog, final int showDialogFlag, final VolleyCallback callback){

        /*
        context : Application context
        jsonBody : jsonBody (usually be used for POST and PUT)
        requestMethod : GET, POST, PUT, DELETE
        REST_URL : Rest API URL
        successDialog : custom Dialog when success call API
        failDialog : custom Dialog when failed call API
        showDialogFlag : 1 = show successDialog / failDialog with filter
        callback : return of the response
        */

        final String requestBody = jsonBody.toString();

        int method = 0;

        switch(requestMethod.toUpperCase()){

            case "GET" :
                method = Request.Method.GET;
                break;
            case "POST" :
                method = Request.Method.POST;
                break;
            case "PUT" :
                method = Request.Method.PUT;
                break;
            case "DELETE" :
                method = Request.Method.DELETE;
                break;
            default: method = Request.Method.GET;
                break;
        }

        //region initial of stringRequest
        StringRequest stringRequest = new StringRequest(method, REST_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string
                try {

                    JSONObject responseAPI = new JSONObject(response);
                    String status = responseAPI.getJSONObject("metadata").getString("status");
                    responseAPI = null;

                    if(status != null){
                        callback.onSuccess(response);
                    }else{
                        //Toast.makeText(context, "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                    }

                    ShowCustomDialog(context, showDialogFlag, successDialog);

                } catch (Exception e) {

                    e.printStackTrace();
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(context, "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
                ShowCustomDialog(context,showDialogFlag,failDialog);
                return;
            }
        }) {

            // Request Header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Client-Service", "frontend-client");
                params.put("Auth-Key", "plesir_blora");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        //endregion

        requestQueue = Volley.newRequestQueue(context);
        // retry when timeout
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30*60*1000, /*DefaultRetryPolicy.DEFAULT_MAX_RETRIES*/0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    // interface for call callback from response API
    public interface VolleyCallback{
        void onSuccess(String result);
        void onError(String result);
    }

    public void ShowCustomDialog(Context context, int flag, String message){
        if(flag == 1){
            //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(requestQueue,
                    new lruBitmapCache());
        }
        return this.mImageLoader;
    }
}



package com.salaheddin.store.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.salaheddin.store.R;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.models.WebServiceResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class DataLoader {

    private static final int NUM_RETRIES = 0;
    private static final int TIMEOUT_MS = 20000;

    public static void loadJsonDataPostWithProgress(final Activity activity, String url, final OnJsonDataLoadedListener listener,
                                                    final Map<String, String> params, final Map<String, String> headers, final Request.Priority priority, final String tag,
                                                    final ProgressDialog progress) {

        Utils.showProgress(progress);
        StringRequest JsonObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.dismissProgress(progress);
                if (listener != null) {
                    WebServiceResponse webServiceResponse = null;
                    try {
                        webServiceResponse = JsonParser.json2WebServiceResponse(new JSONObject(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (webServiceResponse != null) {
                        if (webServiceResponse.getCode() != 1) {
                            try {
                                listener.onJsonDataLoadedWithError(webServiceResponse.getCode(), webServiceResponse.getErrorMessage());
                            } catch (Exception ignored) {
                                Log.e("error", ignored.getMessage());
                            }
                        } else if (webServiceResponse.getCode() == 1) {
                            listener.onJsonDataLoadedSuccessfully(webServiceResponse.getData());
                        }
                    } else {
                        listener.onJsonDataLoadingFailure(R.string.error_parse);
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissProgress(progress);
                if (listener != null) {
                    int errorId = R.string.error_connection;
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        errorId = R.string.error_connection;
                    } else if (error instanceof AuthFailureError) {
                        errorId = R.string.error_connection;
                    } else if (error instanceof ServerError) {
                        errorId = R.string.error_parse;
                    } else if (error instanceof NetworkError) {
                        errorId = R.string.error_connection;
                    } else if (error instanceof ParseError) {
                        errorId = R.string.error_parse;
                    }
                    listener.onJsonDataLoadingFailure(errorId);
                }
            }
        }) {

            @Override
            protected Map<String,String> getParams(){
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }


            @Override
            public Priority getPriority() {
                return priority;
            }
        };
        JsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                NUM_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add request to request queue
        VolleySingleton.getInstance().addToRequestQueue(JsonObjRequest, tag);
        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                VolleySingleton.getInstance().cancelPendingRequests(tag);
            }
        });
    }

    public static void loadJsonDataPost(final Activity activity, String url, final OnJsonDataLoadedListener listener,
                                        final Map<String, String> params, final Map<String, String> headers, final Request.Priority priority, String tag) {

        StringRequest JsonObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SalahTest", response.toString());
                if (listener != null) {
                    WebServiceResponse webServiceResponse = null;
                    try {
                        webServiceResponse = JsonParser.json2WebServiceResponse(new JSONObject(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (webServiceResponse != null) {
                        if (webServiceResponse.getCode() != 1) {
                            try {
                                listener.onJsonDataLoadedWithError(webServiceResponse.getCode(), webServiceResponse.getErrorMessage());
                            } catch (Exception ignored) {
                                Log.e("error", ignored.getMessage());
                            }
                        } else if (webServiceResponse.getCode() == 1) {
                            listener.onJsonDataLoadedSuccessfully(webServiceResponse.getData());
                        }
                    } else {
                        listener.onJsonDataLoadingFailure(R.string.error_parse);
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SalahTest", error.toString());
                if (listener != null) {
                    int errorId = R.string.error_connection;
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        errorId = R.string.error_connection;
                    } else if (error instanceof AuthFailureError) {
                        errorId = R.string.error_connection;
                    } else if (error instanceof ServerError) {
                        errorId = R.string.error_parse;
                    } else if (error instanceof NetworkError) {
                        errorId = R.string.error_connection;
                    } else if (error instanceof ParseError) {
                        errorId = R.string.error_parse;
                    }
                    listener.onJsonDataLoadingFailure(errorId);
                }
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

        };
        JsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                NUM_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add request to request queue
        VolleySingleton.getInstance().addToRequestQueue(JsonObjRequest, tag);
    }

    public interface OnJsonDataLoadedListener {
        void onJsonDataLoadedSuccessfully(JSONObject data);

        void onJsonDataLoadedWithError(int errorCode, String errorMessage);

        void onJsonDataLoadingFailure(int errorId);
    }

}

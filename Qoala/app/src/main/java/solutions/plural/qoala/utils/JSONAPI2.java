package solutions.plural.qoala.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import solutions.plural.qoala.SessionResources;

/**
 * Created by gabriel on 07/09/2016.
 * require <code> <uses-permission android:name=”android.permission.INTERNET” /> </code> in manifest
 */
public class JSONAPI2 {
    private static final String TAG = "JSONAPI2";
    public static final String json_respondeCode = "_responseCode";
    public static final String json_responseMessage = "_responseMessage";
    public static final String json_Message = "Message";
    public static final String json_token = "Token";

    public static JSONObject GetJSON(String specURL, JSONStringer jsonStringer) {
        try {
            URL url = new URL(specURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setUseCaches(false);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                // add Auth Token
                SessionResources sr = SessionResources.getInstance();
                if (sr.isLoggedIn())
                    connection.addRequestProperty("Authorization", "Token " + sr.getToken());

                if (jsonStringer != null)
                    writeInputStream(connection, jsonStringer);

                return readInputStream(connection);
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "GetJSON: " + e.getMessage(), e);
            JSONObject ret = new JSONObject();
            try {
                ret.accumulate("Error", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return ret;
        }
    }

    public static JSONObject PostJSON(String specURL, JSONStringer jsonStringer) {

        try {
            URL url = new URL(specURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setRequestMethod("POST");
                //connection.addRequestProperty("Accept", "application/json");
                //connection.setRequestProperty("accept-encoding", "gzip, deflate, sdch")

                // add Auth Token
                SessionResources sr = SessionResources.getInstance();
                if (sr.isLoggedIn())
                    connection.addRequestProperty("Authorization", "Token " + sr.getToken());

                if (jsonStringer != null)
                    writeInputStream(connection, jsonStringer);

                JSONObject ret;
                try {
                    ret = readInputStream(connection);
                } catch (Exception ex) {
                    ret = new JSONObject();
                }

                return ret;

            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "PostJSON: " + e.getMessage(), e);
            JSONObject ret = new JSONObject();
            try {
                ret.accumulate("Error", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return ret;
        }
    }

    public static JSONObject readInputStream(HttpURLConnection connection)
            throws JSONException, IOException {

        String linha = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader stream;
        try {
            stream = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
        } catch (Exception ex) {
            stream = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream()));
        }
        while ((linha = stream.readLine()) != null) {
            builder.append(linha);
        }
        JSONObject ret = new JSONObject(builder.toString())
                .accumulate(json_respondeCode, connection.getResponseCode())
                .accumulate(json_responseMessage, connection.getResponseMessage());

        return ret;
    }

    public static void writeInputStream(HttpURLConnection connection, JSONStringer jsonStringer)
            throws JSONException, IOException {

        connection.setRequestProperty("Content-Type", "application/json");
        OutputStreamWriter stream = new OutputStreamWriter(connection.getOutputStream());
//                GZIPOutputStream stream = new GZIPOutputStream(connection.getOutputStream());

        try {
//                    stream.write(jsonStringer.toString().getBytes());
            stream.write(jsonStringer.toString());
        } finally {
            stream.close();
        }
    }
}
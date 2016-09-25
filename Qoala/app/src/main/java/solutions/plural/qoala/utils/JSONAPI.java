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

/**
 * Created by gabriel on 07/09/2016.
 * require <code> <uses-permission android:name=”android.permission.INTERNET” /> </code> in manifest
 */
public class JSONAPI {
    public static final String json_responseCode = "_responseCode";
    public static final String json_responseMessage = "_responseMessage";
    // Resposta de do WS
    public static final String json_message = "Message";
    public static final String json_token = "token";
    public static final String json_user = "user";
    private static final String TAG = "JSONAPI";
    private static final String urlService = "http://ws.qoala.com.br/";

    public static JSONObject Get(String specURL) {
        try {
            URL url = new URL(urlService + specURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setUseCaches(false);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                // add Auth Token
                SessionResources sr = SessionResources.getInstance();
                if (sr.hasToken())
                    connection.addRequestProperty("Authorization", "Token " + sr.getToken());

                return readInputStream(connection);
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Get: " + e.getMessage(), e);
            JSONObject ret = new JSONObject();
            try {
                ret.accumulate("Error", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return ret;
        }
    }

    public static JSONObject Post(String specURL, JSONStringer jsonStringer) {
        Log.i(TAG, "Post: " + specURL + " Body: " + jsonStringer);
        HttpURLConnection connection = null;
        try {
            URL url = null;
            try {
                url = new URL(urlService + specURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                // add Auth Token
                SessionResources sr = SessionResources.getInstance();
                if (sr.hasToken())
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

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Post: " + e.getMessage(), e);
                JSONObject ret = new JSONObject();
                try {
                    ret.accumulate("Error", e.getMessage());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                return ret;
            }
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    public static JSONObject readInputStream(HttpURLConnection connection)
            throws JSONException, IOException {

        String linha = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader stream;
        try {
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
        } catch (Exception e) {
            Log.e(TAG, "Erro lendo retorno!", e);
        }
        if (builder.length() == 0)
            builder.append("{}");
        JSONObject ret = new JSONObject(builder.toString());
        ret.accumulate(json_responseCode, connection.getResponseCode())
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
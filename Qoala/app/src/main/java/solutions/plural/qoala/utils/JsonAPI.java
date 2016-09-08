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
    private static final String TAG = "JSONAPI";


    public static JSONObject GetJSON(String specURL) {
        JSONObject resultado = null;
        try {
            URL url = new URL(specURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setUseCaches(false);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                return readInputStream(connection);
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "GetJSON: " + e.getMessage(), e);
        }
        return null;
    }

    public static JSONObject PostJSON(String specURL, JSONStringer jsonStringer) {

        try {
            URL url = new URL(specURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Context-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                //connection.setRequestProperty("accept-encoding", "gzip, deflate, sdch")

//            JSONStringer json = new JSONStringer();
//            json.object();
//            json.key("descricao").value(strings[0]);
//            json.key("quantidade").value(strings[1]);
//            json.key("preco").value(strings[2]);
//            json.endObject();

                writeInputStream(connection, jsonStringer);
                return readInputStream(connection);
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject readInputStream(HttpURLConnection connection)
            throws JSONException, IOException {

        BufferedReader stream = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        String linha = "";
        StringBuilder builder = new StringBuilder();

        while ((linha = stream.readLine()) != null) {
            builder.append(linha);
        }

        return new JSONObject(builder.toString());
    }

    public static void writeInputStream(HttpURLConnection connection, JSONStringer jsonStringer)
            throws JSONException, IOException {

        OutputStreamWriter stream = new OutputStreamWriter(
                connection.getOutputStream());
//                GZIPOutputStream stream = new GZIPOutputStream(connection.getOutputStream());

        try {
//                    stream.write(jsonStringer.toString().getBytes());
            stream.write(jsonStringer.toString());
        } finally {
            stream.close();
        }
    }
}
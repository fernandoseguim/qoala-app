package solutions.plural.qoala.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.R;

/**
 * Created by gabri on 28/08/2016.
 */
public class SessionResources {
    private static SessionResources ourInstance = new SessionResources();

    private String token;

    public static SessionResources getInstance() {
        return getInstance(false);
    }

    public static SessionResources getInstance(boolean getANewOne) {
        if (getANewOne) ourInstance = new SessionResources();
        String msg = "getInstance() -> " + ourInstance;
        if (ourInstance != null)
            msg += ": " + ourInstance.toString();
        Log.i("SessionResources", msg);
        return ourInstance;
    }

    private SessionResources() {
        token = "";
    }

    public boolean isLoggedIn() {
        return !token.isEmpty();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token, Context ctx) {
        this.token = token;
        saveToken(ctx);
    }

    //
    public static final String PREFS_TOKEN = "UserPrefs";

    /**
     * Carrega o Token salvo
     *
     * @param ctx Activity do contexto
     */
    public void loadToken(Context ctx) {
        // Restore Token ID from sharedPreferences
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_TOKEN, 0);
        token = settings.getString(JSONAPI.json_token, "");
    }

    /**
     * Armazena o Token
     *
     * @param ctx Activity do contexto
     */
    private void saveToken(Context ctx) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_TOKEN, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(JSONAPI.json_token, token);
        // Commit the edits!
        editor.commit();
    }

}
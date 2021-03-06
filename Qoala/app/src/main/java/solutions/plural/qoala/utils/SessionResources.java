package solutions.plural.qoala.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import solutions.plural.qoala.models.UserDTO;

/**
 * Created by gabri on 28/08/2016.
 */
public class SessionResources {
    //
    public static final String PREFS_TOKEN = "UserPrefs";
    private static SessionResources ourInstance = new SessionResources();
    private String token;
    private UserDTO user;

    private SessionResources() {
        token = "";
        user=new UserDTO();
    }

    public static SessionResources getInstance() {
        return getInstance(false);
    }

    public static SessionResources getInstance(boolean getANewOne) {
        if (getANewOne) ourInstance = new SessionResources();
        String msg = "getInstance() -> " + ourInstance;
        if (ourInstance != null)
            msg += ": " + ourInstance.toString() + ". tkn: " + ourInstance.token;
        Log.d("SessionResources", msg);
        return ourInstance;
    }

    public boolean hasToken() {
        return !token.isEmpty();
    }

    /**
     * Retorna o token armazenado na sessão, não carrega se não houver
     *
     * @return token da sessão
     */
    public String getToken() {
        return getToken(null);
    }

    /**
     * Busca o token armazenado nas preferencias se não encontrar na sessão.
     *
     * @param ctx <{@link android.app.Activity} do contexto atual, será usado para buscar valor armazenado
     * @return retorna o Token armazenado
     */
    public String getToken(Activity ctx) {
        if (token.isEmpty())
            if (ctx != null)
                loadToken(ctx);

        Log.d("SessionResources", "Session Token: "+token + ". User: "+user);
        return token;
    }

    public void setToken(String token, Context ctx) {
        this.token = token;
        saveToken(ctx);
    }

    /**
     * Carrega o Token salvo
     *
     * @param ctx Activity do contexto
     */
    private void loadToken(Activity ctx) {
        // Restore Token ID from sharedPreferences

        ctx.getPreferences(Context.MODE_PRIVATE);

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
        editor.apply();
    }

    /**
     * Reinicia Preferencias do Token
     *
     * @param ctx Activity do contexto
     */
    private void clear(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_TOKEN, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().apply();
    }

    public int getUserID() {
        return getUser().id_user;
    }

    public UserDTO getUser() {
        Log.d("SessionResources", "User: " + user);
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
package solutions.plural.qoala;

import android.util.Log;

/**
 * Created by gabri on 28/08/2016.
 */
public class SessionResources {
    private static SessionResources ourInstance = new SessionResources();

    private boolean loggedIn;
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
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        this.loggedIn = true;
    }

}

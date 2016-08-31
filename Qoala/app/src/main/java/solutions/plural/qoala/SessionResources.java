package solutions.plural.qoala;

import android.util.Log;

/**
 * Created by gabri on 28/08/2016.
 */
public class SessionResources {
    private static SessionResources ourInstance = new SessionResources();

    private boolean loggedIn;

    public static SessionResources getInstance() {
        String msg = "getInstance() -> "+ourInstance;
        if (ourInstance!=null)
            msg += ": "+ourInstance.toString();
        Log.i("SessionResources", msg );
        return ourInstance;
    }

    private SessionResources() {
        loggedIn=false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}

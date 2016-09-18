package solutions.plural.qoala;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.IBinder;

public class LoginService extends Service {
    public LoginService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SessionResources session = SessionResources.getInstance();
        if(!session.isLoggedIn()){
            // todo login stuff
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

package solutions.plural.qoala.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Util {

    @org.jetbrains.annotations.Contract("null -> false")
    public static boolean isValidEmail(CharSequence email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}

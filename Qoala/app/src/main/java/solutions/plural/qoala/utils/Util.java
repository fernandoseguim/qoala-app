package solutions.plural.qoala.utils;

/**
 * Created by gabri on 18/09/2016.
 */
public class Util {

    @org.jetbrains.annotations.Contract("null -> false")
    public static boolean isValidEmail(CharSequence email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}

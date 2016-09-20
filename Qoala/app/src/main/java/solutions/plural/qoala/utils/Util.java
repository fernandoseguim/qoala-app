package solutions.plural.qoala.utils;

public class Util {

    @org.jetbrains.annotations.Contract("null -> false")
    public static boolean isValidEmail(CharSequence email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}

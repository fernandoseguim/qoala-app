package solutions.plural.qoala.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by gabri on 25/09/2016.
 */
public abstract class UserPermission {

    public static final int PERMISSION_PUBLIC = 1;
    public static final int PERMISSION_EDITOR = 2;
    public static final int PERMISSION_ADMIN = 3;

    @IntDef({PERMISSION_PUBLIC, PERMISSION_EDITOR, PERMISSION_ADMIN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Permission {
    }

}

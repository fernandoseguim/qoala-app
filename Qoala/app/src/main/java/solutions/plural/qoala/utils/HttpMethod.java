package solutions.plural.qoala.utils;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE})
@Retention(RetentionPolicy.SOURCE)
public @interface HttpMethod {
    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";
}

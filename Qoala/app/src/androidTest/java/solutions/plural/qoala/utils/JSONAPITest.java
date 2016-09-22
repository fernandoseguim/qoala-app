package solutions.plural.qoala.utils;

import org.json.JSONObject;
import org.json.JSONStringer;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

/**
 * Created by gabri on 18/09/2016.
 */
public class JSONAPITest {

    @Test
    public void testGetJSON() throws Exception {
        JSONObject retorno = JSONAPI.Get(JSONAPI.urlService, new JSONStringer());
        assertTrue(retorno.has("version"));
    }

    @Test
    public void testPostJSON() throws Exception {
        JSONObject retorno = JSONAPI.Post(JSONAPI.urlService, new JSONStringer());
        assertTrue(retorno.has("version"));
    }
}
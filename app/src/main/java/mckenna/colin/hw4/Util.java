package mckenna.colin.hw4;

/**
 * Created by cmckenna on 11/6/2015.
 */

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Util {
    public static UFOPosition getUFOPosition(JSONObject jsonObject) {
        return new UFOPosition(
                jsonObject.optInt("ship"),
                jsonObject.optDouble("lat"),
                jsonObject.optDouble("lon")
        );
    }
    public static String getJson(UFOPosition ufo) throws JSONException {
        JSONStringer stringer = new JSONStringer().object()
                .key("ship").value(ufo.getShipNum())
                .key("lat").value(ufo.getLat())
                .key("lon").value(ufo.getLon());
        return stringer.endObject().toString();
    }
}

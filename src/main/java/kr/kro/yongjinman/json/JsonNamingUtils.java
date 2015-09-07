/*
 * JsonUtils.java
 *
 * 2015. 6. 15.

 */

package kr.kro.yongjinman.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @author yjmoon
 */
public class JsonNamingUtils {

    // private static final String TAG = JsonNamingUtils.class.getSimpleName();

    public static JsonElement popJsonObject(final String pJsonString
            , final String pNaming) {
        JsonElement pRetValue = null;

        final Gson pGson = new Gson();
        final JsonElement pElement = pGson.fromJson(pJsonString, JsonElement.class);
        pRetValue = popJsonObject(pElement, pNaming);

        return pRetValue;
    }

    public static JsonElement popJsonObject(final JsonElement pJsonElement
            , final String pNaming) {
        JsonElement pRetValue = pJsonElement;
        final String[] pSplit = pNaming.replace(".", ":").split(":");
        try {
            for(int pCount = 0; pCount < pSplit.length; pCount++) {
                if(pSplit[pCount].matches("^[0-9]*$") && pRetValue.isJsonArray()) {
                    final Integer pPos = Integer.parseInt(pSplit[pCount]);
                    pRetValue = pRetValue.getAsJsonArray().get(pPos);
                } else {
                    pRetValue = pRetValue.getAsJsonObject().get(pSplit[pCount]);
                }
            }
        } catch(final Exception pEx) {
            // PrintLog.e(TAG, pEx);
            pRetValue = null;
        }
        return pRetValue;
    }
}

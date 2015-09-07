/*
 * JsonNamingObject.java
 *
 * 2015. 6. 15.

 */

package kr.kro.yongjinman.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author yongjinman
 */
public class JsonNamingObject {

//    private static final String TAG = JsonNamingObject.class.getSimpleName();

    private JsonElement mJsonElement = null;

    public JsonNamingObject() {
        mJsonElement = new JsonObject();
    }

    public JsonNamingObject(final String pJsonString) {
        final Gson pGson = new Gson();
        try {
            mJsonElement = pGson.fromJson(pJsonString, JsonElement.class);
        } catch(final Exception pEx) {
            // PrintLog.e(TAG, "Json parsing error: check json string");
            mJsonElement = null;
        }
    }

    public JsonNamingObject(final String pJsonString, final String pNaming) {
        final Gson pGson = new Gson();
        try {
            mJsonElement = pGson.fromJson(pJsonString, JsonElement.class);
        } catch(final Exception pEx) {
            // PrintLog.e(TAG, "Json parsing error: check json string");
            mJsonElement = null;
        }
        final String[] pSplit = pNaming.replace(".", ":").split(":");
        try {
            for(int pCount = 0; pCount < pSplit.length; pCount++) {
                if(pSplit[pCount].matches("^[(][0-9]*[)]$") && mJsonElement.isJsonArray()) {
                    final Integer pPos = Integer.parseInt(pSplit[pCount].replace("(", "")
                            .replace(")", ""));
                    if(mJsonElement.getAsJsonArray().size() <= pPos) {
                        break;
                    }
                    mJsonElement = mJsonElement.getAsJsonArray().get(pPos);
                } else {
                    mJsonElement = mJsonElement.getAsJsonObject().get(pSplit[pCount]);
                }
            }
        } catch(final Exception pEx) {
            // PrintLog.e(TAG, pEx);
            mJsonElement = null;
        }
    }

    public JsonNamingObject(final JsonElement pJsonElement
            , final String pNaming) {
        mJsonElement = pJsonElement;
        final String[] pSplit = pNaming.replace(".", ":").split(":");
        try {
            for(int pCount = 0; pCount < pSplit.length; pCount++) {
                if(pSplit[pCount].matches("^[(][0-9]*[)]$") && mJsonElement.isJsonArray()) {
                    final Integer pPos = Integer.parseInt(pSplit[pCount].replace("(", "")
                            .replace(")", ""));
                    if(mJsonElement.getAsJsonArray().size() <= pPos) {
                        break;
                    }
                    mJsonElement = mJsonElement.getAsJsonArray().get(pPos);
                } else {
                    mJsonElement = mJsonElement.getAsJsonObject().get(pSplit[pCount]);
                }
            }
        } catch(final Exception pEx) {
            mJsonElement = null;
        }
    }

    public JsonNamingObject(final JsonNamingObject pJsonNamingObject
            , final String pNaming) {
        mJsonElement = pJsonNamingObject.getJsonElement();
        final String[] pSplit = pNaming.replace(".", ":").split(":");
        try {
            for(int pCount = 0; pCount < pSplit.length; pCount++) {
                if(pSplit[pCount].matches("^[(][0-9]*[)]$") && mJsonElement.isJsonArray()) {
                    final Integer pPos = Integer.parseInt(pSplit[pCount].replace("(", "")
                            .replace(")", ""));
                    if(mJsonElement.getAsJsonArray().size() <= pPos) {
                        break;
                    }
                    mJsonElement = mJsonElement.getAsJsonArray().get(pPos);
                } else {
                    mJsonElement = mJsonElement.getAsJsonObject().get(pSplit[pCount]);
                }
            }
        } catch(final Exception pEx) {
            mJsonElement = null;
        }
    }

    public JsonElement getJsonElement(final String pNaming) {
        JsonElement pJsonElement = getJsonElement();
        final String[] pSplit = pNaming.replace(".", ":").split(":");
        try {
            for(int pCount = 0; pCount < pSplit.length; pCount++) {
                // PrintLog.d(TAG, "[split=", pSplit[pCount], "][matches=",
                // pSplit[pCount].matches("^[(][0-9]*[)]$"), "][is array=",
                // pJsonElement.isJsonArray(), "]");
                if(pSplit[pCount].matches("^[(][0-9]*[)]$") && pJsonElement.isJsonArray()) {
                    final Integer pPos = Integer.parseInt(pSplit[pCount].replace("(", "")
                            .replace(")", ""));
                    // PrintLog.d(TAG, "[arr size=",
                    // pJsonElement.getAsJsonArray().size(), "][pos=", pPos,
                    // "]");
                    if(pJsonElement.getAsJsonArray().size() <= pPos) {
                        break;
                    }
                    pJsonElement = pJsonElement.getAsJsonArray().get(pPos);
                } else if(pJsonElement.isJsonPrimitive()) {
                    pJsonElement = null;
                    break;
                } else if(pJsonElement.isJsonObject()) {
                    pJsonElement = pJsonElement.getAsJsonObject().get(pSplit[pCount]);
                }
            }
        } catch(final Exception pEx) {
            // PrintLog.d(TAG, "[mJsonElement=", mJsonElement, "]");
            pJsonElement = null;
        }
        return pJsonElement;
    }

    public boolean setJsonElement(final String pNaming, final String pValue) {
        boolean pRetValue = false;
        StringBuilder pNameTemp = new StringBuilder();
        JsonElement pJsonElement = getJsonElement();
        final String[] pSplit = pNaming.replace(".", ":").split(":");
        try {
            int pCount = 0;
            for(; pCount < pSplit.length - 1; pCount++) {
                pNameTemp.append(pSplit[pCount]).append(".");
                // PrintLog.d(TAG, "[split=", pSplit[pCount], "][pNameTemp=",
                // pNameTemp, "]");
                if(getJsonElement(pNameTemp.toString()) == null) {
                    // PrintLog.d(TAG, "null");
                    if(pSplit[pCount + 1].matches("^[(][0-9]*[)]$")) {
                        pJsonElement.getAsJsonObject().add(pSplit[pCount], new JsonArray());
                    } else {
                        pJsonElement.getAsJsonObject().add(pSplit[pCount], new JsonObject());
                    }
                }
                // PrintLog.d(TAG, "not null");
                if(pSplit[pCount].matches("^[(][0-9]*[)]$")) {
                    final Integer pPos = Integer.parseInt(pSplit[pCount].replace("(", "")
                            .replace(")", ""));
                    if(pJsonElement.getAsJsonArray().size() + 1 <= pPos) {
                        // PrintLog.e(TAG, "index error [arr size=",
                        // pJsonElement.getAsJsonArray().size(), "], [pos=",
                        // pPos, "]");
                        break;
                    }
                    if(pJsonElement.getAsJsonArray().size() == pPos) {
                        pJsonElement.getAsJsonArray().add(new JsonObject());
                    }
                    pJsonElement = pJsonElement.getAsJsonArray().get(pPos);
                } else {
                    pJsonElement = pJsonElement.getAsJsonObject().get(pSplit[pCount]);
                }
                // PrintLog.d(TAG, "[mJsonElement=", mJsonElement, "]");
            }
            pJsonElement.getAsJsonObject().addProperty(pSplit[pCount], pValue);
        } catch(final Exception pEx) {
            // PrintLog.e(TAG, pEx);
        }
        return pRetValue;
    }

    public JsonElement getJsonElement() {
        return mJsonElement;
    }

    public String toString() {
        if(mJsonElement == null) {
            return null;
        }
        return mJsonElement.toString();
    }
}

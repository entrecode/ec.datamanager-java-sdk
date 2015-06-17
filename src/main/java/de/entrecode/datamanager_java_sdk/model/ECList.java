package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 11.06.15.
 */
public class ECList<T> {
    private transient String mAuthHeaderValue;

    private int count;
    private int total;

    private ArrayList<T> _embedded;
    private JsonElement _links;

    public ArrayList<T> getEmbedded() {
        return _embedded;
    }

    public void setEmbedded(ArrayList<T> _embedded) {
        this._embedded = _embedded;
    }

    public String getAuthHeaderValue() {
        return mAuthHeaderValue;
    }

    public void setAuthHeaderValue(String authHeaderValue) {
        this.mAuthHeaderValue = authHeaderValue;
        injectAuthHeaderToEmbedded();
    }

    private void injectAuthHeaderToEmbedded() {
        for (T elem : _embedded) {
            if (elem instanceof ECEntry) {
                ((ECEntry) elem).mAuthHeaderValue = mAuthHeaderValue;
            } else if (elem instanceof ECAsset) {
                ((ECAsset) elem).mAuthHeaderValue = mAuthHeaderValue;
            }
        }
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public JsonElement getLinks() {
        return _links;
    }

    public static class ECListJsonDeserializer<T> implements JsonDeserializer<ECList<T>> {
        Type mTypedClass;

        public ECListJsonDeserializer(Type typedClass) {
            mTypedClass = typedClass;
        }

        @Override
        public ECList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ECList<T> out = new ECList<T>();

            out.count = json.getAsJsonObject().get("count").getAsInt();
            out.total = json.getAsJsonObject().get("total").getAsInt();
            out._links = json.getAsJsonObject().get("_links");

            if (out.count == 0) {
                return out;
            }
            JsonObject embedded = json.getAsJsonObject().get("_embedded").getAsJsonObject();
            JsonArray arr = new JsonArray();

            for (Map.Entry member : embedded.entrySet()) {
                JsonElement elem = (JsonElement) member.getValue();
                if (elem.isJsonObject()) {
                    arr = new JsonArray();
                    arr.add(elem);
                } else {
                    arr = elem.getAsJsonArray();
                }
            }

            ArrayList<T> list = new ArrayList<>();

            for (JsonElement elem : arr) {
                list.add(new ECResourceParser<T>((Class) mTypedClass).fromJson(elem));
                //list.add(context.deserialize(elem, mTypedClass));
            }

            out._embedded = list;

            return out;
        }
    }
}
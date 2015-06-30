package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class representing ECLists of Data Managers.
 */
public class ECList<T> {
    private transient String mAuthHeaderValue;

    private int count;
    private int total;

    private ArrayList<T> _embedded;
    private JsonElement _links;

    /**
     * Get embedded resources.
     *
     * @return ArrayList of list items. Can be ECEntries or ECAssets.
     */
    public ArrayList<T> getEmbedded() {
        return _embedded;
    }

    /**
     * Get authorization header associated with this list.
     *
     * @return The autorization header.
     */
    public String getAuthHeaderValue() {
        return mAuthHeaderValue;
    }

    /**
     * Set autorization header for this list. Will inject the header to all embedded resources.
     *
     * @param authHeaderValue The authorization header.
     */
    public void setAuthHeaderValue(String authHeaderValue) {
        this.mAuthHeaderValue = authHeaderValue;
        injectAuthHeaderToEmbedded();
    }

    /**
     * Method for injecting authorization header to embedded resources. Used by {@link #setAuthHeaderValue(String)}.
     */
    private void injectAuthHeaderToEmbedded() {
        if (_embedded != null) {
            for (T elem : _embedded) {
                if (elem instanceof ECEntry) {
                    ((ECEntry) elem).mAuthHeaderValue = mAuthHeaderValue;
                } else if (elem instanceof ECAsset) {
                    ((ECAsset) elem).mAuthHeaderValue = mAuthHeaderValue;
                }
            }
        }
    }

    /**
     * Get total number of elements of the resources this list represents.
     *
     * @return The total number
     */
    public int getTotal() {
        return total;
    }

    /**
     * Get the count of elements currently in this list.
     *
     * @return The count.
     */
    public int getCount() {
        return count;
    }

    /**
     * Get links element holding HAL-links
     *
     * @return JsonElement with HAL-links
     */
    public JsonElement getLinks() {
        return _links;
    }

    /**
     * GSON JsonDeserializer for ECLists
     *
     * @param <T> The type of the elements in the list.
     */
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
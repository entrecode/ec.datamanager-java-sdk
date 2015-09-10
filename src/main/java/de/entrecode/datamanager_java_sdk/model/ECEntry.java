package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.*;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryDeleteRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntrySaveRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing entries of Data Managers.
 */
public class ECEntry {
    protected transient String mAuthHeaderValue;

    private String id;
    private String created;
    private String modified;
    private Boolean isPrivate;
    private HashMap<String, Object> values;

    private JsonObject _links;

    /**
     * Default constructor.
     */
    public ECEntry() {
        values = new HashMap<>();
        isPrivate = false;
    }

    /**
     * Set all values of the entry.
     *
     * @param values HashMap with all field values for this entry.
     */
    private void setValues(HashMap<String, Object> values) {
        this.values = values;
    }

    /**
     * Universal getter for entry values. Can get all mandatory fields (id, created, modified, isPrivate|private) and all other field values of the entry.
     *
     * @param key Name of the desired field.
     * @return The values for the desired field.
     */
    public Object get(String key) {
        switch (key) {
            case "id":
                return id;
            case "created":
                return created;
            case "modified":
                return modified;
            case "isPrivate":
            case "private":
                return isPrivate;
            default:
                return values.get(key);
        }
    }

    /**
     * Universal setter for entry values. Can set all mandatory fields (id, created, modified, isPrivate|private) and all other field values of the entry.
     *
     * @param key   Name of the desired field.
     * @param value The values for the desired field.
     */
    public void set(String key, Object value) {
        switch (key) {
            case "id":
            case "created":
            case "modified":
                break;
            case "isPrivate":
            case "private":
                isPrivate = (Boolean) value;
                break;
            default:
                values.put(key, value);
                break;
        }
    }

    /**
     * Setter vor authHeaderValue
     *
     * @param authHeaderValue The authHeader which should be set.
     */
    public void setAuthHeaderValue(String authHeaderValue) {
        this.mAuthHeaderValue = authHeaderValue;
    }

    /**
     * Delete request for this entry.
     * <br><br>
     * Example:
     * <pre>{@code
     *     ECEntry entry;
     *     …
     *     entry.delete().onResponse(ok -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @return ECEntryDeleteRequests
     */
    public ECEntryDeleteRequest delete() {
        if (mAuthHeaderValue == null) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return new ECEntryDeleteRequest(
                mAuthHeaderValue,
                _links.getAsJsonObject("self").get("href").getAsString());
    }

    /**
     * Save request for this entry.
     * <br><br>
     * Example:
     * <pre>{@code
     *     ECEntry entry;
     *     …
     *     entry.save().onResponse(entry -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @return ECEntrySaveRequest
     */
    public ECEntrySaveRequest save() {
        if (mAuthHeaderValue == null) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return (ECEntrySaveRequest) new ECEntrySaveRequest(
                mAuthHeaderValue,
                _links.getAsJsonObject("self")
                        .get("href").getAsString()).body(this.toBody());
    }

    /**
     * Method for converting this entry into a {@link RequestBody}.
     *
     * @return Converted entry RequestBody
     */
    public RequestBody toBody() {
        RequestBody b = RequestBody.create(MediaType.parse("application/json"), new ECResourceParser<ECEntry>(ECEntry.class).toJson(this));
        return b;
    }

    /**
     * Get links object holding HAL-links
     *
     * @return JsonObject with HAL-links
     */
    public JsonObject getLinks() {
        return _links;
    }

    /**
     * Set links object holding HAL-links
     *
     * @param links JsonObject with HAL-links
     */
    public void setLinks(JsonObject links) {
        this._links = links;
    }

    /**
     * GSON JsonDeserializer for ECEntries
     */
    public static class ECEntryJsonDeserializer implements JsonDeserializer<ECEntry> {
        @Override
        public ECEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();

            ECEntry out = new ECEntry();
            if (obj.has("id")) {
                out.id = obj.get("id").getAsString();
            }
            if (obj.has("created")) {
                out.created = obj.get("created").getAsString();
            }
            if (obj.has("modified")) {
                out.modified = obj.get("modified").getAsString();
            }
            out.isPrivate = obj.has("private") && obj.get("private").getAsBoolean();
            if (obj.has("_links")) {
                out._links = obj.get("_links").getAsJsonObject();
            }

            JsonElement current;
            HashMap<String, Object> values = new HashMap<>();
            for (Map.Entry elem : obj.entrySet()) {
                if (!elem.getKey().equals("id") &&
                        !elem.getKey().equals("created") &&
                        !elem.getKey().equals("modified") &&
                        !elem.getKey().equals("private") &&
                        !elem.getKey().equals("_links")) {
                    current = (JsonElement) elem.getValue();
                    if (current.isJsonPrimitive() && current.getAsJsonPrimitive().isString()) {
                        values.put(String.valueOf(elem.getKey()), current.getAsString());
                    } else if (current.isJsonPrimitive() && current.getAsJsonPrimitive().isBoolean()) {
                        values.put(String.valueOf(elem.getKey()), current.getAsBoolean());
                    } else if (current.isJsonPrimitive() && current.getAsJsonPrimitive().isNumber()) {
                        values.put(String.valueOf(elem.getKey()), current.getAsNumber());
                    } else {
                        values.put(String.valueOf(elem.getKey()), current);
                    }
                }
            }
            out.setValues(values);

            return out;
        }
    }

    /**
     * GSON JsonSerializer for ECEntries
     */
    public static class ECEntryJsonSerializer implements JsonSerializer<ECEntry> {
        @Override
        public JsonElement serialize(ECEntry src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject out = new JsonObject();
            if (src.id != null) {
                out.addProperty("id", src.id);
            }
            if (src.created != null) {
                out.addProperty("created", src.created);
            }
            if (src.modified != null) {
                out.addProperty("modified", src.modified);
            }
            if (src.isPrivate != null) {
                out.addProperty("private", src.isPrivate);
            }
            if (src._links != null) {
                out.add("_links", src._links);
            }

            if (src.values != null) {
                for (Map.Entry elem : src.values.entrySet()) {
                    if (elem.getValue() instanceof Boolean) {
                        out.addProperty(String.valueOf(elem.getKey()), (boolean) elem.getValue());
                    } else if (elem.getValue() instanceof String) {
                        out.addProperty(String.valueOf(elem.getKey()), String.valueOf(elem.getValue()));
                    } else if (elem.getValue() instanceof Number) {
                        out.addProperty(String.valueOf(elem.getKey()), (Number) elem.getValue());
                    } else {
                        out.add(String.valueOf(elem.getKey()), (JsonElement) elem.getValue());
                    }
                }
            }

            return out;
        }
    }
}

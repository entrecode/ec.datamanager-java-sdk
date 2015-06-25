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
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public class ECEntry {
    protected transient String mAuthHeaderValue;

    private String id;
    private String created;
    private String modified;
    private Boolean isPrivate;
    private HashMap<String, Object> values;

    private JsonObject _links;

    public ECEntry() {
        values = new HashMap<>();
        isPrivate = false;
    }

    private void setValues(HashMap<String, Object> values) {
        this.values = values;
    }

    public Object get(String key) {
        switch (key) {
            case "id":
                return id;
            case "created":
                return created;
            case "modified":
                return modified;
            case "isPrivate":
                return isPrivate;
            default:
                return values.get(key);
        }
    }

    public void set(String key, Object value) {
        switch (key) {
            case "id":
                id = (String) value;
                break;
            case "created":
                created = (String) value;
                break;
            case "modified":
                modified = (String) value;
                break;
            case "isPrivate":
                isPrivate = (Boolean) value;
                break;
            default:
                values.put(key, value);
                break;
        }
    }

    public ECEntryDeleteRequest delete() {
        if (mAuthHeaderValue == null) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return new ECEntryDeleteRequest(
                mAuthHeaderValue,
                _links.getAsJsonObject("self").get("href").getAsString());
    }

    public ECEntrySaveRequest save() {
        if (mAuthHeaderValue == null) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return (ECEntrySaveRequest) new ECEntrySaveRequest(
                mAuthHeaderValue,
                _links.getAsJsonObject("self")
                        .get("href").getAsString()).body(this.toBody());
    }

    public RequestBody toBody() {
        RequestBody b = RequestBody.create(MediaType.parse("application/json"), new ECResourceParser<ECEntry>(ECEntry.class).toJson(this));
        return b;
    }

    public JsonObject getLinks() {
        return _links;
    }

    public void setLinks(JsonObject links) {
        this._links = links;
    }

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
            if (obj.has("private")) {
                out.isPrivate = obj.get("private").getAsBoolean();
            } else {
                out.isPrivate = false;
            }
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
                    if (current.isJsonPrimitive()) {
                        if (current.getAsJsonPrimitive().isString()) {
                            values.put(String.valueOf(elem.getKey()), current.getAsString());
                        } else if (current.getAsJsonPrimitive().isBoolean()) {
                            values.put(String.valueOf(elem.getKey()), current.getAsBoolean());
                        } else if (current.getAsJsonPrimitive().isNumber()) {
                            values.put(String.valueOf(elem.getKey()), current.getAsNumber());
                        } else {
                            values.put(String.valueOf(elem.getKey()), current.getAsJsonPrimitive());
                        }
                    } else {
                        values.put(String.valueOf(elem.getKey()), elem.getValue());
                    }
                }
            }
            out.setValues(values);

            return out;
        }
    }

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

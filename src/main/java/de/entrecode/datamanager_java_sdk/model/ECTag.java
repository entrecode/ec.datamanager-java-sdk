package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.*;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.requests.tags.ECTagDeleteRequest;
import de.entrecode.datamanager_java_sdk.requests.tags.ECTagPutRequest;

import java.lang.reflect.Type;

/**
 * Class representing tags of Data Managers.
 */
public class ECTag {
    protected transient String mAuthHeaderValue;

    private String tag;
    private int count;
    private JsonObject _links;

    /**
     * Default constructor for GSON deserialization.
     */
    public ECTag() {
    }

    /**
     * Get the name of the tag.
     *
     * @return The tag name.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set the name of the tag.
     *
     * @param tag The tag name.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Get the count of the tag.
     *
     * @return The count.
     */
    public int getCount() {
        return count;
    }

    /**
     * Set the count of the tag.
     *
     * @param count The count.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Get the links of the tag.
     *
     * @return The links.
     */
    public JsonObject getLinks() {
        return _links;
    }

    /**
     * Set the links of the tag.
     *
     * @param links The links.
     */
    public void setLinks(JsonObject links) {
        this._links = links;
    }

    /**
     * Set the autHeader of the tag.
     *
     * @param authHeader The authHeader.
     */
    public void setAuthHeader(String authHeader) {
        mAuthHeaderValue = authHeader;
    }

    /**
     * Delete request for this tag.
     * <br><br>
     * Example:
     * <pre>{@code
     *     ECTag tag;
     *     …
     *     tag.delete().onResponse(ok -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @return ECTagDeleteRequests
     */
    public ECTagDeleteRequest delete() {
        if (mAuthHeaderValue == null) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return new ECTagDeleteRequest(
                mAuthHeaderValue,
                _links.getAsJsonObject("self").get("href").getAsString());
    }

    /**
     * Save request for this tag.
     * <br><br>
     * Example:
     * <pre>{@code
     *     ECTag tag;
     *     …
     *     tag.save().onResponse(tag -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @return ECTagPutRequest
     */
    public ECTagPutRequest save() {
        if (mAuthHeaderValue == null) {
            throw new ECDataMangerInReadOnlyModeException();
        }


        return (ECTagPutRequest) new ECTagPutRequest(mAuthHeaderValue,
                _links.getAsJsonObject("self")
                        .get("href").getAsString()).body(this.toBody());
    }

    /**
     * Method for converting this entry into a {@link RequestBody}.
     *
     * @return Converted entry RequestBody
     */
    public RequestBody toBody() {
        RequestBody b = RequestBody.create(MediaType.parse("application/json"), new ECResourceParser<ECTag>(ECTag.class).toJson(this));
        return b;
    }

    /**
     * GSON JsonDeserializer for ECTags
     */
    public static class ECTagJsonDeserializer implements JsonDeserializer<ECTag> {
        @Override
        public ECTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ECTag out = new ECTag();
            JsonObject in = json.getAsJsonObject();
            out.setTag(in.get("tag").getAsString());
            out.setCount(in.get("count").getAsInt());
            out.setLinks(context.deserialize(in.get("_links"), JsonObject.class));
            return out;
        }
    }

    /**
     * GSON JsonSerializer for ECEntries
     */
    public static class ECTagJsonSerializer implements JsonSerializer<ECTag> {
        @Override
        public JsonElement serialize(ECTag src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject out = new JsonObject();
            if (src.tag != null) {
                out.addProperty("tag", src.tag);
            }
            out.addProperty("count", src.count);
            if (src._links != null) {
                out.add("_links", src._links);
            }
            return out;
        }
    }
}

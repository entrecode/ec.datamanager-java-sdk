package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.*;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;
import de.entrecode.datamanager_java_sdk.requests.assets.ECAssetDeleteRequest;

import java.lang.reflect.Type;

/**
 * The class represents assets used in Data Managers.
 */
public class ECAsset {
    protected transient String mAuthHeaderValue;
    private transient String selfRef;

    private String assetID;
    private String title;
    private String type;
    private String created;
    private JsonArray files;
    private String[] tags;
    private JsonObject _links;

    /**
     * Creates an asset object with dataManager, selfRef, and title.
     *
     * @param mDataManager The DataManager this asset is part of.
     * @param selfRef      The selfRef identifying the asset.
     * @param title        The title of the asset.
     */
    public ECAsset(DataManager mDataManager, String selfRef, String title) {
        if (mDataManager.getToken() != null) {
            this.mAuthHeaderValue = mDataManager.getToken().toString();
        }
        this.selfRef = selfRef;
        this.title = title;
    }

    /**
     * Default constructor for GSON deserialization.
     */
    public ECAsset() {
    }

    /**
     * Get the title of the asset.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the asset.
     *
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the type of the asset.
     *
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get created date of the asset.
     *
     * @return The created date.
     */
    public String getCreated() {
        return created;
    }

    /**
     * Get the file list of the asset.
     *
     * @return The file list.
     */
    public JsonArray getFiles() {
        return files;
    }

    /**
     * Set the file list of the asset.
     *
     * @param files The file list.
     */
    public void setFiles(JsonArray files) {
        this.files = files;
    }

    /**
     * Get the array of tags of the asset.
     *
     * @return The array of tags.
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * Set the array of tags of the asset.
     *
     * @param tags The array of tags.
     */
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    /**
     * Get the id of the asset.
     *
     * @return The id.
     */
    public String getAssetID() {
        return assetID;
    }

    /**
     * Set the authorization header for this asset.
     *
     * @param authHeader The authorization header.
     */
    public void setAuthHeader(String authHeader) {
        this.mAuthHeaderValue = authHeader;
    }

    /**
     * Delete request for this asset.
     * <br><br>
     * Example:
     * <pre>{@code
     *     ECAsset asset;
     *     …
     *     asset.delete().onResponse(ok -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @return ECDeleteRequests
     */
    public ECDeleteRequest delete() {
        if (mAuthHeaderValue == null) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        if (selfRef != null) {
            return new ECAssetDeleteRequest(mAuthHeaderValue, selfRef);
        } else {
            return new ECAssetDeleteRequest(mAuthHeaderValue, _links.get("self").getAsJsonObject().get("href").getAsString());
        }
    }

    /**
     * GSON JsonDeserializer for ECAssets.
     */
    public static class ECAssetJsonDeserializer implements JsonDeserializer<ECAsset> {
        @Override
        public ECAsset deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ECAsset out = new ECAsset();
            JsonObject in = json.getAsJsonObject();
            out.assetID = in.get("assetID").getAsString();
            out.setTitle(in.get("title").getAsString());
            out.type = in.get("type").getAsString();
            out.created = in.get("created").getAsString();
            out.setFiles(context.deserialize(in.get("files"), JsonArray.class));
            if (in.has("tags")) {
                out.setTags(context.deserialize(in.get("tags"), String[].class));
            }
            out._links = context.deserialize(in.get("_links"), JsonObject.class);
            return out;
        }
    }
}

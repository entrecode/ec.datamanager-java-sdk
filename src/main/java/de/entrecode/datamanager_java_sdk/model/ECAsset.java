package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.*;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;
import de.entrecode.datamanager_java_sdk.requests.assets.ECAssetDeleteRequest;

import java.lang.reflect.Type;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 11.06.15.
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

    public ECAsset(DataManager mDataManager, String selfRef, String title) {
        if (mDataManager.getToken() != null) {
            this.mAuthHeaderValue = mDataManager.getToken().toString();
        }
        this.selfRef = selfRef;
        this.title = title;
    }

    public ECAsset() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public String getCreated() {
        return created;
    }

    public JsonArray getFiles() {
        return files;
    }

    public void setFiles(JsonArray files) {
        this.files = files;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getAssetID() {
        return assetID;
    }

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

    public void setAuthHeader(String authHeader) {
        this.mAuthHeaderValue = authHeader;
    }

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

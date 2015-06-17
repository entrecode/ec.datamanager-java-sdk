package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.*;
import de.entrecode.datamanager_java_sdk.DataManager;
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
        this.mAuthHeaderValue = mDataManager.getToken().toString();
        this.selfRef = selfRef;
        this.title = title;
    }

    public ECAsset() {
    }

    public ECDeleteRequest delete() {
        if (selfRef != null) {
            return new ECAssetDeleteRequest(mAuthHeaderValue, selfRef);
        } else {
            return new ECAssetDeleteRequest(mAuthHeaderValue, _links.get("self").getAsJsonObject().get("href").getAsString());
        }
    }

    public static class ECAssetJsonDeserializer implements JsonDeserializer<ECAsset> {
        @Override
        public ECAsset deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ECAsset out = new ECAsset();
            JsonObject in = json.getAsJsonObject();
            out.assetID = in.get("assetID").getAsString();
            out.title = in.get("title").getAsString();
            out.type = in.get("type").getAsString();
            out.created = in.get("created").getAsString();
            out.files = context.deserialize(in.get("files"), JsonArray.class);
            if (in.has("tags")) {
                out.tags = context.deserialize(in.get("tags"), String[].class);
            }
            out._links = context.deserialize(in.get("_links"), JsonObject.class);
            return out;
        }
    }
}

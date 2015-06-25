package de.entrecode.datamanager_java_sdk.requests.models;

import com.google.gson.JsonObject;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 04.06.15.
 */
public class ECSchemaRequest extends ECRequest<JsonObject> {
    private static final List<String> METHODS = new ArrayList<String>() {{
        add("get");
        add("put");
        add("post");
    }};

    protected final DataManager mDataManager;
    private final String mModelID;

    protected String mSchema = "get";

    public ECSchemaRequest(DataManager dataManager, String modelID) {
        mDataManager = dataManager;
        mModelID = modelID;
    }

    public ECSchemaRequest forMethod(String method) {
        if (!METHODS.contains(method.toLowerCase())) {
            throw new IllegalArgumentException("Method " + method + " not supported.");
        }
        mSchema = method.toLowerCase();
        return this;
    }

    @Override
    public Request build() {
        HttpUrl url = HttpUrl.parse(mDataManager.getUrl().replace("/api", "/api/schema") + mModelID)
                .newBuilder()
                .addQueryParameter("template", mSchema)
                .build();
        Request.Builder builder = new Request.Builder().url(url)
                .method(this.mMethod, null);
        if (!mDataManager.getReadOnly()) {
            builder.header("Authorization", "Bearer " + mDataManager.getToken());
        }
        return builder.build();
    }

    @Override
    public JsonObject buildResponse(Reader response) {
        return new ECResourceParser<JsonObject>(JsonObject.class).fromJson(response);
    }
}

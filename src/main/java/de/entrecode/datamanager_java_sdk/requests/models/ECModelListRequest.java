package de.entrecode.datamanager_java_sdk.requests.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.Model;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 04.06.15.
 */
public class ECModelListRequest extends ECRequest<List<Model>> {
    protected final DataManager mDataManager;

    public ECModelListRequest(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public Request build() {
        Request.Builder builder = new Request.Builder().url(mDataManager.getUrl());
        builder.method(this.mMethod, null);
        if (!mDataManager.getReadOnly()) {
            builder.addHeader("Authorization", "Bearer " + mDataManager.getToken());
        }
        return builder.build();
    }

    @Override
    public List<Model> buildResponse(Reader response) {
        List<Model> models = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        JsonObject res = gson.fromJson(response, JsonObject.class);
        JsonObject _embedded = res.getAsJsonObject("_embedded");

        for (Map.Entry embedded : _embedded.entrySet()) {
            String titleField = ((JsonObject) embedded.getValue()).get("titleField").getAsString();
            String hexColor = ((JsonObject) embedded.getValue()).get("hexColor").getAsString();
            String modelName = embedded.getKey().toString().substring(embedded.getKey().toString().lastIndexOf(":") + 1);
            models.add(new Model(mDataManager, modelName, titleField, hexColor));
        }

        return models;
    }
}

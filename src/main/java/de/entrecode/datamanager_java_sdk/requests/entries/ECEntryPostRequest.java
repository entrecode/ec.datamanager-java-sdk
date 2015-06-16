package de.entrecode.datamanager_java_sdk.requests.entries;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.Model;
import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.model.ECList;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECPostRequest;

import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 08.06.15.
 */
public class ECEntryPostRequest extends ECPostRequest<ECEntry> {
    private final Model mModel;

    public ECEntryPostRequest(Model model) {
        super(model.getDataManager().getToken() != null ? model.getDataManager().getToken().toString() : null);
        mModel = model;
    }

    @Override
    public Request build() {
        if (mModel.getDataManager().getReadOnly() && !mModel.getModelID().equals("user")) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(mModel.getDataManager().getUrl() + mModel.getModelID()).newBuilder();
        Request.Builder builder = new Request.Builder().url(urlBuilder.build());
        builder.post(mBody);
        if (mAuthHeaderValue != null) { // do not include if null -> user register
            builder.addHeader("Authorization", "Bearer " + mAuthHeaderValue);
        }
        return builder.build();
    }

    @Override
    public ECEntry buildResponse(Reader response) {
        Class clazz = new TypeToken<ECList<ECEntry>>() {
        }.getRawType();
        ECList<ECEntry> res = new ECResourceParser<ECList<ECEntry>>(clazz).fromJson(response);
        if (mModel.getDataManager().getToken() != null) {
            res.setAuthHeaderValue(mModel.getDataManager().getToken().toString());
        } else if (mModel.getModelID().equals("user")) {
            res.setAuthHeaderValue(String.valueOf(res.getEmbedded().get(0).get("temporaryToken")));
        }
        return res.getEmbedded().get(0);
    }
}

package de.entrecode.datamanager_java_sdk.requests.assets;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECAsset;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 17.06.15.
 */
public class ECAssetRequest extends ECRequest<ECAsset> {
    private final DataManager mDataManager;
    private final String mID;

    public ECAssetRequest(DataManager dataManager, String id) {
        mDataManager = dataManager;
        mID = id;
    }

    @Override
    public Request build() {
        HttpUrl.Builder builder = HttpUrl.parse(mDataManager.getAssetUrl()).newBuilder();
        builder.addQueryParameter("assetID", mID);
        Request.Builder requestBuilder = new Request.Builder().url(builder.build());
        if (!mDataManager.getReadOnly()) {
            requestBuilder.addHeader("Authorization", "Bearer " + mDataManager.getToken().toString());
        }
        return requestBuilder.build();
    }

    @Override
    public ECAsset buildResponse(Reader response) throws IOException {
        ECAsset out = new ECResourceParser<ECAsset>(ECAsset.class).fromJson(response);
        if (!mDataManager.getReadOnly()) {
            out.setAuthHeader(mDataManager.getToken().toString());
        }
        return out;
    }
}

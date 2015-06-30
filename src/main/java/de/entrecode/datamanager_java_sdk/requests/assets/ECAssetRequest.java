package de.entrecode.datamanager_java_sdk.requests.assets;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECAsset;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;

/**
 * Request for a single asset.
 */
public class ECAssetRequest extends ECRequest<ECAsset> {
    private final DataManager mDataManager;
    private final String mID;

    /**
     * Default consstructor with associated DataManager and id of the requested asset.
     *
     * @param dataManager The DataManager connected to the Data Manager containing the asset.
     * @param id          The id of the requested asset.
     */
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
    public ECAsset buildResponse(Reader response) {
        ECAsset out = new ECResourceParser<ECAsset>(ECAsset.class).fromJson(response);
        if (!mDataManager.getReadOnly()) {
            out.setAuthHeader(mDataManager.getToken().toString());
        }
        return out;
    }
}

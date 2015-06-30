package de.entrecode.datamanager_java_sdk.requests.assets;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECAsset;
import de.entrecode.datamanager_java_sdk.model.ECList;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;

/**
 * Request for all assets of a Data Manager.
 */
public class ECAssetsRequest extends ECRequest<ECList<ECAsset>> {
    private final DataManager mDataManager;

    /**
     * Default constructor with the DataManager of whom the assets should be requested.
     *
     * @param dataManager The DataManager connected to the Data Manager with the assets of interest.
     */
    public ECAssetsRequest(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public Request build() {
        HttpUrl.Builder builder = HttpUrl.parse(mDataManager.getAssetUrl()).newBuilder();
        addFilterToUrlBuilder(builder);
        Request.Builder requestBuilder = new Request.Builder().url(builder.build());
        if (!mDataManager.getReadOnly()) {
            requestBuilder.addHeader("Authorization", "Bearer " + mDataManager.getToken().toString());
        }
        return requestBuilder.build();
    }

    @Override
    public ECList<ECAsset> buildResponse(Reader response) {
        Class clazz = new TypeToken<ECList<ECAsset>>() {
        }.getRawType();
        ECList<ECAsset> res = new ECResourceParser<ECList<ECAsset>>(clazz, "asset").fromJson(response);
        if (!mDataManager.getReadOnly()) {
            res.setAuthHeaderValue(mDataManager.getToken().toString());
        }
        return res;
    }
}
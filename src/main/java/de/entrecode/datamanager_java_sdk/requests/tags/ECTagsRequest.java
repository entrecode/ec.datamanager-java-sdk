package de.entrecode.datamanager_java_sdk.requests.tags;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECList;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.model.ECTag;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;

/**
 * Request for all tags of a Data Manager.
 */
public class ECTagsRequest extends ECRequest<ECList<ECTag>> {
    private final DataManager mDataManager;

    /**
     * Default constructor with the DataManager of whom the tags should be requested.
     *
     * @param dataManager The DataManager connected to the Data Manager with the tags of interest.
     */
    public ECTagsRequest(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public Request build() {
        HttpUrl.Builder builder = HttpUrl.parse(mDataManager.getTagUrl()).newBuilder();
        addFilterToUrlBuilder(builder);
        Request.Builder requestBuilder = new Request.Builder().url(builder.build());
        if (!mDataManager.getReadOnly()) {
            requestBuilder.addHeader("Authorization", "Bearer " + mDataManager.getToken().toString());
        }
        return requestBuilder.build();
    }

    @Override
    public ECList<ECTag> buildResponse(Reader response) {
        Class clazz = new TypeToken<ECList<ECTag>>() {
        }.getRawType();
        ECList<ECTag> res = new ECResourceParser<ECList<ECTag>>(clazz, "tag").fromJson(response);
        if (!mDataManager.getReadOnly()) {
            res.setAuthHeaderValue(mDataManager.getToken().toString());
        }
        return res;
    }
}

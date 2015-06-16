package de.entrecode.datamanager_java_sdk.requests.entries;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.model.ECList;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public class ECEntriesRequest extends ECRequest<ECList<ECEntry>> {
    protected final DataManager mDataManager;
    protected final String mModelID;

    public ECEntriesRequest(DataManager dataManager, String modelID) {
        mDataManager = dataManager;
        mModelID = modelID;
    }

    @Override
    public Request build() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(mDataManager.getUrl() + mModelID).newBuilder();
        addFilterToUrlBuilder(urlBuilder);
        Request.Builder builder = new Request.Builder().url(urlBuilder.build());
        builder.method(this.mMethod, null);
        if (!mDataManager.getReadOnly()) {
            builder.addHeader("Authorization", "Bearer " + mDataManager.getToken());
        }
        return builder.build();
    }

    @Override
    public ECList<ECEntry> buildResponse(Reader response) {
        Class clazz = new TypeToken<ECList<ECEntry>>() {
        }.getRawType();
        ECList<ECEntry> res = new ECResourceParser<ECList<ECEntry>>(clazz).fromJson(response);
        if (!mDataManager.getReadOnly()) {
            res.setAuthHeaderValue(mDataManager.getToken().toString());
        }
        return res;
    }
}

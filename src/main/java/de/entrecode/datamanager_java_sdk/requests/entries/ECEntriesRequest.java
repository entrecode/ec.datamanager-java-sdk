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
 * Request for all entries of a specific model in a Data Manager.
 */
public class ECEntriesRequest extends ECRequest<ECList<ECEntry>> {
    protected final DataManager mDataManager;
    protected final String mModelID;

    protected int mLevels;

    /**
     * Default constructor with associated DataManager and model id.
     *
     * @param dataManager The associated DataManager
     * @param modelID     The model id.
     */
    public ECEntriesRequest(DataManager dataManager, String modelID) {
        mDataManager = dataManager;
        mModelID = modelID;
    }

    /**
     * Sets the levels of linked entries to request. 1-5.
     *
     * @param levels The number of levels.
     * @return ECEntriesRequest
     */
    public ECEntriesRequest levels(int levels) {
        if (levels <= 0 || levels > 5) {
            throw new IllegalArgumentException("Request Levels must be between 0 and 5.");
        }
        mLevels = levels;
        return this;
    }

    @Override
    public Request build() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(mDataManager.getUrl() + mModelID).newBuilder();
        addFilterToUrlBuilder(urlBuilder);
        if (mLevels > 1) {
            urlBuilder.addQueryParameter("_levels", String.valueOf(mLevels));
        }
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

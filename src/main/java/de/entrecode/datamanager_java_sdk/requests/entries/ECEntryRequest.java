package de.entrecode.datamanager_java_sdk.requests.entries;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;

/**
 * Request for a single entry.
 */
public class ECEntryRequest extends ECRequest<ECEntry> {
    protected final DataManager mDataManager;
    protected final String mModelID;
    protected final String mEntryID;

    protected int mLevels;

    /**
     * Default constructor with DataManager, model id, and entry id of the entry which should be requested.
     *
     * @param dataManager The DataManager.
     * @param modelID     The model id.
     * @param entryID     The entry id.
     */
    public ECEntryRequest(DataManager dataManager, String modelID, String entryID) {
        mDataManager = dataManager;
        mModelID = modelID;
        mEntryID = entryID;
    }

    /**
     * Sets the levels of linked entries to request. 1-5.
     *
     * @param levels The number of levels.
     * @return ECEntryRequest
     */
    public ECEntryRequest levels(int levels) {
        mLevels = levels;
        return this;
    }

    @Override
    public Request build() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(mDataManager.getUrl() + mModelID).newBuilder();
        urlBuilder.addQueryParameter("id", mEntryID);
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
    public ECEntry buildResponse(Reader response) {
        ECEntry res = new ECResourceParser<ECEntry>(ECEntry.class).fromJson(response);
        if (!mDataManager.getReadOnly()) {
            res.setAuthHeaderValue(mDataManager.getToken().toString());
        }
        return res;
    }
}

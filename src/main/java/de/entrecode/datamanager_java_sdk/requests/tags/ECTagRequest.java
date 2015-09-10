package de.entrecode.datamanager_java_sdk.requests.tags;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.model.ECTag;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;

/**
 * Request for a single tag.
 */
public class ECTagRequest extends ECRequest<ECTag> {
    private final DataManager mDataManager;
    private final String tagName;

    public ECTagRequest(DataManager mDataManager, String tagName) {
        this.mDataManager = mDataManager;
        this.tagName = tagName;
    }

    @Override
    public Request build() {
        HttpUrl.Builder builder = HttpUrl.parse(mDataManager.getTagUrl()).newBuilder();
        builder.addQueryParameter("tag", tagName);
        Request.Builder requestBuilder = new Request.Builder().url(builder.build());
        if (!mDataManager.getReadOnly()) {
            requestBuilder.addHeader("Authorization", "Bearer " + mDataManager.getToken().toString());
        }
        return requestBuilder.build();
    }

    @Override
    public ECTag buildResponse(Reader response) {
        ECTag out = new ECResourceParser<ECTag>(ECTag.class).fromJson(response);
        if (!mDataManager.getReadOnly()) {
            out.setAuthHeader(mDataManager.getToken().toString());
        }
        return out;
    }
}

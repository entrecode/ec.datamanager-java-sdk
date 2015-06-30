package de.entrecode.datamanager_java_sdk.requests.entries;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.model.ECList;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECPutRequest;

import java.io.Reader;

/**
 * Request for saving a modified entry.
 */
public class ECEntrySaveRequest extends ECPutRequest<ECEntry> {
    private String mUrl;

    /**
     * Default constructor with authorization header and url of the modified entry.
     *
     * @param authHeaderValue The authorization header.
     * @param url             The url of the modified entry.
     */
    public ECEntrySaveRequest(String authHeaderValue, String url) {
        super(authHeaderValue);
        mUrl = url;
    }

    @Override
    public Request build() {
        return new Request.Builder().url(mUrl).put(mBody).addHeader("Authorization", "Bearer " + mAuthHeaderValue).build();
    }

    @Override
    public ECEntry buildResponse(Reader response) {
        Class clazz = new TypeToken<ECList<ECEntry>>() {
        }.getRawType();
        ECList<ECEntry> res = new ECResourceParser<ECList<ECEntry>>(clazz).fromJson(response);
        if (mAuthHeaderValue != null) {
            res.setAuthHeaderValue(mAuthHeaderValue);
        }
        return res.getEmbedded().get(0);
    }
}
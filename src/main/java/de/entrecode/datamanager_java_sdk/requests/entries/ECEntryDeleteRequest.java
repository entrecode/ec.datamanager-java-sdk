package de.entrecode.datamanager_java_sdk.requests.entries;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;

import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 08.06.15.
 */
public class ECEntryDeleteRequest extends ECDeleteRequest {
    protected final String mUrl;

    public ECEntryDeleteRequest(String authHeaderValue, String url) {
        super(authHeaderValue);
        mUrl = url;
    }

    @Override
    public Request build() {
        return new Request.Builder().url(mUrl).delete().addHeader("Authorization", "Bearer " + mAuthHeaderValue).build();
    }
}

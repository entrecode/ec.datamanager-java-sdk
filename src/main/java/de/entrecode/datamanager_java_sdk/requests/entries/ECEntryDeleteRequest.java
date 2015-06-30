package de.entrecode.datamanager_java_sdk.requests.entries;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;

/**
 * Request for deleting an entry.
 */
public class ECEntryDeleteRequest extends ECDeleteRequest {
    protected final String mUrl;

    /**
     * Default constructor for deleting an entry with authorization header and entry url.
     *
     * @param authHeaderValue The authorization header.
     * @param url             The url of the entry.
     */
    public ECEntryDeleteRequest(String authHeaderValue, String url) {
        super(authHeaderValue);
        mUrl = url;
    }

    @Override
    public Request build() {
        return new Request.Builder().url(mUrl).delete().addHeader("Authorization", "Bearer " + mAuthHeaderValue).build();
    }
}

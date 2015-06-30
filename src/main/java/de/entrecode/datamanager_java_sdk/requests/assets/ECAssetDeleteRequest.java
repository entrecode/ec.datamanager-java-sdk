package de.entrecode.datamanager_java_sdk.requests.assets;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;

/**
 * Request for deleting an asset.
 */
public class ECAssetDeleteRequest extends ECDeleteRequest {
    private final String selfRef;

    /**
     * Default constructor with authorization header and self ref of the asset.
     *
     * @param authHeaderValue The authorization header.
     * @param selfRef         The self ref.
     */
    public ECAssetDeleteRequest(String authHeaderValue, String selfRef) {
        super(authHeaderValue);
        this.selfRef = selfRef;
    }

    @Override
    public Request build() {
        return new Request.Builder().url(selfRef).addHeader("Authorization", "Bearer " + mAuthHeaderValue).delete().build();
    }
}

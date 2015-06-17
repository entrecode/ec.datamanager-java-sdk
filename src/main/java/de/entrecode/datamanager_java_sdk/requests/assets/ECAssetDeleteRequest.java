package de.entrecode.datamanager_java_sdk.requests.assets;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 11.06.15.
 */
public class ECAssetDeleteRequest extends ECDeleteRequest {
    private final String selfRef;

    public ECAssetDeleteRequest(String authHeaderValue, String selfRef) {
        super(authHeaderValue);
        this.selfRef = selfRef;
    }

    @Override
    public Request build() {
        return new Request.Builder().url(selfRef).addHeader("Authorization", "Bearer " + mAuthHeaderValue).build();
    }
}

package de.entrecode.datamanager_java_sdk.requests.tags;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 10.09.15.
 */
public class ECTagDeleteRequest extends ECDeleteRequest {
    private final String selfRef;

    /**
     * Default constructor with authorization header value.
     *
     * @param authHeaderValue The authorization header values to use with this request.
     */
    public ECTagDeleteRequest(String authHeaderValue, String selfRef) {
        super(authHeaderValue);
        this.selfRef = selfRef;
    }

    @Override
    public Request build() {
        return new Request.Builder().url(selfRef).addHeader("Authorization", "Bearer " + mAuthHeaderValue).delete().build();
    }
}
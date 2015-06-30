package de.entrecode.datamanager_java_sdk.requests;

import com.squareup.okhttp.RequestBody;

/**
 * Abstract class for all put requests made by ec.datamanager-java-sdk.
 */
public abstract class ECPutRequest<T> extends ECRequest<T> {
    protected final String mAuthHeaderValue;
    protected RequestBody mBody;

    /**
     * Default constructor with authorization header value.
     *
     * @param authHeaderValue The authorization header values to use with this request.
     */
    public ECPutRequest(String authHeaderValue) {
        mAuthHeaderValue = authHeaderValue;
        mMethod = "PUT";
    }

    /**
     * Sets the body which will be PUTed with the request.
     *
     * @param body The {@link RequestBody}
     * @return ECPutRequest.
     */
    public ECPutRequest body(RequestBody body) {
        mBody = body;
        return this;
    }
}

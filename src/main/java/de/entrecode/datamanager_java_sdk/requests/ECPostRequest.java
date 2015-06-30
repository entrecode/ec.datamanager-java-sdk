package de.entrecode.datamanager_java_sdk.requests;

import com.squareup.okhttp.RequestBody;

/**
 * Abstract class for all post requests made by ec.datamanager-java-sdk.
 */
public abstract class ECPostRequest<T> extends ECRequest<T> {
    protected final String mAuthHeaderValue;
    protected RequestBody mBody;

    /**
     * Default constructor with authorization header value.
     *
     * @param authHeaderValue The authorization header values to use with this request.
     */
    public ECPostRequest(String authHeaderValue) {
        mAuthHeaderValue = authHeaderValue;
        mMethod = "POST";
    }

    /**
     * Sets the body which will be POSTed with the request.
     *
     * @param body The {@link RequestBody}
     * @return ECPostRequest.
     */
    public ECPostRequest body(RequestBody body) {
        mBody = body;
        return this;
    }
}

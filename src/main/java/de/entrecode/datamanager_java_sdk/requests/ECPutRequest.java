package de.entrecode.datamanager_java_sdk.requests;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/**
 * Abstract class for all put requests made by ec.datamanager-java-sdk.
 */
public abstract class ECPutRequest<T> extends ECRequest<T> {
    protected final String mAuthHeaderValue;
    protected final String mUrl;
    protected RequestBody mBody;

    /**
     * Default constructor with authorization header value and selfRef.
     *
     * @param authHeaderValue The authorization header values to use with this request.
     * @param selfRef The selfRef for this request
     */
    public ECPutRequest(String authHeaderValue, String selfRef) {
        mAuthHeaderValue = authHeaderValue;
        mUrl = selfRef;
        mMethod = "PUT";
    }

    @Override
    public Request build() {
        return new Request.Builder().url(mUrl).put(mBody).addHeader("Authorization", "Bearer " + mAuthHeaderValue).build();
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

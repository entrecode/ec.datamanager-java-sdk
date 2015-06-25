package de.entrecode.datamanager_java_sdk.requests;

import com.squareup.okhttp.RequestBody;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public abstract class ECPostRequest<T> extends ECRequest<T> {
    protected final String mAuthHeaderValue;
    protected RequestBody mBody;

    public ECPostRequest(String authHeaderValue) {
        mAuthHeaderValue = authHeaderValue;
        mMethod = "POST";
    }

    public ECRequest body(RequestBody body) {
        mBody = body;
        return this;
    }
}

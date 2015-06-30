package de.entrecode.datamanager_java_sdk.requests;

import java.io.Reader;

/**
 * Abstract class for all delete requests made by ec.datamanager-java-sdk.
 */
public abstract class ECDeleteRequest extends ECRequest<Boolean> {
    protected final String mAuthHeaderValue;

    /**
     * Default constructor with authorization header value.
     *
     * @param authHeaderValue The authorization header values to use with this request.
     */
    public ECDeleteRequest(String authHeaderValue) {
        mAuthHeaderValue = authHeaderValue;
        mMethod = "DELETE";
    }

    @Override
    public Boolean buildResponse(Reader response) {
        return true;
    }
}

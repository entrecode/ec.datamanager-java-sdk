package de.entrecode.datamanager_java_sdk.requests;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public abstract class ECDeleteRequest extends ECRequest<Boolean> {
    protected final String mAuthHeaderValue;

    public ECDeleteRequest(String authHeaderValue) {
        mAuthHeaderValue = authHeaderValue;
        mMethod = "delete";
    }

    @Override
    public Boolean buildResponse(Reader response) throws IOException {
        return true;
    }
}

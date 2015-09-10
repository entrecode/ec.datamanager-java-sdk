package de.entrecode.datamanager_java_sdk.requests.entries;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECPutRequest;

import java.io.Reader;

/**
 * Request for saving a modified entry.
 */
public class ECEntryPutRequest extends ECPutRequest<ECEntry> {
    /**
     * Default constructor with authorization header value and selfRef.
     *
     * @param authHeaderValue The authorization header values to use with this request.
     * @param selfRef The selfRef for this request
     */
    public ECEntryPutRequest(String authHeaderValue, String selfRef) {
        super(authHeaderValue, selfRef);
    }

    @Override
    public ECEntry buildResponse(Reader response) {
        ECEntry res = new ECResourceParser<ECEntry>(ECEntry.class).fromJson(response);
        if (mAuthHeaderValue != null) {
            res.setAuthHeaderValue(mAuthHeaderValue);
        }
        return res;
    }
}
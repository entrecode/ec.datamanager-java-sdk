package de.entrecode.datamanager_java_sdk.requests.tags;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.model.ECTag;
import de.entrecode.datamanager_java_sdk.requests.ECPutRequest;

import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 10.09.15.
 */
public class ECTagPutRequest extends ECPutRequest<ECTag> {

    /**
     * Default constructor with authorization header value and selfRef.
     *
     * @param authHeaderValue The authorization header values to use with this request.
     * @param selfRef         The selfRef for this request
     */
    public ECTagPutRequest(String authHeaderValue, String selfRef) {
        super(authHeaderValue, selfRef);
    }

    @Override
    public Request build() {
        return new Request.Builder().url(mUrl).put(mBody).addHeader("Authorization", "Bearer " + mAuthHeaderValue).build();
    }

    @Override
    public ECTag buildResponse(Reader response) {
        ECTag out = new ECResourceParser<ECTag>(ECTag.class).fromJson(response);
        if (mAuthHeaderValue != null) {
            out.setAuthHeader(mAuthHeaderValue);
        }
        return out;
    }
}

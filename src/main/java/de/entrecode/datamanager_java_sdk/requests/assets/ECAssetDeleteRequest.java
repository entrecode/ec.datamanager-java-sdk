package de.entrecode.datamanager_java_sdk.requests.assets;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;

import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 11.06.15.
 */
public class ECAssetDeleteRequest extends ECDeleteRequest {
    private final DataManager mDataManager;

    public ECAssetDeleteRequest(DataManager dataManager, String authHeaderValue) {
        super(authHeaderValue);
        mDataManager = dataManager;
    }

    @Override
    public Request build() {
        return null;
    }

    @Override
    public Boolean buildResponse(Reader response) {
        return true;
    }
}

package de.entrecode.datamanager_java_sdk.requests.assets;

import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECAsset;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 11.06.15.
 */
public class ECAssetsRequest extends ECRequest<ECAsset> {
    private final DataManager mDataManager;

    public ECAssetsRequest(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public Request build() {
        return null;
    }

    @Override
    public ECAsset buildResponse(Reader response) {
        return null;
    }
}

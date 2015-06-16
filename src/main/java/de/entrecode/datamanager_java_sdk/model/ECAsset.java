package de.entrecode.datamanager_java_sdk.model;

import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.requests.assets.ECAssetDeleteRequest;
import de.entrecode.datamanager_java_sdk.requests.ECDeleteRequest;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 11.06.15.
 */
public class ECAsset {
    private DataManager mDataManager;
    protected String mAuthHeaderValue;

    public ECDeleteRequest delete() {
        return new ECAssetDeleteRequest(mDataManager, mAuthHeaderValue);
    }
}

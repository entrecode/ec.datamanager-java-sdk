package de.entrecode.datamanager_java_sdk;

import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntriesRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryPostRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryRequest;
import de.entrecode.datamanager_java_sdk.requests.models.ECSchemaRequest;

/**
 * Created by Simon Scherzinger, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public class Model {
    private DataManager mDataManager;
    private String mModelID;

    public Model(DataManager dataManager, String modelID) {
        mDataManager = dataManager;
        mModelID = modelID;
    }

    public ECEntriesRequest entries() {
        return new ECEntriesRequest(mDataManager, mModelID);
    }

    public ECEntryRequest entry(String id) {
        return (ECEntryRequest) new ECEntryRequest(mDataManager, mModelID, id);
    }

    public ECEntryPostRequest createEntry(ECEntry entry) {
        if (mDataManager.getReadOnly() && !mModelID.equals("user")) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return (ECEntryPostRequest) new ECEntryPostRequest(this).body(entry.toBody());
    }

    public ECSchemaRequest getSchema() {
        return new ECSchemaRequest(mDataManager, mModelID);
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public String getModelID() {
        return mModelID;
    }
}
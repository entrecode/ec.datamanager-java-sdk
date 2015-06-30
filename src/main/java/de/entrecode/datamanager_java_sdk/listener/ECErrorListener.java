package de.entrecode.datamanager_java_sdk.listener;

import de.entrecode.datamanager_java_sdk.model.ECError;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public interface ECErrorListener {
    void onError(ECError error);
}

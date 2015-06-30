package de.entrecode.datamanager_java_sdk.listener;

import de.entrecode.datamanager_java_sdk.model.ECError;

/**
 * Interface for errors.
 */
public interface ECErrorListener {
    void onError(ECError error);
}

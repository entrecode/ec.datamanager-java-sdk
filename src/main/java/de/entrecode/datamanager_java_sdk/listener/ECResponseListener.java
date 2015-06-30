package de.entrecode.datamanager_java_sdk.listener;

/**
 * Interface for responses.
 */
public interface ECResponseListener<T> {
    void onResponse(T response);
}

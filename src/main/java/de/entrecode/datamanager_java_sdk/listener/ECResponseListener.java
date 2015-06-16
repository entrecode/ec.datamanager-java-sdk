package de.entrecode.datamanager_java_sdk.listener;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public interface ECResponseListener<T> {
    void onResponse(T response);
}

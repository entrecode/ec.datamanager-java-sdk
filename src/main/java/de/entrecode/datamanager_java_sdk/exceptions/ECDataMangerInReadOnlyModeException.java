package de.entrecode.datamanager_java_sdk.exceptions;

/**
 * This exception is thrown when a request is called on a read only DataManager which needs a valid access token.
 */
public class ECDataMangerInReadOnlyModeException extends RuntimeException {
    public ECDataMangerInReadOnlyModeException() {
    }
}

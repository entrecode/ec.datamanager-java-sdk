package de.entrecode.datamanager_java_sdk.exceptions;

/**
 * This exception is thrown when a DataManager object is created with a malformed short id. A valid short id is 8 hex-characters.
 */
public class ECMalformedDataManagerIDException extends Exception {
    public ECMalformedDataManagerIDException(String message) {
        super(message);
    }
}

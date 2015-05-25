package de.entrecode.datamanager_java_sdk;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by simon on 21.05.15.
 */
public class DataManager {

    private URL mUrl;
    private UUID mAccessToken;

    public DataManager(URL url) {
        mUrl = url;
        // TODO getPublicUser
        mAccessToken = UUID.randomUUID(); // TODO this
    }

    public DataManager(String id) throws MalformedURLException {
        this(new URL("https://datamanager.entrecode.de/api/" + id));
    }

    public DataManager(URL url, UUID accessToken) {
        mUrl = url;
        mAccessToken = accessToken;
    }
}

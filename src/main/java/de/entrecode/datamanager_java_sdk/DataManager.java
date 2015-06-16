package de.entrecode.datamanager_java_sdk;

import de.entrecode.datamanager_java_sdk.exceptions.ECMalformedDataManagerIDException;
import de.entrecode.datamanager_java_sdk.listener.ECResponseListener;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.requests.assets.ECAssetsRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryPostRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryRequest;
import de.entrecode.datamanager_java_sdk.requests.files.ECFileURLRequest;
import de.entrecode.datamanager_java_sdk.requests.models.ECModelListRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Simon Scherzinger, entrecode GmbH, Stuttgart (Germany) on 22.05.15.
 */
public class DataManager {
    private static final String shortIDRegEx = "^[0-9a-fA-F]{8}$";
    private static final String baseAPIUrl = "https://datamanager.entrecode.de/api/";

    private String mUrl;
    private String mID;
    private UUID mAccessToken;
    private boolean mReadOnly = false;

    public DataManager(URL url, UUID accessToken) {
        mUrl = url.toString();
        mAccessToken = accessToken;

        appendSlashIfNeeded();
        makeID();
    }

    public DataManager(URL url, boolean readOnly) {
        mUrl = url.toString();
        mReadOnly = readOnly;

        appendSlashIfNeeded();
        makeID();
    }

    public DataManager(String id, UUID accessToken) throws ECMalformedDataManagerIDException {
        if (!id.matches(shortIDRegEx)) {
            throw new ECMalformedDataManagerIDException("Malformed Data Manager ID given.");
        }

        mID = id;
        mUrl = baseAPIUrl + id.toLowerCase() + "/";
        mAccessToken = accessToken;
    }

    public DataManager(String id, boolean readOnly) throws ECMalformedDataManagerIDException {
        if (!id.matches(shortIDRegEx)) {
            throw new ECMalformedDataManagerIDException("Malformed Data Manager ID given.");
        }

        mID = id;
        mUrl = baseAPIUrl + id.toLowerCase() + "/";
        mReadOnly = readOnly;
    }

    public static void create(URL url, ECResponseListener<DataManager> responseListener) throws ECMalformedDataManagerIDException {
        DataManager dm = new DataManager(url, true);

        dm.register().onResponse(user -> {
            dm.setToken(UUID.fromString(String.valueOf(user.get("temporaryToken"))));
            dm.setReadOnly(false);
            responseListener.onResponse(dm);
        }).go();
    }

    public static void create(String id, ECResponseListener<DataManager> responseListener) throws ECMalformedDataManagerIDException {
        DataManager dm = new DataManager(id, true);

        dm.register().onResponse(user -> {
            dm.setToken(UUID.fromString(String.valueOf(user.get("temporaryToken"))));
            dm.setReadOnly(false);
            responseListener.onResponse(dm);
        }).go();
    }

    public UUID getToken() {
        return mAccessToken;
    }

    public void setToken(UUID accessToken) {
        this.mAccessToken = accessToken;
    }

    public boolean getReadOnly() {
        return mReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.mReadOnly = readOnly;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getID() {
        return mID;
    }

    private void makeID() {
        mID = mUrl.substring(mUrl.lastIndexOf("/"));
    }

    private void appendSlashIfNeeded() {
        if (mUrl.lastIndexOf("/") != mUrl.length() - 1) {
            mUrl += "/";
        }
    }

    public Model model(String modelID) {
        return new Model(this, modelID);
    }

    public ECModelListRequest modelList() {
        return new ECModelListRequest(this);
    }

    public ECEntryPostRequest register() {
        return this.model("user").createEntry(new ECEntry());
    }

    public ECEntryRequest user(String id) {
        return this.model("user").entry(id);
    }

    public ECFileURLRequest getFileURL(String id) {
        return new ECFileURLRequest(this, id);
    }

    public ECFileURLRequest getImageURL(String id) {
        return this.getFileURL(id).image();
    }

    public ECFileURLRequest getImageThumbURL(String id) {
        return this.getImageURL(id).crop();
    }

    public ECAssetsRequest assets() {
        return new ECAssetsRequest(this);
    }

    public ECAssetsRequest asset(String id) {
        return (ECAssetsRequest) this.assets().filter(new HashMap<String, String>() {{
            put("assetID", id);
        }});
    }
}

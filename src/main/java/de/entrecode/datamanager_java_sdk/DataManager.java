package de.entrecode.datamanager_java_sdk;

import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.exceptions.ECMalformedDataManagerIDException;
import de.entrecode.datamanager_java_sdk.listener.ECErrorListener;
import de.entrecode.datamanager_java_sdk.listener.ECResponseListener;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.requests.assets.ECAssetRequest;
import de.entrecode.datamanager_java_sdk.requests.assets.ECAssetsPostRequest;
import de.entrecode.datamanager_java_sdk.requests.assets.ECAssetsRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryPostRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryRequest;
import de.entrecode.datamanager_java_sdk.requests.files.ECFileURLRequest;
import de.entrecode.datamanager_java_sdk.requests.models.ECModelListRequest;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * This class connects to a Data Manager either via ID or URL. You can use the DataManager object for requesting, creating,
 * modifying, and deleting asset and entry resources of the Data Manager connected.
 * <br><br>
 * For Examples see the methods of this class.
 */
public class DataManager {
    private static final String shortIDRegEx = "^[0-9a-fA-F]{8}$";
    private static final String baseAPIUrl = "https://datamanager.entrecode.de/api/";

    private String mUrl;
    private String mAssetUrl;
    private String mID;
    private UUID mAccessToken;
    private boolean mReadOnly = false;

    /**
     * Creates a DataManager object connecting with a URL and a previously acquired access token.
     *
     * @param url         Url of the Data Manager to which you want to connect. Example: https://datamanager.entrecode.de/api/beef1234
     * @param accessToken Previously acquired access token of public user.
     */
    public DataManager(URL url, UUID accessToken) {
        mUrl = url.toString();
        mAccessToken = accessToken;

        appendSlashIfNeeded();
        makeID();
        mAssetUrl = mUrl.replace("/api/" + mID + "/", "/asset/" + mID);
    }

    /**
     * Creates a DataManager object in read only mode connecting with a URL.
     *
     * @param url      Url of the Data Manager to which you want to connect. Example: https://datamanager.entrecode.de/api/beef1234
     * @param readOnly Flag indicating read only mode. Only true is an acceptable input.
     */
    public DataManager(URL url, boolean readOnly) {
        if (!readOnly) {
            throw new IllegalArgumentException("Read Only must be true.");
        }
        mUrl = url.toString();
        mReadOnly = readOnly;

        appendSlashIfNeeded();
        makeID();
        mAssetUrl = mUrl.replace("/api/" + mID + "/", "/asset/" + mID);
    }

    /**
     * Creates a DataManager object connecting with the short id of the Data Manager and a previously acquired access token.
     *
     * @param id          The short id of the Data Manager to connect to. Will result in a connection to https://datamanager.entrecode.de/api/'id'
     * @param accessToken Previously acquired access token of public user.
     * @throws ECMalformedDataManagerIDException Thrown when short id of Data Manager is malformed.
     */
    public DataManager(String id, UUID accessToken) throws ECMalformedDataManagerIDException {
        if (!id.matches(shortIDRegEx)) {
            throw new ECMalformedDataManagerIDException("Malformed Data Manager ID given.");
        }

        mID = id;
        mUrl = baseAPIUrl + id.toLowerCase() + "/";
        mAssetUrl = mUrl.replace("/api/" + mID + "/", "/asset/" + mID);
        mAccessToken = accessToken;
    }

    /**
     * Creates a DataManager object in read only mode connecting with the short id of the Data Manager.
     *
     * @param id       The short id of the Data Manager to connect to. Will result in a connection to https://datamanager.entrecode.de/api/'id'
     * @param readOnly Flag indicating read only mode. Only true is an acceptable input.
     * @throws ECMalformedDataManagerIDException Thrown when short id of Data Manager is malformed.
     */
    public DataManager(String id, boolean readOnly) throws ECMalformedDataManagerIDException {
        if (!readOnly) {
            throw new IllegalArgumentException("Read Only must be true.");
        }
        if (!id.matches(shortIDRegEx)) {
            throw new ECMalformedDataManagerIDException("Malformed Data Manager ID given.");
        }

        mID = id;
        mUrl = baseAPIUrl + id.toLowerCase() + "/";
        mAssetUrl = mUrl.replace("/api/" + mID + "/", "/asset/" + mID);
        mReadOnly = readOnly;
    }

    /**
     * Asynchronously creates a DataManager object connecting with a URL and will generate a new public user. Resulting user will be passed to responseListener.
     *
     * @param url              Url of the Data Manager to which you want to connect. Example: https://datamanager.entrecode.de/api/beef1234
     * @param responseListener Response listener for the result.
     * @param errorListener    Error listener for occurred errors.
     */
    public static void create(URL url, ECResponseListener<DataManager> responseListener, ECErrorListener errorListener) {
        DataManager dm = new DataManager(url, true);

        dm.register().onResponse(user -> {
            dm.setToken(UUID.fromString(String.valueOf(user.get("temporaryToken"))));
            dm.setReadOnly(false);
            responseListener.onResponse(dm);
        }).onError(errorListener).go();
    }

    /**
     * Asynchronously creates a DataManager object connecting with the short id of the Data Manager and will generate a new public user. Resulting user will be passed to responseListener.
     *
     * @param id               The short id of the Data Manager to connect to. Will result in a connection to https://datamanager.entrecode.de/api/'id'
     * @param responseListener Response listener for the result.
     * @param errorListener    Error listener for occurred errors.
     * @throws ECMalformedDataManagerIDException Thrown when short id of Data Manager is malformed.
     */
    public static void create(String id, ECResponseListener<DataManager> responseListener, ECErrorListener errorListener) throws ECMalformedDataManagerIDException {
        DataManager dm = new DataManager(id, true);

        dm.register().onResponse(user -> {
            dm.setToken(UUID.fromString(String.valueOf(user.get("temporaryToken"))));
            dm.setReadOnly(false);
            responseListener.onResponse(dm);
        }).onError(errorListener).go();
    }

    /**
     * Get the associated token of this DataManager object.
     *
     * @return Associated access token.
     */
    public UUID getToken() {
        return mAccessToken;
    }

    /**
     * Set the associated token for this DataManager object.
     *
     * @param accessToken Access token which should be associated.
     */
    public void setToken(UUID accessToken) {
        this.mAccessToken = accessToken;
    }

    /**
     * Get the read only state of this DataManager object.
     *
     * @return The read only state.
     */
    public boolean getReadOnly() {
        return mReadOnly;
    }

    /**
     * Set the read only state of this DataManager object. Use with caution. Can result in unintended behavior when no access token is assigned.
     *
     * @param readOnly Read only state to set for this DataManager object.
     */
    public void setReadOnly(boolean readOnly) {
        this.mReadOnly = readOnly;
    }

    /**
     * Get the url of this DataManager object.
     *
     * @return Url of the Data Manager this DataManager object is connected to.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Get the asset url of this DataManager object.
     *
     * @return Url of the asset API of the Data Manager this DataManager object is connected to.
     */
    public String getAssetUrl() {
        return mAssetUrl;
    }

    /**
     * Get the short id of the Data Manager this Data
     *
     * @return The short id.
     */
    public String getID() {
        return mID;
    }

    /**
     * Extracts the short id of the connected Data Manager when this DataManager object is created with an URL.
     */
    private void makeID() {
        mID = mUrl.substring(0, mUrl.length() - 1); // remove last char (always "/")
        mID = mID.substring(mID.lastIndexOf("/") + 1); // get substring from last "/" (is shortID)
    }

    /**
     * Appends a slash '/' to the URL this DataManager objects connects to if the object is created without.
     */
    private void appendSlashIfNeeded() {
        if (mUrl.lastIndexOf("/") != mUrl.length() - 1) {
            mUrl += "/";
        }
    }

    /**
     * Method for working with a specific model. For example 'user' or 'to-do-item'
     *
     * @param modelID Identifier of the model you want to work with.
     * @return Object for working with models.
     */
    public Model model(String modelID) {
        return new Model(this, modelID);
    }

    /**
     * Asynchronous request for the list of models of the connected DataManager.
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.modelList().onResult(modelList ->{
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify();
     *     }).go();
     * }</pre>
     *
     * @return ECModelListRequest
     */
    public ECModelListRequest modelList() {
        return new ECModelListRequest(this);
    }

    /**
     * Convenience method for asynchronously creating a public user entry. Shorthand for {@code DataManager.model("user").createEntry(new ECEntry())…}
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.register().onResult(user -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify();
     *     }).go();
     * }</pre>
     *
     * @return ECEntryPostRequest
     */
    public ECEntryPostRequest register() {
        return this.model("user").createEntry(new ECEntry());
    }

    /**
     * Convenience method for asynchronously getting a public user entry. Shorthand for {@code DataManager.model("user").entry(id)…}
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.user("00000001").onResponse(user -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify();
     *     }).go();
     * }</pre>
     *
     * @param id User or rather entry id of the user to request.
     * @return ECEntryRequest
     */
    public ECEntryRequest user(String id) {
        return this.model("user").entry(id);
    }

    /**
     * Asset helper method for asynchronously getting a content-negotiated URL for a given asset.
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.getFileURL("").onResponse( url -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify();
     *     }).go();
     * }</pre>
     *
     * @param id The id of the asset.
     * @return Url String for the requested file.
     */
    public ECFileURLRequest getFileURL(String id) {
        return new ECFileURLRequest(this, id);
    }

    /**
     * Asset helper method for asynchronously getting a content-negotiated URL for a given image asset. Supporting size filter with {@code …size(Integer)…}.
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.getImageURL("").size(400).onResponse( url -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify();
     *     }).go();
     * }</pre>
     *
     * @param id The id of the asset.
     * @return Url String for the requested file.
     */
    public ECFileURLRequest getImageURL(String id) {
        return this.getFileURL(id).image();
    }

    /**
     * Asset helper method for asynchronously getting a content-negotiated thumbnail URL for a given image asset. Supporting size filter with {@code …size(Integer)…}.
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.getImageThumbURL("3e76550c-f4e3-402b-aca4-203dfc661b9f").size(50).onResponse( url -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify();
     *     }).go();
     * }</pre>
     *
     * @param id The id of the asset.
     * @return Url String for the requested file.
     */
    public ECFileURLRequest getImageThumbURL(String id) {
        return this.getImageURL(id).crop();
    }

    /**
     * Requests a list of all available assets for the connected Data Manager.
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.assets().onResponse(assetList -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @return ECAssetRequest
     */
    public ECAssetsRequest assets() {
        return new ECAssetsRequest(this);
    }

    /**
     * Requests a single asset of the connected Data Manager.
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.asset("3e76550c-f4e3-402b-aca4-203dfc661b9f").onResponse(asset -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @param id The id of the asset to request.
     * @return ECAssetRequest
     */
    public ECAssetRequest asset(String id) {
        return new ECAssetRequest(this, id);
    }

    /**
     * Method for creating a new asset in the connected Data Manager.
     * <br><br>
     * Example:
     * <pre>{@code
     *     File file = new File…
     *     …
     *     dataManager.createAsset(file).onResponse(asset -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @param file The file from which the asset should be created.
     * @return ECAssetsPostRequest
     */
    public ECAssetsPostRequest createAsset(File file) {
        if (getReadOnly()) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return new ECAssetsPostRequest(this, file);
    }

    /**
     * Method for creating multiple new assets in the connected Data Manager.
     * <br><br>
     * Example:
     * <pre>{@code
     *     File file = new File…
     *     File file2 = new File…
     *     …
     *    {@code dataManager.createAsset(new ArrayList<File>}(){{
     *        add(file);
     *        add(file2);
     *     }}).onResponse(assets -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @param files The file from which the asset should be created.
     * @return ECAssetsPostRequest
     */
    public ECAssetsPostRequest createAsset(List<File> files) {
        if (getReadOnly()) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return new ECAssetsPostRequest(this, files);
    }
}

package de.entrecode.datamanager_java_sdk;

import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntriesRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryPostRequest;
import de.entrecode.datamanager_java_sdk.requests.entries.ECEntryRequest;
import de.entrecode.datamanager_java_sdk.requests.models.ECSchemaRequest;

/**
 * Class providing access to all requests for an individual model of a Data Manager. Best created with {@code dataManager.model(modelId);}.
 */
public class Model {
    private DataManager mDataManager;
    private String mModelID;

    private String titleField;
    private String hexColor;

    /**
     * Creates an Model object for a DataManager with the given model id. Can then be used to access all requests for the model.
     *
     * @param dataManager The DataManager object connected to the Data Manager the desired model is part of.
     * @param modelID     The id of the desired model.
     */
    public Model(DataManager dataManager, String modelID) {
        mDataManager = dataManager;
        mModelID = modelID;
        titleField = "id";
        hexColor = "#ffffff";
    }

    /**
     * Creates an Model object for a DataManager with the given model id. Can then be used to access all requests for the model.
     *
     * @param dataManager The DataManager object connected to the Data Manager the desired model is part of.
     * @param modelID     The id of the desired model.
     * @param title       The titleField of the model.
     * @param color       The hexColor of the mode.
     */
    public Model(DataManager dataManager, String modelID, String title, String color) {
        mDataManager = dataManager;
        mModelID = modelID;
        titleField = title;
        hexColor = color;
    }

    /**
     * Requests all entries for this model.
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.model("to-do-item").entries().onResponse(entries -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @return ECEntriesRequest.
     */
    public ECEntriesRequest entries() {
        return new ECEntriesRequest(mDataManager, mModelID);
    }

    /**
     * Requests a single entry of this model.
     * <br><br>
     * Example:
     * <pre>{@code
     *     dataManager.model("to-do-item").entry("00000001").onResponse(entry -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @param id The id of the entry to request.
     * @return ECEntryRequest.
     */
    public ECEntryRequest entry(String id) {
        return new ECEntryRequest(mDataManager, mModelID, id);
    }

    /**
     * Request for creating a new entry for this model.
     * <br><br>
     * Example:
     * <pre>{@code
     *     ECEntry entry = new ECEntry();
     *     …
     *     dataManager.model("to-do-item").createEntry(entry).onResponse(createdEntry -> {
     *         // do something
     *     }).onError(error -> {
     *         System.out.println(error.stringify());
     *     }).go();
     * }</pre>
     *
     * @param entry The entry which will be created.
     * @return ECEntryPostRequest
     */
    public ECEntryPostRequest createEntry(ECEntry entry) {
        if (mDataManager.getReadOnly() && !mModelID.equals("user")) {
            throw new ECDataMangerInReadOnlyModeException();
        }

        return (ECEntryPostRequest) new ECEntryPostRequest(this).body(entry.toBody());
    }

    /**
     * Request the JSON Schema for this model.
     *
     * @return ECSchemaRequest
     */
    public ECSchemaRequest getSchema() {
        return new ECSchemaRequest(mDataManager, mModelID);
    }

    /**
     * Get the associated DataManager
     *
     * @return The associated DataManager
     */
    public DataManager getDataManager() {
        return mDataManager;
    }

    /**
     * Get the id of the desired model.
     *
     * @return The id of the desired model.
     */
    public String getModelID() {
        return mModelID;
    }

    /**
     * Get the titleField of the desired model.
     *
     * @return The titleField of the desired model.
     */
    public String getTitleField() {
        return titleField;
    }

    /**
     * Get the hexColor of the desired model.
     *
     * @return The hexColor of the desired model.
     */
    public String getHexColor() {
        return hexColor;
    }
}
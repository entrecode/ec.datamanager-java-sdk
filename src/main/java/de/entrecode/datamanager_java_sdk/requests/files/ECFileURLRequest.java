package de.entrecode.datamanager_java_sdk.requests.files;

import com.google.gson.JsonObject;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.Reader;

/**
 * Request for all three asset helper methods ({@link DataManager#getFileURL(String)}, {@link DataManager#getImageURL(String)},
 * {@link DataManager#getImageThumbURL(String)}).
 */
public class ECFileURLRequest extends ECRequest<String> {
    private final DataManager mDataManager;
    private final String mAssetID;

    private boolean isImage = false;
    private boolean isThumbnail = false;
    private String mLocale;
    private int mSize = -1;

    /**
     * Default constructor for file url requests with the assigned DataManager and the id of the asset.
     *
     * @param dataManager The assigned DataManager connected to the Data Manager containing the asset.
     * @param id          The id of the asset.
     */
    public ECFileURLRequest(DataManager dataManager, String id) {
        mDataManager = dataManager;
        mAssetID = id;
    }

    /**
     * Enables image file helper requests.
     *
     * @return ECFileURLRequest
     */
    public ECFileURLRequest image() {
        isImage = true;
        return this;
    }

    /**
     * Enables thumbnail file helper requests.
     *
     * @return ECFileURLRequest
     */
    public ECFileURLRequest crop() {
        isThumbnail = true;
        return this;
    }

    /**
     * Set the locale to request.
     *
     * @param locale The locale to request.
     * @return ECFileURLRequest
     */
    public ECFileURLRequest locale(String locale) {
        mLocale = locale;
        return this;
    }

    /**
     * Set the image size.
     *
     * @param size The size to request.
     * @return ECFileURLRequest
     */
    public ECFileURLRequest size(int size) {
        mSize = size;
        return this;
    }

    @Override
    public Request build() {
        if (isImage && isThumbnail && mSize == -1) {
            throw new IllegalArgumentException("Must specify size for thumbnail request");
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.mDataManager.getUrl()
                .replace("api/" + this.mDataManager.getID() + "/", "files/" + mAssetID) + "/url").newBuilder();

        // add nocrop query param for thumbnails
        if (isImage) {
            if (isThumbnail) {
                urlBuilder.addQueryParameter("thumb", String.valueOf(isThumbnail));
            }
            if (mSize >= 0) {
                if (isThumbnail) {
                    if (mSize <= 50) {
                        mSize = 50;
                    } else if (mSize <= 100) {
                        mSize = 100;
                    } else if (mSize <= 200) {
                        mSize = 200;
                    } else {
                        mSize = 400;
                    }
                }
                urlBuilder.addQueryParameter("size", String.valueOf(mSize));
            }
        }

        Request.Builder rb = new Request.Builder().url(urlBuilder.build()).method(this.mMethod, null);
        if (mLocale != null) {
            rb.addHeader("Accept-Language", mLocale);
        }
        return rb.build();
    }

    @Override
    public String buildResponse(Reader response) {
        JsonObject url = new ECResourceParser<JsonObject>(JsonObject.class).fromJson(response);
        return url.get("url").getAsString();
    }
}

package de.entrecode.datamanager_java_sdk.requests.files;

import com.google.gson.JsonObject;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECRequest;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 11.06.15.
 */
public class ECFileURLRequest extends ECRequest<String> {
    private final DataManager mDataManager;
    private final String mAssetID;

    private boolean isImage = false;
    private boolean isThumbnail = false;
    private String mLocale;
    private int mSize = -1;

    public ECFileURLRequest(DataManager dataManager, String id) {
        mDataManager = dataManager;
        mAssetID = id;
    }

    public ECFileURLRequest image() {
        isImage = true;
        return this;
    }

    public ECFileURLRequest crop() {
        isThumbnail = true;
        return this;
    }

    public ECFileURLRequest locale(String locale) {
        mLocale = locale;
        return this;
    }

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
                .replace("datamanager", "f")
                .replace("api/" + this.mDataManager.getID() + "/", mAssetID) + "/url").newBuilder();

        // add nocrop query param for thumbnails
        if (isImage) {
            urlBuilder.addQueryParameter("nocrop", String.valueOf(!isThumbnail));
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
    public String buildResponse(Reader response) throws IOException {
        JsonObject url = new ECResourceParser<JsonObject>(JsonObject.class).fromJson(response);
        return url.get("url").getAsString();
    }
}

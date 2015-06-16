package de.entrecode.datamanager_java_sdk.requests.files;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import de.entrecode.datamanager_java_sdk.DataManager;
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.mDataManager.getUrl()
                .replace("datamanager", "f")
                .replace("api/" + this.mDataManager.getID() + "/", mAssetID) + "/url").newBuilder();

        // add nocrop query param for thumbnails
        if (isImage) {
            urlBuilder.addQueryParameter("nocrop", String.valueOf(!isThumbnail));
            if (mSize >= 0) {
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
        StringBuilder builder = new StringBuilder();
        int charsRead = -1;
        char[] chars = new char[100];
        do {
            charsRead = response.read(chars, 0, chars.length);
            //if we have valid chars, append them to end of string.
            if (charsRead > 0)
                builder.append(chars, 0, charsRead);
        } while (charsRead > 0);
        return builder.toString();
    }
}

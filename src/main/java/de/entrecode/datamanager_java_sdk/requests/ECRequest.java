package de.entrecode.datamanager_java_sdk.requests;

import com.squareup.okhttp.*;
import de.entrecode.datamanager_java_sdk.listener.ECErrorListener;
import de.entrecode.datamanager_java_sdk.listener.ECResponseListener;
import de.entrecode.datamanager_java_sdk.model.ECError;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public abstract class ECRequest<T> {
    private final OkHttpClient mClient = new OkHttpClient();

    protected String mMethod = "GET";
    private HashMap<String, String> mFilter;
    private int mSize = 10;
    private int mPage = 1;

    private ECResponseListener<T> mResponseListener;
    private ECErrorListener mErrorListener;

    abstract public Request build();

    abstract public T buildResponse(Reader response) throws IOException;

    public ECRequest filter(HashMap<String, String> filter) {
        mFilter = filter;
        return this;
    }

    public ECRequest pageSize(int size) {
        mSize = size;
        return this;
    }

    public ECRequest page(int page) {
        mPage = page;
        return this;
    }

    public ECRequest onResponse(ECResponseListener<T> responseListener) {
        mResponseListener = responseListener;
        return this;
    }

    public ECRequest onError(ECErrorListener errorListener) {
        mErrorListener = errorListener;
        return this;
    }

    public void go() {
        Request request = this.build();
        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (mErrorListener != null) {
                    mErrorListener.onError(new ECError(e.getMessage()));
                } else {
                    System.out.println(e.getCause());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException { // does NOT mean successful. check for status code
                if (!response.isSuccessful()) { // not in 200…300 range
                    if (mErrorListener != null) {
                        try {
                            ResponseBody rb = response.body();
                            mErrorListener.onError(new ECResourceParser<ECError>(ECError.class).fromJson(response.body().charStream()));
                        } catch (Exception e) {
                            ECError err = new ECError("Unmatched ECError.");
                            err.setCode(response.code());
                            mErrorListener.onError(err);
                        }
                    }
                } else {
                    if (mResponseListener != null) {
                        mResponseListener.onResponse(buildResponse(response.body().charStream()));
                    }
                }
            }
        });
    }

    public HttpUrl.Builder addFilterToUrlBuilder(HttpUrl.Builder builder) {
        builder.addQueryParameter("size", String.valueOf(mSize));
        builder.addQueryParameter("page", String.valueOf(mPage));

        if (mFilter != null) {
            for (Map.Entry elem : mFilter.entrySet()) {
                builder.addQueryParameter(String.valueOf(elem.getKey()), (String) elem.getValue());
            }
        }

        return builder;
    }
}

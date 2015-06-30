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
 * Abstract class for all requests made by ec.datamanager-java-sdk.
 */
public abstract class ECRequest<T> {
    private final OkHttpClient mClient = new OkHttpClient();

    protected String mMethod = "GET";
    private HashMap<String, String> mFilter;
    private int mSize = 10;
    private int mPage = 1;

    private ECResponseListener<T> mResponseListener;
    private ECErrorListener mErrorListener;

    /**
     * This method will build the {@link Request} which will be executed.
     *
     * @return The request to execute.
     */
    abstract public Request build();

    /**
     * This method will build the desired response when the result of the request is processed.
     *
     * @param response The response of the request.
     * @return The object which should be returned to the caller.
     */
    abstract public T buildResponse(Reader response);

    /**
     * For applying filter to the request.
     * <br><br>
     * Example:
     * <pre>{@code
     *      new HashMap<String, String>(){{
     *          put("propertyA~", "LikeThat");
     *          put("propertyB", "ExactlyThat");
     *          put("propertyCFrom", "FromThat");
     *          put("propertyCTo", "ToThat");
     *      }}
     * }</pre>
     *
     * @param filter HashMap of filter.
     * @return ECRequest.
     */
    public ECRequest filter(HashMap<String, String> filter) {
        mFilter = filter;
        return this;
    }

    /**
     * For setting the size of embedded elements. Applicable for lists. A size of 0 will result in all available entries (starting with Data Manager version 0.3.0).
     *
     * @param size The desired size.
     * @return ECRequest.
     */
    public ECRequest pageSize(int size) {
        mSize = size;
        return this;
    }

    /**
     * The page number for lists.
     *
     * @param page The desired page.
     * @return ECRequest.
     */
    public ECRequest page(int page) {
        mPage = page;
        return this;
    }

    /**
     * Sets the {@link ECResponseListener} for this request.
     *
     * @param responseListener The response listener.
     * @return ECRequest.
     */
    public ECRequest onResponse(ECResponseListener<T> responseListener) {
        mResponseListener = responseListener;
        return this;
    }

    /**
     * Sets the {@link ECErrorListener} for this request.
     *
     * @param errorListener The error listener.
     * @return ECRequest.
     */
    public ECRequest onError(ECErrorListener errorListener) {
        mErrorListener = errorListener;
        return this;
    }

    /**
     * Asynchronously executes the request. It will first call {@link #build()} for obtaining the {@link Request}, then
     * it will execute it. The response will either be parsed as {@link ECError} and passed to the {@link ECErrorListener}
     * or it will be parsed as the expected object and passed to {@link ECResponseListener}.
     */
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
                            ECError err = new ECResourceParser<ECError>(ECError.class).fromJson(response.body().charStream());
                            if (err == null) {
                                err = new ECError("Unmatched ECError.");
                                err.setStatus(response.code());
                            }
                            mErrorListener.onError(err);
                        } catch (Exception e) {
                            ECError err = new ECError("Unmatched ECError.");
                            err.setStatus(response.code());
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

    /**
     * Method which applies the given filter, size, and page parameter to the request. It expects a {@link com.squareup.okhttp.HttpUrl.Builder},
     * applies the filter etc., and returns it again.
     *
     * @param builder The builder to which the filter etc. should be applied.
     * @return The builder.
     */
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

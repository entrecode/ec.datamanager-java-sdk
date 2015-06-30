package de.entrecode.datamanager_java_sdk.requests.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.model.ECAsset;
import de.entrecode.datamanager_java_sdk.model.ECResourceParser;
import de.entrecode.datamanager_java_sdk.requests.ECPostRequest;

import java.io.File;
import java.io.Reader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Request for creation of asset(s).
 */
public class ECAssetsPostRequest extends ECPostRequest<List<ECAsset>> {
    private final DataManager mDataManager;
    private List<File> mFiles;

    /**
     * Constructor for creating a multiple assets in the provided Data Manager.
     *
     * @param dataManager The DataManager connected to the Data Manager in which the assets should be created.
     * @param files       The list of files with what the assets should be created.
     */
    public ECAssetsPostRequest(DataManager dataManager, List<File> files) {
        super(dataManager.getToken().toString());
        mDataManager = dataManager;
        mFiles = files;
    }

    /**
     * Constructor for creating a single asset in the provided Data Manager.
     *
     * @param dataManager The DataManager connected to the Data Manager in which the asset should be created.
     * @param file        The file with what the asset should be created.
     */
    public ECAssetsPostRequest(DataManager dataManager, File file) {
        super(dataManager.getToken().toString());
        mDataManager = dataManager;
        mFiles = new ArrayList<File>() {{
            add(file);
        }};
    }

    @Override
    public Request build() {
        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);

        for (File f : mFiles) {
            try {
                String mediaType = URLConnection.guessContentTypeFromName(f.getName());
                //builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\""), RequestBody.create(MediaType.parse(mediaType), f));
                //builder.addPart(RequestBody.create(MediaType.parse(mediaType), f));
                builder.addFormDataPart("file", f.getName(), RequestBody.create(MediaType.parse(mediaType), f));
            } catch (Exception e) {
                // This should never happen…
                System.out.println("Could not add file to multipart form.");
                e.printStackTrace();
            }
        }

        mBody = builder.build();

        return new Request.Builder()
                .header("Authorization", "Bearer " + mAuthHeaderValue)
                .url(mDataManager.getAssetUrl())
                .post(mBody)
                .build();
    }

    @Override
    public List<ECAsset> buildResponse(Reader response) {
        JsonObject res = new ECResourceParser<JsonObject>(JsonObject.class).fromJson(response);
        JsonElement assetsJson = res.get("_links").getAsJsonObject().get("ec:asset");
        JsonArray arr;
        if (assetsJson.isJsonObject()) {
            arr = new JsonArray();
            arr.add(assetsJson.getAsJsonObject());
        } else {
            arr = assetsJson.getAsJsonArray();
        }

        List<ECAsset> out = new ArrayList<>();
        for (JsonElement elem : arr) {
            out.add(new ECAsset(
                    mDataManager,
                    elem.getAsJsonObject().get("href").getAsString(),
                    elem.getAsJsonObject().get("title").getAsString()));
        }

        return out;
    }
}

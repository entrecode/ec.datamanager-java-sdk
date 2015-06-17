package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 08.06.15.
 */
public class ECResourceParser<T> {
    private Gson gson;
    private Class<T> mTypedClass;
    private String entryOrAssetList;

    public ECResourceParser(Class typedClass) {
        mTypedClass = typedClass;
        entryOrAssetList = "entry";
        gson = getGson();
    }

    public ECResourceParser(Class typedClass, String entryOrAssetList) {
        mTypedClass = typedClass;
        this.entryOrAssetList = entryOrAssetList;
        gson = getGson();
    }

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ECEntry.class, new ECEntry.ECEntryJsonDeserializer());
        builder.registerTypeAdapter(ECEntry.class, new ECEntry.ECEntryJsonSerializer());
        builder.registerTypeAdapter(ECAsset.class, new ECAsset.ECAssetJsonDeserializer());
        if (entryOrAssetList.equals("entry")) {
            Class clazz = new TypeToken<ECList<ECEntry>>() {
            }.getRawType();
            builder.registerTypeAdapter(clazz, new ECList.ECListJsonDeserializer<ECList<ECEntry>>(ECEntry.class));
        } else if (entryOrAssetList.equals("asset")) {
            Class clazz2 = new TypeToken<ECList<ECAsset>>() {
            }.getRawType();
            builder.registerTypeAdapter(clazz2, new ECList.ECListJsonDeserializer<ECList<ECAsset>>(ECAsset.class));
        }
        return builder.create();
    }

    public String toJson(T obj) {
        return gson.toJson(obj, mTypedClass);
    }

    public T fromJson(String json) {
        return gson.fromJson(json, mTypedClass);
    }

    public T fromJson(Reader reader) {
        return gson.fromJson(reader, mTypedClass);
    }

    public T fromJson(JsonElement json) {
        return gson.fromJson(json, mTypedClass);
    }
}

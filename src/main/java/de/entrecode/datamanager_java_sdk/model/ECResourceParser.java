package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;

/**
 * Convenience class for serializing and deserializing of JsonElements with GSON. Adds custom {@link com.google.gson.TypeAdapter}
 * for ec.resources ({@link ECEntry}, {@link ECAsset}, and {@link ECList}).
 */
public class ECResourceParser<T> {
    private Gson gson;
    private Class<T> mTypedClass;
    private String listType;

    /**
     * Constructor for creating a resource parser with support of {@link ECList}s containing {@link ECEntry} elements.
     *
     * @param typedClass The class of the object you want to parse.
     */
    public ECResourceParser(Class typedClass) {
        mTypedClass = typedClass;
        listType = "entry";
        gson = getGson();
    }

    /**
     * Constructor for creating a resource parser with support of {@link ECList}s containing either {@link ECEntry} or {@link ECAsset} elements.
     *
     * @param typedClass The class of the object you want to parse.
     * @param listType   Either "entry" or "asset" for selecting which {@link ECList}s to support. All other values will result in no support.
     */
    public ECResourceParser(Class typedClass, String listType) {
        mTypedClass = typedClass;
        this.listType = listType;
        gson = getGson();
    }

    /**
     * Creates {@link Gson} object containing all {@link com.google.gson.TypeAdapter}.
     *
     * @return The gson object with registered adapter.
     */
    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ECEntry.class, new ECEntry.ECEntryJsonDeserializer());
        builder.registerTypeAdapter(ECEntry.class, new ECEntry.ECEntryJsonSerializer());
        builder.registerTypeAdapter(ECAsset.class, new ECAsset.ECAssetJsonDeserializer());
        builder.registerTypeAdapter(ECTag.class, new ECTag.ECTagJsonDeserializer());
        builder.registerTypeAdapter(ECTag.class, new ECTag.ECTagJsonSerializer());
        if (listType.equals("entry")) {
            Class clazz = new TypeToken<ECList<ECEntry>>() {
            }.getRawType();
            builder.registerTypeAdapter(clazz, new ECList.ECListJsonDeserializer<ECList<ECEntry>>(ECEntry.class));
        } else if (listType.equals("asset")) {
            Class clazz2 = new TypeToken<ECList<ECAsset>>() {
            }.getRawType();
            builder.registerTypeAdapter(clazz2, new ECList.ECListJsonDeserializer<ECList<ECAsset>>(ECAsset.class));
        } else if (listType.equals("tag")) {
            Class clazz2 = new TypeToken<ECList<ECTag>>() {
            }.getRawType();
            builder.registerTypeAdapter(clazz2, new ECList.ECListJsonDeserializer<ECList<ECTag>>(ECTag.class));
        }
        return builder.create();
    }

    /**
     * Serializes the given object of the selected type to String.
     *
     * @param obj The object to serialize.
     * @return The String representation of the object.
     */
    public String toJson(T obj) {
        return gson.toJson(obj, mTypedClass);
    }

    /**
     * Deserializes the given {@link Reader} to a object of the selected Type.
     *
     * @param reader The reader to deserialize.
     * @return The deserialized object.
     */
    public T fromJson(Reader reader) {
        return gson.fromJson(reader, mTypedClass);
    }

    /**
     * Deserializes the given {@link JsonElement} to a object of the selected Type.
     *
     * @param json The reader to deserialize.
     * @return The deserialized object.
     */

    public T fromJson(JsonElement json) {
        return gson.fromJson(json, mTypedClass);
    }
}

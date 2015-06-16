package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.JsonElement;

/**
 * Created by simon, entrecode GmbH, Stuttgart (Germany) on 03.06.15.
 */
public class ECError {
    private int status = 500;
    private int code = 0;
    private String title;
    private String type;
    private String detail;
    private String verbose;

    private JsonElement _embedded;
    private JsonElement _links;

    /**
     * Constructor for generic Errors. Mainly I/O related stuff.
     *
     * @param message Message describing the error.
     */
    public ECError(String message) {
        title = message;
        type = "https://entrecode.de/doc/errors/0000";
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public String getVerbose() {
        return verbose;
    }

    public JsonElement getEmbedded() {
        return _embedded;
    }

    public JsonElement getLinks() {
        return _links;
    }

    public String stringify() {
        StringBuilder b = new StringBuilder();
        b.append(status + ", " + code + ", ");
        b.append(title);
        if (detail != null && detail.length() > 0) {
            b.append(" - " + detail);
        }
        if (verbose != null && verbose.length() > 0) {
            b.append(" - " + verbose);
        }

        return b.toString();
    }
}

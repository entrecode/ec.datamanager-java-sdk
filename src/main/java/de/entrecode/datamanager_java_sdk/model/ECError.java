package de.entrecode.datamanager_java_sdk.model;

import com.google.gson.JsonElement;

/**
 * Class representing ECErrors which can occur during API usage.
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

    /**
     * Get http status of the error.
     *
     * @return The http status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set http status of the error.
     *
     * @param status The http status.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Get code of the error.
     *
     * @return The code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Get title of the error. Always in english.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get type of the error.
     *
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get detail message of the error.
     *
     * @return The detail message.
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Get verbose info of the error.
     *
     * @return The verbose info.
     */
    public String getVerbose() {
        return verbose;
    }

    /**
     * Get embedded errors. If multiple errors occur the most important is represented by the object itself. All other errors are embedded.
     *
     * @return JsonElement containing one or many embedded errors. Can be null.
     */
    public JsonElement getEmbedded() {
        return _embedded;
    }

    /**
     * Get links element holding HAL-links
     *
     * @return JsonElement with HAL-links
     */
    public JsonElement getLinks() {
        return _links;
    }

    /**
     * Creates String representation of this error for convenient logging.
     *
     * @return The error represented as String.
     */
    public String stringify() {
        StringBuilder b = new StringBuilder();
        b.append(getStatus() + ", " + getCode() + ", ");
        b.append(getTitle());
        if (getDetail() != null && getDetail().length() > 0) {
            b.append(" - " + getDetail());
        }
        if (getVerbose() != null && getVerbose().length() > 0) {
            b.append(" - " + getVerbose());
        }
        if (getType() != null & getType().length() > 0) {
            b.append(" - " + getType());
        }

        return b.toString();
    }
}

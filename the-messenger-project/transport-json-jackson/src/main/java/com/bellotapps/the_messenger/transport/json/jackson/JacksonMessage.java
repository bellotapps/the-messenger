package com.bellotapps.the_messenger.transport.json.jackson;

import com.bellotapps.the_messenger.commons.AbstractMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;
import java.util.Map;

/**
 * An extension of an {@link AbstractMessage} decorated to be serialized/deserialized to/into JSON
 * using Jackson.
 */
public class JacksonMessage extends AbstractMessage {

    /**
     * The JSON field for the id.
     */
    private static final String ID = "id";
    /**
     * The JSON field for the sender.
     */
    private static final String SENDER = "sn";
    /**
     * The JSON field for the timestamp.
     */
    private static final String TIMESTAMP = "ts";
    /**
     * The JSON field for the headers.
     */
    private static final String HEADERS = "hs";
    /**
     * The JSON field for the payload.
     */
    private static final String PAYLOAD = "pl";


    /**
     * Constructor.
     *
     * @param id        The message's id.
     * @param sender    An identification of the sender. This allows the recipient to know who has sent the message.
     * @param timestamp The timestamp of the message.
     * @param headers   The message headers.
     * @param payload   The message payload.
     * @throws IllegalArgumentException If any argument is invalid.
     */
    @JsonCreator
    public JacksonMessage(
            @JsonProperty(value = ID, access = JsonProperty.Access.WRITE_ONLY) final String id,
            @JsonProperty(value = SENDER, access = JsonProperty.Access.WRITE_ONLY) final String sender,
            @JsonProperty(value = TIMESTAMP, access = JsonProperty.Access.WRITE_ONLY)
            @JsonDeserialize(using = InstantEpochDeserializer.class) final Instant timestamp,
            @JsonProperty(value = HEADERS, access = JsonProperty.Access.WRITE_ONLY) final Map<String, String> headers,
            @JsonProperty(value = PAYLOAD, access = JsonProperty.Access.WRITE_ONLY) final String payload)
            throws IllegalArgumentException {
        super(id, sender, timestamp, headers, payload);
    }


    @Override
    @JsonProperty(value = ID, access = JsonProperty.Access.READ_ONLY)
    public String getId() {
        return super.getId();
    }

    @Override
    @JsonProperty(value = SENDER, access = JsonProperty.Access.READ_ONLY)
    public String getSender() {
        return super.getSender();
    }

    @Override
    @JsonProperty(value = TIMESTAMP, access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = InstantEpochSerializer.class)
    public Instant getTimestamp() {
        return super.getTimestamp();
    }

    @Override
    @JsonProperty(value = HEADERS, access = JsonProperty.Access.READ_ONLY)
    public Map<String, String> getHeaders() {
        return super.getHeaders();
    }

    @Override
    @JsonProperty(value = PAYLOAD, access = JsonProperty.Access.READ_ONLY)
    public String getPayload() {
        return super.getPayload();
    }
}

package com.bellotapps.the_messenger.json;

import com.bellotapps.the_messenger.commons.payload.PayloadSerializationException;
import com.bellotapps.the_messenger.commons.payload.PayloadSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;

/**
 * A {@link PayloadSerializer} that delegates serialization to a Jackson's {@link ObjectMapper}.
 */
public class JacksonJsonPayloadSerializer<T> implements PayloadSerializer<T> {

    /**
     * The {@link ObjectMapper} to which the serialization is delegated to.
     */
    private final ObjectMapper objectMapper;

    /**
     * The class that was tried to be serialized.
     */
    private final Class<T> classToSerialize;

    /**
     * Constructor.
     *
     * @param objectMapper     The {@link ObjectMapper} to which the serialization is delegated to.
     * @param classToSerialize The class that was tried to be serialized.
     */
    public JacksonJsonPayloadSerializer(final ObjectMapper objectMapper, final Class<T> classToSerialize) {
        Validate.isTrue(objectMapper != null, "The object mapper must not be null");
        Validate.isTrue(classToSerialize != null, "The class to serialize must not be null");
        this.objectMapper = objectMapper;
        this.classToSerialize = classToSerialize;
    }

    @Override
    public String serialize(final T object) throws PayloadSerializationException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new PayloadSerializationException(classToSerialize, e);
        }
    }
}

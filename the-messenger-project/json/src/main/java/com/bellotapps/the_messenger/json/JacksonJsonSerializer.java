package com.bellotapps.the_messenger.json;

import com.bellotapps.the_messenger.commons.data_transfer.SerializationException;
import com.bellotapps.the_messenger.commons.data_transfer.Serializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;

/**
 * A {@link Serializer} that delegates serialization to a Jackson's {@link ObjectMapper}.
 */
public class JacksonJsonSerializer<T> implements Serializer<T> {

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
    public JacksonJsonSerializer(final ObjectMapper objectMapper, final Class<T> classToSerialize) {
        Validate.isTrue(objectMapper != null, "The object mapper must not be null");
        Validate.isTrue(classToSerialize != null, "The class to serialize must not be null");
        this.objectMapper = objectMapper;
        this.classToSerialize = classToSerialize;
    }

    @Override
    public String serialize(final T object) throws SerializationException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new SerializationException(classToSerialize, e);
        }
    }
}

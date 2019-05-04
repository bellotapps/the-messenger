package com.bellotapps.the_messenger.json;

import com.bellotapps.the_messenger.commons.data_transfer.DeserializationException;
import com.bellotapps.the_messenger.commons.data_transfer.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * A {@link Deserializer} that delegates deserialization to a Jackson's {@link ObjectMapper}.
 */
public class JacksonJsonDeserializer<T> implements Deserializer<T> {

    /**
     * The {@link ObjectMapper} to which the deserialization is delegated to.
     */
    private final ObjectMapper objectMapper;

    /**
     * {@link Class} to be instantiated when deserializing.
     */
    private final Class<T> classToInstantiate;

    /**
     * Constructor.
     *
     * @param objectMapper       The {@link ObjectMapper} to which dethe serialization is delegated to.
     * @param classToInstantiate {@link Class} to be instantiated when deserializing.
     */
    public JacksonJsonDeserializer(final ObjectMapper objectMapper, final Class<T> classToInstantiate) {
        this.objectMapper = objectMapper;
        this.classToInstantiate = classToInstantiate;
    }

    @Override
    public T deserialize(final String string) throws DeserializationException {
        try {
            return objectMapper.readValue(string, classToInstantiate);
        } catch (final IOException e) {
            throw new DeserializationException(string, classToInstantiate, e);
        }
    }
}

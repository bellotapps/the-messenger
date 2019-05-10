package com.bellotapps.the_messenger.json;

import com.bellotapps.the_messenger.commons.payload.PayloadDeserializationException;
import com.bellotapps.the_messenger.commons.payload.PayloadDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;

import java.io.IOException;

/**
 * A {@link PayloadDeserializer} that delegates deserialization to a Jackson's {@link ObjectMapper}.
 */
public class JacksonJsonPayloadDeserializer<T> implements PayloadDeserializer<T> {

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
    public JacksonJsonPayloadDeserializer(final ObjectMapper objectMapper, final Class<T> classToInstantiate) {
        Validate.isTrue(objectMapper != null, "The object mapper must not be null");
        Validate.isTrue(classToInstantiate != null, "The class to instantiate must not be null");
        this.objectMapper = objectMapper;
        this.classToInstantiate = classToInstantiate;
    }

    @Override
    public T deserialize(final String string) throws PayloadDeserializationException {
        try {
            return objectMapper.readValue(string, classToInstantiate);
        } catch (final IOException e) {
            throw new PayloadDeserializationException(string, classToInstantiate, e);
        }
    }
}

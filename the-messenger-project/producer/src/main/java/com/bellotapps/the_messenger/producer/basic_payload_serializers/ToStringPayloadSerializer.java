package com.bellotapps.the_messenger.producer.basic_payload_serializers;

import com.bellotapps.the_messenger.commons.payload.PayloadSerializationException;
import com.bellotapps.the_messenger.commons.payload.PayloadSerializer;

import java.util.Objects;

/**
 * A {@link PayloadSerializer} for payload objects of type {@code T}.
 * It uses the {@link Objects#toString(Object)} method to convert the objects into {@link String}s.
 *
 * @param <T> Concrete type of object to be serialized.
 */
public class ToStringPayloadSerializer<T> implements PayloadSerializer<T>, PlainContentTypeHandler {

    @Override
    public String serialize(final T object) throws PayloadSerializationException {
        return Objects.toString(object);
    }
}

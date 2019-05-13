package com.bellotapps.the_messenger.producer.basic_payload_serializers;

import com.bellotapps.the_messenger.commons.payload.PayloadSerializationException;
import com.bellotapps.the_messenger.commons.payload.PayloadSerializer;

/**
 * A {@link PayloadSerializer} for {@link String} payload objects.
 * It just returns the same instance of the {@link String}.
 */
public class PlainPayloadSerializer implements PayloadSerializer<String>, PlainContentTypeHandler {

    @Override
    public String serialize(final String object) throws PayloadSerializationException {
        return object;
    }
}

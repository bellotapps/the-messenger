package com.bellotapps.the_messenger.producer.basic_payload_serializers;

import com.bellotapps.the_messenger.commons.headers.ToStringSerializable;
import com.bellotapps.the_messenger.commons.payload.PayloadSerializationException;
import com.bellotapps.the_messenger.commons.payload.PayloadSerializer;

/**
 * A {@link PayloadSerializer} for {@link ToStringSerializable} payload objects.
 * Uses the {@link ToStringSerializable#serialize()} to serialize the payloads.
 */
public class ToStringSerializablePayloadSerializer
        implements PayloadSerializer<ToStringSerializable>, PlainContentTypeHandler {

    @Override
    public String serialize(final ToStringSerializable object) throws PayloadSerializationException {
        return object.serialize();
    }
}

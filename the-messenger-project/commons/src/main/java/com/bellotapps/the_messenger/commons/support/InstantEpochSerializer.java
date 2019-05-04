package com.bellotapps.the_messenger.commons.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;

/**
 * A {@link StdSerializer} of {@link Instant} that uses the {@link Instant#toEpochMilli()} method
 * to serialize instances of that class.
 */
public class InstantEpochSerializer extends StdSerializer<Instant> {

    /**
     * Constructor.
     */
    protected InstantEpochSerializer() {
        super(Instant.class);
    }

    @Override
    public void serialize(final Instant value, final JsonGenerator generator, final SerializerProvider provider)
            throws IOException {
        generator.writeNumber(value.toEpochMilli());
    }
}

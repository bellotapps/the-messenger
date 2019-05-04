package com.bellotapps.the_messenger.commons.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;

/**
 * A {@link StdDeserializer} of {@link Instant} that uses the {@link Instant#ofEpochMilli(long)} ()} method
 * to deserialize {@code long} numbers into instances of that class.
 */
public class InstantEpochDeserializer extends StdDeserializer<Instant> {

    /**
     * Constructor.
     */
    protected InstantEpochDeserializer() {
        super(Instant.class);
    }

    @Override
    public Instant deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        return Instant.ofEpochMilli(parser.getLongValue());
    }
}

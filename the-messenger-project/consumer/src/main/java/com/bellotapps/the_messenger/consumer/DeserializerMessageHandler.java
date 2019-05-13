package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.headers.DefinedHeader;
import com.bellotapps.the_messenger.commons.payload.PayloadDeserializationException;
import com.bellotapps.the_messenger.commons.payload.PayloadDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * An abstract {@link MessageHandler} that deserializes the {@link Message}'s payload
 * according to a given {@link PayloadDeserializer}, and then performs the overridden action.
 *
 * @param <T> The concrete type into which the payload is deserialized.
 */
public abstract class DeserializerMessageHandler<T> implements MessageHandler {

    /**
     * The {@link Logger}.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeserializerMessageHandler.class);


    /**
     * The {@link PayloadDeserializer} to be used to perform the deserialization phase.
     */
    private final PayloadDeserializer<T> payloadDeserializer;

    /**
     * A {@link MessageHandler} to be invoked in case the deserialization phase fails.
     */
    private final MessageHandler defaultMessageHandler;

    /**
     * Constructor.
     *
     * @param payloadDeserializer The {@link PayloadDeserializer} to be used to perform the deserialization phase.
     * @implNote This constructor sets a the {@link DoNothingMessageHandler} as the
     * {@code defaultMessageHandler}.
     */
    protected DeserializerMessageHandler(final PayloadDeserializer<T> payloadDeserializer) {
        this(payloadDeserializer, DoNothingMessageHandler.getInstance());
    }

    /**
     * Constructor.
     *
     * @param payloadDeserializer   The {@link PayloadDeserializer}
     *                              to be used to perform the deserialization phase.
     * @param defaultMessageHandler A {@link MessageHandler}
     *                              to be invoked in case the deserialization phase fails.
     */
    protected DeserializerMessageHandler(
            final PayloadDeserializer<T> payloadDeserializer,
            final MessageHandler defaultMessageHandler) {
        this.payloadDeserializer = payloadDeserializer;
        this.defaultMessageHandler = defaultMessageHandler;
    }

    @Override
    public void handle(final Message message) {
        deserialize(message).ifPresent(payload -> andThen(payload, message));
    }

    /**
     * Deserializes the payload of the given {@code message}.
     *
     * @param message The {@link Message} containing the payload to be deserialized.
     * @return An {@link Optional} with the deserialized payload if it could be deserialized, or empty otherwise.
     * @implNote Will invoke the {@link #defaultMessageHandler} if a {@link PayloadDeserializationException}
     * is thrown.
     */
    private Optional<T> deserialize(final Message message) {
        // First check content type
        final Optional<String> contentType = message.contentType();
        if (contentType.isPresent()) {
            // If present, check content types.
            if (!contentType.get().equals(payloadDeserializer.contentType())) {
                // Content types are not the same, so deserialization won't be performed.
                LOGGER.warn(
                        "DeserializerHandler received a Message with a {} header value " +
                                "different from the deserializer's. " +
                                "Message will be handled with the default message handle, " +
                                "and deserialization won't be performed",
                        DefinedHeader.CONTENT_TYPE);
                defaultMessageHandler.handle(message);
                return Optional.empty();
            }
        } else {
            // If it is not present, just jog
            LOGGER.warn("DeserializerHandler received a Message without {} header. Will try to deserialize though",
                    DefinedHeader.CONTENT_TYPE);
        }

        final String payload = message.getPayload();
        try {
            return Optional.ofNullable(payloadDeserializer.deserialize(payload));
        } catch (final PayloadDeserializationException e) {
            defaultMessageHandler.handle(message);
            return Optional.empty();
        }
    }

    /**
     * Performs an action with the deserialized {@code payload} and the given {@code message}.
     *
     * @param payload The payload of type {@code T} used to operate.
     * @param message The {@link Message} that contains the {@code payload}, received in case further {@link Message}
     *                processing is needed to be performed (e.g to send a reply).
     */
    protected abstract void andThen(final T payload, final Message message);
}

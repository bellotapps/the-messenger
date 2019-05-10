package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.payload.PayloadDeserializationException;
import com.bellotapps.the_messenger.commons.payload.PayloadDeserializer;

import java.util.Optional;

/**
 * An abstract {@link MessageHandler} that deserializes the {@link Message}'s payload
 * according to a given {@link PayloadDeserializer}, and then performs the overridden action.
 *
 * @param <T> The concrete type into which the payload is deserialized.
 */
public abstract class DeserializerMessageHandler<T> implements MessageHandler {

    /**
     * The {@link PayloadDeserializer} to be used to perform the deserialization phase.
     */
    private final PayloadDeserializer<T> payloadDeserializer;

    /**
     * A {@link MessageHandler} to be invoked in case the deserialization phase fails.
     */
    private final MessageHandler deserializationExceptionMessageHandler;

    /**
     * Constructor.
     *
     * @param payloadDeserializer The {@link PayloadDeserializer} to be used to perform the deserialization phase.
     * @implNote This constructor sets a the {@link DoNothingMessageHandler} as the
     * {@code deserializationExceptionMessageHandler}.
     */
    protected DeserializerMessageHandler(final PayloadDeserializer<T> payloadDeserializer) {
        this(payloadDeserializer, DoNothingMessageHandler.getInstance());
    }

    /**
     * Constructor.
     *
     * @param payloadDeserializer                    The {@link PayloadDeserializer}
     *                                               to be used to perform the deserialization phase.
     * @param deserializationExceptionMessageHandler A {@link MessageHandler}
     *                                               to be invoked in case the deserialization phase fails.
     */
    protected DeserializerMessageHandler(
            final PayloadDeserializer<T> payloadDeserializer,
            final MessageHandler deserializationExceptionMessageHandler) {
        this.payloadDeserializer = payloadDeserializer;
        this.deserializationExceptionMessageHandler = deserializationExceptionMessageHandler;
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
     * @implNote Will invoke the {@link #deserializationExceptionMessageHandler} if a {@link PayloadDeserializationException}
     * is thrown.
     */
    private Optional<T> deserialize(final Message message) {
        final String payload = message.getPayload();
        try {
            return Optional.ofNullable(payloadDeserializer.deserialize(payload));
        } catch (final PayloadDeserializationException e) {
            deserializationExceptionMessageHandler.handle(message);
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

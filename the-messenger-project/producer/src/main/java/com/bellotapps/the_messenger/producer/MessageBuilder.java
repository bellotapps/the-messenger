package com.bellotapps.the_messenger.producer;

import com.bellotapps.the_messenger.commons.AbstractMessage;
import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.headers.ContentType;
import com.bellotapps.the_messenger.commons.headers.DefinedHeader;
import com.bellotapps.the_messenger.commons.headers.MessageType;
import com.bellotapps.the_messenger.commons.headers.ToStringSerializable;
import com.bellotapps.the_messenger.commons.payload.PayloadSerializer;
import com.bellotapps.the_messenger.producer.basic_payload_serializers.ToStringPayloadSerializer;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A builder of {@link Message}s.
 *
 * @param <T> The type of payload.
 */
public final class MessageBuilder<T> {

    /**
     * The id of the {@link Message}.
     */
    private String id;
    /**
     * The sender of the {@link Message}.
     */
    private String sender;
    /**
     * The timestamp of the {@link Message}.
     */
    private Supplier<Instant> timestampSupplier;
    /**
     * The headers of the {@link Message}.
     */
    private final Map<String, String> headers;
    /**
     * The payload of the {@link Message}, which will be serialized using the {@link #payloadSerializer}.
     */
    private T payload;
    /**
     * The {@link PayloadSerializer} to transform the {@link #payload} into a {@link String},
     * to be set to the {@link Message}.
     */
    private PayloadSerializer<T> payloadSerializer;
    /**
     * The {@link MessageCreator} to be used to instantiate the {@link Message} to be built.
     */
    private MessageCreator messageCreator;


    /**
     * Constructor.
     */
    public MessageBuilder() {
        this.headers = new HashMap<>();
        withRandomId();
        atBuildTime();
        withMessageCreator(DefaultMessage::new);
        usingToStringToSerialize();
    }


    /**
     * Sets the id of the {@link Message} to be built.
     *
     * @param id The id of the {@link Message}.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> withId(final String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets a random id to the {@link Message} to be built.
     *
     * @return {@code this} for method chaining.
     * @implNote This methods uses {@link UUID#randomUUID()} for random id generation.
     */
    public MessageBuilder<T> withRandomId() {
        return withId(UUID.randomUUID().toString());
    }

    /**
     * Sets the sender of the {@link Message} to be built.
     *
     * @param sender The sender of the {@link Message}.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> from(final String sender) {
        this.sender = sender;
        return this;
    }

    /**
     * Sets the timestamp of the {@link Message} to be built to now.
     *
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> atNow() {
        return at(Instant.now());
    }

    /**
     * Sets the timestamp of the {@link Message} to be built.
     *
     * @param timestamp The {@link Instant} representing the timestamp of the {@link Message}.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> at(final Instant timestamp) {
        return atSupplied(() -> timestamp);
    }

    /**
     * Makes the builder use {@link Instant#now()} as a {@link Supplier} of {@link Instant}.
     * This means that the {@link Message} will have as a timestamp the moment it is created
     * by the {@link #build()} method.
     *
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> atBuildTime() {
        return atSupplied(Instant::now);
    }

    /**
     * Sets a {@link Supplier} of {@link Instant} to be used to generate the timestamp of the {@link Message}
     * to be built. The {@link Supplier#get()} method will be executed when the {@link #build()} method is executed.
     *
     * @param timestampSupplier The {@link Supplier} of {@link Instant} to be used.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> atSupplied(final Supplier<Instant> timestampSupplier) {
        this.timestampSupplier = timestampSupplier;
        return this;
    }

    /**
     * Replaces all the headers in this builders with the given {@code headers}.
     *
     * @param headers The new headers {@link Map}.
     * @return {@code this} for method chaining.
     * @apiNote This method replaces the header if the key is already in use.
     */
    public MessageBuilder<T> replaceHeaders(final Map<String, String> headers) {
        this.headers.clear();
        return withHeaders(headers);
    }

    /**
     * Adds all the given {@code headers}.
     *
     * @param headers The headers to be added.
     * @return {@code this} for method chaining.
     * @apiNote This operation overrides already set headers.
     */
    public MessageBuilder<T> withHeaders(final Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * Adds a header to the {@link Message} to be built.
     *
     * @param key   The header's key.
     * @param value The header's value.
     * @return {@code this} for method chaining.
     * @apiNote This method replaces the header if the key is already in use.
     */
    public MessageBuilder<T> withHeader(final String key, final String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * Adds a header to the {@link Message} to be built.
     *
     * @param key   The header's key.
     * @param value The header's value.
     * @return {@code this} for method chaining.
     * @apiNote This method replaces the header if the key is already in use.
     */
    public MessageBuilder<T> withHeader(final ToStringSerializable key, final ToStringSerializable value) {
        return withHeader(key.serialize(), value.serialize());
    }

    /**
     * Adds a header to the {@link Message} to be built.
     *
     * @param key   The header's key.
     * @param value The header's value.
     * @return {@code this} for method chaining.
     * @apiNote This method replaces the header if the key is already in use.
     */
    public MessageBuilder<T> withHeader(final ToStringSerializable key, final String value) {
        return withHeader(key.serialize(), value);
    }

    /**
     * Adds a header to the {@link Message} to be built.
     *
     * @param key   The header's key.
     * @param value The header's value.
     * @return {@code this} for method chaining.
     * @apiNote This method replaces the header if the key is already in use.
     */
    public MessageBuilder<T> withHeader(final String key, final ToStringSerializable value) {
        return withHeader(key, value.serialize());
    }

    /**
     * Adds a header to the {@link Message} to be built.
     *
     * @param key   The header's key.
     * @param value The header's value.
     * @return {@code this} for method chaining.
     * @apiNote This method replaces the header if the key is already in use.
     */
    public MessageBuilder<T> withHeader(final DefinedHeader key, final String value) {
        return withHeader((ToStringSerializable) key, value);
    }

    /**
     * Adds a header to the {@link Message} to be built.
     *
     * @param key   The header's key.
     * @param value The header's value.
     * @return {@code this} for method chaining.
     * @apiNote This method replaces the header if the key is already in use.
     */
    public MessageBuilder<T> withHeader(final DefinedHeader key, final ToStringSerializable value) {
        return withHeader((ToStringSerializable) key, value);
    }

    /**
     * Specifies the message type (i.e adds the {@link DefinedHeader#MESSAGE_TYPE} header).
     *
     * @param type The message type.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> messageType(final String type) {
        return withHeader(DefinedHeader.MESSAGE_TYPE, type);
    }

    /**
     * Specifies the message type (i.e adds the {@link DefinedHeader#MESSAGE_TYPE} header).
     *
     * @param type The message type.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> messageType(final ToStringSerializable type) {
        return messageType(type.serialize());
    }

    /**
     * Specifies the message type (i.e adds the {@link DefinedHeader#MESSAGE_TYPE} header).
     *
     * @param type The message type.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> messageType(final MessageType type) {
        return messageType((ToStringSerializable) type);
    }


    /**
     * Specifies the content type of the payload (i.e adds the {@link DefinedHeader#CONTENT_TYPE} header).
     *
     * @param type The content type.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> contentType(final String type) {
        return withHeader(DefinedHeader.CONTENT_TYPE, type);
    }

    /**
     * Specifies the content type of the payload (i.e adds the {@link DefinedHeader#CONTENT_TYPE} header).
     *
     * @param type The content type.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> contentType(final ToStringSerializable type) {
        return contentType(type.serialize());
    }

    /**
     * Specifies the content type of the payload (i.e adds the {@link DefinedHeader#CONTENT_TYPE} header).
     *
     * @param type The content type.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> contentType(final ContentType type) {
        return contentType((ToStringSerializable) type);
    }


    /**
     * Sets the content type to {@link ContentType#PLAIN}.
     *
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> plainText() {
        return contentType(ContentType.PLAIN);
    }

    /**
     * Sets the content type to {@link ContentType#JSON}.
     *
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> json() {
        return contentType(ContentType.JSON);
    }


    /**
     * Sets the {@link DefinedHeader#REPLIES_TO} header with the given {@code repliedMessageId}.
     *
     * @param repliedMessageId The id of the message being replied.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> repliesTo(final String repliedMessageId) {
        return withHeader(DefinedHeader.REPLIES_TO, repliedMessageId);
    }

    /**
     * Sets the {@link DefinedHeader#REPLIES_TO} header with the given {@code repliedMessageId}.
     *
     * @param repliedMessageId The id of the message being replied.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> repliesTo(final ToStringSerializable repliedMessageId) {
        return repliesTo(repliedMessageId.serialize());
    }

    /**
     * Sets the {@link DefinedHeader#COMMAND} header with the given {@code command}.
     *
     * @param command The command being requested to be executed.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> command(final String command) {
        return withHeader(DefinedHeader.COMMAND, command);
    }

    /**
     * Sets the {@link DefinedHeader#COMMAND} header with the given {@code command}.
     *
     * @param command The command being requested to be executed.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> command(final ToStringSerializable command) {
        return command(command.serialize());
    }

    /**
     * Sets the {@link DefinedHeader#COPY_HEADERS} header with the given {@code headersToCopy}.
     *
     * @param headersToCopy A list of headers to be copied to the reply message.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> copyHeaders(final String... headersToCopy) {
        return withHeader(DefinedHeader.COPY_HEADERS, StringUtils.joinWith(", ", (Object[]) headersToCopy));
    }

    /**
     * Sets the {@link DefinedHeader#COPY_HEADERS} header with the given {@code headersToCopy}.
     *
     * @param headersToCopy A list of headers to be copied to the reply message.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> copyHeaders(final ToStringSerializable... headersToCopy) {
        return copyHeaders(Arrays.stream(headersToCopy).map(ToStringSerializable::serialize).toArray(String[]::new));
    }

    /**
     * Removes the header with the given {@code key} from the {@link Message} to be built.
     *
     * @param key The header key to be removed.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> withoutHeader(final String key) {
        this.headers.remove(key);
        return this;
    }

    /**
     * Removes the header with the given {@code key} from the {@link Message} to be built.
     *
     * @param key The header key to be removed.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> withoutHeader(final ToStringSerializable key) {
        return withoutHeader(key.serialize());
    }

    /**
     * Removes the header with the given {@code key} from the {@link Message} to be built.
     *
     * @param key The header key to be removed.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> withoutHeader(final DefinedHeader key) {
        return withoutHeader((ToStringSerializable) key);
    }

    /**
     * Removes all headers.
     *
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> clearHeaders() {
        this.headers.clear();
        return this;
    }

    /**
     * Sets the payload of the {@link Message} to be built.
     *
     * @param payload The payload of the {@link Message}.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> withPayload(final T payload) {
        this.payload = payload;
        return this;
    }

    /**
     * Sets the {@link PayloadSerializer} to be used to convert the payload into a {@link String}.
     *
     * @param serializer The {@link PayloadSerializer} to be used.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> withSerializer(final PayloadSerializer<T> serializer) {
        this.payloadSerializer = serializer;
        return contentType(serializer.contentType());
    }

    /**
     * Sets a {@link ToStringPayloadSerializer} to be used as a {@link PayloadSerializer}.
     *
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> usingToStringToSerialize() {
        return withSerializer(new ToStringPayloadSerializer<>());
    }

    /**
     * Sets the message creator used to create the {@link Message} to be built.
     *
     * @param messageCreator The {@link MessageCreator} to be used.
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> withMessageCreator(final MessageCreator messageCreator) {
        this.messageCreator = messageCreator;
        return this;
    }

    /**
     * Clears this builder.
     *
     * @return {@code this} for method chaining.
     */
    public MessageBuilder<T> clear() {
        this.id = null;
        this.sender = null;
        this.timestampSupplier = null;
        this.headers.clear();
        this.payload = null;
        this.payloadSerializer = null;
        this.messageCreator = null;

        return this;
    }


    /**
     * Creates an instance of {@link Message} according to this builder configuration.
     * Future modifications to this builder won't affect the returned {@link Message}.
     *
     * @return An instance of {@link Message}.
     * @throws IllegalArgumentException If any argument is invalid.
     */
    public Message build() throws IllegalArgumentException {
        return messageCreator.createMessage(
                id,
                sender,
                timestampSupplier.get(),
                headers,
                payloadSerializer.serialize(payload)
        );
    }


    // ================================================================================================================
    // Helpers
    // ================================================================================================================

    /**
     * Defines behaviour for an object that can instantiate {@link Message}s.
     */
    @FunctionalInterface
    public interface MessageCreator {

        /**
         * Creates a {@link Message}.
         *
         * @param id        The id for the {@link Message}.
         * @param sender    The sender for the {@link Message}.
         * @param timestamp The timestamp for the {@link Message}.
         * @param headers   The headers for the {@link Message}.
         * @param payload   The payload for the {@link Message}.
         * @return The created {@link Message}.
         */
        Message createMessage(
                final String id,
                final String sender,
                final Instant timestamp,
                final Map<String, String> headers,
                final String payload
        );
    }

    /**
     * An implementation of {@link Message} to be used in case no {@link MessageCreator} is set
     * in a {@link MessageBuilder} instance.
     */
    public static class DefaultMessage extends AbstractMessage {

        /**
         * Constructor.
         *
         * @param id        The message's id.
         * @param sender    An identification of the sender. This allows the recipient to know who has sent the message.
         * @param timestamp The timestamp of the message.
         * @param headers   The message headers.
         * @param payload   The message payload.
         * @throws IllegalArgumentException If any argument is invalid.
         */
        private DefaultMessage(
                final String id,
                final String sender,
                final Instant timestamp,
                final Map<String, String> headers,
                final String payload) throws IllegalArgumentException {
            super(id, sender, timestamp, headers, payload);
        }
    }
}

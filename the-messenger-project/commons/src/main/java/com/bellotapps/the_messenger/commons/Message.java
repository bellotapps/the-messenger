/*
 * Copyright 2019 BellotApps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bellotapps.the_messenger.commons;

import com.bellotapps.the_messenger.commons.support.InstantEpochDeserializer;
import com.bellotapps.the_messenger.commons.support.InstantEpochSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

/**
 * Represents a message.
 */
public class Message {

    /**
     * The JSON field for the id.
     */
    private static final String ID = "id";
    /**
     * The JSON field for the sender.
     */
    private static final String SENDER = "sn";
    /**
     * The JSON field for the timestamp.
     */
    private static final String TIMESTAMP = "ts";
    /**
     * The JSON field for the headers.
     */
    private static final String HEADERS = "hs";
    /**
     * The JSON field for the payload.
     */
    private static final String PAYLOAD = "pl";


    /**
     * The message's id.
     */
    private final String id;

    /**
     * An identification of the sender.
     * This allows the recipient to know who has sent the message.
     */
    private final String sender;

    /**
     * The timestamp of the message.
     */
    private final Instant timestamp;

    /**
     * The message headers.
     */
    private final Map<String, String> headers;

    /**
     * The message payload.
     */
    private final String payload;


    /**
     * Private constructor. Use
     *
     * @param id        The message's id.
     * @param sender    An identification of the sender. This allows the recipient to know who has sent the message.
     * @param timestamp The timestamp of the message.
     * @param headers   The message headers.
     * @param payload   The message payload.
     * @throws IllegalArgumentException If any argument is invalid.
     */
    @JsonCreator
    private Message(
            @JsonProperty(value = ID, access = JsonProperty.Access.WRITE_ONLY) final String id,
            @JsonProperty(value = SENDER, access = JsonProperty.Access.WRITE_ONLY) final String sender,
            @JsonProperty(value = TIMESTAMP, access = JsonProperty.Access.WRITE_ONLY)
            @JsonDeserialize(using = InstantEpochDeserializer.class) final Instant timestamp,
            @JsonProperty(value = HEADERS, access = JsonProperty.Access.WRITE_ONLY) final Map<String, String> headers,
            @JsonProperty(value = PAYLOAD, access = JsonProperty.Access.WRITE_ONLY) final String payload)
            throws IllegalArgumentException {
        Validate.isTrue(StringUtils.isNotBlank(id), "The id must have text");
        Validate.isTrue(StringUtils.isNotBlank(sender), "The sender must have text");
        Validate.isTrue(timestamp != null, "The timestamp must not be null");
        Validate.isTrue(headers != null, "The headers map must not be null");
        Validate.isTrue(
                headers.entrySet().stream().noneMatch(e -> StringUtils.isAnyBlank(e.getKey(), e.getValue())),
                "All the headers key and value must have text"
        );
        this.id = id;
        this.sender = sender;
        this.timestamp = timestamp;
        this.headers = Collections.unmodifiableMap(headers);
        this.payload = payload;
    }

    /**
     * @return The message's id.
     */
    @JsonProperty(value = ID, access = JsonProperty.Access.READ_ONLY)
    public String getId() {
        return id;
    }

    /**
     * @return An identification of the sender. This allows the recipient to know who has sent the message.
     */
    @JsonProperty(value = SENDER, access = JsonProperty.Access.READ_ONLY)
    public String getSender() {
        return sender;
    }

    /**
     * @return The timestamp of the message.
     */
    @JsonProperty(value = TIMESTAMP, access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = InstantEpochSerializer.class)
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * @return The message headers.
     */
    @JsonProperty(value = HEADERS, access = JsonProperty.Access.READ_ONLY)
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return The message payload.
     */
    @JsonProperty(value = PAYLOAD, access = JsonProperty.Access.READ_ONLY)
    public String getPayload() {
        return payload;
    }

    /**
     * A convenient method for inquiring the type of message.
     *
     * @return An {@link Optional} containing the type of message if any, or empty otherwise.
     */
    public Optional<String> type() {
        return Optional.ofNullable(headers.get(DefinedHeader.MESSAGE_TYPE.serialize()));
    }

    /**
     * A convenient method for inquiring the content type of the message.
     *
     * @return An {@link Optional} containing the content type of the message if any, or empty otherwise.
     */
    public Optional<String> contentType() {
        return Optional.ofNullable(headers.get(DefinedHeader.CONTENT_TYPE.serialize()));
    }

    /**
     * A convenient method for inquiring the command in the message (if it is a Command message).
     *
     * @return An {@link Optional} containing the command of message if any, or empty otherwise.
     * @apiNote For non command messages, an empty {@link Optional} will be returned.
     */
    public Optional<String> command() {
        // First check whether it is a Command message.
        if (type().filter(type -> MessageType.COMMAND.serialize().equals(type)).isPresent()) {
            // If yes, get the command (wrapped in an Optional as it might not be present, though it shouldn't).
            return Optional.ofNullable(headers.get(DefinedHeader.COMMAND.serialize()));
        }
        // If not, return an empty Optional.
        return Optional.empty();
    }

    /**
     * A builder of {@link Message}s.
     */
    public static class Builder {

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
         * The payload of the {@link Message}.
         */
        private String payload;

        /**
         * Constructor.
         */
        private Builder() {
            this.headers = new HashMap<>();
        }

        /**
         * Sets the id of the {@link Message} to be built.
         *
         * @param id The id of the {@link Message}.
         * @return {@code this} for method chaining.
         */
        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets a random id to the {@link Message} to be built.
         *
         * @return {@code this} for method chaining.
         * @implNote This methods uses {@link UUID#randomUUID()} for random id generation.
         */
        public Builder withRandomId() {
            return withId(UUID.randomUUID().toString());
        }

        /**
         * Sets the sender of the {@link Message} to be built.
         *
         * @param sender The sender of the {@link Message}.
         * @return {@code this} for method chaining.
         */
        public Builder from(final String sender) {
            this.sender = sender;
            return this;
        }

        /**
         * Sets the timestamp of the {@link Message} to be built to now.
         *
         * @return {@code this} for method chaining.
         */
        public Builder atNow() {
            return at(Instant.now());
        }

        /**
         * Sets the timestamp of the {@link Message} to be built.
         *
         * @param timestamp The {@link Instant} representing the timestamp of the {@link Message}.
         * @return {@code this} for method chaining.
         */
        public Builder at(final Instant timestamp) {
            return atSupplied(() -> timestamp);
        }


        /**
         * Makes the builder use {@link Instant#now()} as a {@link Supplier} of {@link Instant}.
         * This means that the {@link Message} will have as a timestamp the moment it is created
         * by the {@link #build()} method.
         *
         * @return {@code this} for method chaining.
         */
        public Builder atBuildTime() {
            return atSupplied(Instant::now);
        }

        /**
         * Sets a {@link Supplier} of {@link Instant} to be used to generate the timestamp of the {@link Message}
         * to be built. The {@link Supplier#get()} method will be executed when the {@link #build()} method is executed.
         *
         * @param timestampSupplier The {@link Supplier} of {@link Instant} to be used.
         * @return {@code this} for method chaining.
         */
        public Builder atSupplied(final Supplier<Instant> timestampSupplier) {
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
        public Builder replaceHeaders(final Map<String, String> headers) {
            this.headers.clear();
            this.headers.putAll(headers);
            return this;
        }

        /**
         * Adds all the given {@code headers}.
         *
         * @param headers The headers to be added.
         * @return {@code this} for method chaining.
         * @apiNote This operation overrides already set headers.
         */
        public Builder withHeaders(final Map<String, String> headers) {
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
        public Builder withHeader(final String key, final String value) {
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
        public Builder withHeader(final DefinedHeader key, final String value) {
            this.headers.put(key.serialize(), value);
            return this;
        }


        /**
         * Specifies the message type (i.e adds the {@link DefinedHeader#MESSAGE_TYPE} header).
         *
         * @param type The message type.
         * @return {@code this} for method chaining.
         */
        public Builder messageType(final String type) {
            return withHeader(DefinedHeader.MESSAGE_TYPE.serialize(), type);
        }

        /**
         * Specifies the message type (i.e adds the {@link DefinedHeader#MESSAGE_TYPE} header).
         *
         * @param type The message type.
         * @return {@code this} for method chaining.
         */
        public Builder messageType(final MessageType type) {
            return messageType(type.serialize());
        }

        /**
         * Specifies the content type of the payload (i.e adds the {@link DefinedHeader#CONTENT_TYPE} header).
         *
         * @param type The content type.
         * @return {@code this} for method chaining.
         */
        public Builder contentType(final String type) {
            return withHeader(DefinedHeader.CONTENT_TYPE.serialize(), type);
        }

        /**
         * Specifies the content type of the payload (i.e adds the {@link DefinedHeader#CONTENT_TYPE} header).
         *
         * @param type The content type.
         * @return {@code this} for method chaining.
         */
        public Builder contentType(final ContentType type) {
            return contentType(type.serialize());
        }

        /**
         * Sets the content type to {@link ContentType#JSON}.
         *
         * @return {@code this} for method chaining.
         */
        public Builder json() {
            return contentType(ContentType.JSON);
        }

        /**
         * Sets the {@link DefinedHeader#REPLIES_TO} header with the given {@code repliedMessageId}.
         *
         * @param repliedMessageId The id of the message being replied.
         * @return {@code this} for method chaining.
         */
        public Builder repliesTo(final String repliedMessageId) {
            return withHeader(DefinedHeader.REPLIES_TO, repliedMessageId);
        }

        /**
         * Sets the {@link DefinedHeader#COMMAND} header with the given {@code command}.
         *
         * @param command The command being requested to be executed.
         * @return {@code this} for method chaining.
         */
        public Builder command(final String command) {
            return withHeader(DefinedHeader.COMMAND, command);
        }

        /**
         * Sets the {@link DefinedHeader#COPY_HEADERS} header with the given {@code headersToCopy}.
         *
         * @param headersToCopy A list of headers to be copied to the reply message.
         * @return {@code this} for method chaining.
         */
        public Builder copyHeaders(final String... headersToCopy) {
            return withHeader(DefinedHeader.COPY_HEADERS, String.join(", ", headersToCopy));
        }


        /**
         * Removes the header with the given {@code key} from the {@link Message} to be built.
         *
         * @param key The header key to be removed.
         * @return {@code this} for method chaining.
         */
        public Builder withoutHeader(final String key) {
            this.headers.remove(key);
            return this;
        }

        /**
         * Removes the header with the given {@code key} from the {@link Message} to be built.
         *
         * @param key The header key to be removed.
         * @return {@code this} for method chaining.
         */
        public Builder withoutHeader(final DefinedHeader key) {
            this.headers.remove(key.serialize());
            return this;
        }

        /**
         * Removes all headers.
         *
         * @return {@code this} for method chaining.
         */
        public Builder clearHeaders() {
            this.headers.clear();
            return this;
        }


        /**
         * Sets the payload of the {@link Message} to be built.
         *
         * @param payload The payload of the {@link Message}.
         * @return {@code this} for method chaining.
         */
        public Builder withPayload(final String payload) {
            this.payload = payload;
            return this;
        }


        /**
         * Clears this builder.
         *
         * @return {@code this} for method chaining.
         */
        public Builder clear() {
            this.id = null;
            this.sender = null;
            this.timestampSupplier = null;
            this.headers.clear();
            this.payload = null;

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
            if (id == null) {
                withRandomId();
            }
            if (timestampSupplier == null) {
                atBuildTime();
            }
            return new Message(id, sender, timestampSupplier.get(), headers, payload);
        }

        /**
         * Creates a new builder instance.
         *
         * @return A new builder instance.
         */
        public static Builder create() {
            return new Builder();
        }

        /**
         * Creates a new builder instance, preconfigured to be a simple message.
         *
         * @return A new builder instance, preconfigured to be a simple message.
         * @see MessageType#SIMPLE
         */
        public static Builder simpleMessage() {
            return create()
                    .messageType(MessageType.SIMPLE);
        }

        /**
         * Creates a new builder instance, preconfigured to be a reply message.
         *
         * @param repliedMessageId The id of the message being replied.
         * @return A new builder instance, preconfigured to be a reply message.
         * @see MessageType#REPLY
         */
        public static Builder replyMessage(final String repliedMessageId) {
            return create()
                    .messageType(MessageType.REPLY)
                    .repliesTo(repliedMessageId);
        }

        /**
         * Creates a new builder instance, preconfigured to be a command message.
         *
         * @param command The command being requested to be executed.
         * @return A new builder instance, preconfigured to be a command message.
         * @see MessageType#SIMPLE
         */
        public static Builder commandMessage(final String command) {
            return create()
                    .messageType(MessageType.COMMAND)
                    .command(command);
        }
    }
}

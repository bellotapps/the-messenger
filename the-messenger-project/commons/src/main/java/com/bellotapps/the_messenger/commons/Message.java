package com.bellotapps.the_messenger.commons;

import com.bellotapps.the_messenger.commons.headers.DefinedHeader;
import com.bellotapps.the_messenger.commons.headers.MessageType;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a message.
 */
public interface Message {

    /**
     * @return The message's id.
     */
    String getId();

    /**
     * @return An identification of the sender. This allows the recipient to know who has sent the message.
     */
    String getSender();

    /**
     * @return The timestamp of the message.
     */
    Instant getTimestamp();

    /**
     * @return The message headers.
     */
    Map<String, String> getHeaders();

    /**
     * @return The message payload.
     */
    String getPayload();


    // ================================================================================================================
    // Convenient methods
    // ================================================================================================================

    /**
     * Returns the value for the given {@code header} if it exists.
     *
     * @param header The header to be inquired.
     * @return An {@link Optional} with the header's value if present, or empty otherwise.
     */
    default Optional<String> headerValue(final String header) {
        return Optional.ofNullable(getHeaders().get(header));
    }

    /**
     * A convenient method for inquiring the type of message.
     *
     * @return An {@link Optional} containing the type of message if any, or empty otherwise.
     */
    default Optional<String> type() {
        return headerValue(DefinedHeader.MESSAGE_TYPE.serialize());
    }

    /**
     * A convenient method for inquiring the content type of the message.
     *
     * @return An {@link Optional} containing the content type of the message if any, or empty otherwise.
     */
    default Optional<String> contentType() {
        return headerValue(DefinedHeader.CONTENT_TYPE.serialize());
    }

    /**
     * A convenient method for inquiring the command in the message (if it is a Command message).
     *
     * @return An {@link Optional} containing the command of message if any, or empty otherwise.
     * @apiNote For non command messages, an empty {@link Optional} will be returned.
     */
    default Optional<String> command() {
        // First check whether it is a Command message.
        if (type().filter(type -> MessageType.COMMAND.serialize().equals(type)).isPresent()) {
            // If yes, get the command (wrapped in an Optional as it might not be present, though it shouldn't).
            return headerValue(DefinedHeader.COMMAND.serialize());
        }
        // If not, return an empty Optional.
        return Optional.empty();
    }

    /**
     * A convenient method for inquiring the headers to be copied in a reply to this message.
     *
     * @return A {@link List} containing the headers that must be copied to a reply to this message.
     * @apiNote This method will show all the headers that were requested to be copied,
     * even if they don't exist in the message.
     */
    default List<String> copyHeaders() {
        return headerValue(DefinedHeader.COPY_HEADERS.serialize())
                .map(headers -> StringUtils.split(headers, ", "))
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    /**
     * A convenient method for inquiring the headers to be copied in a reply to this message,
     * together with the values of those headers.
     *
     * @return A {@link Map} containing the headers that must be copied to a reply to this message,
     * together with the values of those headers.
     * @apiNote This method will return only headers that exist in the message.
     * Those that were requested but do not exist in the message won't be included.
     */
    default Map<String, String> copyHeadersKeysAndValues() {
        final List<String> copyHeaders = copyHeaders();
        return getHeaders().entrySet().stream()
                .filter(e -> copyHeaders.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

package com.bellotapps.the_messenger.producer;

import com.bellotapps.the_messenger.commons.Message;

/**
 * Defines behaviour for objects that can send {@link Message}s.
 */
public interface MessageProducer {

    /**
     * Sends the given {@code message} to the given {@code recipient}.
     *
     * @param message   The {@link Message} to be sent.
     * @param recipient The destination of the given {@code message}.
     * @throws IllegalArgumentException If the given {@code message} or {@code recipient} are null.
     */
    void send(final Message message, final String recipient) throws IllegalArgumentException;
}

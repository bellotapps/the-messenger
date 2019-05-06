package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;

/**
 * Defines behaviour for objects that can handle incoming {@link Message}s.
 */
@FunctionalInterface
public interface MessageHandler {

    /**
     * Handles the given {@code message}.
     *
     * @param message The {@link Message} to be handled.
     */
    void handle(final Message message);
}

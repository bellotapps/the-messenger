package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;

/**
 * A {@link MessageHandler} that discards {@link Message}s.
 */
public class DoNothingMessageHandler implements MessageHandler {

    /**
     * The unique instance of a {@link DoNothingMessageHandler}.
     */
    private static final DoNothingMessageHandler SINGLETON = new DoNothingMessageHandler();

    /**
     * Private constructor.
     * Use {@link #getInstance()} to get an instance of the {@link DoNothingMessageHandler}.
     */
    private DoNothingMessageHandler() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final Message message) {
        // NO-OP
    }

    /**
     * Returns the singleton.
     *
     * @return The unique instance of a {@link DoNothingMessageHandler}.
     */
    public static DoNothingMessageHandler getInstance() {
        return SINGLETON;
    }
}

package com.bellotapps.the_messenger.producer;

import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.headers.MessageType;

/**
 * Defines behaviour for an object that can create instances of {@link MessageBuilder}s of a given type of payload.
 *
 * @param <T> The type of payload to be set in the {@link Message}s built with the created {@link MessageBuilder}s.
 */
public interface MessageBuilderFactory<T> {

    /**
     * Creates a new builder instance.
     *
     * @return A new builder instance.
     */
    default MessageBuilder<T> create() {
        return new MessageBuilder<>();
    }

    /**
     * Creates a new builder instance, preconfigured to be a simple message.
     *
     * @return A new builder instance, preconfigured to be a simple message.
     * @see MessageType#SIMPLE
     */
    default MessageBuilder<T> simpleMessage() {
        return this.create()
                .messageType(MessageType.SIMPLE);
    }

    /**
     * Creates a new builder instance, preconfigured to be a reply message.
     *
     * @param repliedMessageId The id of the message being replied.
     * @return A new builder instance, preconfigured to be a reply message.
     * @see MessageType#REPLY
     */
    default MessageBuilder<T> replyMessage(final String repliedMessageId) {
        return this.create()
                .messageType(MessageType.REPLY)
                .repliesTo(repliedMessageId);
    }

    /**
     * Creates a new builder instance, preconfigured to be a reply message.
     *
     * @param repliedMessage The {@link Message} being replied.
     * @return A new builder instance, preconfigured to be a reply message.
     * @see MessageType#REPLY
     */
    default MessageBuilder<T> replyMessage(final Message repliedMessage) {
        return this.replyMessage(repliedMessage.getId())
                .withHeaders(repliedMessage.copyHeadersKeysAndValues());
    }

    /**
     * Creates a new builder instance, preconfigured to be a command message.
     *
     * @param command The command being requested to be executed.
     * @return A new builder instance, preconfigured to be a command message.
     * @see MessageType#SIMPLE
     */
    default MessageBuilder<T> commandMessage(final String command) {
        return this.create()
                .messageType(MessageType.COMMAND)
                .command(command);
    }
}

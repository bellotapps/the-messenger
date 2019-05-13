package com.bellotapps.the_messenger.producer.basic_factories;

import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.payload.PayloadSerializer;
import com.bellotapps.the_messenger.producer.MessageBuilder;
import com.bellotapps.the_messenger.producer.MessageBuilderFactory;

/**
 * An implementation of {@link MessageBuilderFactory} that allows creating {@link MessageBuilder}s
 * with a sender, a {@link PayloadSerializer}, and a {@link MessageBuilder.MessageCreator} preconfigured.
 *
 * @param <T> The type of payload to be set in the {@link Message}s built with the created {@link MessageBuilder}s.
 */
public class GenericMessageBuilderFactory<T> implements MessageBuilderFactory<T> {

    /**
     * The sender to be configured to the created {@link MessageBuilder}s.
     */
    private final String sender;
    /**
     * The {@link PayloadSerializer} to be configured to the created {@link MessageBuilder}s.
     */
    private final PayloadSerializer<T> serializer;
    /**
     * The {@link MessageBuilder.MessageCreator} to be configured to the created {@link MessageBuilder}s.
     */
    private final MessageBuilder.MessageCreator messageCreator;


    /**
     * Constructor.
     *
     * @param sender         The sender to be configured to the created {@link MessageBuilder}s.
     * @param serializer     The {@link PayloadSerializer} to be configured to the created {@link MessageBuilder}s.
     * @param messageCreator The {@link MessageBuilder.MessageCreator}
     *                       to be configured to the created {@link MessageBuilder}s.
     */
    public GenericMessageBuilderFactory(
            final String sender,
            final PayloadSerializer<T> serializer,
            final MessageBuilder.MessageCreator messageCreator) {
        this.sender = sender;
        this.serializer = serializer;
        this.messageCreator = messageCreator;
    }


    @Override
    public MessageBuilder<T> create() {
        return MessageBuilderFactory.super.create()
                .from(sender)
                .withSerializer(serializer)
                .contentType(serializer.contentType())
                .withMessageCreator(messageCreator);
    }
}

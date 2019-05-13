package com.bellotapps.the_messenger.producer.basic_factories;

import com.bellotapps.the_messenger.producer.MessageBuilder;
import com.bellotapps.the_messenger.producer.basic_payload_serializers.ToStringPayloadSerializer;

/**
 * An extension of a {@link GenericMessageBuilderFactory} that creates {@link MessageBuilder}s of {@link String},
 * * using a {@link ToStringPayloadSerializer}.
 */
public class ToStringPayloadMessageBuilderFactory<T> extends GenericMessageBuilderFactory<T> {

    /**
     * The sender to be configured to the created {@link MessageBuilder}s.
     */
    private final String sender;
    /**
     * The {@link MessageBuilder.MessageCreator} to be configured to the created {@link MessageBuilder}s.
     */
    private final MessageBuilder.MessageCreator messageCreator;


    /**
     * Constructor.
     *
     * @param sender         The sender to be configured to the created {@link MessageBuilder}s.
     * @param messageCreator The {@link MessageBuilder.MessageCreator}
     *                       to be configured to the created {@link MessageBuilder}s.
     */
    public ToStringPayloadMessageBuilderFactory(
            final String sender,
            final MessageBuilder.MessageCreator messageCreator) {
        super(sender, new ToStringPayloadSerializer<>(), messageCreator);
        this.sender = sender;
        this.messageCreator = messageCreator;
    }
}

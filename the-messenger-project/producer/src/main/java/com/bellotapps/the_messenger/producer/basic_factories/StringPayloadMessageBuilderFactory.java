package com.bellotapps.the_messenger.producer.basic_factories;

import com.bellotapps.the_messenger.producer.MessageBuilder;
import com.bellotapps.the_messenger.producer.basic_payload_serializers.PlainPayloadSerializer;

/**
 * An extension of a {@link GenericMessageBuilderFactory} that creates {@link MessageBuilder}s of {@link String},
 * using a {@link PlainPayloadSerializer}.
 */
public class StringPayloadMessageBuilderFactory extends GenericMessageBuilderFactory<String> {

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
    public StringPayloadMessageBuilderFactory(
            final String sender,
            final MessageBuilder.MessageCreator messageCreator) {
        super(sender, new PlainPayloadSerializer(), messageCreator);
        this.sender = sender;
        this.messageCreator = messageCreator;
    }
}

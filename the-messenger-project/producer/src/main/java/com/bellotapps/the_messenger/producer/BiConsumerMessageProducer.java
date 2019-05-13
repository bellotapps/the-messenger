package com.bellotapps.the_messenger.producer;

import com.bellotapps.the_messenger.commons.Message;
import org.apache.commons.lang3.Validate;

import java.util.function.BiConsumer;

/**
 * A {@link MessageProducer} that delegates the sending operation to a {@link BiConsumer}
 * of {@link Message} and a recipient.
 */
public class BiConsumerMessageProducer implements MessageProducer {

    /**
     * The {@link BiConsumer} to which the sending operation is delegated.
     * It should be stateless.
     */
    private final BiConsumer<Message, String> sender;

    /**
     * Constructor.
     *
     * @param sender The {@link BiConsumer} to which the sending operation is delegated. It should be stateless.
     */
    public BiConsumerMessageProducer(final BiConsumer<Message, String> sender) {
        this.sender = sender;
    }


    @Override
    public void send(final Message message, final String recipient) throws IllegalArgumentException {
        Validate.isTrue(message != null, "The message must not be null");
        Validate.isTrue(recipient != null, "The recipient must not be null");
        sender.accept(message, recipient);
    }
}

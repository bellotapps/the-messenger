package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;

import java.util.Optional;

/**
 * A built in {@link MessageHandler} that can be easily configured.
 */
public class BuiltInMessageHandler implements MessageHandler {

    /**
     * A {@link MessageHandler} for non typed {@link Message}s.
     */
    private final MessageHandler nonTypedMessageHandler;

    /**
     * A {@link MessageHandler} for {@link Message}s that are typed.
     * A {@link TypedMessageHandler} is suggested to be used here.
     */
    private final MessageHandler typedMessageHandler;

    /**
     * Constructor.
     *
     * @param nonTypedMessageHandler A {@link MessageHandler} for non typed {@link Message}s.
     * @param typedMessageHandler    A {@link TypedMessageHandler} for {@link Message}s that are typed.
     *                               A {@link TypedMessageHandler} is suggested to be used here.
     */
    private BuiltInMessageHandler(
            final MessageHandler nonTypedMessageHandler,
            final MessageHandler typedMessageHandler) {
        this.nonTypedMessageHandler = nonTypedMessageHandler;
        this.typedMessageHandler = typedMessageHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final Message message) {
        final Optional<String> messageType = message.type();
        if (messageType.isPresent()) {
            typedMessageHandler.handle(message);
            return;
        }
        nonTypedMessageHandler.handle(message);
    }

    /**
     * A builder for {@link BuiltInMessageHandler}.
     */
    public static final class Builder {

        /**
         * A {@link MessageHandler} for non typed {@link Message}s.
         */
        private MessageHandler nonTypedMessageHandler;

        /**
         * A {@link MessageHandler} for {@link Message}s that are typed.
         */
        private MessageHandler typedMessageHandler;

        /**
         * Private constructor.
         * Use {@link Builder#create()} to get an instance of this builder.
         */
        private Builder() {
            this.nonTypedMessageHandler = DoNothingMessageHandler.getInstance();
            this.typedMessageHandler = DoNothingMessageHandler.getInstance();
        }


        /**
         * Sets a {@link MessageHandler} for non typed {@link Message}s.
         *
         * @param nonTypedMessageHandler A {@link MessageHandler} for non typed {@link Message}s.
         * @return {@code this} for method chaining.
         */
        public Builder withNonTypedMessageHandler(final MessageHandler nonTypedMessageHandler) {
            this.nonTypedMessageHandler = nonTypedMessageHandler;
            return this;
        }

        /**
         * Sets a {@link MessageHandler} for {@link Message}s that are typed.
         * A {@link TypedMessageHandler} is suggested to be used here, which can be configured with the
         * {@link #configureTypedMessageHandlers()} method.
         *
         * @param typedMessageHandler A {@link TypedMessageHandler} is suggested to be used here.
         * @return {@code this} for method chaining.
         * @apiNote Calling this method will replace the {@link TypedMessageHandler} configured with the
         * {@link TypedMessageHandlerBuilderWrapper} returned by the {@link #configureTypedMessageHandlers()} method.
         * @see TypedMessageHandler
         */
        public Builder withTypedMessageHandler(final MessageHandler typedMessageHandler) {
            this.typedMessageHandler = typedMessageHandler;
            return this;
        }

        /**
         * Creates a {@link TypedMessageHandlerBuilderWrapper} to configure a {@link TypedMessageHandler}.
         *
         * @return A {@link TypedMessageHandlerBuilderWrapper}.
         * @apiNote Calling {@link TypedMessageHandlerBuilderWrapper#continueWithParentBuilder()}
         * will replace the {@link MessageHandler} set with the {@link #withTypedMessageHandler(MessageHandler)} method.
         * @see TypedMessageHandler
         * @see TypedMessageHandlerBuilderWrapper
         * @see TypedMessageHandler.Builder
         */
        public TypedMessageHandlerBuilderWrapper<Builder> configureTypedMessageHandlers() {
            return TypedMessageHandlerBuilderWrapper
                    .createForParent(this, Builder::withTypedMessageHandler);
        }

        /**
         * Clears this builder.
         *
         * @return {@code this} for method chaining.
         */
        public Builder clear() {
            this.nonTypedMessageHandler = DoNothingMessageHandler.getInstance();
            this.typedMessageHandler = DoNothingMessageHandler.getInstance();
            return this;
        }


        /**
         * Builds the configured {@link BuiltInMessageHandler}.
         *
         * @return The created {@link BuiltInMessageHandler} configured by this builder.
         */
        public BuiltInMessageHandler build() {
            return new BuiltInMessageHandler(nonTypedMessageHandler, typedMessageHandler);
        }


        /**
         * Creates an instance of this builder.
         *
         * @return A new instance of this builder.
         */
        public static Builder create() {
            return new Builder();
        }
    }
}

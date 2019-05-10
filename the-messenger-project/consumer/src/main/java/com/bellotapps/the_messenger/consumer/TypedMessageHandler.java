package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.headers.MessageType;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link MessageHandler} for {@link Message}s that have type.
 */
public class TypedMessageHandler implements MessageHandler {

    /**
     * The {@link Logger}.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TypedMessageHandler.class);

    /**
     * A {@link Map} containing a {@link MessageHandler} for each managed type.
     */
    private final Map<String, MessageHandler> typedMessageHandlers;
    /**
     * The {@link MessageHandler} for types not contained in the {@link #typedMessageHandlers} {@link Map}.
     */
    private final MessageHandler defaultMessageHandler;

    /**
     * Constructor.
     *
     * @param typedMessageHandlers  A {@link Map} containing a {@link MessageHandler} for each managed type.
     * @param defaultMessageHandler The {@link MessageHandler} for types not contained
     *                              in the {@code typedMessageHandlers} {@link Map}.
     */
    private TypedMessageHandler(
            final Map<String, MessageHandler> typedMessageHandlers,
            final MessageHandler defaultMessageHandler) {
        Validate.isTrue(typedMessageHandlers != null, "The message handlers map must not be null.");
        Validate.isTrue(defaultMessageHandler != null, "The default message handler must not be null.");
        this.typedMessageHandlers = Collections.unmodifiableMap(typedMessageHandlers);
        this.defaultMessageHandler = defaultMessageHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final Message message) {
        if (message == null) {
            LOGGER.warn("TypedMessageHandler received a null message! Will skip it!");
            return;
        }
        final MessageHandler handler = message.type()
                .map(this::getHandlerForType)
                .orElse(TypedMessageHandler::logNonTypedMessageReceived);
        handler.handle(message);
    }

    /**
     * Gets the {@link MessageHandler} for the given {@code type} if there is any,
     * or the {@code defaultMessageHandler} if the {@code type} is not managed.
     *
     * @param type The type to be handled.
     * @return The {@link MessageHandler} for the given {@code type} if it exists,
     * or the {@code defaultMessageHandler} if the type is not managed.
     */
    private MessageHandler getHandlerForType(final String type) {
        return typedMessageHandlers.getOrDefault(type, defaultMessageHandler);
    }

    /**
     * Logs that the given {@code message} is not typed.
     *
     * @param message The non typed {@link Message}.
     * @apiNote This method can be used as a {@link MessageHandler} when written as a lambda.
     * @see MessageHandler
     */
    private static void logNonTypedMessageReceived(final Message message) {
        LOGGER.warn(
                "TypedMessageHandler received a non typed message message with id {}. Discarding it...",
                message.getId()
        );
    }

    /**
     * A builder for {@link TypedMessageHandler}.
     */
    public static final class Builder {

        /**
         * The {@link Map} containing a {@link MessageHandler} for each managed type
         * for the {@link TypedMessageHandler} to be built.
         */
        private final Map<String, MessageHandler> typedMessageHandlers;
        /**
         * The default {@link MessageHandler} for the {@link TypedMessageHandler} to be built.
         */
        private MessageHandler defaultMessageHandler;


        /**
         * Private constructor.
         * Use {@link Builder#create()} to get an instance of this builder.
         */
        private Builder() {
            typedMessageHandlers = new HashMap<>();
            this.defaultMessageHandler = DoNothingMessageHandler.getInstance();
        }


        /**
         * Configures a {@link MessageHandler} for the given {@code type}.
         *
         * @param type    The type to be handled by the given {@code handler}.
         * @param handler The {@link MessageHandler} that will handle {@link Message}s of the given {@code type}.
         * @return {@code this} for method chaining.
         */
        public Builder handleTypeWith(final String type, final MessageHandler handler) {
            this.typedMessageHandlers.put(type, handler);
            return this;
        }

        /**
         * Sets a {@link MessageHandler} for simple {@link Message}s.
         *
         * @param messageHandler A {@link MessageHandler} for simple {@link Message}s.
         * @return {@code this} for method chaining.
         * @see MessageType#SIMPLE
         */
        public Builder handleSimpleMessageWith(final MessageHandler messageHandler) {
            return handleTypeWith(MessageType.SIMPLE.serialize(), messageHandler);
        }

        /**
         * Sets a {@link MessageHandler} for reply {@link Message}s.
         *
         * @param messageHandler A {@link MessageHandler} for reply {@link Message}s.
         * @return {@code this} for method chaining.
         * @see MessageType#REPLY
         */
        public Builder handleReplyMessageWith(final MessageHandler messageHandler) {
            return handleTypeWith(MessageType.REPLY.serialize(), messageHandler);
        }

        /**
         * Sets a {@link MessageHandler} for command {@link Message}s.
         *
         * @param messageHandler A {@link MessageHandler} for reply {@link Message}s.
         * @return {@code this} for method chaining.
         * @see MessageType#COMMAND
         */
        public Builder handleCommandMessageWith(final MessageHandler messageHandler) {
            return handleTypeWith(MessageType.COMMAND.serialize(), messageHandler);
        }

        /**
         * Replaces all the configured {@link MessageHandler}s with the given {@code messageHandlers}.
         *
         * @param messageHandlers The new {@link MessageHandler}s.
         * @return {@code this} for method chaining.
         */
        public Builder replaceHandlers(final Map<String, MessageHandler> messageHandlers) {
            this.typedMessageHandlers.clear();
            return addHandlers(messageHandlers);
        }

        /**
         * Configures all the given {@code messageHandlers}.
         *
         * @param messageHandlers The {@link MessageHandler}s to be configured.
         * @return {@code this} for method chaining.
         */
        public Builder addHandlers(final Map<String, MessageHandler> messageHandlers) {
            this.typedMessageHandlers.putAll(messageHandlers);
            return this;
        }

        /**
         * Configures the default {@link MessageHandler}
         * (will handle typed {@link Message}s whose type has no {@link MessageHandler} configured).
         *
         * @param defaultMessageHandler The default {@link MessageHandler}.
         * @return {@code this} for method chaining.
         */
        public Builder defaultHandler(final MessageHandler defaultMessageHandler) {
            this.defaultMessageHandler = defaultMessageHandler;
            return this;
        }

        /**
         * Creates a {@link CommandMessageHandlerBuilderWrapper} to configure a {@link CommandMessageHandler}.
         *
         * @return A {@link CommandMessageHandlerBuilderWrapper} that uses this builder as a parent.
         * @apiNote Calling {@link CommandMessageHandlerBuilderWrapper#continueWithParentBuilder()}
         * will replace the {@link MessageHandler} set with the {@link #handleCommandMessageWith(MessageHandler)}
         * method.
         * @see CommandMessageHandler
         * @see CommandMessageHandler.Builder
         * @see CommandMessageHandlerBuilderWrapper
         */
        public CommandMessageHandlerBuilderWrapper<Builder> configureCommandMessageHandlers() {
            return CommandMessageHandlerBuilderWrapper
                    .createForParent(this, Builder::handleCommandMessageWith);
        }

        /**
         * Clears this builder.
         *
         * @return {@code this} for method chaining.
         */
        public Builder clear() {
            this.typedMessageHandlers.clear();
            this.defaultMessageHandler = DoNothingMessageHandler.getInstance();
            return this;
        }


        /**
         * Builds the configured {@link TypedMessageHandler}.
         *
         * @return The created {@link TypedMessageHandler} configured by this builder.
         */
        public TypedMessageHandler build() {
            return new TypedMessageHandler(typedMessageHandlers, defaultMessageHandler);
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

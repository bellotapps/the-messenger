package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link MessageHandler} for command {@link Message}s.
 */
public class CommandMessageHandler implements MessageHandler {

    /**
     * The {@link Logger}.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandMessageHandler.class);

    /**
     * A {@link Map} containing a {@link MessageHandler} for each managed command.
     */
    private final Map<String, MessageHandler> commandMessageHandlers;
    /**
     * The {@link MessageHandler} for commands not contained in the {@link #commandMessageHandlers} {@link Map}.
     */
    private final MessageHandler defaultMessageHandler;

    /**
     * Constructor.
     *
     * @param commandMessageHandlers A {@link Map} containing a {@link MessageHandler} for each managed command.
     * @param defaultMessageHandler  The {@link MessageHandler} for commands not contained
     *                               in the {@code commandMessageHandlers} {@link Map}.
     */
    private CommandMessageHandler(
            final Map<String, MessageHandler> commandMessageHandlers,
            final MessageHandler defaultMessageHandler) {
        Validate.isTrue(commandMessageHandlers != null, "The message handlers map must not be null.");
        Validate.isTrue(defaultMessageHandler != null, "The default message handler must not be null.");
        this.commandMessageHandlers = Collections.unmodifiableMap(commandMessageHandlers);
        this.defaultMessageHandler = defaultMessageHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final Message message) {
        if (message == null) {
            LOGGER.warn("CommandMessageHandler received a null message! Discarding it...");
            return;
        }
        final MessageHandler handler = message.command()
                .map(this::getHandlerForCommand)
                .orElse(CommandMessageHandler::logNonCommandMessageReceived);
        handler.handle(message);
    }

    /**
     * Gets the {@link MessageHandler} for the given {@code command} if there is any,
     * or the {@code defaultMessageHandler} if the {@code command} is not managed.
     *
     * @param command The command to be handled.
     * @return The {@link MessageHandler} for the given {@code command} if it exists,
     * or the {@code defaultMessageHandler} if the command is not managed.
     */
    private MessageHandler getHandlerForCommand(final String command) {
        return commandMessageHandlers.getOrDefault(command, defaultMessageHandler);
    }

    /**
     * Logs that the given {@code message} is not a command.
     *
     * @param message The {@link Message} that is not a command.
     * @apiNote This method can be used as a {@link MessageHandler} when written as a lambda.
     * @see MessageHandler
     */
    private static void logNonCommandMessageReceived(final Message message) {
        LOGGER.warn(
                "CommandMessageHandler received a non command message with id {}. Discarding it...",
                message.getId()
        );
    }

    /**
     * A builder for {@link CommandMessageHandler}.
     */
    public static final class Builder {

        /**
         * The {@link Map} containing a {@link MessageHandler} for each managed command
         * for the {@link CommandMessageHandler} to be built.
         */
        private final Map<String, MessageHandler> commandMessageHandlers;
        /**
         * The default {@link MessageHandler} for the {@link CommandMessageHandler} to be built.
         */
        private MessageHandler defaultMessageHandler;


        /**
         * Private constructor.
         * Use {@link Builder#create()} to get an instance of this builder.
         */
        private Builder() {
            commandMessageHandlers = new HashMap<>();
            this.defaultMessageHandler = DoNothingMessageHandler.getInstance();
        }


        /**
         * Configures a {@link MessageHandler} for the given {@code command}.
         *
         * @param command The command to be handled by the given {@code handler}.
         * @param handler The {@link MessageHandler} that will handle {@link Message}s of the given {@code command}.
         * @return {@code this} for method chaining.
         */
        public Builder handleCommandWith(final String command, final MessageHandler handler) {
            this.commandMessageHandlers.put(command, handler);
            return this;
        }

        /**
         * Replaces all the configured {@link MessageHandler}s with the given {@code messageHandlers}.
         *
         * @param messageHandlers The new {@link MessageHandler}s.
         * @return {@code this} for method chaining.
         */
        public Builder replaceHandlers(final Map<String, MessageHandler> messageHandlers) {
            this.commandMessageHandlers.clear();
            return addHandlers(messageHandlers);
        }

        /**
         * Configures all the given {@code messageHandlers}.
         *
         * @param messageHandlers The {@link MessageHandler}s to be configured.
         * @return {@code this} for method chaining.
         */
        public Builder addHandlers(final Map<String, MessageHandler> messageHandlers) {
            this.commandMessageHandlers.putAll(messageHandlers);
            return this;
        }

        /**
         * Configures the default {@link MessageHandler}
         * (will handle command {@link Message}s that have no {@link MessageHandler} configured).
         *
         * @param defaultMessageHandler The default {@link MessageHandler}.
         * @return {@code this} for method chaining.
         */
        public Builder defaultHandler(final MessageHandler defaultMessageHandler) {
            this.defaultMessageHandler = defaultMessageHandler;
            return this;
        }

        /**
         * Clears this builder.
         *
         * @return {@code this} for method chaining.
         */
        public Builder clear() {
            this.commandMessageHandlers.clear();
            this.defaultMessageHandler = DoNothingMessageHandler.getInstance();
            return this;
        }


        /**
         * Builds the configured {@link CommandMessageHandler}.
         *
         * @return The created {@link CommandMessageHandler} configured by this builder.
         */
        public CommandMessageHandler build() {
            return new CommandMessageHandler(commandMessageHandlers, defaultMessageHandler);
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

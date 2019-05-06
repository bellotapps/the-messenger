package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * An adapter of a {@link CommandMessageHandler.Builder} to be used in the context of a parent builder.
 *
 * @param <P> The concrete type of parent builder.
 */
public class CommandMessageHandlerBuilderWrapper<P> {

    /**
     * A builder of type {@code P} that creates this builder.
     */
    private final P parentBuilder;

    /**
     * The {@link CommandMessageHandler.Builder} being wrapped (the building process is delegated to it).
     */
    private final CommandMessageHandler.Builder commandMessageHandlerBuilder;

    /**
     * A {@link BiConsumer} that takes the parent and a {@link MessageHandler} that will be built with the wrapped
     * {@link CommandMessageHandler.Builder} and sets the latter in the former.
     */
    private final BiConsumer<P, CommandMessageHandler> commandMessageHandlerSetter;


    /**
     * Private Constructor. To get an instance of this wrapper us {@link #createForParent(Object, BiConsumer)}.
     *
     * @param parentBuilder               A builder of type {@code P} that creates this builder.
     * @param commandMessageHandlerSetter A {@link BiConsumer} that takes the parent and a {@link MessageHandler}
     *                                    that will be built with the wrapped
     *                                    {@link CommandMessageHandler.Builder} and sets the latter in the former.
     */
    private CommandMessageHandlerBuilderWrapper(
            final P parentBuilder,
            final BiConsumer<P, CommandMessageHandler> commandMessageHandlerSetter) {
        this.parentBuilder = parentBuilder;
        this.commandMessageHandlerSetter = commandMessageHandlerSetter;
        this.commandMessageHandlerBuilder = CommandMessageHandler.Builder.create();
    }


    /**
     * Builds the configured {@link CommandMessageHandler}.
     *
     * @return The created {@link CommandMessageHandler}.
     */
    private CommandMessageHandler build() {
        return commandMessageHandlerBuilder.build();
    }

    /**
     * Configures a {@link MessageHandler} for the given {@code command}.
     *
     * @param command The command to be handled by the given {@code handler}.
     * @param handler The {@link MessageHandler} that will handle {@link Message}s of the given {@code command}.
     * @return {@code this} for method chaining.
     */
    public CommandMessageHandlerBuilderWrapper<P> handleCommandWith(final String command, final MessageHandler handler) {
        commandMessageHandlerBuilder.handleCommandWith(command, handler);
        return this;
    }

    /**
     * Replaces all the configured {@link MessageHandler}s with the given {@code messageHandlers}.
     *
     * @param messageHandlers The new {@link MessageHandler}s.
     * @return {@code this} for method chaining.
     */
    public CommandMessageHandlerBuilderWrapper<P> replaceHandlers(final Map<String, MessageHandler> messageHandlers) {
        commandMessageHandlerBuilder.replaceHandlers(messageHandlers);
        return this;
    }

    /**
     * Configures all the given {@code messageHandlers}.
     *
     * @param messageHandlers The {@link MessageHandler}s to be configured.
     * @return {@code this} for method chaining.
     */
    public CommandMessageHandlerBuilderWrapper<P> addHandlers(final Map<String, MessageHandler> messageHandlers) {
        commandMessageHandlerBuilder.addHandlers(messageHandlers);
        return this;
    }

    /**
     * Configures the default {@link MessageHandler}
     * (will handle command {@link Message}s that have no {@link MessageHandler} configured).
     *
     * @param defaultMessageHandler The default {@link MessageHandler}.
     * @return {@code this} for method chaining.
     */
    public CommandMessageHandlerBuilderWrapper<P> defaultHandler(final MessageHandler defaultMessageHandler) {
        commandMessageHandlerBuilder.defaultHandler(defaultMessageHandler);
        return this;
    }

    /**
     * Clears this builder wrapper.
     *
     * @return {@code this} for method chaining.
     */
    public CommandMessageHandlerBuilderWrapper<P> clear() {
        commandMessageHandlerBuilder.clear();
        return this;
    }


    /**
     * Continues with the builder of type {@code P}, calling the {@link #commandMessageHandlerBuilder} with
     * the parent builder and the {@link CommandMessageHandler} that is created by this builder wrapper.
     *
     * @return The parent builder this builder wrapper.
     */
    public P continueWithParentBuilder() {
        this.commandMessageHandlerSetter.accept(parentBuilder, build());
        return parentBuilder;
    }


    /**
     * Creates a {@link CommandMessageHandlerBuilderWrapper} of type {@code T}.
     *
     * @param parentBuilder        A builder of type {@code T} that creates this builder.
     * @param commandHandlerSetter A {@link BiConsumer} that takes the parent and a {@link MessageHandler}
     *                             that will be built with the wrapped
     *                             {@link CommandMessageHandler.Builder}, and sets the latter in the former.
     * @param <T>                  The concrete type of parent builder.
     * @return The created {@link CommandMessageHandlerBuilderWrapper}.
     */
    public static <T> CommandMessageHandlerBuilderWrapper<T> createForParent(
            final T parentBuilder,
            final BiConsumer<T, CommandMessageHandler> commandHandlerSetter) {
        return new CommandMessageHandlerBuilderWrapper<>(parentBuilder, commandHandlerSetter);
    }
}

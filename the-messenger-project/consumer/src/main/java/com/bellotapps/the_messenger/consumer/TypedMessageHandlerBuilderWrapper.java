package com.bellotapps.the_messenger.consumer;

import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.MessageType;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * An adapter of a {@link TypedMessageHandler.Builder} to be used in the context of a parent builder.
 *
 * @param <P> The concrete type of parent builder.
 */
public final class TypedMessageHandlerBuilderWrapper<P> {

    /**
     * A builder of type {@code P} that creates this builder.
     */
    private final P parentBuilder;

    /**
     * The {@link TypedMessageHandler.Builder} being wrapped (the building process is delegated to it).
     */
    private final TypedMessageHandler.Builder typedMessageHandlerBuilder;

    /**
     * A {@link BiConsumer} that takes the parent and a {@link MessageHandler} that will be built with the wrapped
     * {@link TypedMessageHandler.Builder} and sets the latter in the former.
     */
    private final BiConsumer<P, TypedMessageHandler> typedMessageHandlerSetter;


    /**
     * Private Constructor. To get an instance of this wrapper us {@link #createForParent(Object, BiConsumer)}.
     *
     * @param parentBuilder             A builder of type {@code P} that creates this builder.
     * @param typedMessageHandlerSetter A {@link BiConsumer} that takes the parent and a {@link MessageHandler}
     *                                  that will be built with the wrapped
     *                                  * {@link TypedMessageHandler.Builder} and sets the latter in the former.
     */
    private TypedMessageHandlerBuilderWrapper(
            final P parentBuilder,
            final BiConsumer<P, TypedMessageHandler> typedMessageHandlerSetter) {
        this.parentBuilder = parentBuilder;
        this.typedMessageHandlerSetter = typedMessageHandlerSetter;
        this.typedMessageHandlerBuilder = TypedMessageHandler.Builder.create();
    }


    /**
     * Builds the configured {@link TypedMessageHandler}.
     *
     * @return The created {@link TypedMessageHandler}.
     */
    private TypedMessageHandler build() {
        return typedMessageHandlerBuilder.build();
    }


    /**
     * Configures a {@link MessageHandler} for the given {@code type}.
     *
     * @param type    The type to be handled by the given {@code handler}.
     * @param handler The {@link MessageHandler} that will handle {@link Message}s of the given {@code type}.
     * @return {@code this} for method chaining.
     */
    public TypedMessageHandlerBuilderWrapper<P> handleTypeWith(final String type, final MessageHandler handler) {
        typedMessageHandlerBuilder.handleTypeWith(type, handler);
        return this;
    }

    /**
     * Sets a {@link MessageHandler} for simple {@link Message}s.
     *
     * @param messageHandler A {@link MessageHandler} for simple {@link Message}s.
     * @return {@code this} for method chaining.
     * @see MessageType#SIMPLE
     */
    public TypedMessageHandlerBuilderWrapper<P> handleSimpleMessageWith(final MessageHandler messageHandler) {
        typedMessageHandlerBuilder.handleSimpleMessageWith(messageHandler);
        return this;
    }

    /**
     * Sets a {@link MessageHandler} for reply {@link Message}s.
     *
     * @param messageHandler A {@link MessageHandler} for reply {@link Message}s.
     * @return {@code this} for method chaining.
     * @see MessageType#REPLY
     */
    public TypedMessageHandlerBuilderWrapper<P> handleReplyMessageWith(final MessageHandler messageHandler) {
        typedMessageHandlerBuilder.handleReplyMessageWith(messageHandler);
        return this;
    }

    /**
     * Sets a {@link MessageHandler} for command {@link Message}s.
     *
     * @param messageHandler A {@link MessageHandler} for reply {@link Message}s.
     * @return {@code this} for method chaining.
     * @see MessageType#COMMAND
     */
    public TypedMessageHandlerBuilderWrapper<P> handleCommandMessageWith(final MessageHandler messageHandler) {
        typedMessageHandlerBuilder.handleCommandMessageWith(messageHandler);
        return this;
    }

    /**
     * Replaces all the configured {@link MessageHandler}s with the given {@code messageHandlers}.
     *
     * @param messageHandlers The new {@link MessageHandler}s.
     * @return {@code this} for method chaining.
     */
    public TypedMessageHandlerBuilderWrapper<P> replaceHandlers(final Map<String, MessageHandler> messageHandlers) {
        typedMessageHandlerBuilder.replaceHandlers(messageHandlers);
        return this;
    }

    /**
     * Configures all the given {@code messageHandlers}.
     *
     * @param messageHandlers The {@link MessageHandler}s to be configured.
     * @return {@code this} for method chaining.
     */
    public TypedMessageHandlerBuilderWrapper<P> addHandlers(final Map<String, MessageHandler> messageHandlers) {
        typedMessageHandlerBuilder.addHandlers(messageHandlers);
        return this;
    }

    /**
     * Configures the default {@link MessageHandler}
     * (will handle typed {@link Message}s whose type has no {@link MessageHandler} configured).
     *
     * @param defaultMessageHandler The default {@link MessageHandler}.
     * @return {@code this} for method chaining.
     */
    public TypedMessageHandlerBuilderWrapper<P> defaultHandler(final MessageHandler defaultMessageHandler) {
        typedMessageHandlerBuilder.defaultHandler(defaultMessageHandler);
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
    public CommandMessageHandlerBuilderWrapper<TypedMessageHandlerBuilderWrapper<P>> configureCommandMessageHandlers() {
        return CommandMessageHandlerBuilderWrapper
                .createForParent(this, TypedMessageHandlerBuilderWrapper::handleCommandMessageWith);
    }

    /**
     * Clears this builder.
     *
     * @return {@code this} for method chaining.
     */
    public TypedMessageHandlerBuilderWrapper<P> clear() {
        typedMessageHandlerBuilder.clear();
        return this;
    }


    /**
     * Continues with the builder of type {@code P}, calling the {@link #typedMessageHandlerBuilder} with
     * the parent builder and the {@link TypedMessageHandler} that is created by this builder wrapper.
     *
     * @return The parent builder this builder wrapper.
     */
    public P continueWithParentBuilder() {
        this.typedMessageHandlerSetter.accept(parentBuilder, build());
        return parentBuilder;
    }


    /**
     * Creates a {@link TypedMessageHandlerBuilderWrapper} of type {@code T}.
     *
     * @param parentBuilder      A builder of type {@code T} that creates this builder.
     * @param typedHandlerSetter A {@link BiConsumer} that takes the parent and a {@link MessageHandler}
     *                           that will be built with the wrapped
     *                           {@link CommandMessageHandler.Builder}, and sets the latter in the former.
     * @param <T>                The concrete type of parent builder.
     * @return The created {@link CommandMessageHandlerBuilderWrapper}.
     */
    public static <T> TypedMessageHandlerBuilderWrapper<T> createForParent(
            final T parentBuilder,
            final BiConsumer<T, TypedMessageHandler> typedHandlerSetter) {
        return new TypedMessageHandlerBuilderWrapper<>(parentBuilder, typedHandlerSetter);
    }
}

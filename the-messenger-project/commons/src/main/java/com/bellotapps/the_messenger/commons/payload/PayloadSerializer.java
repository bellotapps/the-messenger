package com.bellotapps.the_messenger.commons.payload;

/**
 * Defines behaviour for an object that can serialize payload objects of type {@code T} into {@link String}s.
 *
 * @param <T> Concrete type of object to be serialized.
 */
public interface PayloadSerializer<T> {

    /**
     * Serializes the given {@code object}.
     *
     * @param object The object to be serialized.
     * @return A {@link String} representation of the given {@code object}.
     * @throws PayloadSerializationException If any error occurs while serializing the given {@code object}.
     */
    String serialize(final T object) throws PayloadSerializationException;
}

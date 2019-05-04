package com.bellotapps.the_messenger.commons.data_transfer;

/**
 * Defines behaviour for an object that can deserialize {@link String}s into objects of type {@code T}.
 *
 * @param <T> Concrete type of object to be created from the {@link String} to be deserialized.
 */
public interface Deserializer<T> {

    /**
     * Deserializes the given {@code string}.
     *
     * @param string The {@link String} to be deserialized.
     * @return An object of type {@code T} created from the given {@code string}.
     * @throws DeserializationException If any error occurs while deserializing the given {@code string}.
     */
    T deserialize(final String string) throws DeserializationException;
}

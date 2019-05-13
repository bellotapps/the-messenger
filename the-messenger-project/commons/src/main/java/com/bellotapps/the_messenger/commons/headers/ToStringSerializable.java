package com.bellotapps.the_messenger.commons.headers;

/**
 * Defines behaviour for an object that can be serialized into a {@link String}.
 * Mostly used for headers and payload of {@link com.bellotapps.the_messenger.commons.Message}s.
 */
public interface ToStringSerializable {

    /**
     * Serializes into a {@link String}.
     *
     * @return A {@link String} representation of the implementor of this interface.
     */
    String serialize();
}

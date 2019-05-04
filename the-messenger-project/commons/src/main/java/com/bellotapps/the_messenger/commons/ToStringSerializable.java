package com.bellotapps.the_messenger.commons;

/**
 * Defines behaviour for an object that can be serializable to a {@link String}.
 */
public interface ToStringSerializable {

    /**
     * Serializes into a {@link String}.
     *
     * @return A {@link String} representation of the implementor of this interface.
     */
    String serialize();
}

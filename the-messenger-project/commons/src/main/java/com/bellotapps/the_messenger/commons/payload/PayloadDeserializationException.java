package com.bellotapps.the_messenger.commons.payload;

/**
 * A {@link RuntimeException} to be thrown when a payload deserialization errors occur.
 */
public class PayloadDeserializationException extends RuntimeException {

    /**
     * The {@link String} that was tried to be deserialized.
     */
    private final String stringToDeserialize;

    /**
     * {@link Class} to be instantiated when deserializing.
     */
    private final Class<?> classToInstantiate;

    /**
     * Constructor.
     *
     * @param stringToDeserialize The {@link String} that was tried to be deserialized.
     * @param classToInstantiate  {@link Class} to be instantiated when deserializing.
     */
    public PayloadDeserializationException(final String stringToDeserialize, final Class<?> classToInstantiate) {
        super("Could not deserialize the string: " + stringToDeserialize
                + " into an object of class " + classToInstantiate);
        this.stringToDeserialize = stringToDeserialize;
        this.classToInstantiate = classToInstantiate;
    }

    /**
     * Constructor in which a message can be set.
     *
     * @param stringToDeserialize The {@link String} that was tried to be deserialized.
     * @param classToInstantiate  {@link Class} to be instantiated when deserializing.
     * @param message             The detail message, which can be retrieved by the {@link #getMessage()} method.
     */
    public PayloadDeserializationException(
            final String stringToDeserialize,
            final Class<?> classToInstantiate,
            final String message) {
        super(message);
        this.stringToDeserialize = stringToDeserialize;
        this.classToInstantiate = classToInstantiate;
    }

    /**
     * Constructor in which a message and a cause can be set.
     *
     * @param stringToDeserialize The {@link String} that was tried to be deserialized.
     * @param classToInstantiate  {@link Class} to be instantiated when deserializing.
     * @param message             The detail message, which can be retrieved by the {@link #getMessage()} method.
     * @param cause               The {@link Throwable} that causes this exception to be thrown.
     *                            Can be retrieved by the {@link #getCause()} method.
     */
    public PayloadDeserializationException(
            final String stringToDeserialize,
            final Class<?> classToInstantiate,
            final String message,
            final Throwable cause) {
        super("Could not deserialize the string: " + stringToDeserialize + ". Message: " + message, cause);
        this.stringToDeserialize = stringToDeserialize;
        this.classToInstantiate = classToInstantiate;
    }

    /**
     * Constructor in which a cause can be set.
     *
     * @param stringToDeserialize The {@link String} that was tried to be deserialized.
     * @param cause               The {@link Throwable} that causes this exception to be thrown.
     *                            Can be retrieved by the {@link #getCause()} method.
     * @param classToInstantiate  {@link Class} to be instantiated when deserializing.
     * @since 1.4
     */
    public PayloadDeserializationException(
            final String stringToDeserialize,
            final Class<?> classToInstantiate,
            final Throwable cause) {
        super("Could not deserialize the string: " + stringToDeserialize
                + " into an object of class " + classToInstantiate, cause);
        this.stringToDeserialize = stringToDeserialize;
        this.classToInstantiate = classToInstantiate;
    }

    /**
     * @return The {@link String} that was tried to be deserialized.
     */
    public String getStringToDeserialize() {
        return stringToDeserialize;
    }

    /**
     * @return {@link Class} to be instantiated when deserializing.
     */
    public Class<?> getClassToInstantiate() {
        return classToInstantiate;
    }
}

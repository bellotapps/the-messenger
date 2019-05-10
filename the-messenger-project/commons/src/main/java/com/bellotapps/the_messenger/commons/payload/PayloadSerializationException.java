package com.bellotapps.the_messenger.commons.payload;

/**
 * A {@link RuntimeException} to be thrown when a payload serialization errors occur.
 */
public class PayloadSerializationException extends RuntimeException {

    /**
     * The class that was tried to be serialized.
     */
    private final Class<?> classToSerialize;

    /**
     * Constructor.
     *
     * @param classToSerialize The class that was tried to be serialized.
     */
    public PayloadSerializationException(final Class<?> classToSerialize) {
        super("Could not serialize instance of type: " + classToSerialize.getName());
        this.classToSerialize = classToSerialize;
    }

    /**
     * Constructor in which a message can be set.
     *
     * @param classToSerialize The class that was tried to be serialized.
     * @param message          The detail message, which can be retrieved by the {@link #getMessage()} method.
     */
    public PayloadSerializationException(final Class<?> classToSerialize, final String message) {
        super(message);
        this.classToSerialize = classToSerialize;
    }

    /**
     * Constructor in which a message and a cause can be set.
     *
     * @param classToSerialize The class that was tried to be serialized.
     * @param message          The detail message, which can be retrieved by the {@link #getMessage()} method.
     * @param cause            The {@link Throwable} that causes this exception to be thrown.
     *                         Can be retrieved by the {@link #getCause()} method.
     */
    public PayloadSerializationException(final Class<?> classToSerialize, final String message, final Throwable cause) {
        super("Could not serialize instance of type: " + classToSerialize.getName() + ". Message: " + message, cause);
        this.classToSerialize = classToSerialize;
    }

    /**
     * Constructor in which a cause can be set.
     *
     * @param classToSerialize The class that was tried to be serialized.
     * @param cause            The {@link Throwable} that causes this exception to be thrown.
     *                         Can be retrieved by the {@link #getCause()} method.
     * @since 1.4
     */
    public PayloadSerializationException(final Class<?> classToSerialize, final Throwable cause) {
        super("Could not serialize instance of type: " + classToSerialize.getName(), cause);
        this.classToSerialize = classToSerialize;
    }

    /**
     * @return The class that was tried to be serialized.
     */
    public Class<?> getClassToSerialize() {
        return classToSerialize;
    }
}

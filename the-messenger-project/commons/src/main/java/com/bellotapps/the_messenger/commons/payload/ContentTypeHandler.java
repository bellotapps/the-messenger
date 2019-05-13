package com.bellotapps.the_messenger.commons.payload;

/**
 * Defines behaviour for object that can handle a content type.
 */
public interface ContentTypeHandler {

    /**
     * Indicates the content type that the implementor can handle.
     *
     * @return The content type that the implementor can handle.
     */
    String contentType();
}

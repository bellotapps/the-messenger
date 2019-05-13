package com.bellotapps.the_messenger.json;

import com.bellotapps.the_messenger.commons.headers.ContentType;
import com.bellotapps.the_messenger.commons.payload.ContentTypeHandler;

/**
 * An extension of {@link ContentTypeHandler} that returns JSON as Content-Type.
 */
public interface JsonContentTypeHandler extends ContentTypeHandler {

    @Override
    default String contentType() {
        return ContentType.JSON.serialize();
    }
}

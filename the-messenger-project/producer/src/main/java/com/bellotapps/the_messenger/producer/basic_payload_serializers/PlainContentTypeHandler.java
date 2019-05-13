package com.bellotapps.the_messenger.producer.basic_payload_serializers;

import com.bellotapps.the_messenger.commons.headers.ContentType;
import com.bellotapps.the_messenger.commons.payload.ContentTypeHandler;

/**
 * An extension of {@link ContentTypeHandler} that returns Plain as Content-Type.
 */
public interface PlainContentTypeHandler extends ContentTypeHandler {

    @Override
    default String contentType() {
        return ContentType.PLAIN.serialize();
    }
}

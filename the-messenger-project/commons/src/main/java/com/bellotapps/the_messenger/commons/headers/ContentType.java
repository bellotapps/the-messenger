package com.bellotapps.the_messenger.commons.headers;

import com.bellotapps.the_messenger.commons.ToStringSerializable;

/**
 * Enum containing well-known content types.
 *
 * @see DefinedHeader#CONTENT_TYPE
 */
public enum ContentType implements ToStringSerializable {
    /**
     * JSON content type.
     */
    JSON {
        @Override
        public String serialize() {
            return "JSON";
        }
    },
    ;
}

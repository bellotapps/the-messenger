package com.bellotapps.the_messenger.commons.headers;

/**
 * Enum containing well-known content types.
 *
 * @see DefinedHeader#CONTENT_TYPE
 */
public enum ContentType implements ToStringSerializable {
    /**
     * Plain text content type.
     */
    PLAIN {
        @Override
        public String serialize() {
            return "Plain";
        }
    },
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

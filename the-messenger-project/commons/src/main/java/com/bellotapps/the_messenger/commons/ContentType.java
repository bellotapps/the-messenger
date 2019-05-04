package com.bellotapps.the_messenger.commons;

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

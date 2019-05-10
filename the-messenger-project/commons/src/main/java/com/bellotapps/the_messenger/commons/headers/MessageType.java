package com.bellotapps.the_messenger.commons.headers;

import com.bellotapps.the_messenger.commons.ToStringSerializable;

/**
 * Enum containing well-known message types.
 *
 * @see DefinedHeader#MESSAGE_TYPE
 */
public enum MessageType implements ToStringSerializable {
    /**
     * A simple message.
     */
    SIMPLE {
        @Override
        public String serialize() {
            return "Simple";
        }
    },
    /**
     * A reply message.
     * Messages of this type must include a {@link DefinedHeader#REPLIES_TO} header.
     *
     * @see DefinedHeader#REPLIES_TO
     */
    REPLY {
        @Override
        public String serialize() {
            return "Reply";
        }
    },
    /**
     * A message containing a command to be executed. The sender expects a reply.
     * Messages of this type must include a {@link DefinedHeader#COMMAND} header.
     *
     * @see DefinedHeader#COMMAND
     */
    COMMAND {
        @Override
        public String serialize() {
            return "Command";
        }
    },
    ;
}

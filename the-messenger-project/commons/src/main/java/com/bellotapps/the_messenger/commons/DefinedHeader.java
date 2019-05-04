package com.bellotapps.the_messenger.commons;

/**
 * Enum containing well-known headers.
 */
public enum DefinedHeader implements ToStringSerializable {
    /**
     * Indicates the type of message
     *
     * @see MessageType
     */
    MESSAGE_TYPE {
        @Override
        public String serialize() {
            return "Message-Type";
        }
    },
    /**
     * Indicates the type of content of the message (i.e the payload).
     *
     * @see ContentType
     */
    CONTENT_TYPE {
        @Override
        public String serialize() {
            return "Content-Type";
        }
    },
    /**
     * Indicates the id of the message being replied.
     *
     * @see MessageType#REPLY
     */
    REPLIES_TO {
        @Override
        public String serialize() {
            return "Replies-To";
        }
    },
    /**
     * Indicates the command being requested to execute.
     *
     * @see MessageType#COMMAND
     */
    COMMAND {
        @Override
        public String serialize() {
            return "Command";
        }
    },
    /**
     * Indicates a list of headers to be copied in the reply message.
     * The format MUST be &lt;header&gt;[,&lt;header&gt;]*, where each &lt;header&gt; is a header
     * to be copied to the reply.
     */
    COPY_HEADERS {
        @Override
        public String serialize() {
            return "Copy-Headers";
        }
    },
    ;
}

/*
 * Copyright 2019 BellotApps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bellotapps.the_messenger.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * A concrete implementation of a {@link Message}.
 */
public abstract class AbstractMessage implements Message {

    /**
     * The message's id.
     */
    private final String id;

    /**
     * An identification of the sender.
     * This allows the recipient to know who has sent the message.
     */
    private final String sender;

    /**
     * The timestamp of the message.
     */
    private final Instant timestamp;

    /**
     * The message headers.
     */
    private final Map<String, String> headers;

    /**
     * The message payload.
     */
    private final String payload;


    /**
     * Private constructor. Use
     *
     * @param id        The message's id.
     * @param sender    An identification of the sender. This allows the recipient to know who has sent the message.
     * @param timestamp The timestamp of the message.
     * @param headers   The message headers.
     * @param payload   The message payload.
     * @throws IllegalArgumentException If any argument is invalid.
     */
    protected AbstractMessage(
            final String id,
            final String sender,
            final Instant timestamp,
            final Map<String, String> headers,
            final String payload) throws IllegalArgumentException {
        Validate.isTrue(StringUtils.isNotBlank(id), "The id must have text");
        Validate.isTrue(StringUtils.isNotBlank(sender), "The sender must have text");
        Validate.isTrue(timestamp != null, "The timestamp must not be null");
        Validate.isTrue(headers != null, "The headers map must not be null");
        Validate.isTrue(
                headers.entrySet().stream().noneMatch(e -> StringUtils.isAnyBlank(e.getKey(), e.getValue())),
                "All the headers key and value must have text"
        );
        this.id = id;
        this.sender = sender;
        this.timestamp = timestamp;
        this.headers = Collections.unmodifiableMap(headers);
        this.payload = payload;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getPayload() {
        return payload;
    }
}

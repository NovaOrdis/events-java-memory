/*
 * Copyright (c) 2017 Nova Ordis LLC
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

package io.novaordis.events.java.memory.gc.parallel;

import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.java.memory.gc.GCEventType;
import io.novaordis.events.java.memory.gc.GCParsingException;
import io.novaordis.events.java.memory.gc.g1.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class ParallelGCFullCollection extends ParallelGCEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COLLECTION_TRIGGER_PROPERTY_NAME = "trigger";

    private static final Logger log = LoggerFactory.getLogger(ParallelGCFullCollection.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParallelGCFullCollection(Time time, ParallelGCEventPayload preParsedContent) throws GCParsingException {

        super(preParsedContent.getLineNumber(),
                preParsedContent.getPositionInLine(),
                time, preParsedContent.getTrigger(),
                preParsedContent.getFirstSquareBracketedSegment());

        setType(ParallelGCEventType.FULL_COLLECTION);

        if (log.isDebugEnabled()) { log.debug(this + " constructed"); }
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    // GCEventBase implementation --------------------------------------------------------------------------------------

    @Override
    protected void validateEventType(GCEventType type) {

        if (type == null || ParallelGCEventType.FULL_COLLECTION.equals(type)) {

            return;
        }

        throw new IllegalArgumentException(type + " is not a valid event type for " + this);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public ParallelGCCollectionTrigger getCollectionTrigger() {

        StringProperty p = getStringProperty(COLLECTION_TRIGGER_PROPERTY_NAME);

        if (p == null) {

            return null;
        }

        String value = p.getString();

        ParallelGCCollectionTrigger t = ParallelGCCollectionTrigger.fromExternalValue(value);

        if (t == null) {

            //
            // the stored value was not recognized
            //

            throw new IllegalStateException("\"" + value + "\" is not a valid GC collection trigger");
        }

        return t;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @param trigger null removes the underlying property.
     */
    void setCollectionTrigger(ParallelGCCollectionTrigger trigger) {

        //
        // maintained as a String property where the value is the externalized format of the enum
        //

        if (trigger == null) {

            removeStringProperty(COLLECTION_TRIGGER_PROPERTY_NAME);
        }
        else {

            setStringProperty(COLLECTION_TRIGGER_PROPERTY_NAME, trigger.toExternalValue());
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}

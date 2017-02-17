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

package io.novaordis.events.gc.g1;

import io.novaordis.events.api.event.BooleanProperty;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.api.gc.GCEventType;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/16/17
 */
public class G1Collection extends G1Event {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COLLECTION_TRIGGER_PROPERTY_NAME = "trigger";
    public static final String COLLECTION_SCOPE_PROPERTY_NAME = "scope";
    public static final String INITIAL_MARK_PROPERTY_NAME = "initial-mark";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public G1Collection(Long lineNumber, Time time, G1CollectionTrigger trigger) {

        super(lineNumber, time);
        setType(G1EventType.COLLECTION);
        setCollectionTrigger(trigger);
        setCollectionScope(G1CollectionScope.YOUNG);
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    /**
     * We override this because we want to protect against changing the type to anything else but a COLLECTION
     */
    @Override
    protected void setType(GCEventType type) {

        if (G1EventType.COLLECTION.equals(type)) {

            super.setType(G1EventType.COLLECTION);
        }
        else {

            throw new IllegalArgumentException("cannot set type to anything else but " + G1EventType.COLLECTION);
        }
    }

    @Override
    public boolean isCollection() {

        return true;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public G1CollectionTrigger getCollectionTrigger() {

        StringProperty p = getStringProperty(COLLECTION_TRIGGER_PROPERTY_NAME);

        if (p == null) {

            return null;
        }

        String value = p.getString();

        G1CollectionTrigger t = G1CollectionTrigger.fromExternalValue(value);

        if (t == null) {

            //
            // the stored value was not recognized
            //

            throw new IllegalStateException("\"" + value + "\" is not a valid GC collection trigger");
        }

        return t;
    }

    public G1CollectionScope getCollectionScope() {

        StringProperty p = getStringProperty(COLLECTION_SCOPE_PROPERTY_NAME);

        if (p == null) {

            return null;
        }

        String value = p.getString();

        if (G1CollectionScope.YOUNG.toString().equals(value)) {

            return G1CollectionScope.YOUNG;
        }
        else if (G1CollectionScope.MIXED.toString().equals(value)) {

            return G1CollectionScope.MIXED;
        }
        else {

            throw new IllegalStateException("\"" + value + "\" is not a valid GC collection scope");
        }
    }

    /**
     * Some of the G1 collection events, such as a regular young collection, or a metadata threshold initiated
     * collections can also trigger as concurrent cycle initial marks.
     *
     * @return true if this is an "concurrent cycle initial mark" event, false otherwise.
     */
    public boolean isInitialMark() {

        BooleanProperty p = getBooleanProperty(INITIAL_MARK_PROPERTY_NAME);

        if (p == null) {

            return false;
        }

        return p.getBoolean();
    }

    @Override
    public String toString() {

        String s = "COLLECTION (" + getCollectionTrigger() + ", " + getCollectionScope().getLogMarker();

        if (isInitialMark()) {

            s += " , initial-mark)";
        }
        else {

            s += ")";
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @param trigger null removes the underlying property.
     */
    void setCollectionTrigger(G1CollectionTrigger trigger) {

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

    void setCollectionScope(G1CollectionScope scope) {

        //
        // maintained as a String property where the value is the externalized format of the enum
        //

        if (scope == null) {

            removeStringProperty(COLLECTION_SCOPE_PROPERTY_NAME);
        }
        else {

            setStringProperty(COLLECTION_SCOPE_PROPERTY_NAME, scope.toString());
        }
    }

    void setInitialMark(boolean b) {

        setBooleanProperty(INITIAL_MARK_PROPERTY_NAME, b);
    }


    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
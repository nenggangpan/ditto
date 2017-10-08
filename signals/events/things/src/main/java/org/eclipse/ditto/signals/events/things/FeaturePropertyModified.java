/*
 * Copyright (c) 2017 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * Contributors:
 *    Bosch Software Innovations GmbH - initial contribution
 */
package org.eclipse.ditto.signals.events.things;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonField;
import org.eclipse.ditto.json.JsonFieldDefinition;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonObjectBuilder;
import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.json.JsonValue;
import org.eclipse.ditto.model.base.headers.DittoHeaders;
import org.eclipse.ditto.model.base.json.FieldType;
import org.eclipse.ditto.model.base.json.JsonSchemaVersion;
import org.eclipse.ditto.signals.base.WithFeatureId;
import org.eclipse.ditto.signals.events.base.EventJsonDeserializer;

/**
 * This event is emitted after a Property of a Feature's {@link org.eclipse.ditto.model.things.FeatureProperties} was
 * modified.
 */
@Immutable
public final class FeaturePropertyModified extends AbstractThingEvent<FeaturePropertyModified> implements
        ThingModifiedEvent<FeaturePropertyModified>, WithFeatureId {

    /**
     * Name of the "Feature Property Modified" event.
     */
    public static final String NAME = "featurePropertyModified";

    /**
     * Type of this event.
     */
    public static final String TYPE = TYPE_PREFIX + NAME;

    static final JsonFieldDefinition JSON_PROPERTY =
            JsonFactory.newFieldDefinition("property", String.class, FieldType.REGULAR,
                    // available in schema versions:
                    JsonSchemaVersion.V_1,
                    JsonSchemaVersion.V_2);

    static final JsonFieldDefinition JSON_VALUE =
            JsonFactory.newFieldDefinition("value", JsonValue.class, FieldType.REGULAR,
                    // available in schema versions:
                    JsonSchemaVersion.V_1,
                    JsonSchemaVersion.V_2);

    private final String featureId;
    private final JsonPointer propertyPointer;
    private final JsonValue propertyValue;

    private FeaturePropertyModified(final String thingId, final String featureId, final JsonPointer propertyPointer,
            final JsonValue propertyValue, final long revision, final Instant timestamp,
            final DittoHeaders dittoHeaders) {
        super(TYPE, thingId, revision, timestamp, dittoHeaders);
        this.featureId = requireNonNull(featureId, "The Feature ID must not be null!");
        this.propertyPointer =
                Objects.requireNonNull(propertyPointer, "The Property JSON Pointer must not be null!");
        this.propertyValue = Objects.requireNonNull(propertyValue, "The Property Value must not be null!");
    }

    /**
     * Constructs a new {@code FeaturePropertyModified} object.
     *
     * @param thingId the ID of the Thing whose Feature's Property was modified.
     * @param featureId the ID of the Feature whose Property was modified.
     * @param propertyJsonPointer the JSON pointer of the modified Property key.
     * @param propertyValue the value of the modified Property.
     * @param revision the revision of the Thing.
     * @param dittoHeaders the headers of the command which was the cause of this event.
     * @return the FeaturePropertyModified created.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public static FeaturePropertyModified of(final String thingId, final String featureId,
            final JsonPointer propertyJsonPointer, final JsonValue propertyValue, final long revision,
            final DittoHeaders dittoHeaders) {
        return of(thingId, featureId, propertyJsonPointer, propertyValue, revision, null,
                dittoHeaders);
    }

    /**
     * Constructs a new {@code FeaturePropertyModified} object.
     *
     * @param thingId the ID of the Thing whose Feature's Property was modified.
     * @param featureId the ID of the Feature whose Property was modified.
     * @param propertyJsonPointer the JSON pointer of the modified Property key.
     * @param propertyValue the value of the modified Property.
     * @param revision the revision of the Thing.
     * @param timestamp the timestamp of this event.
     * @param dittoHeaders the headers of the command which was the cause of this event.
     * @return the FeaturePropertyModified created.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public static FeaturePropertyModified of(final String thingId, final String featureId,
            final JsonPointer propertyJsonPointer, final JsonValue propertyValue, final long revision,
            final Instant timestamp, final DittoHeaders dittoHeaders) {
        return new FeaturePropertyModified(thingId, featureId, propertyJsonPointer, propertyValue, revision, timestamp,
                dittoHeaders);
    }

    /**
     * Creates a new {@code FeaturePropertyModified} from a JSON string.
     *
     * @param jsonString the JSON string of which a new FeaturePropertyModified instance is to be created.
     * @param dittoHeaders the headers of the command which was the cause of this event.
     * @return the {@code FeaturePropertyModified} which was created from the given JSON string.
     * @throws NullPointerException if {@code jsonString} is {@code null}.
     * @throws IllegalArgumentException if {@code jsonString} is newInstance.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonString} was not in the expected
     * 'FeaturePropertyModified' format.
     */
    public static FeaturePropertyModified fromJson(final String jsonString, final DittoHeaders dittoHeaders) {
        return fromJson(JsonFactory.newObject(jsonString), dittoHeaders);
    }

    /**
     * Creates a new {@code FeaturePropertyModified} from a JSON object.
     *
     * @param jsonObject the JSON object from which a new FeaturePropertyModified instance is to be created.
     * @param dittoHeaders the headers of the command which was the cause of this event.
     * @return the {@code FeaturePropertyModified} which was created from the given JSON object.
     * @throws NullPointerException if {@code jsonObject} is {@code null}.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} was not in the expected
     * 'FeaturePropertyModified' format.
     */
    public static FeaturePropertyModified fromJson(final JsonObject jsonObject, final DittoHeaders dittoHeaders) {
        return new EventJsonDeserializer<FeaturePropertyModified>(TYPE, jsonObject)
                .deserialize((revision, timestamp, jsonObjectReader) -> {
                    final String extractedThingId = jsonObjectReader.get(JsonFields.THING_ID);
                    final String extractedFeatureId = jsonObjectReader.get(JsonFields.FEATURE_ID);
                    final String pointerString = jsonObjectReader.get(JSON_PROPERTY);
                    final JsonPointer extractedPointer = JsonFactory.newPointer(pointerString);
                    final JsonValue extractedValue = jsonObjectReader.get(JSON_VALUE);

                    return of(extractedThingId, extractedFeatureId, extractedPointer, extractedValue, revision,
                            timestamp,
                            dittoHeaders);
                });
    }

    @Override
    public String getFeatureId() {
        return featureId;
    }

    /**
     * Returns the JSON pointer of the Property to modify.
     *
     * @return the JSON pointer of the Property to modify.
     */
    public JsonPointer getPropertyPointer() {
        return propertyPointer;
    }

    /**
     * Returns the value of the Property to modify.
     *
     * @return the value of the Property to modify.
     */
    public JsonValue getPropertyValue() {
        return propertyValue;
    }

    @Override
    public Optional<JsonValue> getEntity(final JsonSchemaVersion schemaVersion) {
        return Optional.ofNullable(propertyValue);
    }

    @Override
    public JsonPointer getResourcePath() {
        final String path = "/features/" + featureId + "/properties" + propertyPointer;
        return JsonPointer.of(path);
    }

    @Override
    public FeaturePropertyModified setRevision(final long revision) {
        return of(getThingId(), featureId, propertyPointer, propertyValue, revision, getTimestamp().orElse(null),
                getDittoHeaders());
    }

    @Override
    public FeaturePropertyModified setDittoHeaders(final DittoHeaders dittoHeaders) {
        return of(getThingId(), featureId, propertyPointer, propertyValue, getRevision(), getTimestamp().orElse(null),
                dittoHeaders);
    }

    @Override
    protected void appendPayloadAndBuild(final JsonObjectBuilder jsonObjectBuilder,
            final JsonSchemaVersion schemaVersion, final Predicate<JsonField> thePredicate) {
        final Predicate<JsonField> predicate = schemaVersion.and(thePredicate);
        jsonObjectBuilder.set(JsonFields.FEATURE_ID, featureId, predicate);
        jsonObjectBuilder.set(JSON_PROPERTY, propertyPointer.toString(), predicate);
        jsonObjectBuilder.set(JSON_VALUE, propertyValue, predicate);
    }

    @SuppressWarnings("squid:S109")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hashCode(featureId);
        result = prime * result + Objects.hashCode(propertyPointer);
        result = prime * result + Objects.hashCode(propertyValue);
        return result;
    }

    @SuppressWarnings({"squid:MethodCyclomaticComplexity", "squid:S1067", "OverlyComplexMethod"})
    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FeaturePropertyModified that = (FeaturePropertyModified) o;
        return that.canEqual(this) && Objects.equals(featureId, that.featureId) && Objects
                .equals(propertyPointer, that.propertyPointer) && Objects.equals(propertyValue, that.propertyValue)
                && super.equals(that);
    }

    @Override
    protected boolean canEqual(final Object other) {
        return (other instanceof FeaturePropertyModified);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + super.toString() + ", featureId=" + featureId
                + ", propertyPointer=" + propertyPointer + ", propertyValue" + propertyValue + "]";
    }

}
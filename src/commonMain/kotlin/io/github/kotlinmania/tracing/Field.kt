// port-lint: source tracing/src/field.rs
package io.github.kotlinmania.tracing

/**
 * Trait implemented to allow a type to be used as a field key.
 */
public interface AsField {
    /**
     * Attempts to convert this key into a [Field] with the specified [metadata].
     */
    public fun asField(metadata: Metadata): Field?
}

/**
 * Returns this field if its callsite matches the given metadata, otherwise null.
 */
public fun Field.asField(metadata: Metadata): Field? =
    if (this.callsite == metadata.callsite) {
        this
    } else {
        null
    }

/**
 * Attempts to resolve a field by name within the metadata's fieldset.
 */
public fun String.asField(metadata: Metadata): Field? = metadata.fields.field(this)

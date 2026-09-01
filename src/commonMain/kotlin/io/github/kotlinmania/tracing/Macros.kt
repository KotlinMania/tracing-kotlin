// port-lint: source macros.rs
package io.github.kotlinmania.tracing

/**
 * Checks if the given metadata is enabled by the current subscriber.
 */
public fun enabled(metadata: Metadata): Boolean =
    getDefault { dispatch ->
        dispatch.subscriber.enabled(metadata)
    }

/**
 * Checks if the given level is enabled under the static max level and current subscriber.
 */
public fun enabled(level: Level, target: String = ""): Boolean {
    if (STATIC_MAX_LEVEL.contains(level)) {
        val meta =
            Metadata(
                name = "event",
                target = target,
                level = level,
                kind = Kind.EVENT,
            )
        return enabled(meta)
    }
    return false
}

/**
 * Emits a trace-level event.
 */
public fun trace(message: String, vararg fields: Pair<String, Any?>) {
    event(Level.TRACE, message, *fields)
}

/**
 * Emits a debug-level event.
 */
public fun debug(message: String, vararg fields: Pair<String, Any?>) {
    event(Level.DEBUG, message, *fields)
}

/**
 * Emits an info-level event.
 */
public fun info(message: String, vararg fields: Pair<String, Any?>) {
    event(Level.INFO, message, *fields)
}

/**
 * Emits a warn-level event.
 */
public fun warn(message: String, vararg fields: Pair<String, Any?>) {
    event(Level.WARN, message, *fields)
}

/**
 * Emits an error-level event.
 */
public fun error(message: String, vararg fields: Pair<String, Any?>) {
    event(Level.ERROR, message, *fields)
}

/**
 * Emits a tracing event with a specified level and message.
 */
public fun event(level: Level, message: String, vararg fields: Pair<String, Any?>, target: String = "") {
    val fieldNames = mutableListOf("message")
    val valueMap = mutableMapOf<String, Any?>("message" to message)
    for ((k, v) in fields) {
        fieldNames.add(k)
        valueMap[k] = v
    }
    val fieldSet = FieldSet(fieldNames)
    val meta =
        Metadata(
            name = "event",
            target = target,
            level = level,
            fields = fieldSet,
            kind = Kind.EVENT,
        )
    val vs = ValueSet(fieldSet, valueMap)
    val ev = Event(metadata = meta, isContextual = true, values = vs)
    getDefault { dispatch ->
        if (dispatch.subscriber.eventEnabled(ev)) {
            dispatch.subscriber.event(ev)
        }
    }
}

/**
 * Constructs a new span with the given level and name.
 */
public fun span(level: Level, name: String, vararg fields: Pair<String, Any?>, target: String = ""): Span {
    val fieldNames = fields.map { it.first }
    val valueMap = fields.toMap()
    val fieldSet = FieldSet(fieldNames)
    val meta =
        Metadata(
            name = name,
            target = target,
            level = level,
            fields = fieldSet,
            kind = Kind.SPAN,
        )
    val vs = ValueSet(fieldSet, valueMap)
    return Span.new(meta, vs)
}

/**
 * Constructs a new trace-level span.
 */
public fun traceSpan(name: String, vararg fields: Pair<String, Any?>): Span =
    span(Level.TRACE, name, *fields)

/**
 * Constructs a new debug-level span.
 */
public fun debugSpan(name: String, vararg fields: Pair<String, Any?>): Span =
    span(Level.DEBUG, name, *fields)

/**
 * Constructs a new info-level span.
 */
public fun infoSpan(name: String, vararg fields: Pair<String, Any?>): Span =
    span(Level.INFO, name, *fields)

/**
 * Constructs a new warn-level span.
 */
public fun warnSpan(name: String, vararg fields: Pair<String, Any?>): Span =
    span(Level.WARN, name, *fields)

/**
 * Constructs a new error-level span.
 */
public fun errorSpan(name: String, vararg fields: Pair<String, Any?>): Span =
    span(Level.ERROR, name, *fields)

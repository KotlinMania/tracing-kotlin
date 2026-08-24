// port-lint: source lib.rs
package io.github.kotlinmania.tracing

/**
 * Top-level tracing namespace module.
 */
public object Tracing {
    public const val VERSION: String = "0.1.41"
}

/**
 * Sealed interface marker for internal implementations.
 */
public interface Sealed

/**
 * Internal macro support helpers.
 */
public fun __isEnabled(metadata: Metadata, interest: Interest): Boolean =
    if (interest.isNever()) {
        false
    } else {
        getDefault { dispatch ->
            dispatch.subscriber.enabled(metadata)
        }
    }

/**
 * Internal helper to construct a disabled span.
 */
public fun __disabledSpan(metadata: Metadata): Span = Span.disabled(metadata)

/**
 * Internal helper to forward log events.
 */
public fun __tracingLog(logger: Any?, logMeta: Any?, values: ValueSet) {
    val sb = StringBuilder()
    val visitor = LogVisitor(sb)
    values.record(visitor)
}

/**
 * Helper to normalize field names with prefix or keywords.
 */
public class FieldName(
    private val name: String,
) {
    public fun asStr(): String = name

    public fun len(): Int = name.length

    public fun fmt(): String = "FieldName($name)"

    override fun toString(): String = fmt()

    public companion object {
        public fun new(input: String): FieldName =
            FieldName(input.replace("r#", ""))

        public fun len(input: String): Int =
            new(input).len()
    }
}

/**
 * Utility to format [ValueSet]s for logging.
 */
public class LogValueSet(
    public val values: ValueSet,
    public val isFirst: Boolean = true,
) {
    override fun toString(): String {
        val sb = StringBuilder()
        val visitor = LogVisitor(sb, isFirst)
        values.record(visitor)
        return sb.toString()
    }
}

/**
 * Visitor implementation for formatting log values.
 */
public class LogVisitor(
    private val out: StringBuilder,
    private var isFirst: Boolean = true,
) : Visit {
    override fun visit(name: String, value: Any?) {
        recordDebug(Field(name), value)
    }

    public fun recordDebug(field: Field, value: Any?) {
        if (isFirst) {
            isFirst = false
            if (field.name == "message") {
                out.append(value)
            } else {
                out.append(field.name).append("=").append(value)
            }
        } else {
            out.append(" ").append(field.name).append("=").append(value)
        }
    }

    public fun recordStr(field: Field, value: String) {
        recordDebug(field, value)
    }
}

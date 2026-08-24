package io.github.kotlinmania.tracing

/**
 * Severity level for spans and events.
 */
public enum class Level(
    public val priority: Int,
) : Comparable<Level> {
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4),
    TRACE(5),
    ;

    public companion object {
        /**
         * Parses a level from a string name.
         */
        public fun fromString(value: String): Level? =
            when (value.trim().lowercase()) {
                "trace" -> TRACE
                "debug" -> DEBUG
                "info" -> INFO
                "warn", "warning" -> WARN
                "error" -> ERROR
                else -> null
            }
    }
}

/**
 * Filter level indicating the maximum enabled verbosity.
 */
public enum class LevelFilter(
    public val priority: Int,
) : Comparable<LevelFilter> {
    OFF(0),
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4),
    TRACE(5),
    ;

    /**
     * Returns whether this filter contains the specified level.
     */
    public operator fun contains(level: Level): Boolean = this.priority >= level.priority

    /**
     * Converts this filter to its corresponding level if not OFF.
     */
    public fun toLevel(): Level? =
        when (this) {
            OFF -> null
            ERROR -> Level.ERROR
            WARN -> Level.WARN
            INFO -> Level.INFO
            DEBUG -> Level.DEBUG
            TRACE -> Level.TRACE
        }

    public companion object {
        /**
         * Converts a level to a level filter.
         */
        public fun fromLevel(level: Level): LevelFilter =
            when (level) {
                Level.TRACE -> TRACE
                Level.DEBUG -> DEBUG
                Level.INFO -> INFO
                Level.WARN -> WARN
                Level.ERROR -> ERROR
            }

        /**
         * Parses a level filter from a string representation.
         */
        public fun fromString(value: String): LevelFilter =
            when (value.trim().lowercase()) {
                "off" -> OFF
                "error", "1" -> ERROR
                "warn", "warning", "2" -> WARN
                "info", "3" -> INFO
                "debug", "4" -> DEBUG
                "trace", "5" -> TRACE
                else -> throw IllegalArgumentException("Invalid level filter: $value")
            }

        /**
         * Parses a level filter from a string representation or returns null.
         */
        public fun fromStringOrNull(value: String): LevelFilter? =
            try {
                fromString(value)
            } catch (_: IllegalArgumentException) {
                null
            }
    }
}

/**
 * Call-site and event interest cache state.
 */
public enum class Interest {
    NEVER,
    SOMETIMES,
    ALWAYS,
    ;

    public fun isNever(): Boolean = this == NEVER

    public fun isSometimes(): Boolean = this == SOMETIMES

    public fun isAlways(): Boolean = this == ALWAYS
}

/**
 * Kind of metadata (Span or Event).
 */
public enum class Kind {
    SPAN,
    EVENT,
    ;

    public fun isSpan(): Boolean = this == SPAN

    public fun isEvent(): Boolean = this == EVENT
}

/**
 * Unique identifier for a span within an active trace session.
 */
public data class Id(
    public val value: Long,
) {
    public companion object {
        public fun fromLong(value: Long): Id = Id(value)
    }
}

/**
 * Set of field names associated with metadata.
 */
public class FieldSet(
    private val names: List<String>,
    private val callsite: Callsite? = null,
) {
    public fun field(name: String): Field? {
        val index = names.indexOf(name)
        return if (index >= 0) Field(name, index, callsite) else null
    }

    public fun contains(name: String): Boolean = names.contains(name)

    public fun names(): List<String> = names

    public fun size(): Int = names.size

    public companion object {
        public fun empty(): FieldSet = FieldSet(emptyList())
    }
}

/**
 * An opaque field descriptor.
 */
public data class Field(
    public val name: String,
    public val index: Int = 0,
    public val callsite: Callsite? = null,
)

/**
 * Key-value pair collection for recorded fields.
 */
public class ValueSet(
    public val fieldSet: FieldSet,
    public val values: Map<String, Any?> = emptyMap(),
) {
    public fun record(visitor: Visit) {
        for ((k, v) in values) {
            visitor.visit(k, v)
        }
    }

    public companion object {
        public fun empty(): ValueSet = ValueSet(FieldSet.empty(), emptyMap())
    }
}

/**
 * Visitor interface for recording field values.
 */
public interface Visit {
    public fun visit(name: String, value: Any?)
}

/**
 * Location and callsite registration for spans and events.
 */
public interface Callsite {
    public fun metadata(): Metadata

    public fun interest(): Interest = Interest.ALWAYS
}

/**
 * Static metadata associated with a trace call-site, span, or event.
 */
public data class Metadata(
    public val name: String,
    public val target: String,
    public val level: Level,
    public val fields: FieldSet = FieldSet.empty(),
    public val kind: Kind = Kind.EVENT,
    public val file: String? = null,
    public val line: Int? = null,
    public val callsite: Callsite? = null,
) {
    public val isSpan: Boolean get() = kind.isSpan()
    public val isEvent: Boolean get() = kind.isEvent()
}

/**
 * Attributes used to create a new span.
 */
public data class Attributes(
    public val metadata: Metadata,
    public val parent: Id? = null,
    public val isRoot: Boolean = false,
    public val isContextual: Boolean = true,
    public val values: ValueSet = ValueSet.empty(),
)

/**
 * Key-value record updates for an active span.
 */
public data class Record(
    public val values: ValueSet = ValueSet.empty(),
)

/**
 * An event emitted during tracing.
 */
public data class Event(
    public val metadata: Metadata,
    public val parent: Id? = null,
    public val isRoot: Boolean = false,
    public val isContextual: Boolean = true,
    public val values: ValueSet = ValueSet.empty(),
)

/**
 * Core interface for recording and consuming diagnostic trace events and spans.
 */
public interface Subscriber {
    public fun registerCallsite(metadata: Metadata): Interest = Interest.ALWAYS

    public fun enabled(metadata: Metadata): Boolean = true

    public fun newSpan(attributes: Attributes): Id

    public fun record(id: Id, values: Record) {}

    public fun recordFollowsFrom(span: Id, follows: Id) {}

    public fun eventEnabled(event: Event): Boolean = enabled(event.metadata)

    public fun event(event: Event) {}

    public fun enter(id: Id) {}

    public fun exit(id: Id) {}

    public fun currentSpan(): Id? = null

    public fun cloneSpan(id: Id): Id = id

    public fun tryClose(id: Id): Boolean = true

    public fun maxLevelHint(): LevelFilter? = null
}

/**
 * A subscriber that does nothing and records no data.
 */
public class NoSubscriber : Subscriber {
    override fun registerCallsite(metadata: Metadata): Interest = Interest.NEVER

    override fun enabled(metadata: Metadata): Boolean = false

    override fun newSpan(attributes: Attributes): Id = Id(0)

    override fun record(id: Id, values: Record) {}

    override fun recordFollowsFrom(span: Id, follows: Id) {}

    override fun event(event: Event) {}

    override fun enter(id: Id) {}

    override fun exit(id: Id) {}

    override fun currentSpan(): Id? = null

    override fun cloneSpan(id: Id): Id = id

    override fun tryClose(id: Id): Boolean = true

    override fun maxLevelHint(): LevelFilter = LevelFilter.OFF

    public companion object {
        public val DEFAULT: NoSubscriber = NoSubscriber()
    }
}

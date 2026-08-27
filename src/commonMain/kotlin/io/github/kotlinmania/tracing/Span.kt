// port-lint: source span.rs
package io.github.kotlinmania.tracing

/**
 * Trait implemented by types which represent a span ID.
 */
public interface AsId {
    public fun asId(): Id?
}

/**
 * An entered span guard.
 */
public class Entered(
    private val span: Span,
) : AutoCloseable {
    private var exited = false

    public fun exit() {
        if (!exited) {
            exited = true
            span.doExit()
        }
    }

    override fun close() {
        exit()
    }
}

/**
 * An entered span wrapper that returns the inner [Span] on exit.
 */
public class EnteredSpan(
    private val span: Span,
) : AutoCloseable {
    private var exited = false

    public fun exit(): Span {
        if (!exited) {
            exited = true
            span.doExit()
        }
        return span
    }

    public fun span(): Span = span

    override fun close() {
        if (!exited) {
            exited = true
            span.doExit()
        }
    }
}

/**
 * Internal handle to active span state.
 */
public class Inner(
    public val id: Id,
    public val dispatch: Dispatch,
)

/**
 * Target descriptor for a span.
 */
public data class Target(
    public val name: String,
)

/**
 * A handle to a span of execution.
 */
public class Span(
    private val inner: Inner?,
    private val meta: Metadata?,
) : AsId {
    public constructor(id: Id?, meta: Metadata?, dispatch: Dispatch?) : this(
        if (id != null && dispatch != null) Inner(id, dispatch) else null,
        meta,
    )

    public override fun asId(): Id? = id()

    public fun id(): Id? = inner?.id

    public fun metadata(): Metadata? = meta

    public fun isNone(): Boolean = inner == null && meta == null

    public fun isDisabled(): Boolean = inner == null

    public fun orCurrent(): Span = if (isDisabled()) current() else this

    public fun field(name: String): Field? = meta?.fields?.field(name)

    public fun hasField(name: String): Boolean = meta?.fields?.contains(name) ?: false

    public fun withSubscriber(subscriber: Subscriber): Span {
        val currentId = id()
        return if (currentId != null) {
            Span(currentId, meta, Dispatch.fromSubscriber(subscriber))
        } else {
            this
        }
    }

    public fun enter(): Entered {
        doEnter()
        return Entered(this)
    }

    public fun entered(): EnteredSpan {
        doEnter()
        return EnteredSpan(this)
    }

    internal fun doEnter() {
        inner?.dispatch?.subscriber?.enter(inner.id)
    }

    internal fun doExit() {
        inner?.dispatch?.subscriber?.exit(inner.id)
    }

    public fun <T> inScope(block: () -> T): T {
        val guard = enter()
        return try {
            block()
        } finally {
            guard.exit()
        }
    }

    public fun record(name: String, value: Any?): Span {
        val currentInner = inner
        if (currentInner != null) {
            val fieldSet = meta?.fields ?: FieldSet.empty()
            val vs = ValueSet(fieldSet, mapOf(name to value))
            currentInner.dispatch.subscriber.record(currentInner.id, Record(vs))
        }
        return this
    }

    public fun record(field: Field, value: Any?): Span = record(field.name, value)

    public fun recordAll(values: ValueSet): Span {
        val currentInner = inner
        if (currentInner != null) {
            currentInner.dispatch.subscriber.record(currentInner.id, Record(values))
        }
        return this
    }

    public fun followsFrom(follows: Id): Span {
        val currentInner = inner
        if (currentInner != null) {
            currentInner.dispatch.subscriber.recordFollowsFrom(currentInner.id, follows)
        }
        return this
    }

    public fun followsFrom(follows: Span): Span {
        val followId = follows.id()
        if (followId != null) {
            followsFrom(followId)
        }
        return this
    }

    public fun clone(): Span =
        if (inner != null) {
            val clonedId = inner.dispatch.subscriber.cloneSpan(inner.id)
            Span(Inner(clonedId, inner.dispatch), meta)
        } else {
            Span(null, meta)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Span) return false
        return inner?.id == other.inner?.id
    }

    override fun hashCode(): Int = inner?.id?.hashCode() ?: 0

    override fun toString(): String =
        "Span(id=${inner?.id}, meta=${meta?.name})"

    public companion object {
        public fun none(): Span = Span(null, null)

        public fun disabled(meta: Metadata): Span = Span(null, meta)

        public fun newDisabled(meta: Metadata): Span = disabled(meta)

        public fun from(id: Id): Span =
            getDefault { dispatch ->
                Span(Inner(id, dispatch), null)
            }

        public fun current(): Span =
            getDefault { dispatch ->
                val currentId = dispatch.subscriber.currentSpan()
                if (currentId != null) {
                    val cloned = dispatch.subscriber.cloneSpan(currentId)
                    Span(Inner(cloned, dispatch), null)
                } else {
                    none()
                }
            }

        public fun new(meta: Metadata, values: ValueSet = ValueSet.empty()): Span =
            getDefault { dispatch ->
                newWith(meta, values, dispatch)
            }

        public fun newWith(meta: Metadata, values: ValueSet, dispatch: Dispatch): Span {
            val attrs = Attributes(metadata = meta, isContextual = true, values = values)
            return makeWith(meta, attrs, dispatch)
        }

        public fun newRoot(meta: Metadata, values: ValueSet = ValueSet.empty()): Span =
            getDefault { dispatch ->
                newRootWith(meta, values, dispatch)
            }

        public fun newRootWith(meta: Metadata, values: ValueSet, dispatch: Dispatch): Span {
            val attrs = Attributes(metadata = meta, isRoot = true, isContextual = false, values = values)
            return makeWith(meta, attrs, dispatch)
        }

        public fun childOf(parent: Id?, meta: Metadata, values: ValueSet = ValueSet.empty()): Span =
            getDefault { dispatch ->
                childOfWith(parent, meta, values, dispatch)
            }

        public fun childOfWith(parent: Id?, meta: Metadata, values: ValueSet, dispatch: Dispatch): Span {
            val attrs = Attributes(metadata = meta, parent = parent, isContextual = false, values = values)
            return makeWith(meta, attrs, dispatch)
        }

        public fun root(meta: Metadata, values: ValueSet = ValueSet.empty()): Span =
            newRoot(meta, values)

        public fun makeWith(meta: Metadata, newSpan: Attributes, dispatch: Dispatch): Span =
            if (dispatch.subscriber.enabled(meta)) {
                val id = dispatch.subscriber.newSpan(newSpan)
                Span(Inner(id, dispatch), meta)
            } else {
                newDisabled(meta)
            }
    }
}

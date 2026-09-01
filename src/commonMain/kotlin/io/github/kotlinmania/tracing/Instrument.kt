// port-lint: source instrument.rs
package io.github.kotlinmania.tracing

/**
 * Attaches spans to a block or computation.
 */
public interface Instrument<T> {
    /**
     * Instruments this type with the provided [Span].
     */
    public fun instrument(span: Span): Instrumented<T>

    /**
     * Instruments this type with the current [Span].
     */
    public fun inCurrentSpan(): Instrumented<T>
}

/**
 * Extension trait allowing computations to be instrumented with a [Subscriber].
 */
public interface WithSubscriber<T> {
    /**
     * Attaches the provided [Subscriber] to this type.
     */
    public fun withSubscriber(subscriber: Subscriber): WithDispatch<T>

    /**
     * Attaches the current default [Subscriber] to this type.
     */
    public fun withCurrentSubscriber(): WithDispatch<T>
}

/**
 * A wrapper that has been instrumented with a [Span].
 */
public class Instrumented<T>(
    public var inner: T,
    public var span: Span,
) {
    public fun spanMut(): Span = span

    public fun innerMut(): T = inner

    public fun innerPinRef(): T = inner

    public fun innerPinMut(): T = inner

    public fun spanAndInnerPinRef(): Pair<Span, T> = Pair(span, inner)

    public fun spanAndInnerPinMut(): Pair<Span, T> = Pair(span, inner)

    public fun poll(): T = inner

    public fun intoInner(): T = inner

    public fun <R> run(block: (T) -> R): R =
        span.inScope {
            block(inner)
        }
}

/**
 * A wrapper that has been instrumented with a [Dispatch].
 */
public class WithDispatch<T>(
    public var inner: T,
    public val dispatcher: Dispatch,
) {
    public fun innerMut(): T = inner

    public fun innerPinRef(): T = inner

    public fun innerPinMut(): T = inner

    public fun poll(): T = inner

    public fun intoInner(): T = inner

    public fun <R> run(block: (T) -> R): R =
        withDefault(dispatcher) {
            block(inner)
        }
}

/**
 * Instruments this value with the provided [Span].
 */
public fun <T> T.instrument(span: Span): Instrumented<T> = Instrumented(this, span)

/**
 * Instruments this value with the current [Span].
 */
public fun <T> T.inCurrentSpan(): Instrumented<T> = Instrumented(this, Span.current())

/**
 * Instruments this value with the provided [Subscriber].
 */
public fun <T> T.withSubscriber(subscriber: Subscriber): WithDispatch<T> =
    WithDispatch(this, Dispatch.fromSubscriber(subscriber))

/**
 * Instruments this value with the current default [Subscriber].
 */
public fun <T> T.withCurrentSubscriber(): WithDispatch<T> =
    WithDispatch(this, getDefault { it })

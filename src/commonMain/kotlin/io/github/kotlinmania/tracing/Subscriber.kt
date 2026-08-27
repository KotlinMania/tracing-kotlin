// port-lint: source tracing/tests/subscriber.rs
package io.github.kotlinmania.tracing

/**
 * Sets this [Subscriber] as the default for the duration of a closure.
 */
public fun <T> withDefault(subscriber: Subscriber, f: () -> T): T =
    withDefault(Dispatch.fromSubscriber(subscriber), f)

/**
 * Sets this subscriber as the global default for the duration of the entire program.
 */
public fun setGlobalDefault(subscriber: Subscriber): Result<Unit> =
    setGlobalDefault(Dispatch.fromSubscriber(subscriber))

/**
 * Sets the [Subscriber] as the default for the current scope, returning a [DefaultGuard].
 */
public fun setDefault(subscriber: Subscriber): DefaultGuard =
    setDefault(Dispatch.fromSubscriber(subscriber))

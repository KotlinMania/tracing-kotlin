// port-lint: source dispatcher.rs
package io.github.kotlinmania.tracing

import kotlin.concurrent.Volatile

/**
 * An error returned when attempting to set the global default subscriber more than once.
 */
public class SetGlobalDefaultError(message: String = "a global default subscriber has already been set") :
    Exception(message)

/**
 * A cloneable, type-erased reference to a subscriber.
 */
public class Dispatch(
    public val subscriber: Subscriber,
) {
    public companion object {
        public fun fromSubscriber(subscriber: Subscriber): Dispatch = Dispatch(subscriber)

        public fun none(): Dispatch = Dispatch(NoSubscriber.DEFAULT)
    }
}

/**
 * A weak reference to a dispatch.
 */
public class WeakDispatch(
    private val dispatch: Dispatch?,
) {
    public fun upgrade(): Dispatch? = dispatch
}

/**
 * Guard object returned by `setDefault` that restores the previous default when closed.
 */
public class DefaultGuard(
    private val previous: Dispatch?,
    private val onRestore: (Dispatch?) -> Unit,
) : AutoCloseable {
    private var closed = false

    override fun close() {
        if (!closed) {
            closed = true
            onRestore(previous)
        }
    }
}

@Volatile
private var globalDefault: Dispatch? = null
private val defaultDispatcherHolder = ThreadLocalDefaultHolder()

private class ThreadLocalDefaultHolder {
    private var current: Dispatch? = null

    fun get(): Dispatch? = current

    fun set(dispatch: Dispatch?) {
        current = dispatch
    }
}

/**
 * Sets the default dispatcher for the duration of a closure.
 */
public fun <T> withDefault(dispatch: Dispatch, f: () -> T): T {
    val previous = defaultDispatcherHolder.get()
    defaultDispatcherHolder.set(dispatch)
    return try {
        f()
    } finally {
        defaultDispatcherHolder.set(previous)
    }
}

/**
 * Sets the default dispatcher for the current scope, returning a guard.
 */
public fun setDefault(dispatch: Dispatch): DefaultGuard {
    val previous = defaultDispatcherHolder.get()
    defaultDispatcherHolder.set(dispatch)
    return DefaultGuard(previous) { restored ->
        defaultDispatcherHolder.set(restored)
    }
}

/**
 * Sets the global default subscriber.
 */
public fun setGlobalDefault(dispatch: Dispatch): Result<Unit> =
    if (globalDefault != null) {
        Result.failure(SetGlobalDefaultError())
    } else {
        globalDefault = dispatch
        Result.success(Unit)
    }

/**
 * Returns whether a global default subscriber has been set.
 */
public fun hasBeenSet(): Boolean = globalDefault != null

/**
 * Executes a closure with the currently active default Dispatch.
 */
public fun <T> getDefault(f: (Dispatch) -> T): T {
    val active = defaultDispatcherHolder.get()
        ?: globalDefault
        ?: Dispatch.none()
    return f(active)
}

/**
 * Resets global and local dispatchers (primarily for testing).
 */
public fun resetGlobalDefault() {
    globalDefault = null
    defaultDispatcherHolder.set(null)
}

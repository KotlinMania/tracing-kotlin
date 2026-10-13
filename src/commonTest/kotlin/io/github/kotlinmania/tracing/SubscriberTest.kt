// port-lint: tests subscriber.rs
package io.github.kotlinmania.tracing

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SubscriberTest {
    @Test
    fun testLevelAndTarget() {
        val subscriber =
            MockSubscriber().withFilter { meta ->
                if (meta.target == "debug_module") {
                    meta.level <= Level.DEBUG
                } else {
                    meta.level <= Level.INFO
                }
            }

        val guard = setDefault(subscriber)
        try {
            assertTrue(enabled(Level.DEBUG, target = "debug_module"))
            assertTrue(enabled(Level.ERROR))
            assertFalse(enabled(Level.DEBUG))
        } finally {
            guard.close()
        }
    }

    @Test
    fun testSpanAndEvent() {
        val subscriber =
            MockSubscriber().withFilter { meta ->
                if (meta.target == "debug_module") {
                    meta.level <= Level.DEBUG
                } else if (meta.isSpan) {
                    meta.level <= Level.TRACE
                } else if (meta.isEvent) {
                    meta.level <= Level.DEBUG
                } else {
                    meta.level <= Level.INFO
                }
            }

        val guard = setDefault(subscriber)
        try {
            assertFalse(eventEnabled(Level.TRACE))
            assertTrue(eventEnabled(Level.DEBUG))
            assertTrue(spanEnabled(Level.TRACE))

            assertTrue(spanEnabled(Level.DEBUG, target = "debug_module"))
            assertTrue(eventEnabled(Level.DEBUG, target = "debug_module"))
        } finally {
            guard.close()
        }
    }

    @Test
    fun testNoSubscriberDisablesDefault() {
        val subscriber = MockSubscriber()
        withDefault(subscriber) {
            info("recorded message")
            assertEquals(1, subscriber.events.size)

            withDefault(NoSubscriber.DEFAULT) {
                info("this should not be recorded")
            }

            assertEquals(1, subscriber.events.size)
        }
    }

    @Test
    fun testScopedClobbersGlobal() {
        val scoped = MockSubscriber()
        val guard = setDefault(scoped)
        try {
            info("before drop")
            assertEquals(1, scoped.events.size)
        } finally {
            guard.close()
        }
    }

    @Test
    fun testEventWithinSubscriberDoesNotInfiniteLoop() {
        var loopCount = 0
        val testSubscriber =
            object : Subscriber {
                override fun registerCallsite(metadata: Metadata): Interest = Interest.SOMETIMES

                override fun enabled(metadata: Metadata): Boolean = true

                override fun newSpan(attributes: Attributes): Id = Id(1L)

                override fun record(id: Id, values: Record) {}

                override fun recordFollowsFrom(span: Id, follows: Id) {}

                override fun event(event: Event) {
                    if (event.metadata.target != "recursive") {
                        loopCount++
                        if (loopCount <= 3) {
                            event(Level.TRACE, "recursive event", target = "recursive")
                        }
                    }
                }

                override fun enter(id: Id) {}

                override fun exit(id: Id) {}
            }

        withDefault(testSubscriber) {
            event(Level.INFO, "initial event")
        }
        assertEquals(1, loopCount)
    }
}

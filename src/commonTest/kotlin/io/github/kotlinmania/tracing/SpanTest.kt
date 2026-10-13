// port-lint: tests span.rs
package io.github.kotlinmania.tracing

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SpanTest {
    @Test
    fun testRecordBackwardsCompat() {
        Span.current().record("some-key", "some text")
        Span.current().record("some-key", false)
    }

    @Test
    fun testSpanLifecycleWithSubscriber() {
        val subscriber = MockSubscriber()
        withDefault(subscriber) {
            val fieldSet = FieldSet(listOf("user_id", "message"))
            val meta =
                Metadata(
                    name = "test_span",
                    target = "test",
                    level = Level.INFO,
                    fields = fieldSet,
                    kind = Kind.SPAN,
                )

            val span = Span.new(meta, ValueSet(fieldSet, mapOf("user_id" to 42L)))
            assertNotNull(span.id())
            assertEquals(1, subscriber.spans.size)

            span.inScope {
                assertEquals(span.id(), subscriber.currentSpan())
                span.record("message", "inside span")
            }

            assertNull(subscriber.currentSpan())
            assertEquals(1, subscriber.entered.size)
            assertEquals(1, subscriber.exited.size)
            assertEquals(1, subscriber.records.size)
        }
    }

    @Test
    fun testDisabledSpan() {
        val meta =
            Metadata(
                name = "disabled_span",
                target = "test",
                level = Level.TRACE,
                kind = Kind.SPAN,
            )
        val disabled = Span.disabled(meta)
        assertTrue(disabled.isDisabled())
        assertNull(disabled.id())
        // Recording on disabled span should not throw
        disabled.record("key", "val")
    }

    @Test
    fun testEnteredGuardExitTwice() {
        val subscriber = MockSubscriber()
        withDefault(subscriber) {
            val meta = Metadata(name = "guard_test", target = "test", level = Level.INFO, kind = Kind.SPAN)
            val span = Span.new(meta)
            val guard = span.enter()
            guard.exit()
            guard.exit() // idempotent
            guard.close() // idempotent
            assertEquals(1, subscriber.entered.size)
            assertEquals(1, subscriber.exited.size)
        }
    }

    @Test
    fun testHandlesToTheSameSpanAreEqual() {
        val subscriber = MockSubscriber()
        withDefault(subscriber) {
            val meta = Metadata(name = "foo", target = "test", level = Level.TRACE, kind = Kind.SPAN)
            val foo1 = Span.new(meta)
            val foo2 = foo1.clone()
            assertEquals(foo1, foo2)
        }
    }

    @Test
    fun testHandlesToDifferentSpansAreNotEqual() {
        val subscriber = MockSubscriber()
        withDefault(subscriber) {
            val meta1 = Metadata(name = "foo", target = "test", level = Level.TRACE, kind = Kind.SPAN)
            val meta2 = Metadata(name = "foo", target = "test", level = Level.TRACE, kind = Kind.SPAN)
            val foo1 = Span.new(meta1)
            val foo2 = Span.new(meta2)
            assertNotEquals(foo1, foo2)
        }
    }

    @Test
    fun testSpansAlwaysGoToTheSubscriberThatTaggedThem() {
        val subscriber1 = MockSubscriber()
        val subscriber2 = MockSubscriber()

        val foo =
            withDefault(subscriber1) {
                val meta = Metadata(name = "foo", target = "test", level = Level.TRACE, kind = Kind.SPAN)
                val span = Span.new(meta)
                span.inScope {}
                span
            }

        withDefault(subscriber2) {
            foo.inScope {}
        }

        assertEquals(2, subscriber1.entered.size)
        assertEquals(2, subscriber1.exited.size)
        assertEquals(0, subscriber2.entered.size)
        assertEquals(0, subscriber2.exited.size)
    }
}

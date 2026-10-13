// port-lint: tests instrument.rs
package io.github.kotlinmania.tracing

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class InstrumentTest {
    @Test
    fun testInstrumentedWrappingAndAccessors() {
        val subscriber = MockSubscriber()
        withDefault(subscriber) {
            val meta =
                Metadata(
                    name = "test_instrument",
                    target = "test",
                    level = Level.INFO,
                    kind = Kind.SPAN,
                )
            val span = Span.new(meta)
            val value = "hello-tracing"
            val instrumented = value.instrument(span)

            assertEquals(value, instrumented.inner)
            assertEquals(value, instrumented.innerMut())
            assertEquals(value, instrumented.innerPinRef())
            assertEquals(value, instrumented.innerPinMut())
            assertSame(span, instrumented.span)
            assertSame(span, instrumented.spanMut())
            assertEquals(Pair(span, value), instrumented.spanAndInnerPinRef())
            assertEquals(Pair(span, value), instrumented.spanAndInnerPinMut())
            assertEquals(value, instrumented.poll())

            var executed = false
            val result =
                instrumented.run { v ->
                    assertEquals(span.id(), subscriber.currentSpan())
                    executed = true
                    "$v-done"
                }
            assertEquals("hello-tracing-done", result)
            assertEquals(true, executed)

            assertEquals(value, instrumented.intoInner())
        }
    }

    @Test
    fun testInCurrentSpan() {
        val subscriber = MockSubscriber()
        withDefault(subscriber) {
            val meta =
                Metadata(
                    name = "parent_span",
                    target = "test",
                    level = Level.INFO,
                    kind = Kind.SPAN,
                )
            val span = Span.new(meta)
            span.inScope {
                val num = 123
                val instrumented = num.inCurrentSpan()
                assertEquals(span.id(), instrumented.span.id())
                assertEquals(123, instrumented.inner)
            }
        }
    }

    @Test
    fun testWithSubscriberWrappingAndAccessors() {
        val subscriber = MockSubscriber()
        val data = 42
        val withDispatch = data.withSubscriber(subscriber)

        assertEquals(data, withDispatch.inner)
        assertEquals(data, withDispatch.innerMut())
        assertEquals(data, withDispatch.innerPinRef())
        assertEquals(data, withDispatch.innerPinMut())
        assertEquals(data, withDispatch.poll())
        assertNotNull(withDispatch.dispatcher)

        val result =
            withDispatch.run { v ->
                v * 2
            }
        assertEquals(84, result)
        assertEquals(data, withDispatch.intoInner())
    }

    @Test
    fun testWithCurrentSubscriber() {
        val subscriber = MockSubscriber()
        withDefault(subscriber) {
            val str = "test-string"
            val withDispatch = str.withCurrentSubscriber()
            assertEquals("test-string", withDispatch.inner)
            val res = withDispatch.run { it.uppercase() }
            assertEquals("TEST-STRING", res)
        }
    }
}
